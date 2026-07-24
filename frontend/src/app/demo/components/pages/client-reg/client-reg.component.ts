import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { ClientFormComponent } from './client-form/client-form.component';

@Component({
    selector: 'app-client-reg',
    templateUrl: './client-reg.component.html',
    styleUrls: ['./client-reg.component.scss']
})

export class ClientRegComponent implements OnInit {
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;
    dataSource = new MatTableDataSource<any>([]);
    // Object displayedColumns of type - String array
    // Point 2: The component that contains the list of the columns you want to render
    // Point 3: Add the displayedColumns to mat-header-row in the HTML file
    displayedColumns: string[] = [
        'firstName',
        'lastName',
        'email',
        'phoneNumber',
        'dateOfBirth',
        'gender',
        'preferredStylist',
        'allergies',
        'totalVisits',
        'lastVisitedDate',
        'lifetimeValue',
        'actions'
    ];

    // state control (Not logic)
    selectedRow = null;
    lastAddedRow: any = null;
    lastEditedRow: any = null;
    totalValue: number = 0;

    //Date Filter
    selectedDuration: string = 'allTime';
    customStartDate: Date | null = null;
    customEndDate: Date | null = null;
    allClients: any[] = [];
    filteredClients: any[] = [];
    maxDate: Date = new Date();

    constructor(
        private clientRegService: ClientRegServiceService,
        private messageService: MessageServiceService,
        private dialog: MatDialog,
        private router: Router
    ) { }


    ngOnInit(): void {
        sessionStorage.clear();
        this.populateData();
        // this.clientLastVisitedDate();
        this.applyFilterPredicate();
    }

    populateData(): void {
        this.clientRegService.getData().subscribe({

            next: (response: any[]) => {

                this.allClients = response || [];
                this.filteredClients = response || [];

                console.log("CLIENTS:", response);

                // this.onDurationChange();


                // const sortedClients = (response || []).sort((a, b) =>
                //     b.lifetimeValue - a.lifetimeValue
                // );

                // this.dataSource = new MatTableDataSource(sortedClients);
                // this.dataSource.sort = this.sort;

                // Calculate total LTV for all clients

                // (response || []).forEach(client => {
                //     this.totalValue = this.totalValue + client.lifetimeValue;
                // });
                // console.log("Total LTV:", this.totalValue);


                // dataSource.filter examples

                // console.log("July 11 Client Data:", response);
                // const filteredCients = (response || []).filter(client =>
                //     client.id && client.id >= 10
                // );

                // const filteredVisits = (response || []).filter(client =>
                //     client.totalVisits >= 2
                // );
                // console.log("July 11 Client Total Visits Over 2:", filteredCients);

                // console.log("clients over id of 10", filteredCients);
                // const filteredDob = (response || []).filter(client =>
                //     client.dateOfBirth && client.dateOfBirth >= "2000-01-01"
                // );

            },
            // error is a callback function of observable
            // Just like response stores the data emitted by next, the (error) stores the error object emitted by the Observable when something goes wrong.
            error: (error) => {
                this.messageService.showError('Error fetching data: ' + error.message);
            }
        });
    }

    // map cients first name to uppercase
    //  populateData(): void {
    //     this.clientRegService.getData()
    //     .pipe(map((response: any[]) => 
    //         response.map(client => ({
    //             ...client,
    //             firstName: client.firstName.toUpperCase()
    //         }))
    //     ))
    //     .subscribe({
    //         next: (response: any[]) => {
    //             this.dataSource = new MatTableDataSource(response || []);
    //             this.dataSource.paginator = this.paginator;
    //             this.dataSource.sort = this.sort;
    //         },
    //         error: (error) => {
    //             this.messageService.showError('Error fetching data: ' + error.message);
    //         }
    //     });
    // }

    // // filter only active clients
    //  populateData(): void {
    //     this.clientRegService.getData()
    //     .pipe(
    //     map((response: any[]) =>
    //         response.filter(client => client.status === 'ACTIVE')
    //     )
    // )
    //     .subscribe({
    //         next: (response: any[]) => {
    //             this.dataSource = new MatTableDataSource(response || []);
    //             this.dataSource.paginator = this.paginator;
    //             this.dataSource.sort = this.sort;
    //         },
    //         error: (error) => {
    //             this.messageService.showError('Error fetching data: ' + error.message);
    //         }
    //     });
    // }

    // clientLastVisitedDate() {
    //     this.clientRegService.getClientLastVisitedDate().subscribe({
    //         next: (response: any[]) => {
    //             console.log("Client Last Visited Date: ", response);
    //             this.dataSource = new MatTableDataSource(response || []);
    //             this.dataSource.paginator = this.paginator;
    //             this.dataSource.sort = this.sort;
    //         },
    //         error: (error) => {
    //             this.messageService.showError('Error fetching data: ' + error.message);
    //         }
    //     });
    // }

    // The modal that openes for 'Add New' Button
    openAddClientModal(): void {
        // The open method of MatDialog service will return an instance of MatDialogRef:
        // ClientFormComponent and an object containing styles are passes as parameters
        // 06/08 : diaog is my local varaible pointing to the MatDialogRef Instance
        // An instance is just a real usable copy of something that is created from a blueprint (class)
        const dialogRef = this.dialog.open(ClientFormComponent, {
            width: '600px',
            data: { mode: 'add' }
        });

        // afterClosed is a handle of MatDialog and it emits the data of the dialog when it's closed. subscribe is used to listen to that emitted data and do something with it
        // It can be used to close the dialog and to receive notifications when the dialog has been closed
        // The subscribe method is used to listen for the close event of the dialog, and when the dialog is closed, it will execute the callback function with the result passed from the dialog
        // result is a variable and it returns the data from the Dialog when it's closed
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.populateData();
                // 'add' is a union type defined in the highlightRow method
                this.highlightRow('add', result);
            }
        });
    }

    // The modal that openes for 'Edit' Button
    editData(data: any): void {
        const dialogRef = this.dialog.open(ClientFormComponent, {
            width: '600px',
            data: { mode: 'edit', client: data }
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

    // calculate Total Visit button
    calculateTotalVisits(): void {
        this.clientRegService.calculateTotalVisitsPerClient().subscribe({
            next: (response: any) => {
                console.log("Total Visits: ", response);
                alert("Total Visits Updated")
                this.populateData();
            },
            error: (error) => {
                this.messageService.showError('Error fetching data: ' + error.message);
            }
        })
    }

    // calls service.ts file 
    // subscribes and gets the response it returns
    // pass it to template to update the UI
    calculateClientLifeTimeValue() {
        this.clientRegService.getClientLifeTimeValue().subscribe({
            next: (response: any) => {
                console.log("This is client LifeTime Values:", response);
                alert("Client LifeTime Values calculates successfully!");
                this.populateData();
            },
            error: (error) => {
                this.messageService.showError('Error fetching data: ' + error.message);
            }
        })
    }

    viewData(data: any): void {
        this.router.navigate(['/pages/client-profile', data.id]);
    }

    deleteData(data: any): void {
        if (confirm(`Are you sure you want to delete client "${data.firstName} ${data.lastName}"?`)) {
            this.clientRegService.deleteData(data.id).subscribe({
                next: () => {
                    this.messageService.showSuccess('Deleted Successfully!');
                    this.populateData();
                },
                error: (error) => this.messageService.showError('Delete failed: ' + (error?.error?.message ?? error?.message ?? 'Unknown error'))
            });
        }
    }

    // Filters the table data based on the user's keystrokes in the search input
    applyFilter(event: Event): void {
        // Gets the input value, removes leading/trailing whitespace, and converts it to lowercase for case-insensitive filtering
        const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
        // Applies the filter string to the table's datasource
        this.dataSource.filter = filterValue;
        // Resets the paginator to the first page so the user sees the start of the matching results
        if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
    }

    refreshData(): void {
        this.populateData();
        this.dataSource.filter = '';
        this.selectedRow = null;
        if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
    }

    // helpers
    // temporarily highlights the last edited or added row for 3 seconds
    private highlightRow(type: 'add' | 'edit', response: any): void {
        //tunneling response to the top to show the highlight effect
        // if a new client is added, store it in 'lastAddedRow', if a client is edited store it in 'lastEditedRow'
        if (type === 'add') this.lastAddedRow = response;
        else this.lastEditedRow = response;
        setTimeout(() => {
            this.lastAddedRow = null;
            this.lastEditedRow = null;
        }, 3000);
    }

    // Date Filter

    private applyFilterPredicate() {
        this.dataSource.filterPredicate = (data: any, filter: string) => {
            const searchText = filter.trim().toLowerCase();
            return (
                (data.phoneNumber || '').toLowerCase().includes(searchText) ||
                (data.email || '').toLowerCase().includes(searchText)
            );
        };
    }




}