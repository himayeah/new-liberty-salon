import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, Validators, ReactiveFormsModule, FormsModule, FormGroup } from '@angular/forms';
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
import { MatCheckboxModule } from '@angular/material/checkbox';
import { TaxFormComponent } from './tax-form/tax-form.component';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { TaxServiceService } from 'src/app/services/tax/tax-service.service';

@Component({
  selector: 'app-tax',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
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
    MatCheckboxModule,
    TaxFormComponent
  ],
  templateUrl: './tax.component.html',
  styleUrl: './tax.component.scss'
})
export class TaxComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['taxName', 'taxRate', 'effectiveDate', 'isActive', 'actions'];

  isButtonDisabled = false;
  submitted = false;
  saveButtonLabel = 'Save';
  mode: 'add' | 'edit' = 'add';
  selectedData: any = null;
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;
  constructor(
    private taxService: TaxServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.populateData();
  }



  populateData(): void {
    this.taxService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Error fetching data: ' + error.message);
      }
    });
  }

  openAddTaxModal(): void {
    const dialogRef = this.dialog.open(TaxFormComponent, {
      width: '600px',
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
    const dialogRef = this.dialog.open(TaxFormComponent, {
      width: '600px',
      data: { mode: 'edit', tax: data }
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

  viewData(data: any): void {
    // view logic here or can open a dialog
  }

  deleteData(data: any): void {
    this.taxService.deleteData(data.id).subscribe({
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
    this.dataSource.filter = '';
    this.selectedRow = null;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  resetData(): void {
    this.submitted = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedRow = null;
    this.isButtonDisabled = false;
  }

  private handleError(error: any): void {
    this.messageService.showError('Action failed: ' + error.message);
    this.isButtonDisabled = false;
  }

  private highlightRow(type: 'add' | 'edit', response: any): void {
    if (type === 'add') this.lastAddedRow = response;
    else this.lastEditedRow = response;
    setTimeout(() => {
      this.lastAddedRow = null;
      this.lastEditedRow = null;
    }, 3000);
  }

  private resetFormState(): void {
    this.resetData();
    this.isButtonDisabled = false;
    this.populateData();
  }

}
