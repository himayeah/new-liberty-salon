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
import { GrnService } from 'src/app/services/grn/grn.service';
import { ProductServiceService } from 'src/app/services/product/product-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-grn-form',
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
    templateUrl: './grn-form.component.html',
    styleUrl: './grn-form.component.scss'
})
export class GrnFormComponent implements OnInit {
    grnForm: FormGroup;
    isButtonDisabled = false;
    submitted = false;
    products: any[] = [];

    constructor(
        private fb: FormBuilder,
        private grnService: GrnService,
        private productService: ProductServiceService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<GrnFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.grnForm = this.fb.group({
            purchaseOrderId: [null, Validators.required],
            productId: [null, Validators.required],
            orderedDate: ['', Validators.required],
            receivedDate: ['', Validators.required],
            orderedQuantity: [0, [Validators.required, Validators.min(1)]],
            receivedQuantity: [0, [Validators.required, Validators.min(0)]],
            remarks: [''],
            status: ['PENDING']
        });

        if (data && data.grn) {
            this.grnForm.patchValue(data.grn);
        } else if (data && data.purchaseOrderId) {
            this.grnForm.patchValue({ purchaseOrderId: data.purchaseOrderId });
        }
    }

    ngOnInit(): void {
        this.fetchProducts();
        this.listenToProductChange();

        // Auto-fill PO Date if available
        if (this.data.mode === 'add' && this.data.poDate) {
            this.grnForm.patchValue({ orderedDate: this.data.poDate });
        }
    }

    listenToProductChange(): void {
        this.grnForm.get('productId')!.valueChanges.subscribe(productId => {
            if (!productId || !this.data.poDetails) return;

            // Match product id to its name first
            const selectedProduct = this.products.find(p => p.id === productId);
            if (!selectedProduct) return;

            // Find in poDetails by productName
            const poItem = this.data.poDetails.find((d: any) => d.productName === selectedProduct.productName);
            if (poItem) {
                this.grnForm.patchValue({
                    orderedQuantity: poItem.quantityOrdered,
                    orderedDate: this.data.poDate || this.grnForm.get('orderedDate')?.value
                });
            }
        });
    }

    fetchProducts(): void {
        this.productService.getData().subscribe({
            next: (response: any[]) => {
                const allProducts = response || [];
                if (this.data.poDetails) {
                    const poProductNames = this.data.poDetails.map((d: any) => d.productName);
                    this.products = allProducts.filter(p => poProductNames.includes(p.productName));
                } else {
                    this.products = allProducts;
                }
            },
            error: () => this.messageService.showError('Failed to fetch products')
        });
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.grnForm.invalid) return;

        this.isButtonDisabled = true;
        const formValue = this.grnForm.value;

        if (this.data.mode === 'add') {
            this.grnService.addGrn(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('GRN Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.grnService.updateGrn(this.data.grn.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('GRN Updated Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        }
    }

    onCancel(): void {
        this.dialogRef.close(false);
    }

    private handleError(error: any): void {
        this.messageService.showError('Action failed: ' + error.message);
        this.isButtonDisabled = false;
    }
}
