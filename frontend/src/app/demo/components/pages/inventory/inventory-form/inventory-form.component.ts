import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { InventoryServiceService } from 'src/app/services/inventory/inventory-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ProductServiceService } from 'src/app/services/product/product-service.service'; // Fixed service name

@Component({
  selector: 'app-inventory-form',
  templateUrl: './inventory-form.component.html',
  styleUrls: ['./inventory-form.component.scss']
})
export class InventoryFormComponent implements OnInit {
  inventoryForm!: FormGroup;
  isEditMode: boolean = false;
  products: any[] = [];

  constructor(
    private fb: FormBuilder,
    private dialogRef: MatDialogRef<InventoryFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private inventoryService: InventoryServiceService,
    private messageService: MessageServiceService,
    private productService: ProductServiceService // Fixed service name
  ) { }

  ngOnInit(): void {
    this.isEditMode = this.data.mode === 'edit';
    this.initForm();
    this.loadProducts();

    if (this.isEditMode && this.data.inventory) {
      this.inventoryForm.patchValue({
        ...this.data.inventory,
        productId: this.data.inventory.product?.id
      });
    }
  }

  loadProducts(): void {
    this.productService.getData().subscribe({ // Fixed method name
      next: (data: any) => {
        this.products = data;
      },
      error: (err: any) => {
        this.messageService.showError('Error loading products: ' + err.message);
      }
    });
  }

  initForm(): void {
    this.inventoryForm = this.fb.group({
      productId: ['', Validators.required],
      currentStock: [0, [Validators.required, Validators.min(0)]],
      minimumStock: [0, [Validators.required, Validators.min(0)]],
      maximumStock: [0, [Validators.required, Validators.min(0)]],
      shelfLocation: ['', Validators.required],
      lastRestockedDate: [new Date(), Validators.required]
    });
  }

  onSave(): void {
    if (this.inventoryForm.valid) {
      const formValue = this.inventoryForm.value;
      
      if (this.isEditMode && this.data.inventory?.id) {
        this.inventoryService.editData(this.data.inventory.id, formValue).subscribe({
          next: (res) => {
            this.messageService.showSuccess('Inventory updated successfully');
            this.dialogRef.close(res);
          },
          error: (err) => this.messageService.showError('Update failed: ' + err.message)
        });
      } else {
        this.inventoryService.serviceCall(formValue).subscribe({
          next: (res) => {
            this.messageService.showSuccess('Stock added successfully');
            this.dialogRef.close(res);
          },
          error: (err) => this.messageService.showError('Save failed: ' + err.message)
        });
      }
    } else {
      this.messageService.showError('Please fill in all required fields correctly');
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
