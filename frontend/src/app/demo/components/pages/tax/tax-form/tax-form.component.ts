import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { TaxServiceService } from 'src/app/services/tax/tax-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-tax-form',
  standalone: true,
  imports: [
    CommonModule, 
    ReactiveFormsModule, 
    MatDialogModule, 
    MatFormFieldModule, 
    MatInputModule, 
    MatButtonModule, 
    MatCheckboxModule, 
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './tax-form.component.html',
  styleUrls: ['./tax-form.component.scss']
})
export class TaxFormComponent implements OnInit {
  taxForm: FormGroup;
  
  constructor(
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<TaxFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private taxService: TaxServiceService,
    private messageService: MessageServiceService
  ) {
    this.taxForm = this.fb.group({
      taxName: ['', Validators.required],
      taxRate: ['', Validators.required],
      effectiveDate: ['', Validators.required],
      isActive: [true]
    });
  }

  ngOnInit(): void {
    if (this.data?.mode === 'edit' && this.data?.tax) {
      this.taxForm.patchValue(this.data.tax);
    }
  }

  onSave(): void {
    if (this.taxForm.invalid) return;
    
    const formValue = this.taxForm.value;

    if (this.data?.mode === 'add') {
      this.taxService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Tax Saved Successfully!');
          this.dialogRef.close(response);
        },
        error: (error) => this.messageService.showError('Action failed: ' + error.message)
      });
    } else {
      this.taxService.editData(this.data.tax.id, formValue).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Tax Updated Successfully!');
          this.dialogRef.close(response);
        },
        error: (error) => this.messageService.showError('Action failed: ' + error.message)
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
