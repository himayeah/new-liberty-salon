import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SupplierServiceService } from 'src/app/services/supplier/supplier-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-supplier',
  templateUrl: './supplier.component.html',
  styleUrl: './supplier.component.scss'
})
export class SupplierComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  supplierForm: FormGroup;
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['supplierName', 'contactName', 'phoneNumber', 'emailAddress', 'address', 'paymentTerms', 'actions'];

  isButtonDisabled = false;
  saveButtonLabel = 'Save';
  submitted = false;
  selectedData: any = null;
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;
  mode: 'add' | 'edit' = 'add';

  constructor(
    private fb: FormBuilder,
    private supplierService: SupplierServiceService,
    private messageService: MessageServiceService
  ) {
    this.supplierForm = this.fb.group({
      supplierName: ['', Validators.required],
      contactName: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      emailAddress: ['', Validators.required],
      address: ['', Validators.required],
      paymentTerms: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.populateData();
  }

  get f() {
    return this.supplierForm.controls;
  }

  isInvalid(controlName: string, errorType: string): boolean {
    const control = this.f[controlName];
    return (control.touched || this.submitted) && control.hasError(errorType);
  }

  populateData(): void {
    this.supplierService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error: any) => {
        this.messageService.showError('Error fetching data:' + error.message);
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    if (this.supplierForm.invalid) return;

    this.isButtonDisabled = true;
    const formValue = this.supplierForm.value;

    if (this.mode === 'add') {
      this.supplierService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.dataSource.data = [response, ...this.dataSource.data];
          this.messageService.showSuccess('Saved Successfully!');
          this.highlightRow('add', response);
          this.resetFormState();
        },
        error: (error) => this.handleError(error)
      });
    } else {
      this.supplierService.editData(this.selectedData?.id, formValue).subscribe({
        next: (response) => {
          const index = this.dataSource.data.findIndex(item => item.id === this.selectedData?.id);
          if (index > -1) this.dataSource.data[index] = response;
          this.messageService.showSuccess('Updated Successfully!');
          this.highlightRow('edit', response);
          this.resetFormState();
        },
        error: (error) => this.handleError(error)
      });
    }
  }

  editData(data: any): void {
    this.supplierForm.patchValue({
      ...data
    });
    this.selectedData = data;
    this.saveButtonLabel = 'Update';
    this.mode = 'edit';
    this.isButtonDisabled = false;
    this.selectedRow = data;
  }

  deleteData(data: any): void {
    this.supplierService.deleteData(data.id).subscribe({
      next: () => {
        this.messageService.showSuccess('Deleted Successfully!');
        this.populateData();
      },
      error: (error) => this.messageService.showError('Delete failed:' + error.message)
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
    this.supplierForm.reset();
    this.supplierForm.enable();
    this.submitted = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedRow = null;
    this.isButtonDisabled = false;
  }

  private handleError(error: any): void {
    this.messageService.showError('Error: ' + error.message);
  }

  private highlightRow(mode: 'add' | 'edit', data: any): void {
    if (mode === 'add') this.lastAddedRow = data;
    else this.lastEditedRow = data;
  }

  private resetFormState(): void {
    this.resetData();
    this.isButtonDisabled = false;
    this.populateData();
  }

}
