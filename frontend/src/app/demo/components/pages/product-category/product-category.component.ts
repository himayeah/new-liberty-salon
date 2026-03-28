import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ProductCategoryServiceService } from 'src/app/services/product-category/product-category-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ProductCategoryFormComponent } from './product-category-form/product-category-form.component';

@Component({
  selector: 'app-product-category',
  templateUrl: './product-category.component.html',
  styleUrl: './product-category.component.scss'
})
export class ProductCategoryComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['productCategoryName', 'actions'];

  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private productCategoryService: ProductCategoryServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.productCategoryService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Failed to fetch product categories.');
      }
    });
  }

  openAddCategoryModal(): void {
    const dialogRef = this.dialog.open(ProductCategoryFormComponent, {
      width: '500px',
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
    const dialogRef = this.dialog.open(ProductCategoryFormComponent, {
      width: '500px',
      data: { mode: 'edit', category: data }
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
    this.productCategoryService.deleteData(data.id).subscribe({
      next: () => {
        this.messageService.showSuccess('Category deleted successfully.');
        this.populateData();
      },
      error: (error) => this.messageService.showError('Failed to delete category.')
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  refreshData(): void {
    this.populateData();
    this.dataSource.filter = '';
    this.selectedRow = null;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  private highlightRow(action: 'add' | 'edit', rowData: any): void {
    if (action === 'add') {
      this.lastAddedRow = rowData;
      setTimeout(() => this.lastAddedRow = null, 3000);
    } else {
      this.lastEditedRow = rowData;
      setTimeout(() => this.lastEditedRow = null, 3000);
    }
  }
}