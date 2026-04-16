import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { PurchaseOrderServiceService } from 'src/app/services/purchase-order/purchase-order-service.service';
import { PurchaseOrderDetailServiceService } from 'src/app/services/purchase-order-detail/purchase-order-detail-service.service';
import { GrnService } from 'src/app/services/grn/grn.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { PurchaseOrderDetailFormComponent } from './purchase-order-detail-form/purchase-order-detail-form.component';
import { GrnFormComponent } from '../grn/grn-form/grn-form.component';

@Component({
  selector: 'app-purchase-order-detail',
  templateUrl: './purchase-order-detail.component.html'
})
export class PurchaseOrderDetailComponent implements OnInit {
  purchaseOrderId: any;
  purchaseOrder: any;
  
  // PO Details
  poDataSource = new MatTableDataSource<any>([]);
  poColumns: string[] = ['productName', 'unitPrice', 'quantityOrdered', 'actions'];
  
  // GRN Details
  grnDataSource = new MatTableDataSource<any>([]);
  grnColumns: string[] = ['productName', 'orderedQuantity', 'receivedQuantity', 'receivedDate', 'status', 'remarks', 'actions'];

  constructor(
    private route: ActivatedRoute,
    private dialog: MatDialog,
    private purchaseOrderService: PurchaseOrderServiceService,
    private purchaseOrderDetailService: PurchaseOrderDetailServiceService,
    private grnService: GrnService,
    private messageService: MessageServiceService
  ) {}

  ngOnInit(): void {
    this.purchaseOrderId = this.route.snapshot.paramMap.get('id');
    if (this.purchaseOrderId) {
      this.loadPurchaseOrder();
      this.loadPurchaseOrderDetails();
      this.loadGrnDetails();
    }
  }

  loadPurchaseOrder(): void {
    this.purchaseOrderService.getById(this.purchaseOrderId).subscribe({
      next: (data) => this.purchaseOrder = data,
      error: () => this.messageService.showError('Error loading purchase order')
    });
  }

  // --- PO DETAIL CRUD ---
  loadPurchaseOrderDetails(): void {
    this.purchaseOrderDetailService.getByPurchaseOrderId(this.purchaseOrderId).subscribe({
      next: (data) => this.poDataSource.data = data,
      error: () => this.messageService.showError('Error loading items')
    });
  }

  openPoDetailModal(mode: 'add' | 'edit', item?: any): void {
    const dialogRef = this.dialog.open(PurchaseOrderDetailFormComponent, {
      width: '600px',
      data: {
        mode,
        purchaseOrderId: this.purchaseOrderId,
        orderNumber: this.purchaseOrder?.orderNumber,
        item
      }
    });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.loadPurchaseOrderDetails();
        this.loadPurchaseOrder(); // Reload status/total
      }
    });
  }

  deletePoDetail(id: any): void {
    if (confirm('Are you sure you want to delete this item?')) {
      this.purchaseOrderDetailService.deleteData(id).subscribe({
        next: () => {
          this.messageService.showSuccess('Item deleted');
          this.loadPurchaseOrderDetails();
          this.loadPurchaseOrder(); // Reload status/total
        },
        error: () => this.messageService.showError('Delete failed')
      });
    }
  }

  // --- GRN CRUD ---
  loadGrnDetails(): void {
    this.grnService.getByPurchaseOrderId(this.purchaseOrderId).subscribe({
      next: (data) => this.grnDataSource.data = data,
      error: () => this.messageService.showError('Error loading GRN records')
    });
  }

  openGrnModal(mode: 'add' | 'edit', grn?: any): void {
    const dialogRef = this.dialog.open(GrnFormComponent, {
      width: '600px',
      data: {
        mode,
        purchaseOrderId: this.purchaseOrderId,
        poDate: this.purchaseOrder?.orderDate,
        poDetails: this.poDataSource.data,
        grn
      }
    });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.loadGrnDetails();
        this.loadPurchaseOrder(); // Reload status
      }
    });
  }

  deleteGrn(id: any): void {
    if (confirm('Are you sure you want to delete this GRN record?')) {
      this.grnService.deleteGrn(id).subscribe({
        next: () => {
          this.messageService.showSuccess('GRN record deleted');
          this.loadGrnDetails();
          this.loadPurchaseOrder(); // Reload status
        },
        error: () => this.messageService.showError('Delete failed')
      });
    }
  }
}
