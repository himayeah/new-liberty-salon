import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeScheduleService } from 'src/app/services/employee-schedule/employee-schedule-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeScheduleFormComponent } from './employee-schedule-form/employee-schedule-form.component';

@Component({
  selector: 'app-employee-schedule',
  templateUrl: './employee-schedule.component.html',
  styleUrls: ['./employee-schedule.component.scss']
})
export class EmployeeScheduleComponent implements OnInit {
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);
  displayedColumns: string[] = [
    'employeeName', 'workDay', 'isActive',
    'startTime', 'endTime', 'effectiveDate', 'endDate', 'actions'
  ];

  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private scheduleService: EmployeeScheduleService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.scheduleService.getData().subscribe({
      next: (response: any) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        this.messageService.showError('Error fetching schedules: ' + error.message);
      }
    });
  }

  openAddScheduleModal(): void {
    const dialogRef = this.dialog.open(EmployeeScheduleFormComponent, {
      width: '680px',
      data: { mode: 'add' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('add', result);
      }
    });
  }

  editSchedule(schedule: any): void {
    this.selectedRow = schedule;
    const dialogRef = this.dialog.open(EmployeeScheduleFormComponent, {
      width: '680px',
      data: { mode: 'edit', schedule }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('edit', result);
      }
      this.selectedRow = null;
    });
  }

  deleteSchedule(schedule: any): void {
    this.scheduleService.deleteData(schedule.id).subscribe({
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

  private highlightRow(type: 'add' | 'edit', response: any): void {
    if (type === 'add') this.lastAddedRow = response;
    else this.lastEditedRow = response;
    setTimeout(() => {
      this.lastAddedRow = null;
      this.lastEditedRow = null;
    }, 3000);
  }
}
