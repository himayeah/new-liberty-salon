import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceCategoryService } from 'src/app/services/service-category/service-category.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
    selector: 'app-service-category-form',
    templateUrl: './service-category-form.component.html'
})
export class ServiceCategoryFormComponent implements OnInit {
    serviceCategoryForm: FormGroup;
    isButtonDisabled = false;
    submitted = false;

    constructor(
        private fb: FormBuilder,
        private serviceCategoryService: ServiceCategoryService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<ServiceCategoryFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.serviceCategoryForm = this.fb.group({
            categoryName: ['', Validators.required],
            displayOrder: [0, Validators.min(0)],
            description: [''],
        });

        if (data && data.category) {
            this.serviceCategoryForm.patchValue(data.category);
        }
    }

    ngOnInit(): void { }

    get f() { return this.serviceCategoryForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.serviceCategoryForm.invalid) return;

        this.isButtonDisabled = true;
        const formValue = this.serviceCategoryForm.value;

        if (this.data.mode === 'add') {
            this.serviceCategoryService.serviceCall(formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.serviceCategoryService.editData(this.data.category.id, formValue).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Updated Successfully!');
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
        this.serviceCategoryForm.reset();
        if (this.data.mode === 'edit') {
            this.serviceCategoryForm.patchValue(this.data.category);
        }
        this.submitted = false;
        this.isButtonDisabled = false;
    }

    private handleError(error: any): void {
        this.messageService.showError('Action failed: ' + (error?.message || 'Unknown error'));
        this.isButtonDisabled = false;
    }
}
