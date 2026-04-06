import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatPaginator } from '@angular/material/paginator';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatDialog } from '@angular/material/dialog';
import { MatSort } from '@angular/material/sort';
import { BillingService } from 'src/app/services/billing/billing.service';
import { BillingFormComponent } from './billing-form/billing-form.component';

@Component({
  selector: 'app-billing',
  templateUrl: './billing.component.html',
  styleUrl: './billing.component.scss'
})

export class BillingComponent implements OnInit {
  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  dataSource = new MatTableDataSource<any>([]);

  displayedColumns: string[] = [
    'clientName',
    'billingCategory',
    'clientType',
    'paymentStatus',
    'billingDate',
    'paymentMethod',
    'actions',
  ];

  //state
  selectedRow: any = null;
  lastAddedRow: any = null;
  lastEditedRow: any = null;

  constructor(
    private billingService: BillingService,
    private messageService: MessageServiceService,
    private dialog: MatDialog,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.populateData();
    this.checkQueryParams();
  }

  //listening for the data passed from the appointment schedule page (Billing data related to a specific client)
  checkQueryParams(): void {
    this.route.queryParams.subscribe(params => {
      if (params['data']) {
        try {
          //turns the string back to a usable object
          const billingData = JSON.parse(params['data']);
          if (billingData.autoOpen) {
            // Slight delay to ensure page is loaded
            setTimeout(() => {
              this.openAddModal(billingData);
            }, 500);
          }
        } catch (e) {
          console.error('Error parsing billing data', e);
        }
      }
    });
  }

  populateData(): void {
    this.billingService.getData().subscribe({
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

  openAddModal(preFillData?: any): void {
    const dialogRef = this.dialog.open(BillingFormComponent, {
      width: '800px',
      data: {
        mode: 'add',
        preFill: preFillData
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('add', result);
      }
    });
  }

  viewInvoiceDetails(data: any): void {
    const dialogRef = this.dialog.open(BillingFormComponent, {
      width: '1000px',
      data: { 
        mode: 'view', 
        billing: data 
      }
    });

    this.selectedRow = data;

    dialogRef.afterClosed().subscribe(() => {
      this.selectedRow = null;
    });
  }

  editData(data: any): void {
    const dialogRef = this.dialog.open(BillingFormComponent, {
      width: '800px',
      data: { mode: 'edit', billing: data }
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
    this.billingService.deleteData(data.id).subscribe({
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
