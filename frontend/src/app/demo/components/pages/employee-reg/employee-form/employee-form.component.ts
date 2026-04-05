import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.scss']
})
export class EmployeeFormComponent implements OnInit {
  employeeForm!: FormGroup;
  isEditMode: boolean = false;

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<EmployeeFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private employeeService: EmployeeRegServicesService,
    private messageService: MessageServiceService
  ) { }

  ngOnInit(): void {
    this.isEditMode = this.data.mode === 'edit';
    this.initForm();

    if (this.isEditMode && this.data.employee) {
      const emp = { ...this.data.employee };
      if (emp.weeklyOffDays && typeof emp.weeklyOffDays === 'string') {
        emp.weeklyOffDays = emp.weeklyOffDays.split(',').map((d: string) => d.trim());
      }
      this.employeeForm.patchValue(emp);
    }
  }

  initForm(): void {
    this.employeeForm = this.fb.group({
      employeeName: ['', Validators.required],
      dateJoined: ['', Validators.required],
      designation: ['', Validators.required],
      specializations: [''],
      hourlyRate: [''],
      commissionRate: [''],
      weeklyOffDays: [''],
      maxAppointmentsPerDay: ['']
    });
  }

  onSave(): void {
    if (this.employeeForm.valid) {
      const formValue = { ...this.employeeForm.value };

      if (Array.isArray(formValue.weeklyOffDays)) {
        formValue.weeklyOffDays = formValue.weeklyOffDays.join(', ');
      }

      if (this.isEditMode && this.data.employee?.id) {
        this.employeeService.editData(this.data.employee.id, formValue).subscribe({
          next: (res) => {
            this.messageService.showSuccess('Employee updated successfully');
            this.dialogRef.close(res);
          },
          error: (err) => this.messageService.showError('Update failed: ' + err.message)
        });
      } else {
        this.employeeService.serviceCall(formValue).subscribe({
          next: (res) => {
            this.messageService.showSuccess('Employee added successfully');
            this.dialogRef.close(res);
          },
          error: (err) => this.messageService.showError('Save failed: ' + err.message)
        });
      }
    } else {
        this.messageService.showError('Please fill in all required fields');
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
