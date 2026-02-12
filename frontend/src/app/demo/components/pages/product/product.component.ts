import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatTableDataSource } from '@angular/material/table';
import { ProductServiceService } from 'src/app/services/product/product-service.service';
import { ProductCategoryServiceService } from 'src/app/services/product-category/product-category-service.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss']
})
export class ProductComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  productManagementForm: FormGroup;
  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['productName', 'categoryName', 'brand', 'productDescription', 'unit', 'purchasePrice', 'sellingPrice', 'barcode', 'sku', 'isTaxable', 'reOrderLevel', 'actions'];

  categories: any[] = [];

  isButtonDisabled = false;
  submitted = false;
  saveButtonLabel = 'Save';
  mode: 'add' | 'edit' = 'add';
  selectedData: any = null;
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private fb: FormBuilder,
    private productService: ProductServiceService,
    private messageService: MessageServiceService,
    private productCategoryService: ProductCategoryServiceService
  ) {
    this.productManagementForm = this.fb.group({
      productName: ['', Validators.required],
      categoryName: ['', Validators.required],
      brand: [''],
      productDescription: [''],
      unit: ['', Validators.required],
      purchasePrice: [''],
      sellingPrice: [''],
      barcode: [''],
      sku: [''],
      isTaxable: [false],
      reOrderLevel: [''],
    });
  }

  ngOnInit(): void {
    this.populateData();
    this.fetchCategories();
  }

  fetchCategories() {
    this.productCategoryService.getData().subscribe({
      next: (response: any[]) => {
        this.categories = response || [];
      },
      error: (error) => {
        this.messageService.showError('Failed to fetch categories.');
      }
    });
  }

  get f() { return this.productManagementForm.controls; }

  isInvalid(controlName: string, errorType: string): boolean {
    const control = this.f[controlName];
    return (control.touched || this.submitted) && control.hasError(errorType);
  }

  populateData(): void {
    this.productService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Failed to fetch product data.');
      }
    });

  }

  onSubmit(): void {
    this.submitted = true;

    if (this.productManagementForm.invalid) return;

    this.isButtonDisabled = true;
    const formValue = this.productManagementForm.value;

    if (this.mode === 'add') {
      this.productService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.dataSource.data = [response, ...this.dataSource.data];
          this.messageService.showSuccess('Saved Successfully!');
          this.highlightRow('add', response);
          this.resetFormState();
        },
        error: (error) => this.handleError(error)
      });
    } else {
      this.productService.editData(this.selectedData?.id, formValue).subscribe({
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

  editProduct(data: any): void {
    this.productManagementForm.patchValue({
      ...data,
    });
    this.selectedData = data;
    this.saveButtonLabel = 'Update';
    this.mode = 'edit';
    this.isButtonDisabled = false;
    this.selectedRow = data;
  }

  deleteProduct(data: any): void {
    this.productService.deleteData(data.id).subscribe({
      next: () => {
        this.dataSource.data = this.dataSource.data.filter(item => item.id !== data.id);
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

  resetData(): void {
    this.productManagementForm.reset();
    this.submitted = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedData = null;
    this.isButtonDisabled = false;
  }

  //helpers
  private handleError(error: any): void {
    this.messageService.showError('Action failed:' + error.message);
    this.isButtonDisabled = false;
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

  private resetFormState(): void {
    this.resetData();
    this.isButtonDisabled = false;
    this.populateData();
  }
}
