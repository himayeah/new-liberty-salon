import { Component, ViewChild, OnInit } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeLeaveServiceService } from 'src/app/services/employee-leave/employee-leave-service.service';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeLeaveFormComponent } from './employee-leave-form/employee-leave-form.component';

@Component({
  selector: 'app-employee-leave',
  templateUrl: './employee-leave.component.html',
  styleUrls: ['./employee-leave.component.scss']
})
export class EmployeeLeaveComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['employeeName', 'leaveType', 'startDate', 'endDate', 'reason', 'actions'];

  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private employeeLeaveService: EmployeeLeaveServiceService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.employeeLeaveService.getData().subscribe({
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

  openAddEmployeeLeaveModal(): void {
    const dialogRef = this.dialog.open(EmployeeLeaveFormComponent, {
      width: '600px',
      data: { mode: 'add' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.dataSource.data = [result, ...this.dataSource.data];
        this.highlightRow('add', result);
      }
    });
  }

  editData(data: any): void {
    this.selectedRow = data;
    const dialogRef = this.dialog.open(EmployeeLeaveFormComponent, {
      width: '600px',
      data: { mode: 'edit', employeeLeave: data }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const index = this.dataSource.data.findIndex(item => item.id === data.id);
        if (index > -1) {
          this.dataSource.data[index] = result;
          this.dataSource.data = [...this.dataSource.data];
          this.highlightRow('edit', result);
        }
      }
    });
  }

  deleteData(data: any): void {
    if (confirm(`Are you sure you want to delete leave record?`)) {
      this.employeeLeaveService.deleteData(data.id).subscribe({
        next: () => {
          this.messageService.showSuccess('Deleted Successfully!');
          this.populateData();
        },
        error: (error) => this.messageService.showError('Delete failed: ' + error.message)
      });
    }
  }

  refreshData(): void {
    this.populateData();
    this.dataSource.filter = '';
    this.selectedRow = null;
    if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
    this.dataSource.filter = filterValue;
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
