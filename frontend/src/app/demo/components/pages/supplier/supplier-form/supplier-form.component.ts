import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { SupplierServiceService } from 'src/app/services/supplier/supplier-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-supplier-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatDialogModule,
        MatSelectModule
    ],
    templateUrl: './supplier-form.component.html',
    styleUrl: './supplier-form.component.scss'
})
export class SupplierFormComponent implements OnInit {
    supplierForm: FormGroup;
    isButtonDisabled = false;
    submitted = false;

    constructor(
        private fb: FormBuilder,
        private supplierService: SupplierServiceService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<SupplierFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.supplierForm = this.fb.group({
            supplierName: ['', Validators.required],
            contactName: ['', Validators.required],
            phoneNumber: ['', [Validators.required, Validators.pattern(/^[0-9+]{10,15}$/)]],
            emailAddress: ['', [Validators.required, Validators.email]],
            address: ['', Validators.required],
            paymentTerms: ['', Validators.required]
        });

        if (data && data.supplier) {
            this.supplierForm.patchValue(data.supplier);
        }
    }

    ngOnInit(): void { }

    get f() { return this.supplierForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.supplierForm.invalid) return;

        this.isButtonDisabled = true;
        const formValue = this.supplierForm.value;

        if (this.data.mode === 'add') {
            this.supplierService.serviceCall(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Supplier Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.supplierService.editData(this.data.supplier.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Supplier Updated Successfully!');
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
        if (this.data.mode === 'edit') {
            this.supplierForm.patchValue(this.data.supplier);
        } else {
            this.supplierForm.reset();
        }
        this.submitted = false;
    }

    private handleError(error: any): void {
        this.messageService.showError('Action failed: ' + error.message);
        this.isButtonDisabled = false;
    }
}
