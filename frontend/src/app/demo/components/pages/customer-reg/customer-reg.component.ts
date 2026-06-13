import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Observable } from 'rxjs';
import { CustomerRegService } from 'src/app/services/customer-reg/customer-reg.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';
import { CustomerRegFormComponent } from './customer-reg-form/customer-reg-form.component';

@Component({
  selector: 'app-customer-reg',
  templateUrl: './customer-reg.component.html',
  styleUrl: './customer-reg.component.scss'
})

// Every code line goes inside the class
export class CustomerRegComponent implements OnInit {
  // @ViewChild allows your TS file to access HTML elements or Angular components directly
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  dataSource = new MatTableDataSource<any>([]);

  constructor(
    private customerRegService: CustomerRegService,
    private messageService: MessageServiceService,
    private dialog: MatDialog
  ) { }

  displayedColumns: string[] = [
    'firstName',
    'lastName',
    'dateOfBirth',
    'email',
    'phoneNumber',
    'preferredStylist',
    'action'
  ];

  selectedRow = null;
  lastEditedRow = null;
  lastAddedRow = null;

  ngOnInit(): void {
    this.populateData();
  }

  populateData(): void {
    this.customerRegService.getData().subscribe({
      next: (response: any[]) => {
        this.dataSource = new MatTableDataSource(response || []);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error : (error) => {
         this.messageService.showError('Error fetching data: ' + error.message);
      }
    });
  }

  openAddNewModal(): void {
     const dialogRef = this.dialog.open(CustomerRegFormComponent, {
       width: '600px',
       data: { mode: 'add' }
     });
     dialogRef.afterClosed().subscribe(result => {
       if (result) {
         this.populateData();
         this.highlightRow('add', result);
       } 
       this.selectedRow = null;
     });
  }

  viewData(data: any): void {
    const dialogRef = this.dialog.open(CustomerRegFormComponent, {
      width: '600px',
      data: { mode: 'view', customer: data }
    });
    dialogRef.afterClosed().subscribe(() => {
      this.selectedRow = null;
    });
  }

  editData(data: any): void {
    const dialogRef = this.dialog.open(CustomerRegFormComponent, {
      width: '600px',
      data: { mode: 'edit', customer: data }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.populateData();
        this.highlightRow('edit', result);
      }
      this.selectedRow = null;
    });
  }

  deleteData(data: any): void {
    this.customerRegService.deleteData(data.id).subscribe({
      next: () => {
        this.messageService.showSuccess("Data Deleted Successfully!");
        this.populateData();
      },
      error: (error) => {
        this.messageService.showError("Data delete unsuccessful" + error.message)
      }
    });
  }

    private highlightRow(type: 'add' | 'edit', response: any): void { 
        if (type === 'add') this.lastAddedRow = response;
        else this.lastEditedRow = response;
        setTimeout(() => {
            this.lastAddedRow = null;
            this.lastEditedRow = null;
        }, 3000);
    }

    applyFilter(event: Event): void {
        const filterValue = (event.target as HTMLInputElement).value.trim().toLowerCase();
        this.dataSource.filter = filterValue;
        if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
    }


    //refreshData
   refreshData(): void {
        this.populateData();
        this.dataSource.filter = '';
        this.selectedRow = null;
        if (this.dataSource.paginator) this.dataSource.paginator.firstPage();
    }

}

