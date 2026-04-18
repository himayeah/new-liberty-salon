import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { Router } from '@angular/router';
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
        private dialog: MatDialog,
        private router: Router
    ) { }

    ngOnInit(): void {
        this.populateData();
        //code test
    }

    populateData(): void {
        this.appointmentService.getData().subscribe({
            next: (response: any[]) => {
                this.dataSource = new MatTableDataSource(response || []);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;

                // Auto-update No Show status
                this.checkAndMarkNoShows(response);
            },
            error: (error) => {
                this.messageService.showError('Error fetching data: ' + error.message);
            }
        });
    }

    private checkAndMarkNoShows(appointments: any[]): void {
        const now = new Date();
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const appointmentsToUpdate = appointments.filter(app => {
            if (!app.appointmentDate) return false;
            
            const appDate = new Date(app.appointmentDate);
            appDate.setHours(0, 0, 0, 0);

            const isFinalStatus = ['COMPLETED', 'CANCELLED', 'NO_SHOW'].includes(app.appointmentStatus);
            if (isFinalStatus) return false;

            // If the appointment was on a previous day, mark it as NO_SHOW
            if (appDate < today) return true;

            // If the appointment is today, only mark as NO_SHOW if the end time has passed
            if (appDate.getTime() === today.getTime() && app.appointmentEndTime) {
                const [hours, minutes] = app.appointmentEndTime.split(':').map(Number);
                const endTime = new Date();
                endTime.setHours(hours, minutes, 0, 0);
                return now > endTime;
            }

            return false;
        });

        if (appointmentsToUpdate.length > 0) {
            let updateCount = 0;
            appointmentsToUpdate.forEach(app => {
                const updatedApp = { ...app, appointmentStatus: 'NO_SHOW' };
                this.appointmentService.editData(app.id, updatedApp).subscribe({
                    next: () => {
                        updateCount++;
                        if (updateCount === appointmentsToUpdate.length) {
                            // Refresh data only after all updates are complete
                            this.populateData();
                        }
                    },
                    error: (err) => console.error('Error marking as NO_SHOW:', err)
                });
            });
        }
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

    //preparing some data and redirecting the user to the billing page.
    proceedToBilling(appointment: any): void {
        const clientName = appointment.clientName || (appointment.client ? (appointment.client.firstName + ' ' + appointment.client.lastName) : '');
        const billingData = {
            clientName: clientName,
            billingCategory: 'SALON_SERVICE',
            clientType: 1, // Registered
            billingDate: appointment.appointmentDate,
            serviceId: appointment.serviceId || (appointment.service ? appointment.service.id : null),
            serviceName: appointment.serviceName || (appointment.service ? (appointment.service.serviceName || appointment.service.name) : ''),
            appointmentId: appointment.id,
            appointment: appointment, // Include the full object for status update
            autoOpen: true
        };

        this.router.navigate(['/pages/billing'], {
            state: { data: billingData }
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
