import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSort, MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatCardModule } from '@angular/material/card';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { PurchaseOrderServiceService } from 'src/app/services/purchase-order/purchase-order-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { PurchaseOrderFormComponent } from './purchase-order-form/purchase-order-form.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-purchase-order',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatTooltipModule,
    MatCardModule,
    MatDialogModule,
    PurchaseOrderFormComponent
  ],
  templateUrl: './purchase-order.component.html',
  styleUrl: './purchase-order.component.scss'
})
export class PurchaseOrderComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['orderNumber', 'supplier', 'orderDate', 'expectedDeliveryDate', 'totalAmount', 'status', 'actions'];

  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private purchaseOrderService: PurchaseOrderServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.purchaseOrderService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error: any) => {
        this.messageService.showError('Error fetching data: ' + error.message);
      }
    });
  }

  openAddPurchaseOrderModal(): void {
    const dialogRef = this.dialog.open(PurchaseOrderFormComponent, {
      width: '700px',
      data: { mode: 'add' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('add', result);
      }
    });
  }

  editData(data: any): void {
    const dialogRef = this.dialog.open(PurchaseOrderFormComponent, {
      width: '700px',
      data: { mode: 'edit', order: data }
    });

    this.selectedRow = data;

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('edit', result);
      }
      this.selectedRow = null;
    });
  }

  deleteData(data: any): void {
    if (confirm('Are you sure you want to delete this purchase order?')) {
      this.purchaseOrderService.deleteData(data.id).subscribe({
        next: () => {
          this.messageService.showSuccess('Deleted Successfully!');
          this.populateData();
        },
        error: (error) => this.messageService.showError('Delete failed: ' + error.message)
      });
    }
  }

  goToDetails(data: any): void {
    this.router.navigate(['/pages/purchase-order-detail', data.id]);
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  refreshData(): void {
    this.populateData();
    if (this.dataSource) {
      this.dataSource.filter = '';
    }
    this.selectedRow = null;
    if (this.paginator) this.paginator.firstPage();
  }

  private highlightRow(mode: 'add' | 'edit', data: any): void {
    if (mode === 'add') {
      this.lastAddedRow = data;
      setTimeout(() => this.lastAddedRow = null, 3000);
    } else {
      this.lastEditedRow = data;
      setTimeout(() => this.lastEditedRow = null, 3000);
    }
  }
}
