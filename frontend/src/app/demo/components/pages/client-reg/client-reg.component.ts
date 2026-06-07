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

    constructor(
        private clientRegService: ClientRegServiceService,
        private messageService: MessageServiceService,
        private dialog: MatDialog,
        private router: Router
    ) { }


    ngOnInit(): void {
        this.populateData();
    }

    populateData(): void {
        this.clientRegService.getData().subscribe({
            //next is a callback function of observable, subscribe also emits data, and those data is stored in 'response' variable of an array type any
            next: (response: any[]) => { 
                // the response or a empty array (if response is null or undefined) is assigned to the dataSource of the table
                this.dataSource = new MatTableDataSource(response || []);
                this.dataSource.paginator = this.paginator;
                this.dataSource.sort = this.sort;
            },
            // error is a callback function of observable
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

    // // Add timeout
    //  populateData(): void {
    //     this.clientRegService.getData()
    //     .pipe(timeout(500))
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

    //  // display a new field combining first name and last name
    //  populateData(): void {
    //     this.clientRegService.getData()
    //       .pipe(
    //         map((response: any[]) =>
    //             response.map(client => ({
    //                 ...client,
    //                 displayName: client.firstName + ' ' + client.lastName
    //             }))
    //         )
    //     )
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


    openAddClientModal(): void {
        // The open method will return an instance of MatDialogRef:
        // ClientFormComponent and an object containing styles are passes as parameters
        const dialogRef = this.dialog.open(ClientFormComponent, {
            width: '600px',
            data: { mode: 'add' }
        });

        // afterClosed is a handle of MatDialog
        // It can be used to close the dialog and to receive notifications when the dialog has been closed
        // The subscribe method is used to listen for the close event of the dialog, and when the dialog is closed, it will execute the callback function with the result passed from the dialog
        // result is a variable and it returns the data from the DIalog when it's closed
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.populateData();
                // 'add' is a union type defined in the highlightRow method
                this.highlightRow('add', result);
            }
        });
    }

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

    viewData(data: any): void {
        this.router.navigate(['/pages/client-profile', data.id]);
    }

    deleteData(data: any): void {
        this.clientRegService.deleteData(data.id).subscribe({
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

    // helpers
   private highlightRow(type: 'add' | 'edit', response: any): void { 
        //tunneling response to the top to show the highlight effect
        if (type === 'add') this.lastAddedRow = response;
        else this.lastEditedRow = response;
        setTimeout(() => {
            this.lastAddedRow = null;
            this.lastEditedRow = null;
        }, 3000);
    }
}