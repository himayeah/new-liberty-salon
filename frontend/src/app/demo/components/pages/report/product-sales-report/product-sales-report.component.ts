import { Component, OnInit } from '@angular/core';
import { ReportProductSalesService } from 'src/app/services/report-product-sales/report-product-sales.service';

@Component({
  selector: 'app-product-sales-report',
  templateUrl: './product-sales-report.component.html',
  styleUrl: './product-sales-report.component.scss'
})
export class ProductSalesReportComponent implements OnInit {

  productSalesData: any[] = [];
  loading: boolean = true;
  chartData: any;
  chartOptions: any;

  constructor(private reportService: ReportProductSalesService) { }

  ngOnInit(): void {
    this.initChartOptions();
    this.fetchData();
  }

  initChartOptions(): void {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    const textColorSecondary = documentStyle.getPropertyValue('--text-color-secondary');
    const surfaceBorder = documentStyle.getPropertyValue('--content-border-color') || '#dfe7ef';

    this.chartOptions = {
      plugins: {
        legend: {
          labels: {
            color: textColor
          }
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        },
        x: {
          ticks: {
            color: textColorSecondary
          },
          grid: {
            color: surfaceBorder,
            drawBorder: false
          }
        }
      }
    };
  }

  fetchData(): void {
    this.loading = true;
    this.reportService.getData().subscribe({
      next: (data) => {
        this.productSalesData = data || [];
        this.prepareChartData();
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load report data', err);
        this.loading = false;
      }
    });
  }

  prepareChartData(): void {
    const labels = this.productSalesData.map(item => item.productName);
    const data = this.productSalesData.map(item => item.total);

    this.chartData = {
      labels: labels,
      datasets: [
        {
          label: 'Total Product Sales',
          data: data,
          backgroundColor: [
            'rgba(255, 159, 64, 0.2)',
            'rgba(75, 192, 192, 0.2)',
            'rgba(54, 162, 235, 0.2)',
            'rgba(153, 102, 255, 0.2)'
          ],
          borderColor: [
            'rgb(255, 159, 64)',
            'rgb(75, 192, 192)',
            'rgb(54, 162, 235)',
            'rgb(153, 102, 255)'
          ],
          borderWidth: 1
        }
      ]
    };
  }

}
