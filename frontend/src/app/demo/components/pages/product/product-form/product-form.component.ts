import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductServiceService } from 'src/app/services/product/product-service.service';
import { ProductCategoryServiceService } from 'src/app/services/product-category/product-category-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-product-form',
    templateUrl: './product-form.component.html',
    styleUrl: './product-form.component.scss'
})
export class ProductFormComponent implements OnInit {
    productManagementForm: FormGroup;
    categories: any[] = [];
    isButtonDisabled = false;
    submitted = false;

    constructor(
        private fb: FormBuilder,
        private productService: ProductServiceService,
        private productCategoryService: ProductCategoryServiceService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<ProductFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.productManagementForm = this.fb.group({
            productName: ['', Validators.required],
            categoryName: ['', Validators.required],
            productDescription: [''],
            unit: ['', Validators.required],
            purchasePrice: [''],
            sellingPrice: [''],
            barcode: [''],
            sku: [''],
            isTaxable: [false],
            reOrderLevel: [''],
        });

        if (data && data.product) {
            this.productManagementForm.patchValue(data.product);
        }
    }

    ngOnInit(): void {
        this.fetchCategories();
    }

    fetchCategories() {
        this.productCategoryService.getData().subscribe({
            next: (response: any[]) => {
                this.categories = response || [];
            },
            error: (error) => {
                this.messageService.showError('Failed to fetch categories.');
            }
        });
    }

    get f() { return this.productManagementForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    onSubmit(): void {
        this.submitted = true;

        if (this.productManagementForm.invalid) return;

        this.isButtonDisabled = true;
        const formValue = this.productManagementForm.value;

        if (this.data.mode === 'add') {
            this.productService.serviceCall(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Product Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.productService.editData(this.data.product.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Product Updated Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        }
    }

    private handleError(error: any): void {
        this.messageService.showError('Action failed: ' + error.message);
        this.isButtonDisabled = false;
    }

    onCancel(): void {
        this.dialogRef.close(false);
    }

    resetForm(): void {
        if (this.data.mode === 'edit') {
            this.productManagementForm.patchValue(this.data.product);
        } else {
            this.productManagementForm.reset();
        }
        this.submitted = false;
    }
}
