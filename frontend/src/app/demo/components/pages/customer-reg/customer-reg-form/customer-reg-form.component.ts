import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { CustomerRegService } from 'src/app/services/customer-reg/customer-reg.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-customer-reg-form',
  templateUrl: './customer-reg-form.component.html',
  styleUrl: './customer-reg-form.component.scss'
})
export class CustomerRegFormComponent implements OnInit {
  customerForm !: FormGroup;
  isEditMode: boolean = false;
  isViewMode: boolean = false;
  employees: any[] = [];

  constructor(
    private fb: FormBuilder,
    private messageService: MessageServiceService,
    private dialogRef: MatDialogRef<CustomerRegFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private employeeService: EmployeeRegServicesService,
    private customerService: CustomerRegService
  ) {}

  ngOnInit(): void {
    this.isEditMode = this.data?.mode === 'edit';
    this.isViewMode = this.data?.mode === 'view';
    this.initForm();
    this.loadEmployees();

    if (this.data?.customer) {
      this.customerForm.patchValue(this.data.customer);
      if (this.isViewMode) {
        this.customerForm.disable();
      }
    }
  }

  loadEmployees(): void {
    this.employeeService.getData().subscribe({
      next: (data: any) => {
        this.employees = data || [];
      },
      error: (err: any) => {
        this.messageService.showError('Error loading stylists: ' + err.message);
      }
    });
  }

  initForm(): void {
    this.customerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName:  ['', Validators.required],
      email: ['',[ Validators.required, Validators.email]],
      phoneNumber:  ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      preferredStylist: ['', Validators.required]
    });
  }

  onSave(): void {
    if (this.customerForm.valid) {
      const formValue = this.customerForm.value;

      if (this.isEditMode && this.data.customer?.id) {
        this.customerService.editData(this.data.customer.id, formValue).subscribe({
          next: (response) => {
            this.messageService.showSuccess('Updated successfully!');
            this.dialogRef.close(response);
          }, 
          error: (err) => {
            this.messageService.showError('Update failed: ' + err.message)
          }
        });
      } else {
        this.customerService.serviceCall(formValue).subscribe({
          next: (response) => {
            this.messageService.showSuccess('Client successfully added');
            this.dialogRef.close(response);
          },
          error: (err) => {
            this.messageService.showError('Client could not be added: ' + err.message);
          }
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



