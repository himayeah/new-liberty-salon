import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { ServiceFormComponent } from './service-form/service-form.component';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ServiceService } from 'src/app/services/service/service.service';
import { EmployeeAuthService } from 'src/app/employee-workspace/services/employee-auth.service';
import { Role } from 'src/app/models/role.enum';

@Component({
  selector: 'app-service',
  templateUrl: './service.component.html',
  styleUrl: './service.component.scss'
})
export class ServiceComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['serviceName', 'serviceCategory', 'duration', 'price', 'description', 'isActive', 'actions'];

  //state control
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;
  isReceptionist = false;
  isManager = false;
  // disableButton = false;

  constructor(
    private serviceService: ServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog,
    private employeeAuthService: EmployeeAuthService
  ) { }

  ngOnInit(): void {
    this.isReceptionist = this.employeeAuthService.getRole() === Role.RECEPTIONIST;
    const currentRole = this.employeeAuthService.getRole();
    this.isManager = currentRole === Role.MANAGER || currentRole === Role.SENIORSTYIST;
    this.populateData();
  }

  populateData(): void {
    this.serviceService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource<any>(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        console.error('Error fetching Data:', error);
        this.messageService.showError('Failed to fetch data: ' + (error?.message ?? error?.statusText ?? 'Unknown error'));
      }
    });
  }

  //Add New Service pop-up
  openAddServiceModal(): void {
    if (this.isReceptionist) return;
    const dialogRef = this.dialog.open(ServiceFormComponent, {
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

  editService(data: any): void {
    // if the Receptionist is trying to edit Service, show a Informative message they aren't allowed to
    if (this.isReceptionist) {
      alert("Only The Manager can edit Services");
      return;
    } else {
      const dialogRef = this.dialog.open(ServiceFormComponent, {
        width: '600px',
        data: { mode: 'edit', service: data }
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
  }

  //   if (this.isReceptionist) return;
  //   const dialogRef = this.dialog.open(ServiceFormComponent, {
  //     width: '600px',
  //     data: { mode: 'edit', service: data }
  //   });

  //   this.selectedRow = data;

  //   dialogRef.afterClosed().subscribe(result => {
  //     if (result) {
  //       this.populateData();
  //       this.highlightRow('edit', result);
  //     }
  //     this.selectedRow = null;
  //   });
  // }

  deleteService(data: any): void {
    // If the Receptionist Role is deleting, return (Delete doesn't happen)
    if (this.isReceptionist) return;
    if (confirm(`Are you sure you want to delete service "${data.serviceName}"?`)) {
      this.serviceService.deleteService(data.id).subscribe({
        next: () => {
          this.messageService.showSuccess('Deleted Successfully!');
          this.populateData();
        },
        error: (error) => {
          console.error('Delete failed:', error);
          this.messageService.showError('Delete Failed: ' + (error?.error?.message ?? error?.message ?? error?.statusText ?? 'Unknown error'))
        }
      });
    }
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  refreshData(): void {
    this.populateData();
    this.dataSource.filter = '';
    this.selectedRow = null;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  // Toggle active state from the table
  toggleActive(element: any, newState: boolean): void {
    if (this.isReceptionist) return;
    const prev = element.is_active;
    element.is_active = newState;
    this.serviceService.editService(element.id, { is_active: newState }).subscribe({
      next: (response) => {
        const idx = this.dataSource.data.findIndex((d: any) => d.id === element.id);
        if (idx > -1) {
          this.dataSource.data[idx] = response;
          this.dataSource._updateChangeSubscription();
        }
        this.messageService.showSuccess('Status updated');
      },
      error: (error) => {
        element.is_active = prev;
        this.dataSource._updateChangeSubscription();
        this.handleError(error);
      }
    });
  }

  private handleError(error?: any): void {
    console.error('Request error:', error);
    const message = error?.message ?? error?.error?.message ?? (error?.status ? `${error.status} ${error.statusText}` : 'Unknown error');
    this.messageService.showError('Action failed: ' + message);
  }

  private highlightRow(type: 'add' | 'edit', response: any): void {
    if (type === 'add') this.lastAddedRow = response;
    else this.lastEditedRow = response;
    setTimeout(() => {
      this.lastAddedRow = null;
      this.lastEditedRow = null;
    }, 3000);
  }
}
