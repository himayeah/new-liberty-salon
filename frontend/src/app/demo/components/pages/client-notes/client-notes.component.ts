import { Component, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { OnInit } from '@angular/core';
import { FormGroup, Validators } from '@angular/forms';
import { FormBuilder } from '@angular/forms';
import { ClientNotesServiceService } from 'src/app/services/client-notes/client-notes-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';

@Component({
  selector: 'app-client-notes',
  templateUrl: './client-notes.component.html',
  styleUrl: './client-notes.component.scss'
})
export class ClientNotesComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  clientNotesForm: FormGroup;
  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['clientName', 'stylistName', 'noteType', 'noteContent', 'noteDate', 'actions'];

  clients: any[] = [];
  stylists: any[] = [];

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
    private clientNotesService: ClientNotesServiceService,
    private messageService: MessageServiceService,
    private employeeRegService: EmployeeRegServicesService,
    private clientRegService: ClientRegServiceService
  ) {
    this.clientNotesForm = this.fb.group({
      clientName: ['', Validators.required],
      stylistName: ['', Validators.required],
      noteType: ['', Validators.required],
      noteContent: [''],
      noteDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.populateData();
    this.fetchStylists();
    this.fetchClients();
  }

  fetchStylists(): void {
    this.employeeRegService.getData().subscribe({
      next: (response: any[]) => {
        this.stylists = response.filter(employee => employee.designation === 'Bridal stylist' || employee.designation === 'Groom stylist');
      },
      error: (error) => {
        this.messageService.showError('Error fetching stylists: ' + error.message);
      }
    });
  }

  fetchClients(): void {
    this.clientRegService.getData().subscribe({
      next: (response: any[]) => {
        this.clients = response;
      },
      error: (error) => {
        this.messageService.showError('Error fetching clients: ' + error.message);
      }
    });
  }

  get f() { return this.clientNotesForm.controls; }

  isInvalid(controlName: string, errorType: string): boolean {
    const control = this.f[controlName];
    return (control.touched || this.submitted) && control.hasError(errorType);
  }

  populateData(): void {
    this.clientNotesService.getData().subscribe({
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

  onSubmit(): void {
    this.submitted = true;
    if (this.clientNotesForm.invalid) return;

    this.isButtonDisabled = true;
    const formValue = this.clientNotesForm.value;

    if (this.mode === 'add') {
      this.clientNotesService.serviceCall(formValue).subscribe({
        next: (response) => {
          this.dataSource.data = [response, ...this.dataSource.data];
          this.messageService.showSuccess('Saved Successfully!');
          this.highlightRow('add', response);
        },
        error: (error) => {
          this.isButtonDisabled = false;
          this.messageService.showError('Error saving data: ' + error.message);
        }
      });
    } else {
      this.clientNotesService.editData(this.selectedData.id, formValue).subscribe({
        next: (response) => {
          const index = this.dataSource.data.findIndex(item => item.id === this.selectedData.id);
          if (index > -1) this.dataSource.data[index] = response;
          this.messageService.showSuccess('Data updated successfully!');
          this.highlightRow('edit', response);
          this.resetFormState();
        },
        error: (error) => {
          this.isButtonDisabled = false;
          this.messageService.showError('Error updating data: ' + error.message);
        }
      });
    }
  }

  //table edit icon
  editData(data: any): void {
    this.clientNotesForm.patchValue({
      ...data
    });
    this.selectedData = data;
    this.saveButtonLabel = 'Update';
    this.mode = 'edit';
    this.isButtonDisabled = false;
    this.selectedRow = data;
  }

  //table delete icon
  deleteData(data: any): void {
    this.clientNotesService.deleteData(data.id).subscribe({
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
    this.clientNotesForm.reset();
    this.submitted = false;
    this.isButtonDisabled = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedData = null;
    this.selectedRow = null;
  }

  // helpers
  private highlightRow(action: 'add' | 'edit', rowData: any): void {
    if (action === 'add') {
      this.lastAddedRow = rowData;
    } else {
      this.lastEditedRow = rowData;
    }
    this.selectedRow = rowData;
  }

  private resetFormState(): void {
    this.clientNotesForm.reset();
    this.submitted = false;
    this.isButtonDisabled = false;
    this.saveButtonLabel = 'Save';
    this.mode = 'add';
    this.selectedData = null;
    this.selectedRow = null;
  }

  private handleError(error: any): void {
    this.messageService.showError('Action failed: ' + error.message);
    this.isButtonDisabled = false;
  }


}
