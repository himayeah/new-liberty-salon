import { Component, OnInit } from '@angular/core';
import { ReportProcurementService } from 'src/app/services/report-procurement/report-procurement.service';

@Component({
  selector: 'app-report-procurement',
  templateUrl: './report-procurement.component.html',
  styleUrl: './report-procurement.component.scss'
})
export class ReportProcurementComponent implements OnInit {

  pendingPurchaseOrders: any[] = [];
  loading: boolean = true;


  constructor(private reportService: ReportProcurementService) { }

  ngOnInit(): void {
    this.fetchData();
  }


  fetchData(): void {
    this.loading = true;
    this.reportService.getPendingPurchaseOrders().subscribe({
      next: (data) => {
        this.pendingPurchaseOrders = data || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load report data', err);
        this.loading = false;
      }
    });
  }


}

