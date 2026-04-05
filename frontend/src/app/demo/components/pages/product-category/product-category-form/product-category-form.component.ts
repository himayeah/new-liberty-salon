import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ProductCategoryServiceService } from 'src/app/services/product-category/product-category-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-product-category-form',
    templateUrl: './product-category-form.component.html',
    styleUrl: './product-category-form.component.scss'
})
export class ProductCategoryFormComponent implements OnInit {
    categoryForm: FormGroup;
    isButtonDisabled = false;
    submitted = false;

    constructor(
        private fb: FormBuilder,
        private productCategoryService: ProductCategoryServiceService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<ProductCategoryFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.categoryForm = this.fb.group({
            productCategoryName: ['', Validators.required]
        });

        if (data && data.category) {
            this.categoryForm.patchValue(data.category);
        }
    }

    ngOnInit(): void { }

    get f() { return this.categoryForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.categoryForm.invalid) return;

        this.isButtonDisabled = true;
        const payload = this.categoryForm.value;

        if (this.data.mode === 'add') {
            this.productCategoryService.serviceCall(payload).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Product Category Saved Successfully');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.productCategoryService.editData(this.data.category.id, payload).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Product Category Updated Successfully');
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
            this.categoryForm.patchValue(this.data.category);
        } else {
            this.categoryForm.reset();
        }
        this.submitted = false;
    }

    private handleError(error: any): void {
        const message = error?.message ?? error?.error?.message ?? 'Unknown error';
        this.messageService.showError('Action failed: ' + message);
        this.isButtonDisabled = false;
    }
}
