import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
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
  preFillData: any = null;

  constructor(
    private billingService: BillingService,
    private messageService: MessageServiceService,
    private dialog: MatDialog,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.populateData();
    this.checkNavigationState();
  }

  //listening for the data passed from the appointment schedule page (Billing data related to a specific client)
  checkNavigationState(): void {
    const navigation = this.router.getCurrentNavigation();
    const state = navigation?.extras.state || (window.history.state as any);
    
    if (state && state.data && state.data.autoOpen) {
      this.preFillData = state.data;
      setTimeout(() => {
        this.openAddModal(this.preFillData);
        // Clear the autoOpen state so it doesn't open again on window focus or other triggers if any
        if (window.history.state) window.history.state.data.autoOpen = false;
      }, 500);
    }
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
    const dataToUse = preFillData || this.preFillData;
    const dialogRef = this.dialog.open(BillingFormComponent, {
      width: '800px',
      data: {
        mode: 'add',
        preFill: dataToUse
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
