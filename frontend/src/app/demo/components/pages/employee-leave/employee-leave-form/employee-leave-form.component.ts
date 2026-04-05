import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeLeaveServiceService } from 'src/app/services/employee-leave/employee-leave-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-employee-leave-form',
  templateUrl: './employee-leave-form.component.html',
  styleUrls: ['./employee-leave-form.component.scss'],
  providers: [DatePipe]
})
export class EmployeeLeaveFormComponent implements OnInit {
  employeeLeaveForm: FormGroup;
  mode: 'add' | 'edit' = 'add';
  isButtonDisabled = false;
  submitted = false;
  employees: any[] = [];

  constructor(
    private fb: FormBuilder,
    private employeeLeaveService: EmployeeLeaveServiceService,
    private messageService: MessageServiceService,
    private employeeRegService: EmployeeRegServicesService,
    private dialogRef: MatDialogRef<EmployeeLeaveFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private datePipe: DatePipe
  ) {
    this.employeeLeaveForm = this.fb.group({
      employeeName: ['', Validators.required],
      leaveType: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      reason: ['']
    });

    if (data && data.employeeLeave) {
      this.mode = 'edit';
      const leave = data.employeeLeave;
      this.employeeLeaveForm.patchValue({
        employeeName: leave.employeeName,
        leaveType: leave.leaveType,
        startDate: leave.startDate ? new Date(leave.startDate) : '',
        endDate: leave.endDate ? new Date(leave.endDate) : '',
        reason: leave.reason
      });
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

  get f() { return this.employeeLeaveForm.controls; }

  isInvalid(controlName: string, errorType: string): boolean {
    const control = this.f[controlName];
    return (control.touched || this.submitted) && control.hasError(errorType);
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.employeeLeaveForm.invalid) return;

    this.isButtonDisabled = true;
    const formValue = this.employeeLeaveForm.value;

    // Format dates as yyyy-MM-dd strings to match backend DTO
    const payload = {
      ...formValue,
      startDate: this.datePipe.transform(formValue.startDate, 'yyyy-MM-dd') || formValue.startDate,
      endDate: this.datePipe.transform(formValue.endDate, 'yyyy-MM-dd') || formValue.endDate
    };

    if (this.mode === 'add') {
      this.employeeLeaveService.serviceCall(payload).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Created Successfully!');
          this.dialogRef.close(response);
        },
        error: (error) => this.handleError(error)
      });
    } else {
      this.employeeLeaveService.editData(this.data.employeeLeave.id, payload).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Updated Successfully!');
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
    this.employeeLeaveForm.reset();
    if (this.mode === 'edit') {
      const leave = this.data.employeeLeave;
      this.employeeLeaveForm.patchValue({
        employeeName: leave.employeeName,
        leaveType: leave.leaveType,
        startDate: leave.startDate ? new Date(leave.startDate) : '',
        endDate: leave.endDate ? new Date(leave.endDate) : '',
        reason: leave.reason
      });
    }
  }

  private handleError(error: any): void {
    this.messageService.showError('Action failed: ' + error.message);
    this.isButtonDisabled = false;
  }
}
