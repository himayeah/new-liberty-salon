import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { PurchaseOrderServiceService } from 'src/app/services/purchase-order/purchase-order-service.service';
import { SupplierServiceService } from 'src/app/services/supplier/supplier-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-purchase-order-form',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,
        FormsModule,
        MatDialogModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule
    ],
    templateUrl: './purchase-order-form.component.html',
    styleUrl: './purchase-order-form.component.scss'
})
export class PurchaseOrderFormComponent implements OnInit {
    purchaseOrderForm: FormGroup;
    isButtonDisabled = false;
    submitted = false;
    suppliers: any[] = [];

    constructor(
        private fb: FormBuilder,
        private purchaseOrderService: PurchaseOrderServiceService,
        private supplierService: SupplierServiceService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<PurchaseOrderFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.purchaseOrderForm = this.fb.group({
            orderNumber: ['', Validators.required],
            supplierId: ['', Validators.required],
            orderDate: ['', Validators.required],
            expectedDeliveryDate: ['', Validators.required],
            status: ['', Validators.required],
            totalAmount: ['', Validators.required],
            notes: ['']
        });

        if (data && data.order) {
            this.purchaseOrderForm.patchValue({
                ...data.order,
                supplierId: data.order.supplier ? data.order.supplier.id : data.order.supplierId || null
            });
        }
    }

    ngOnInit(): void {
        this.fetchSuppliers();
    }

    fetchSuppliers(): void {
        this.supplierService.getData().subscribe({
            next: (response: any[]) => this.suppliers = response || [],
            error: (error) => this.messageService.showError('Failed to fetch suppliers')
        });
    }

    get f() { return this.purchaseOrderForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.purchaseOrderForm.invalid) return;

        this.isButtonDisabled = true;
        const formValue = {
            ...this.purchaseOrderForm.value,
            orderDate: this.formatDate(this.purchaseOrderForm.value.orderDate),
            expectedDeliveryDate: this.formatDate(this.purchaseOrderForm.value.expectedDeliveryDate)
        };

        if (this.data.mode === 'add') {
            this.purchaseOrderService.serviceCall(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Purchase Order Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.purchaseOrderService.editData(this.data.order.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Purchase Order Updated Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        }
    }

    private formatDate(date: any): string | null {
        if (!date) return null;
        const d = new Date(date);
        return d.getFullYear() + '-' +
            String(d.getMonth() + 1).padStart(2, '0') + '-' +
            String(d.getDate()).padStart(2, '0');
    }

    onCancel(): void {
        this.dialogRef.close(false);
    }


    resetForm(): void {
        if (this.data.mode === 'edit') {
            this.purchaseOrderForm.patchValue(this.data.order);
        } else {
            this.purchaseOrderForm.reset();
        }
        this.submitted = false;
    }

    private handleError(error: any): void {
        const message = error?.message ?? error?.error?.message ?? 'Unknown error';
        this.messageService.showError('Action failed: ' + message);
        this.isButtonDisabled = false;
    }
}
