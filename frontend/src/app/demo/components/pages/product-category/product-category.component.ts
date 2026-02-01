import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ProductCategoryServiceService } from 'src/app/services/product-category/product-category-service.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-product-category',
  templateUrl: './product-category.component.html',
  styleUrl: './product-category.component.scss'
})
export class ProductCategoryComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  productCategoryForm: FormGroup;
  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['productCategoryName', 'actions'];

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
    private productCategoryService: ProductCategoryServiceService,
    private messageService: MessageServiceService
  ) {
    this.productCategoryForm = this.fb.group({
      productCategoryName: ['']
    });
    
   }

   ngOnInit(): void {
    this.populateData();
   }

   get f() { return this.productCategoryForm.controls;}
   isInvalid(controlName: string, errorType: string): boolean {
    const control = this.productCategoryForm.get(controlName);
    return control ? control.invalid && (control.dirty || control.touched) : false;
  }

  populateData(): void {
    this.productCategoryService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource.data = response;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Failed to fetch product categories.');
      }
    });
  }

  onSubmit(): void {
    this.submitted = true;
    
    if (this.productCategoryForm.invalid) {
      return;
    }
    this.isButtonDisabled = true;
    
    const formValue = this.productCategoryForm.value;
    if (this.mode === 'add') {
      this.productCategoryService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.messageService.showSuccess('Product category added successfully.');
          this.dataSource.data = [response, ...this.dataSource.data];
          this.highlightRow('add', response);
          this.resetFormState();
        },
        error: (error) => this.handleError(error)
      });
    } else {
      this.productCategoryService.editData(this.selectedData.id, formValue).subscribe({
        next: (response) => {
          const index = this.dataSource.data.findIndex(item => item.id === this.selectedData.id);
          if (index > -1) this.dataSource.data[index] = response;
          this.messageService.showSuccess('Product category updated successfully.');
          this.highlightRow('edit', response);
          this.resetFormState();
        },
        error: (error) => this.handleError(error)
      });
    }
  }

  editData(data: any): void {
    this.productCategoryForm.patchValue ({

    });
    this.selectedData = data;
    this.mode = 'edit';
    this.saveButtonLabel = 'Update';
    this.isButtonDisabled = false;
    this.selectedRow = data;
  }

  deleteData(data: any): void {
    this.productCategoryService.deleteData(data.id).subscribe({
      next: () => {
        this.messageService.showSuccess('Product category deleted successfully.');
        this.populateData();
      },
      error: (error) => this.messageService.showError('Failed to delete product category.')
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
    this.productCategoryForm.reset();
    this.productCategoryForm.enable();
    this.submitted = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedRow = null;
    this.isButtonDisabled = false;
  }

  // helpers
  private highlightRow(action: 'add' | 'edit', rowData: any): void {
    if (action === 'add') {
      this.lastAddedRow = rowData;
      setTimeout(() => this.lastAddedRow = null, 3000);
    } else {
      this.lastEditedRow = rowData;
      setTimeout(() => this.lastEditedRow = null, 3000);
    }
  }
  
  private resetFormState(): void {
    this.productCategoryForm.reset();
    this.productCategoryForm.enable();
    this.submitted = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedData = null;
    this.selectedRow = null;
    this.isButtonDisabled = false;
  }

  private handleError(error: any): void {
    this.messageService.showError('Action failed: ' + error.message);
    this.isButtonDisabled = false;
  } 


}