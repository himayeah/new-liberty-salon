import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EmployeeAttendanceServiceService } from 'src/app/services/employee-attendance/employee-attendance-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-employee-attendance-form',
    templateUrl: './employee-attendance-form.component.html',
    styleUrls: ['./employee-attendance-form.component.scss']
})
export class EmployeeAttendanceFormComponent implements OnInit {
    attendanceForm: FormGroup;

    constructor(
        private fb: FormBuilder,
        private dialogRef: MatDialogRef<EmployeeAttendanceFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any,
        private attendanceService: EmployeeAttendanceServiceService,
        private messageService: MessageServiceService
    ) {
        this.attendanceForm = this.fb.group({
            employeeName: [{ value: '', disabled: true }],
            checkInTime: ['', Validators.required],
            checkOutTime: [''],
            status: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        if (this.data && this.data.attendance) {
            const attendance = this.data.attendance;
            this.attendanceForm.patchValue({
                employeeName: attendance.employeeName,
                checkInTime: attendance.checkInTime ? new Date(attendance.checkInTime).toISOString().slice(0, 16) : '',
                checkOutTime: attendance.checkOutTime ? new Date(attendance.checkOutTime).toISOString().slice(0, 16) : '',
                status: attendance.status
            });
        }
    }

    onSave(): void {
        if (this.attendanceForm.valid) {
            const formValue = this.attendanceForm.getRawValue();
            const attendanceData = {
                ...this.data.attendance,
                ...formValue
            };

            if (attendanceData.id) {
                this.attendanceService.editData(attendanceData.id, attendanceData).subscribe({
                    next: (res) => {
                        this.messageService.showSuccess('Attendance updated successfully');
                        this.dialogRef.close(res);
                    },
                    error: (err) => this.messageService.showError('Update failed: ' + err.message)
                });
            } else {
                // If there's no attendance record yet, we create one
                this.attendanceService.serviceCall(attendanceData).subscribe({
                    next: (res) => {
                        this.messageService.showSuccess('Attendance recorded successfully');
                        this.dialogRef.close(res);
                    },
                    error: (err) => this.messageService.showError('Save failed: ' + err.message)
                });
            }
        }
    }

    onCancel(): void {
        this.dialogRef.close();
    }
}
