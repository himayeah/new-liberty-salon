import { Component, ViewChild } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { EmployeeFormComponent } from './employee-form/employee-form.component';

@Component({
    selector: 'app-employee-reg',
    standalone: false,
    templateUrl: './employee-reg.component.html',
    styleUrls: ['./employee-reg.component.scss'],
})
export class EmployeeRegComponent implements OnInit {
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    dataSource = new MatTableDataSource<any>([]);
    displayedColumns: string[] = [
        'employeeName',
        'dateJoined',
        'designation',
        'specializations',
        'hourlyRate',
        'commissionRate',
        'weeklyOffDays',
        'maxAppointmentsPerDay',
        'actions'
    ];

    // state
    selectedRow: any = null;
    lastAddedRow: any = null;
    lastEditedRow: any = null;

    constructor(
        private employeeRegService: EmployeeRegServicesService,
        private messageService: MessageServiceService,
        private dialog: MatDialog
    ) { }

    ngOnInit(): void {
        this.populateData();
    }

    populateData(): void {
        this.employeeRegService.getData().subscribe({
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

    openAddEmployeeModal(): void {
        const dialogRef = this.dialog.open(EmployeeFormComponent, {
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
        const dialogRef = this.dialog.open(EmployeeFormComponent, {
            width: '600px',
            data: { mode: 'edit', employee: data }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                const index = this.dataSource.data.findIndex(item => item.id === data.id);
                if (index > -1) {
                    this.dataSource.data[index] = result;
                    this.dataSource.data = [...this.dataSource.data]; // Trigger change detection
                    this.highlightRow('edit', result);
                }
            }
        });
    }

    deleteData(data: any): void {
        if (confirm(`Are you sure you want to delete ${data.employeeName}?`)) {
            this.employeeRegService.deleteData(data.id).subscribe({
                next: () => {
                    this.messageService.showSuccess('Deleted Successfully!');
                    this.populateData();
                },
                error: (error) => this.messageService.showError('Delete failed: ' + error.message)
            });
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