import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EmployeeScheduleService } from 'src/app/services/employee-schedule/employee-schedule-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-employee-schedule-form',
    templateUrl: './employee-schedule-form.component.html',
    styleUrls: ['./employee-schedule-form.component.scss']
})
export class EmployeeScheduleFormComponent implements OnInit {
    scheduleForm: FormGroup;
    employees: any[] = [];
    mode: 'add' | 'edit' = 'add';
    isButtonDisabled = false;
    submitted = false;

    constructor(
        private fb: FormBuilder,
        private scheduleService: EmployeeScheduleService,
        private employeeRegService: EmployeeRegServicesService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<EmployeeScheduleFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.scheduleForm = this.fb.group({
            employeeId: ['', Validators.required],
            workDay: ['', [Validators.required, Validators.min(0), Validators.max(6)]],
            isActive: [true],
            startTime: ['', this.timeFormatValidator.bind(this)],
            endTime: ['', this.timeFormatValidator.bind(this)],
            effectiveDate: [null],
            endDate: [null]
        });

        if (data) {
            this.mode = data.mode || 'add';
            if (data.schedule) {
                const s = data.schedule;
                this.scheduleForm.patchValue({
                    employeeId: s.employeeId,
                    workDay: s.workDay,
                    isActive: s.isActive,
                    startTime: this.formatTimeForInput(s.startTime),
                    endTime: this.formatTimeForInput(s.endTime),
                    effectiveDate: s.effectiveDate ? new Date(s.effectiveDate) : null,
                    endDate: s.endDate ? new Date(s.endDate) : null
                });
            }
        }
    }

    ngOnInit(): void {
        this.fetchEmployees();
    }

    fetchEmployees(): void {
        this.employeeRegService.getData().subscribe({
            next: (response: any[]) => {
                this.employees = response || [];
            },
            error: (error) => {
                this.messageService.showError('Error fetching employees: ' + error.message);
            }
        });
    }

    get f() { return this.scheduleForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    /** Formats a Date/Moment/string value to a plain 'YYYY-MM-DD' string or null */
    private formatDate(value: any): string | null {
        if (!value) return null;
        if (typeof value === 'string') return value;
        if (value instanceof Date) {
            const y = value.getFullYear();
            const m = String(value.getMonth() + 1).padStart(2, '0');
            const d = String(value.getDate()).padStart(2, '0');
            return `${y}-${m}-${d}`;
        }
        // Moment object (from @angular/material datepicker with moment adapter)
        if (typeof value.format === 'function') {
            return value.format('YYYY-MM-DD');
        }
        return String(value);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.scheduleForm.invalid) return;

        this.isButtonDisabled = true;
        const raw = this.scheduleForm.value;
        const formValue = {
            ...raw,
            startTime: this.parseTimeInput(raw.startTime),
            endTime: this.parseTimeInput(raw.endTime),
            effectiveDate: this.formatDate(raw.effectiveDate),
            endDate: this.formatDate(raw.endDate)
        };

        if (this.mode === 'add') {
            this.scheduleService.serviceCall(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Schedule saved successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            if (!this.data?.schedule?.id) {
                this.messageService.showError('Cannot update: schedule ID is missing.');
                this.isButtonDisabled = false;
                return;
            }
            this.scheduleService.editData(this.data.schedule.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Schedule updated successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        }
    }

    onCancel(): void {
        this.dialogRef.close(false);
    }

    resetForm(): void {
        this.scheduleForm.reset({ isActive: true });
        if (this.mode === 'edit' && this.data?.schedule) {
            const s = this.data.schedule;
            this.scheduleForm.patchValue({
                employeeId: s.employeeId,
                workDay: s.workDay,
                isActive: s.isActive,
                startTime: this.formatTimeForInput(s.startTime),
                endTime: this.formatTimeForInput(s.endTime),
                effectiveDate: s.effectiveDate ? new Date(s.effectiveDate) : null,
                endDate: s.endDate ? new Date(s.endDate) : null
            });
        }
        this.submitted = false;
        this.isButtonDisabled = false;
    }

    private formatTimeForInput(value: string | null | undefined): string {
        if (!value) {
            return '';
        }

        const normalized = this.parseTimeInput(value);
        if (!normalized) {
            return String(value);
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

    private timeFormatValidator(control: AbstractControl): ValidationErrors | null {
        const value = control.value;
        if (!value) {
            return null;
        }
        return this.parseTimeInput(value) ? null : { invalidTime: true };
    }

    private handleError(error: any): void {
        const msg = error?.error?.message || error?.message || 'Unknown error';
        this.messageService.showError('Action failed: ' + msg);
        this.isButtonDisabled = false;
    }
}
