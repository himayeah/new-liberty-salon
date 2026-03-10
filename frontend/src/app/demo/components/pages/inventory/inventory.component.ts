import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';

import { InventoryServiceService } from 'src/app/services/inventory/inventory-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { InventoryFormComponent } from './inventory-form/inventory-form.component';

@Component({
    selector: 'app-inventory',
    templateUrl: './inventory.component.html',
    styleUrls: ['./inventory.component.scss']
})
export class InventoryComponent implements OnInit {
    @ViewChild(MatPaginator) paginator!: MatPaginator;
    @ViewChild(MatSort) sort!: MatSort;

    dataSource = new MatTableDataSource<any>([]);
    displayedColumns: string[] = [
        'productName',
        'currentStock',
        'minimumStock',
        'maximumStock',
        'lastRestockedDate',
        'shelfLocation',
        'actions'
    ];

    selectedRow: any = null;
    lastAddedRow: any = null;
    lastEditedRow: any = null;

    constructor(
        private inventoryService: InventoryServiceService,
        private messageService: MessageServiceService,
        private dialog: MatDialog
    ) { }

    ngOnInit(): void {
        this.populateData();
    }

    populateData(): void {
        this.inventoryService.getData().subscribe({
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

    // Open Add New Stock modal
    openAddStockModal(): void {
        const dialogRef = this.dialog.open(InventoryFormComponent, {
            width: '700px',
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
        const dialogRef = this.dialog.open(InventoryFormComponent, {
            width: '700px',
            data: { mode: 'edit', inventory: data }
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
        if (confirm('Are you sure you want to delete this record?')) {
            this.inventoryService.deleteData(data.id).subscribe({
                next: () => {
                    this.messageService.showSuccess('Deleted successfully!');
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

    private highlightRow(type: 'add' | 'edit', response: any): void {
        if (type === 'add') this.lastAddedRow = response;
        else this.lastEditedRow = response;
        setTimeout(() => {
            this.lastAddedRow = null;
            this.lastEditedRow = null;
        }, 3000);
    }
}
