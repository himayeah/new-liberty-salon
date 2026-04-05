import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ServiceService } from 'src/app/services/service/service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ServiceCategoryService } from 'src/app/services/service-category/service-category.service';

@Component({
    selector: 'app-service-form',
    templateUrl: './service-form.component.html'
})
export class ServiceFormComponent implements OnInit {
    serviceForm: FormGroup;
    isButtonDisabled = false;
    submitted = false;
    categories: any[] = [];

    constructor(
        private fb: FormBuilder,
        private serviceService: ServiceService,
        private serviceCategoryService: ServiceCategoryService,
        private messageService: MessageServiceService,
        public dialogRef: MatDialogRef<ServiceFormComponent>,
        @Inject(MAT_DIALOG_DATA) public data: any
    ) {
        this.serviceForm = this.fb.group({
            serviceName: ['', Validators.required],
            duration: [30, Validators.min(1)],
            price: [0, Validators.min(0)],
            commission: [0, [Validators.min(0), Validators.max(100)]],
            colorCode: ['#ffffff'],
            isActive: [true],
            description: [''],
            serviceCategory: [null, Validators.required]
        });

        if (data && data.service) {
            // Map backend is_active to form control isActive
            const patch = {
                ...data.service,
                isActive: data.service.is_active ?? true,
                serviceCategory: data.service.serviceCategory || null
            };
            this.serviceForm.patchValue(patch);
        }
    }

    ngOnInit(): void {
        this.fetchCategories();
    }

    fetchCategories(): void {
        this.serviceCategoryService.getData().subscribe({
            next: (response: any[]) => {
                this.categories = response || [];
            },
            error: (error) => {
                this.messageService.showError('Error fetching categories: ' + error.message);
            }
        });
    }

    // Comparison function for mat-select to work with objects
    compareCategories(c1: any, c2: any): boolean {
        return c1 && c2 ? c1.id === c2.id : c1 === c2;
    }

    get f() { return this.serviceForm.controls; }

    isInvalid(controlName: string, errorType: string): boolean {
        const control = this.f[controlName];
        return (control.touched || this.submitted) && control.hasError(errorType);
    }

    onSubmit(): void {
        this.submitted = true;
        if (this.serviceForm.invalid) return;

        this.isButtonDisabled = true;
        const rawValue = this.serviceForm.value;
        const payload: any = { ...rawValue, is_active: !!rawValue.isActive };
        delete payload.isActive;

        if (this.data.mode === 'add') {
            this.serviceService.serviceCall(payload).subscribe({
                next: (response) => {
                    this.messageService.showSuccess('Saved Successfully!');
                    this.dialogRef.close(response);
                },
                error: (error) => this.handleError(error)
            });
        } else {
            this.serviceService.editService(this.data.service.id, payload).subscribe({
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
        this.serviceForm.reset({
            serviceName: '',
            duration: 30,
            price: 0,
            commission: 0,
            colorCode: '#ffffff',
            isActive: true,
            description: '',
            serviceCategory: null
        });
        if (this.data.mode === 'edit') {
            const patch = { ...this.data.service, isActive: this.data.service.is_active ?? true };
            this.serviceForm.patchValue(patch);
        }
        this.submitted = false;
        this.isButtonDisabled = false;
    }

    private handleError(error: any): void {
        const message = error?.message ?? error?.error?.message ?? 'Unknown error';
        this.messageService.showError('Action failed: ' + message);
        this.isButtonDisabled = false;
    }
}
