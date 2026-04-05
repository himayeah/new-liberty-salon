import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';

@Component({
  selector: 'app-client-form',
  templateUrl: './client-form.component.html',
  styleUrls: ['./client-form.component.scss']
})
export class ClientFormComponent implements OnInit {
  clientForm!: FormGroup;
  isEditMode: boolean = false;
  employees: any[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ClientFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private clientService: ClientRegServiceService,
    private messageService: MessageServiceService,
    private employeeService: EmployeeRegServicesService
  ) { }

  ngOnInit(): void {
    this.isEditMode = this.data.mode === 'edit';
    this.initForm();
    this.loadEmployees();

    if (this.isEditMode && this.data.client) {
      this.clientForm.patchValue(this.data.client);
    }
  }

  loadEmployees(): void {
    this.employeeService.getData().subscribe({
      next: (data: any) => {
        this.employees = data;
      },
      error: (err: any) => {
        this.messageService.showError('Error loading stylists: ' + err.message);
      }
    });
  }

  initForm(): void {
    this.clientForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', Validators.required],
      dateOfBirth: ['', Validators.required],
      gender: ['', Validators.required],
      preferredStylist: [''],
      allergies: ['']
    });
  }

  onSave(): void {
    if (this.clientForm.valid) {
      const formValue = this.clientForm.value;

      if (this.isEditMode && this.data.client?.id) {
        this.clientService.editData(this.data.client.id, formValue).subscribe({
          next: (res) => {
            this.messageService.showSuccess('Client updated successfully');
            this.dialogRef.close(res);
          },
          error: (err) => this.messageService.showError('Update failed: ' + err.message)
        });
      } else {
        this.clientService.serviceCall(formValue).subscribe({
          next: (res) => {
            this.messageService.showSuccess('Client added successfully');
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
