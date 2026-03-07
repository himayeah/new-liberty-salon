import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { SupplierServiceService } from 'src/app/services/supplier/supplier-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { SupplierFormComponent } from './supplier-form/supplier-form.component';

@Component({
  selector: 'app-supplier',
  templateUrl: './supplier.component.html',
  styleUrl: './supplier.component.scss'
})
export class SupplierComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['supplierName', 'contactName', 'phoneNumber', 'emailAddress', 'address', 'paymentTerms', 'actions'];

  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private supplierService: SupplierServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.supplierService.getData().subscribe({
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

  openAddSupplierModal(): void {
    const dialogRef = this.dialog.open(SupplierFormComponent, {
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
    const dialogRef = this.dialog.open(SupplierFormComponent, {
      width: '700px',
      data: { mode: 'edit', supplier: data }
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
    this.supplierService.deleteData(data.id).subscribe({
      next: () => {
        this.messageService.showSuccess('Deleted Successfully!');
        this.populateData();
      },
      error: (error) => this.messageService.showError('Delete failed: ' + error.message)
    });
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
