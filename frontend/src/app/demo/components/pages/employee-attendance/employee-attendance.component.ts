import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';

import { EmployeeAttendanceServiceService } from 'src/app/services/employee-attendance/employee-attendance-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeAttendanceFormComponent } from './employee-attendance-form/employee-attendance-form.component';

@Component({
  selector: 'app-employee-attendance',
  templateUrl: './employee-attendance.component.html',
  styleUrl: './employee-attendance.component.scss'
})
export class EmployeeAttendanceComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = ['employeeName', 'date', 'checkInTime', 'checkOutTime', 'status', 'actions'];

  selectedDate: Date = new Date();
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(

    private employeeAttendanceService: EmployeeAttendanceServiceService,
    private employeeRegService: EmployeeRegServicesService,
    private messageService: MessageServiceService,
    private dialog: MatDialog,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    forkJoin({
      employees: this.employeeRegService.getData(),
      attendance: this.employeeAttendanceService.getData()
    }).subscribe({
      next: (result: any) => {
        const employees = result.employees || [];
        const attendanceRecords = result.attendance || [];

        // Map all employees to their attendance for the selected date
        const combinedData = employees.map((emp: any) => {
          const attendance = attendanceRecords.find((a: any) => {
            const isSameEmployee = a.employeeId === emp.id;
            if (!isSameEmployee) return false;

            if (a.checkInTime) {
              const recordDate = new Date(a.checkInTime).toDateString();
              const selectedDateStr = this.selectedDate.toDateString();
              return recordDate === selectedDateStr;
            }
            return false;
          });


          return {
            id: attendance?.id,
            employeeId: emp.id,
            employeeName: emp.employeeName,
            checkInTime: attendance?.checkInTime || null,
            checkOutTime: attendance?.checkOutTime || null,
            status: attendance?.status || 'Unknown'
          };
        });

        this.dataSource = new MatTableDataSource(combinedData);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Error fetching data: ' + error.message);
      }
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

  viewData(data: any): void {
    // Implementation for viewing data
  }

  editData(data: any): void {
    const dialogRef = this.dialog.open(EmployeeAttendanceFormComponent, {
      width: '500px',
      data: { attendance: data, selectedDate: this.selectedDate }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('edit', result);
      }
    });
  }

  deleteData(data: any): void {
    if (!data.id) {
      this.messageService.showError('No attendance record to delete');
      return;
    }
    if (confirm('Are you sure you want to delete this record?')) {
      this.employeeAttendanceService.deleteData(data.id).subscribe({
        next: () => {
          this.messageService.showSuccess('Deleted Successfully!');
          this.populateData();
        },
        error: (error) => this.messageService.showError('Delete failed: ' + error.message)
      });
    }
  }

  private highlightRow(type: 'add' | 'edit', response: any): void {
    if (type === 'edit') this.lastEditedRow = response;
    setTimeout(() => {
      this.lastEditedRow = null;
    }, 3000);
  }
}
