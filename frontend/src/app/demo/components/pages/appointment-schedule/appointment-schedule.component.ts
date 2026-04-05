import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { AppointmentFormComponent } from './appointment-form/appointment-form.component';

@Component({
    selector: 'app-appointment-schedule',
    templateUrl: './appointment-schedule.component.html',
    styleUrls: ['./appointment-schedule.component.scss']
})
export class AppointmentScheduleComponent implements OnInit {
    @ViewChild(MatPaginator) paginator: MatPaginator;
    @ViewChild(MatSort) sort: MatSort;

    dataSource = new MatTableDataSource<any>([]);

    displayedColumns: string[] = [
        'clientName',
        'employeeName',
        'serviceName',
        'appointmentDate',
        'appointmentStartTime',
        'appointmentEndTime',
        'appointmentStatus',
        'bookingSource',
        'cancellationReason',
        'cancelledDate',
        'actions',
    ];

    //state
    selectedRow: any = null;
    lastAddedRow: any = null;
    lastEditedRow: any = null;

    constructor(
        private appointmentService: AppointmentSchedulingServiceService,
        private messageService: MessageServiceService,
        private dialog: MatDialog
    ) { }

    ngOnInit(): void {
        this.populateData();
    }

    populateData(): void {
        this.appointmentService.getData().subscribe({
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

    openAddModal(): void {
        const dialogRef = this.dialog.open(AppointmentFormComponent, {
            width: '800px',
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
        const dialogRef = this.dialog.open(AppointmentFormComponent, {
            width: '800px',
            data: { mode: 'edit', appointment: data }
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
        this.appointmentService.deleteData(data.id).subscribe({
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
