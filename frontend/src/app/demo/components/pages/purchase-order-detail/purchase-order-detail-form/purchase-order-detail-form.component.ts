import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductServiceService } from 'src/app/services/product/product-service.service';
import { PurchaseOrderDetailServiceService } from 'src/app/services/purchase-order-detail/purchase-order-detail-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-purchase-order-detail-form',
  templateUrl: './purchase-order-detail-form.component.html'
})
export class PurchaseOrderDetailFormComponent implements OnInit {
  itemForm: FormGroup;
  products: any[] = [];
  isEditMode = false;

  constructor(
    private fb: FormBuilder,
    private productService: ProductServiceService,
    private purchaseOrderDetailService: PurchaseOrderDetailServiceService,
    private messageService: MessageServiceService,
    public dialogRef: MatDialogRef<PurchaseOrderDetailFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.isEditMode = data.mode === 'edit';
    this.itemForm = this.fb.group({
      id: [null],
      purchaseOrderId: [data.purchaseOrderId, Validators.required],
      orderNumber: [{ value: data.orderNumber, disabled: true }],
      productId: [null, Validators.required],
      quantityOrdered: [0, [Validators.required, Validators.min(1)]],
      unitPrice: [0, [Validators.required, Validators.min(0)]]
    });

    if (this.isEditMode && data.item) {
      this.itemForm.patchValue(data.item);
    }
  }

  ngOnInit(): void {
    this.loadProducts();
    this.listenToProductChange();
  }

  listenToProductChange(): void {
    this.itemForm.get('productId')?.valueChanges.subscribe(productId => {
      if (productId) {
        const selectedProduct = this.products.find(p => p.id === productId);
        if (selectedProduct) {
          this.itemForm.patchValue({
            unitPrice: selectedProduct.purchasePrice
          });
        }
      }
    });
  }

  loadProducts(): void {
    this.productService.getData().subscribe({
      next: (data) => this.products = data,
      error: (err) => this.messageService.showError('Error loading products')
    });
  }

  onSubmit(): void {
    if (this.itemForm.valid) {
      const formData = this.itemForm.getRawValue();
      const action = this.isEditMode
        ? this.purchaseOrderDetailService.editData(formData.id, formData)
        : this.purchaseOrderDetailService.serviceCall(formData);

      action.subscribe({
        next: (result) => {
          this.messageService.showSuccess(this.isEditMode ? 'Item updated' : 'Item added');
          this.dialogRef.close(true);
        },
        error: (err) => this.messageService.showError('Error saving item: ' + err.message)
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }
}
