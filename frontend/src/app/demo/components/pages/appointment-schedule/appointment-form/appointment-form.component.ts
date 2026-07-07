import { Component, Inject, OnInit, OnDestroy } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeLeaveServiceService } from 'src/app/services/employee-leave/employee-leave-service.service';

@Component({
    selector: 'app-appointment-form',
    templateUrl: './appointment-form.component.html',
    styleUrls: ['./appointment-form.component.scss']
})
export class AppointmentFormComponent implements OnInit, OnDestroy {
    // define the class properties
    // Form-related state: appointmentScheduleForm, submitted, isButtonDisabled
    // Mode/control flags: mode, endTimeAutoCalculated
    // Data sources: clients, employees, services
    // UI constraints/config: minDate, salonOpeningTime, salonClosingTime
    // Dropdown options: appointmentStatuses, bookingSources
    // Lifecycle management: subs
    // are defined here

    appointmentScheduleForm: FormGroup;
    // defined the property 'mode', and you can pass this as an argument to a function
    mode: 'add' | 'edit' = 'add';
    clients: any[] = [];
    employees: any[] = [];
    services: any[] = [];
    submitted = false;
    isButtonDisabled = false;
    minDate: Date = new Date();
    readonly salonOpeningTime = '10:00';
    readonly salonClosingTime = '18:00';
    fullyBooked: boolean = false;
    appointments: any[] = [];
    employeeLeaveData: any[] = [];

    // UI Appointment Status Dropdown options defined
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
        private employeeLeaveService: EmployeeLeaveServiceService,
        public dialogRef: MatDialogRef<AppointmentFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        // sets the date as today's date (00:00:00) -> user can't select a previous date
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
        this.loadAppointments();
        this.loadEmployeeLeaveData();

        if (this.mode === 'edit' && this.data.appointment) {
            this.patchForm(this.data.appointment);
        }

        const startCtrl = this.appointmentScheduleForm.get('appointmentStartTime');
        const serviceCtrl = this.appointmentScheduleForm.get('serviceId');
        const endCtrl = this.appointmentScheduleForm.get('appointmentEndTime');
        const statusChange = this.appointmentScheduleForm.get('appointmentStatus');

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

        const dateCtrl = this.appointmentScheduleForm.get('appointmentDate');
        const startTimeCtrl = this.appointmentScheduleForm.get('appointmentStartTime');
        const endTimeCtrl = this.appointmentScheduleForm.get('appointmentEndTime');

        const checkAvailabilityAndReset = () => {
            const employeeIdCtrl = this.appointmentScheduleForm.get('employeeId');
            const currentEmployeeId = employeeIdCtrl?.value;
            if (currentEmployeeId) {
                const selectedEmployee = this.employees.find(emp => emp.id === currentEmployeeId);
                if (selectedEmployee && (this.isEmployeeUnavailable(selectedEmployee) || this.isEmployeeBooked(selectedEmployee))) {
                    employeeIdCtrl.setValue(null);
                }
            }
        };

        if (dateCtrl) {
            this.subs.push(dateCtrl.valueChanges.subscribe(() => checkAvailabilityAndReset()));
        }
        if (startTimeCtrl) {
            this.subs.push(startTimeCtrl.valueChanges.subscribe(() => checkAvailabilityAndReset()));
        }
        if (endTimeCtrl) {
            this.subs.push(endTimeCtrl.valueChanges.subscribe(() => checkAvailabilityAndReset()));
        }

        // Disable cancellationReason Validators if the appointmentStatus != 'CANCELLED'
        // Because *ngIf only removes the cancellationReason field from the UI and it's validators(minLength and required) are validated by Angular
        // This might cause issues

        const statusCtrl = this.appointmentScheduleForm.get('appointmentStatus');
        const cancellationReasonCtrl = this.appointmentScheduleForm.get('cancellationReason');
        const bookingSourceCtrl = this.appointmentScheduleForm.get('bookingSource');
        const notesCtrl = this.appointmentScheduleForm.get('notes');

        // Set initial validation for cancellationReason
        if (statusCtrl?.value === 'CANCELLED') {
            cancellationReasonCtrl?.setValidators([Validators.required, Validators.minLength(5)]);
        } else {
            cancellationReasonCtrl?.clearValidators();
        }
        cancellationReasonCtrl?.updateValueAndValidity();

        // Liisten to the value change of statusCtrl, if the status is CANCELLED then set the required and minLength validatos,
        // If not, remove the validators
        if (statusCtrl) {
            this.subs.push(statusCtrl.valueChanges.subscribe(status => {
                if (status === 'CANCELLED') {
                    cancellationReasonCtrl?.setValidators([Validators.required, Validators.minLength(5)]);
                } else {
                    cancellationReasonCtrl?.clearValidators();
                }
                // This will make Angular re-run all the validators of the field again (required and minLength)
                cancellationReasonCtrl?.updateValueAndValidity();
            }));
        }

        // Set initial validation for notes
        if (bookingSourceCtrl?.value === 'ONLINE') {
            notesCtrl?.setValidators([Validators.required, Validators.minLength(5), Validators.maxLength(500)]);
        } else {
            notesCtrl?.clearValidators();
        }
        notesCtrl?.updateValueAndValidity();

        // Listen to bookingSource changes
        if (bookingSourceCtrl) {
            this.subs.push(bookingSourceCtrl.valueChanges.subscribe(source => {
                if (source === 'ONLINE') {
                    notesCtrl?.setValidators([Validators.required, Validators.minLength(5), Validators.maxLength(500)]);
                } else {
                    notesCtrl?.clearValidators();
                }
                notesCtrl?.updateValueAndValidity();
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

    // You don't add the login here, Just pass the received data from html to frontend service
    // The component received status string and forwards it to the Angular data service along with the Appointment's DB ID
    onStatusChange(status: string): void {
        //Angular injects the appointment object into your component: so 'this.data' contains the currently used Appointment Object's data
        this.appointmentService.sendStatusUpdates(this.data.id, status).subscribe({
            next: () => {
                this.messageService.showSuccess('Data sent Successfully!');
            },
            error: (error) => this.messageService.showError('Data sent to backend Failed: ' + error.message)
        });
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

        // In this, Angular built in Validation is used to check the vaidity of the form
        // This is preffered because of cleaner code
        if (this.appointmentScheduleForm.invalid) {
            this.appointmentScheduleForm.markAllAsTouched();
            return;
        }

        // We have created a custom flag called 'submitted' as a method of checking
        // All required field errors appear because submitted === true 
        // (Usually all validationd don't appear until the user has touched those fields)
        // this.submitted = true;
        // if (this.appointmentScheduleForm.invalid) return;

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

        // if the Backend returns 409 it means there is an overlapping appointment or another issue

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
        if (error.status === 409) {
            this.appointmentScheduleForm.get('appointmentData')?.setErrors({
                fullyBooked: true
            });
            this.messageService.showError("The salon is fully booked at the requested time slot");
        } else {
            this.messageService.showError("Action failed: " + error.message);
        }
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

    loadAppointments(): void {
        this.appointmentService.getData().subscribe(
            res => this.appointments = (res as any[]) || []
        );
    }

    // checks if the employee is unavailable (weekly off days), if yes he will be displayed disabled
    isEmployeeUnavailable(employee: any): boolean {
        const selectedDate = this.appointmentScheduleForm.get('appointmentDate')?.value;
        if (!selectedDate || !employee.weeklyOffDays) {
            return false;
        }

        const dateObj = new Date(selectedDate);
        // Fixed: only retrieve the weekday name (e.g., "Monday") so it matches weeklyOffDays string
        const currentDayName = dateObj.toLocaleString('default', {
            weekday: 'long'
        });
        console.log("Current weekday Name:", currentDayName);

        return employee.weeklyOffDays.includes(currentDayName);
    }

    // checks whether the employee is already booked, if yes he will be displayed disabled
    isEmployeeBooked(employee: any): boolean {

        const selectedDate = this.appointmentScheduleForm.get('appointmentDate')?.value;
        const selectedStart = this.toMinutes(this.parseTimeInput(this.appointmentScheduleForm.get('appointmentStartTime')?.value));
        const selectedEnd = this.toMinutes(this.parseTimeInput(this.appointmentScheduleForm.get('appointmentEndTime')?.value));

        if (!selectedDate || selectedStart == null || selectedEnd == null) {
            return false;
        }

        const d = new Date(selectedDate);
        const date = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;

        for (const appointment of this.appointments) {

            // Ignore cancelled appointments and the current appointment when editing
            if (
                appointment.appointmentStatus === 'CANCELLED' ||
                appointment.id === this.data.appointment?.id
            ) {
                continue;
            }

            // Skip if different employee or different date
            if (
                appointment.employeeId !== employee.id ||
                appointment.appointmentDate !== date
            ) {
                continue;
            }

            const bookedStart = this.toMinutes(
                this.parseTimeInput(appointment.appointmentStartTime)
            );
            const bookedEnd = this.toMinutes(
                this.parseTimeInput(appointment.appointmentEndTime)
            );

            // Standard overlap check
            if (selectedStart < bookedEnd && selectedEnd > bookedStart) {
                return true;
            }
        }

        return false;
    }

    loadEmployeeLeaveData(): void {
        this.employeeLeaveService.getData().subscribe({
            next: (response: any[]) => {
                this.employeeLeaveData = response || [];
                console.log("Employee Leave Data:", this.employeeLeaveData);
            },
            error: (err) =>
                this.messageService.showError('Employee Leave data retrieval failed: ' + err.message)
        });
    }

    isEmployeeOnLeave(employee: any): boolean {
        const selectedDate = this.appointmentScheduleForm.get('appointmentDate')?.value;
        if (!selectedDate || !employee) {
            return false;
        }
        const selectedDateObject = new Date(selectedDate);
        const selectedDateFormatted = selectedDateObject.toISOString().split('T')[0];
        const employeeName = employee.employeeName;

        for (const leave of this.employeeLeaveData) {
            if (leave.employeeName !== employeeName) {
                continue;
            }

            if (selectedDateFormatted >= leave.startDate && selectedDateFormatted <= leave.endDate) {
                return true;
            }
        }
        return false;
    }
}