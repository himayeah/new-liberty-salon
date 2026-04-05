import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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

    appointmentStatuses = [
        { value: 'BOOKED', viewValue: 'Booked' },
        { value: 'CONFIRMED', viewValue: 'Confirmed' },
        { value: 'CHECKED_IN', viewValue: 'Checked In' },
        { value: 'IN_PROGRESS', viewValue: 'In Progress' },
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
        this.mode = data.mode || 'add';
        this.appointmentScheduleForm = this.fb.group({
            clientId: [null, [Validators.required]],
            employeeId: [null, [Validators.required]],
            //remove serviceID required validator for now until Salon service module is added
            serviceId: [null],
            appointmentDate: [null, [Validators.required]],
            appointmentStartTime: [null, [Validators.required]],
            appointmentEndTime: [null, [Validators.required]],
            appointmentStatus: ['BOOKED', [Validators.required]],
            bookingSource: [null, [Validators.required]],
            notes: [null],
            cancellationReason: [null],
        });
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
            this.subs.push(startCtrl.valueChanges.subscribe(() => this.computeEndTimeIfNeeded()));
        }
        if (serviceCtrl) {
            this.subs.push(serviceCtrl.valueChanges.subscribe(() => this.computeEndTimeIfNeeded()));
        }
        if (endCtrl) {
            this.subs.push(endCtrl.valueChanges.subscribe(() => this.endTimeAutoCalculated = false));
        }
    }

    private patchForm(data: any): void {
        const patchVal: any = { ...data };
        patchVal.clientId = data.clientId ?? data.client?.id ?? null;
        patchVal.employeeId = data.employeeId ?? data.employee?.id ?? data.stylistId ?? data.stylist?.id ?? null;
        patchVal.serviceId = data.serviceId ?? data.service?.id ?? null;
        patchVal.appointmentDate = data.appointmentDate ? new Date(data.appointmentDate) : null;
        patchVal.appointmentStatus = data.appointmentStatus ?? data.status ?? 'BOOKED';
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
        const formValue = this.appointmentScheduleForm.value;

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
        if (!startTime) return null;
        const duration = this.getServiceDuration(serviceId);
        const dateBase = this.appointmentScheduleForm.get('appointmentDate')?.value ? new Date(this.appointmentScheduleForm.get('appointmentDate')!.value) : new Date();
        const parts = startTime.split(':').map(p => Number(p));
        if (parts.length < 2) return null;
        dateBase.setHours(parts[0], parts[1], 0, 0);
        const endDate = new Date(dateBase.getTime() + duration * 60000);
        return `${String(endDate.getHours()).padStart(2, '0')}:${String(endDate.getMinutes()).padStart(2, '0')}`;
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
