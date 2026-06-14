import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-appointment-form',
    templateUrl: './appointment-form.component.html',
    styleUrls: ['./appointment-form.component.scss']
})
export class AppointmentFormComponent implements OnInit, OnDestroy {
    appointmentScheduleForm: FormGroup;
    mode: 'add' | 'edit' = 'add';
    clients: any[] = [];
    employees: any[] = [];
    services: any[] = [];
    submitted = false;
    isButtonDisabled = false;
    minDate: Date = new Date();
    readonly salonOpeningTime = '10:00';
    readonly salonClosingTime = '18:00';

    appointmentStatuses = [
        { value: 'BOOKED', viewValue: 'Booked' },
        { value: 'CHECKED_IN', viewValue: 'Checked In' },
        { value: 'IN_PROGRESS', viewValue: 'In Progress' },
        { value: 'READY FOR BILLING', viewValue: 'Ready for Billing' },
        { value: 'COMPLETED', viewValue: 'Completed' },
        { value: 'CANCELLED', viewValue: 'Cancelled' },
        { value: 'NO_SHOW', viewValue: 'No Show' }
    ];

    bookingSources = [
        { value: 'ONLINE', viewValue: 'Online' },
        { value: 'PHONE', viewValue: 'Phone' },
        { value: 'WALK_IN', viewValue: 'Walk-in' }
    ];

    private endTimeAutoCalculated = true;
    private subs: Subscription[] = [];

    constructor(
        private fb: FormBuilder,
        private appointmentService: AppointmentSchedulingServiceService,
        private clientService: ClientRegServiceService,
        private employeeService: EmployeeRegServicesService,
        private serviceService: ServiceService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<AppointmentFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.minDate.setHours(0, 0, 0, 0);
        this.mode = data.mode || 'add';
        this.appointmentScheduleForm = this.fb.group({
            clientId: [null, [Validators.required]],
            employeeId: [null, [Validators.required]],
            serviceId: [null],
            appointmentDate: [null, [Validators.required]],
            appointmentStartTime: [null, [Validators.required, this.timeFormatValidator.bind(this)]],
            appointmentEndTime: [null, [Validators.required, this.timeFormatValidator.bind(this)]],
            appointmentStatus: ['BOOKED', [Validators.required]],
            bookingSource: [null, [Validators.required]],
            notes: [null],
            cancellationReason: [null],
            cancelledDate: [null],
        }, { validators: this.validateSalonHours.bind(this) });
    }

    ngOnInit(): void {
        this.loadClients();
        this.loadEmployees();
        this.loadServices();

        if (this.mode === 'edit' && this.data.appointment) {
            this.patchForm(this.data.appointment);
        }

        const startCtrl = this.appointmentScheduleForm.get('appointmentStartTime');
        const serviceCtrl = this.appointmentScheduleForm.get('serviceId');
        const endCtrl = this.appointmentScheduleForm.get('appointmentEndTime');

        if (startCtrl) {
            this.subs.push(startCtrl.valueChanges.subscribe((value) => {
                this.applyTimeFormatting(startCtrl);
                this.computeEndTimeIfNeeded();
            }));
        }
        if (serviceCtrl) {
            this.subs.push(serviceCtrl.valueChanges.subscribe(() => this.computeEndTimeIfNeeded()));
        }
        if (endCtrl) {
            this.subs.push(endCtrl.valueChanges.subscribe((value) => {
                this.applyTimeFormatting(endCtrl);
                this.endTimeAutoCalculated = false;
            }));
        }
    }

    private patchForm(data: any): void {
        const patchVal: any = { ...data };
        patchVal.clientId = data.clientId ?? data.client?.id ?? null;
        patchVal.employeeId = data.employeeId ?? data.employee?.id ?? data.stylistId ?? data.stylist?.id ?? null;
        patchVal.serviceId = data.serviceId ?? data.service?.id ?? null;
        patchVal.appointmentDate = data.appointmentDate ? new Date(data.appointmentDate) : null;
        patchVal.appointmentStatus = data.appointmentStatus ?? data.status ?? 'BOOKED';
        patchVal.appointmentStartTime = this.formatTimeForInput(data.appointmentStartTime);
        patchVal.appointmentEndTime = this.formatTimeForInput(data.appointmentEndTime);
        this.appointmentScheduleForm.patchValue(patchVal);

        if (!patchVal.appointmentEndTime) {
            this.computeEndTimeIfNeeded();
        }
    }

    loadClients(): void {
        this.clientService.getData().subscribe(res => this.clients = (res as any[]) || []);
    }

    loadEmployees(): void {
        this.employeeService.getData().subscribe(res => this.employees = (res as any[]) || []);
    }

    loadServices(): void {
        this.serviceService.getData().subscribe(res => this.services = (res as any[]) || []);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.appointmentScheduleForm.invalid) return;

        this.isButtonDisabled = true;
        const formValue = { ...this.appointmentScheduleForm.value };
        const startTime = this.parseTimeInput(formValue.appointmentStartTime);
        const endTime = this.parseTimeInput(formValue.appointmentEndTime);

        if (!startTime || !endTime) {
            this.messageService.showError('Please enter valid times in HH:MM AM/PM format.');
            this.isButtonDisabled = false;
            return;
        }

        const startMinutes = this.toMinutes(startTime);
        const endMinutes = this.toMinutes(endTime);
        const openingMinutes = this.toMinutes(this.salonOpeningTime);
        const closingMinutes = this.toMinutes(this.salonClosingTime);

        if (startMinutes < openingMinutes || endMinutes > closingMinutes || endMinutes <= startMinutes) {
            this.messageService.showError('Appointments must be booked between 10:00 AM and 06:00 PM, with end time after start time.');
            this.isButtonDisabled = false;
            return;
        }

        formValue.appointmentStartTime = startTime;
        formValue.appointmentEndTime = endTime;

        if (formValue.appointmentDate instanceof Date) {
            const d = formValue.appointmentDate;
            formValue.appointmentDate = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
        }

        if (this.mode === 'add') {
            this.appointmentService.serviceCall(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.appointmentService.editData(this.data.appointment.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Updated Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        }
    }

    private handleError(error: any): void {
        this.messageService.showError('Action failed: ' + error.message);
        this.isButtonDisabled = false;
    }

    private computeEndTimeIfNeeded(): void {
        const start = this.appointmentScheduleForm.get('appointmentStartTime')?.value;
        if (!start) return;
        const endCtrl = this.appointmentScheduleForm.get('appointmentEndTime');
        if (!endCtrl || (endCtrl.value && !this.endTimeAutoCalculated)) return;

        const serviceId = this.appointmentScheduleForm.get('serviceId')?.value;
        const end = this.computeEndTime(start, serviceId);
        if (end) {
            this.endTimeAutoCalculated = true;
            endCtrl.setValue(end, { emitEvent: false });
        }
    }

    private computeEndTime(startTime: string, serviceId: any): string | null {
        const normalizedStart = this.parseTimeInput(startTime);
        if (!normalizedStart) return null;

        const duration = this.getServiceDuration(serviceId);
        const dateBase = this.appointmentScheduleForm.get('appointmentDate')?.value ? new Date(this.appointmentScheduleForm.get('appointmentDate')!.value) : new Date();
        const parts = normalizedStart.split(':').map(p => Number(p));
        if (parts.length < 2) return null;
        dateBase.setHours(parts[0], parts[1], 0, 0);
        const endDate = new Date(dateBase.getTime() + duration * 60000);
        return `${String(endDate.getHours()).padStart(2, '0')}:${String(endDate.getMinutes()).padStart(2, '0')}`;
    }

    private formatTimeForInput(value: string | null | undefined): string {
        if (!value) {
            return '';
        }

        const normalized = this.parseTimeInput(value);
        if (!normalized) {
            return value;
        }

        const [hoursStr, minutes] = normalized.split(':');
        const hours = Number(hoursStr);
        const suffix = hours >= 12 ? 'PM' : 'AM';
        let hours12 = hours % 12;
        if (hours12 === 0) {
            hours12 = 12;
        }

        return `${String(hours12).padStart(2, '0')}:${minutes} ${suffix}`;
    }

    private parseTimeInput(value: string | null | undefined): string | null {
        if (!value) {
            return null;
        }

        const trimmed = String(value).trim().toUpperCase();
        const explicitMatch = trimmed.match(/^(\d{1,2}):(\d{2})\s*(AM|PM)$/);
        if (explicitMatch) {
            let hours = Number(explicitMatch[1]);
            const minutes = explicitMatch[2];
            const suffix = explicitMatch[3];
            if (suffix === 'PM' && hours < 12) {
                hours += 12;
            }
            if (suffix === 'AM' && hours === 12) {
                hours = 0;
            }
            return `${String(hours).padStart(2, '0')}:${minutes}`;
        }

        const simpleMatch = trimmed.match(/^(\d{1,2}):(\d{2})$/);
        if (simpleMatch) {
            const hours = Number(simpleMatch[1]);
            const minutes = simpleMatch[2];
            return `${String(hours).padStart(2, '0')}:${minutes}`;
        }

        return null;
    }

    private applyTimeFormatting(control: AbstractControl | null): void {
        if (!control) {
            return;
        }

        const value = control.value;
        if (typeof value !== 'string' || !value.trim()) {
            return;
        }

        const normalized = this.parseTimeInput(value);
        if (!normalized) {
            return;
        }

        const formatted = this.formatTimeForInput(normalized);
        if (formatted !== value) {
            control.setValue(formatted, { emitEvent: false });
        }
    }

    private timeFormatValidator(control: AbstractControl): ValidationErrors | null {
        const value = control.value;
        if (!value) {
            return null;
        }

        return this.parseTimeInput(value) ? null : { invalidTime: true };
    }

    private validateSalonHours(group: AbstractControl): ValidationErrors | null {
        const startValue = group.get('appointmentStartTime')?.value;
        const endValue = group.get('appointmentEndTime')?.value;
        if (!startValue || !endValue) {
            return null;
        }

        const startMinutes = this.toMinutes(this.parseTimeInput(startValue));
        const endMinutes = this.toMinutes(this.parseTimeInput(endValue));
        if (startMinutes == null || endMinutes == null) {
            return null;
        }

        const openingMinutes = this.toMinutes(this.salonOpeningTime);
        const closingMinutes = this.toMinutes(this.salonClosingTime);
        if (startMinutes < openingMinutes || endMinutes > closingMinutes || endMinutes <= startMinutes) {
            return { salonHours: true };
        }

        return null;
    }

    private toMinutes(value: string | null | undefined): number | null {
        if (!value) {
            return null;
        }
        const [hours, minutes] = value.split(':').map(part => Number(part));
        if (Number.isNaN(hours) || Number.isNaN(minutes)) {
            return null;
        }
        return hours * 60 + minutes;
    }

    private getServiceDuration(serviceId: any): number {
        const s = this.services.find(x => x.id === serviceId);
        return s?.duration || 30;
    }

    ngOnDestroy(): void {
        this.subs.forEach(s => s.unsubscribe());
    }

    onCancel(): void {
        this.dialogRef.close();
    }
}
