import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import { MatDividerModule } from '@angular/material/divider';
import { BillingService } from 'src/app/services/billing/billing.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-billing-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
    MatDividerModule
  ],
  templateUrl: './billing-form.component.html',
  styleUrl: './billing-form.component.scss'
})
export class BillingFormComponent implements OnInit {
  billingForm: FormGroup;
  mode: 'add' | 'edit' = 'add';
  submitted = false;
  isButtonDisabled = false;

  billingCategories = [
    { value: 'SALON_SERVICE', viewValue: 'Salon Service' },
    { value: 'PRODUCT_SALE', viewValue: 'Product Sale' },
    { value: 'OTHER', viewValue: 'Other' }
  ];

  paymentStatuses = [
    { value: 'PAID', viewValue: 'Paid' },
    { value: 'PENDING', viewValue: 'Pending' },
    { value: 'PARTIALLY_PAID', viewValue: 'Partially Paid' }
  ];

  paymentMethods = [
    { value: 'CASH', viewValue: 'Cash' },
    { value: 'CARD', viewValue: 'Card' },
    { value: 'ONLINE_TRANSFER', viewValue: 'Online Transfer' }
  ];

  constructor(
    private fb: FormBuilder,
    private billingService: BillingService,
    private messageService: MessageServiceService,
    public dialogRef: MatDialogRef<BillingFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.mode = data?.mode || 'add';
    this.billingForm = this.fb.group({
      clientName: ['', [Validators.required]],
      billingCategory: ['', [Validators.required]],
      clientType: [1, [Validators.required]],
      paymentStatus: ['', [Validators.required]],
      billingDate: [new Date(), [Validators.required]],
      paymentMethod: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    if (this.mode === 'edit' && this.data.billing) {
      this.billingForm.patchValue(this.data.billing);
      if (this.data.billing.billingDate) {
          this.billingForm.get('billingDate')?.setValue(new Date(this.data.billing.billingDate));
      }
    }
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.billingForm.invalid) return;

    this.isButtonDisabled = true;
    const formValue = { ...this.billingForm.value };
    
    // Convert date to timestamp for backend
    if (formValue.billingDate instanceof Date) {
        formValue.billingDate = formValue.billingDate.getTime();
    }

    if (this.mode === 'add') {
      this.billingService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Billing added successfully!');
          this.dialogRef.close(response);
        },
        error: (error) => {
          this.messageService.showError('Error adding billing: ' + (error.message || 'Unknown error'));
          this.isButtonDisabled = false;
        }
      });
    } else {
        // Edit logic could go here if implemented in the service
        this.isButtonDisabled = false;
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
