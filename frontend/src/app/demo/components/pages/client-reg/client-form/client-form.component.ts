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
  //isEditMode is initialized as false, the form starts in add mode by default
  // It switches to edit mode only when data.mode is set to 'edit' when opening the dialog
  isEditMode: boolean = false;
  employees: any[] = [];
  maxDate: Date = new Date();

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<ClientFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private clientService: ClientRegServiceService,
    private messageService: MessageServiceService,
    private employeeService: EmployeeRegServicesService
  ) {
    this.maxDate.setHours(23, 59, 59, 999);
  }

  //
  ngOnInit(): void {
    this.isEditMode = this.data.mode === 'edit';
    this.initForm();
    this.loadEmployees();
    this.getClientLastVisitedDate();

    // if the mode is edit and client data exists, patch the form with existing client data 
    // Data table's 'edit' button will pass the client data to the dialog, and data will be autofilled in the form
    // client-reg.component.ts file's openAddCientModal function passes the client data and the mode -> data: { mode: 'edit', client: data }
    //  data: { mode: 'edit', client: data }
    // patchValue is a method of Angular reactive FormGroup,
    //  whereas setValue requires all fields to be provided because it needs to follow the object structure of the form, patchValue allows you to update only specific fields
    if (this.isEditMode && this.data.client) {
      const client = { ...this.data.client };
      if (client.dateOfBirth) {
        client.dateOfBirth = this.toDate(client.dateOfBirth);
      }
      this.clientForm.patchValue(client);
    }
  }

  // employee data is needed to populate the dropdown for preferred stylist in the client form
  loadEmployees(): void {
    this.employeeService.getData().subscribe({
      // subscribe has 3 RxJS callback function parameters: next, error, and complete
      // next runs when the data is successfuly received from the observable
      // data = whaterver the API returns, in this case, it's the list of employees
      next: (data: any) => {
        this.employees = data;
      },
      error: (err: any) => {
        this.messageService.showError('Error loading stylists: ' + err.message);
      }
    });
  }

  //everytime you click 'edit' button, initForm() creates a new form and add these fields and patch the values from the client object
  initForm(): void {
    this.clientForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.pattern('^[a-zA-Z]+$')]],
      lastName: ['', Validators.required],
      email: ['', [Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern('^0[0-9]{9}$')]],
      //exampleValidator(can contain letters, numbers (0-9), and the character limit should be between 3-10): 
      // ['', Validators.pattern('^[a-zA-Z0-9]{3,10}$')],
      //exampleValidator(can contain letters, numbers (0-9), and the characters & and #: 
      // ['', Validators.pattern('^[a-zA-Z0-9&#]+$')],
      dateOfBirth: ['', Validators.required],
      gender: ['', Validators.required],
      preferredStylist: [''],
      allergies: ['', [Validators.minLength(10), Validators.maxLength(200)]]
    });
  }

  private toDate(value: any): Date | null {
    if (!value) {
      return null;
    }

    if (value instanceof Date) {
      return value;
    }

    const dateValue = new Date(value);
    return isNaN(dateValue.getTime()) ? null : dateValue;
  }

  private formatDateValue(value: any): string | null {
    if (!value) {
      return null;
    }

    if (value instanceof Date) {
      return value.toISOString().split('T')[0];
    }

    return String(value);
  }

  onSave(): void {
    if (this.clientForm.valid) {
      const formValue = {
        ...this.clientForm.value,
        dateOfBirth: this.formatDateValue(this.clientForm.get('dateOfBirth')?.value)
      };

      // if (form data + client.id) exists, meaning the form is editing data of an existing client, hence call the editData() method 
      if (this.isEditMode && this.data.client?.id) {
        //editData() method takes client id and form data as parameters
        this.clientService.editData(this.data.client.id, formValue).subscribe({

          // you edit the data, send to backend, observabe waits for a response, and when it's received next runs and shows success message and closes the dialog, 
          // and also sends the response data to the dialogRef.close() method, which will be used in the client-reg.component.ts file to show the highlight effect on the edited row
          // response stores the received data from the backend through the observable. it could be something like ;
          // {
          //   "id": 5,
          //   "firstName": "John",
          //   "lastName": "Doe"
          // }
          next: (response) => {
            this.messageService.showSuccess('Client updated successfully');
            // close is a MatDialogRef function. It closes the dialog and it can also pass the data back to the component that opened the dialog, 
            // in this case, it's the client-reg.component.ts file
            // which will receive the response data and use it to show the highlight effect on the edited row
            this.dialogRef.close(response);
          },
          // didn't resceive the response from backend, use subscribe's error parameter to show error message
          error: (err) => this.messageService.showError('Update failed: ' + err.message)
        });

        // if the form is not in edit mode, meaning it's adding a new client, hence call the serviceCall() method to add a new client
      } else {
        this.clientService.serviceCall(formValue).subscribe({
          next: (response) => {
            this.messageService.showSuccess('Client added successfully');
            this.dialogRef.close(response);
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

  getClientLastVisitedDate(): void {
    this.clientService.getData()
  }

}
