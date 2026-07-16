import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { InvoiceService } from 'src/app/services/invoice/invoice.service';

@Component({
  selector: 'app-invoice-details',
  templateUrl: './invoice-details.component.html',
  styleUrls: ['./invoice-details.component.scss']
})
export class InvoiceDetailsComponent implements OnInit {
  invoice: any = null;
  loading: boolean = true;

  constructor(
    private invoiceService: InvoiceService,
    public dialogRef: MatDialogRef<InvoiceDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    if (this.data && this.data.billingId) {
      this.invoiceService.getInvoiceByBillingId(this.data.billingId).subscribe({
        next: (res) => {
          this.invoice = res;
          this.loading = false;
        },
        error: (err) => {
          console.error('Error fetching invoice details:', err);
          this.loading = false;
        }
      });
    } else {
      this.loading = false;
    }
  }

  formatInvoiceDate(dateVal: any): string {
    if (!dateVal) return '-';
    const parsed = Number(dateVal);
    if (!isNaN(parsed)) {
      return new Date(parsed).toLocaleDateString();
    }
    return dateVal;
  }

  onClose(): void {
    this.dialogRef.close();
  }
}
