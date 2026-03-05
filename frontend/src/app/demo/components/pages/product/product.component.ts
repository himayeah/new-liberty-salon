import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { ProductServiceService } from 'src/app/services/product/product-service.service';
import { ProductFormComponent } from './product-form/product-form.component';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['productName', 'categoryName', 'brand', 'unit', 'purchasePrice', 'sellingPrice', 'barcode', 'sku', 'productDescription', 'reOrderLevel', 'isTaxable', 'actions'];
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private productService: ProductServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.productService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Failed to fetch product data');
      }
    });

  }

  openAddProductModal(): void {
    const dialogRef = this.dialog.open(ProductFormComponent, {
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

  editProduct(data: any): void {
    const dialogRef = this.dialog.open(ProductFormComponent, {
      width: '700px',
      data: { mode: 'edit', product: data }
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

  deleteProduct(data: any): void {
    this.productService.deleteData(data.id).subscribe({
      next: () => {
        this.messageService.showSuccess('Deleted Successfully!');
        this.populateData();
      },
      error: (error) => this.handleError(error)
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  refreshData(): void {
    this.populateData();
    this.dataSource.filter = '';
    this.selectedRow = null;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }


  //helpers
  private handleError(error: any): void {
    this.messageService.showError('Action failed:' + error.message);
  }

  private highlightRow(action: 'add' | 'edit', rowData: any): void {
    if (action === 'add') {
      this.lastAddedRow = rowData;
      setTimeout(() => this.lastAddedRow = null, 3000);
    } else if (action === 'edit') {
      this.lastEditedRow = rowData;
      setTimeout(() => this.lastEditedRow = null, 3000);
    }
  }
}
