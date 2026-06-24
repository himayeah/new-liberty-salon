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

    noShowAppointments: any[] = [];
    appointmentIDs: any[] = [];
    showData: any[] = [];
 
    constructor(
        private appointmentService: AppointmentSchedulingServiceService,
        private messageService: MessageServiceService,
        private dialog: MatDialog,
        private router: Router,
    ) { }

    ngOnInit(): void {
        this.populateData();
    }

    populateData(): void {
       
        this.appointmentService.getData().subscribe({
            
            //  const filteredCients = (response || []).filter(client =>
                //     client.id && client.id >= 10
                // );

            //next is a callback function of observable, It runs when the data is successfully returned
            // you can save the returned data in a variable : 'response'
            next: (response: any[]) => {

                //client filter
                // Takes the response received from backend, takes one client object as 'client' and accessses it's property 'clientName
                // Optimise search using trim()
                const trimmedAmaya = (response || []).filter(client =>
                    client.clientName && client.clientName.trim().toLowerCase() === 'amaya ratnayake'
                );

                console.log("Trimmed client Name :", trimmedAmaya);

                const filteredData = (response || []).filter(client =>
                    client.clientName && client.clientName.toLowerCase().startsWith('a')
                );

                console.log("Client Filtered Data: ", filteredData);

                //replace(searchFor, replaceWith
                const replaceHypen = (response || []).filter(client =>
                    client.clientName && client.clientName.replace('-', ' ').toLowerCase() === 'amaya ratnayake'
                );

                console.log("Hyphen replaced Name:", replaceHypen);


                // using ("Clients name starting with letter A:" + filteredData); PLUS (+) will result in string concatenation,
                // filteredData is an array and Angular will try to convert it into string and it won't give you response
                // Instead when you use "," what happenes is that the String ("Clients name starting with letter A:") 
                // and the array (fiteredData) gets passed as 2 separate arguments to console.log

                console.log("Clients name starting with letter A:", filteredData);

                // console.log("Response received from Backend :", response);

                // const filteredAppointments =(response || []).filter(appointment =>
                //     appointment.appointmentStatus === 'BOOKED'
                // );
                // console.log("Filtered Appointments:", filteredAppointments);



                // Filter all IDs after 10
                const filteredIDAfter10 = (response || []).filter(client =>
                    // The DB value
                    client.id && client.id >= 10
                );

                console.log("IDs After 10:", filteredIDAfter10);

                // Filter BOOKED appointments by client name = Amaya
                // includes(), startsWith() are string methods
                // other similar; 
                // 1. trim() = remove spaces from both end
                // 2. toLowerCase() = convert to lowercase
                // 3. toUpperCase() = convert to uppercase
                // 4. length = get length of string
                // 5. replace(searchfor, replaceWith) = replace string with another string
                // 6. replaceAll(searchFor, replaceWith) = replace all occurrence of string with another string
                // 7. slice(startIndex, endIndex) = extract a section of string
                // 8. substring(startIndex, endIndex) = extract a section of string
                // 9. trim() = remove spaces from both end
                // 10.trimStart() = remove spaces from start
                // 11.trimEnd() = remove spaces from end
                // 12.indexOf() = return the index of the first occurrence of a string
                // 13.lastIndexOf() = return the index of the last occurrence of a string
                // 14. concat() = concatenate two strings
                // 15. charAt() = return the character at the specified index
                // 16.includes(searchFor) = check if string contains another string
                const filteredName = (response || []).filter(client =>
                    client.appointmentStatus == 'BOOKED' && client.clientName.toLowerCase().includes('amaya')
                );
                console.log("Filtered Name Amaya: ", filteredName);

                // this.sort.active = 'clientId'; // sort by cloumn clientId
                // this.sort.direction = 'desc'; // sort in desc order

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

    // //Show ID, Names of All Persons who's Appointment Status = "No-Show" - Q1
    // getNoShowAppointments(): void {
    //     this.appointmentService.getData().subscribe({
    //         next: (response: any[]) => {

    //             this.noShowAppointments = response
    //                 .filter(appointments => appointments.appointmentStatus === 'NO_SHOW')
    //                 .map(appointments => ({
    //                     id: appointments.id,
    //                     clientName: appointments.clientName
    //                 }))
    //             console.log(this.noShowAppointments);
    //         }
    //     })
    // }

    //Show Appointment IDs after 10
    getIDAfter10(): void {
        this.appointmentService.getData().subscribe({
            next: (response: any[]) => {

                this.appointmentIDs = response
                    .filter(appointments => appointments.id > 10)
                    //ASC
                    .sort((a, b) => a.id - b.id)
                    //DESC ((a,b) => b.id - a.id)
                    .map(appointments => ({
                        id: appointments.id,
                    }
                    ))
                console.log(this.appointmentIDs);
            }
        })
    }

    //Retrieve the Max ID, Increment that value from 10 and return the final Result - Q2
    getMaxId(): void {
        this.appointmentService.getMaxId().subscribe({
            next: (response: number) => {
                console.log("Max Id + 10 = ", response);
            },
            error: (error) => {
                this.messageService.showError('Error fetching max ID: ' + error.message);
            }
        })
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
        const confirmed = confirm("Are you sure you want to delete this appointment?");
            if (confirmed){
                this.appointmentService.deleteData(data.id).subscribe({
                next: () => {
                    this.messageService.showSuccess('Deleted Successfully!');
                    this.populateData();
                },
                error: (error) => this.messageService.showError('Delete failed: ' + error.message)
            });
            } else {
                alert("Appointment Not Deleted");
            }
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


    // How MatSort works:
    // ------------------
    // To reverse the sort order for all headers, set the matSortStart to desc on the matSort directive
    // To reverse the order only for a specific header, set the start input only on the header instead
    // To prevent the user from clearing the sort sort state from an already sorted column, set matSortDisableClear to true
    // MatSort = controls sorting for the whole table, MatSortHeader = makes each column clickable for sorting (HTML)

    // start: 'asc' | 'desc' -> Default direction when first clicked
    // active: string -> Which column is currently sorted
    // direction: 'asc' | 'desc' | '' -> Sort order
    // @Output() sortChange: EventEmitter<Sort> -> Fires when the User changes sort
    // {
    //   active: 'clientName',
    //   direction: 'asc' | 'desc'
    // }
    // Ex:      // this.sort.active = 'clientId'; // sort by cloumn clientId
                // this.sort.direction = 'desc';

}
