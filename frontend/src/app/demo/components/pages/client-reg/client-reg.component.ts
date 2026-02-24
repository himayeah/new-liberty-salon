import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
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
    displayedColumns: string[] = ['firstName', 'lastName', 'email', 'phoneNumber', 'dateOfBirth', 'gender', 'preferredStylist', 'allergies', 'totalVisits', 'lastVisitedDate', 'lifetimeValue', 'actions'];

    // state control (Not logic)
    selectedRow: any = null;
    lastAddedRow: any = null;
    lastEditedRow: any = null;

    constructor(
        private clientRegService: ClientRegServiceService,
        private messageService: MessageServiceService,
        private dialog: MatDialog
    ) { }

    ngOnInit(): void {
        this.populateData();
    }

    populateData(): void {
        this.clientRegService.getData().subscribe({
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

    openAddClientModal(): void {
        const dialogRef = this.dialog.open(ClientFormComponent, {
            width: '600px',
            data: { mode: 'add' }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.populateData();
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
            }
            this.selectedRow = null;
        });
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
        if (type === 'add') this.lastAddedRow = response;
        else this.lastEditedRow = response;
        setTimeout(() => {
            this.lastAddedRow = null;
            this.lastEditedRow = null;
        }, 3000);
    }
}