import { Component, OnInit } from '@angular/core';
import { ReportProcurementService } from 'src/app/services/report-procurement/report-procurement.service';

@Component({
  selector: 'app-report-procurement',
  templateUrl: './report-procurement.component.html',
  styleUrl: './report-procurement.component.scss'
})
export class ReportProcurementComponent implements OnInit {

  pendingPurchaseOrders: any[] = [];
  productSalesBySupplier: any = { labels: [], datasets: [] };
  productSalesOptions: any;
  loading: boolean = true;


  constructor(private reportService: ReportProcurementService) { }

  ngOnInit(): void {
    this.fetchData();
    this.fetchSalesData();
    this.initChartOptions();
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

  fetchSalesData(): void {
    this.loading = true;
    this.reportService.getProductSalesBySupplier().subscribe({
      next: (data) => {
        this.formatChartData(data);
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load sales data', err);
        this.loading = false;
      }
    });
  }

  formatChartData(data: any[]): void {
    const labels = data.map(item => item.supplier);
    const dataset = data.map(item => parseFloat(item.totalWorth));

    this.productSalesBySupplier = {
      labels: labels,
      datasets: [
        {
          label: 'Total Value (LKR)',
          backgroundColor: '#AAD5FF',
          borderColor: '#99C5EF',
          data: dataset
        }
      ]
    };
  }

  initChartOptions(): void {
    this.productSalesOptions = {
      indexAxis: 'y',
      plugins: {
        legend: {
          display: false
        }
      },
      scales: {
        x: {
          beginAtZero: true,
          grid: {
            display: false
          }
        },
        y: {
          grid: {
            display: false
          }
        }
      }
    };
  }


}

