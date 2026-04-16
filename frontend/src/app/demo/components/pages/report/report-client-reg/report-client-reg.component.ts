import { Component, OnInit } from '@angular/core';
import { ReportClientRegService } from 'src/app/services/report-client-reg/report-client-reg.service';

@Component({
  selector: 'app-report-client-reg',
  templateUrl: './report-client-reg.component.html',
  styleUrls: ['./report-client-reg.component.scss']
})
export class ReportClientRegComponent implements OnInit {

  clientsData: any[] = [];
  registrationData: any[] = [];
  loading: boolean = true;
  chartData: any;
  chartOptions: any;

  constructor(private reportService: ReportClientRegService) { }

  ngOnInit(): void {
    this.initChartOptions();
    this.fetchData();
    this.fetchTableData();
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
    this.reportService.getRegistrationData().subscribe({
      next: (data) => {
        this.clientsData = data || [];
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load report data', err);
        this.loading = false;
      }
    });
  }

  fetchTableData(): void {
    this.loading = true;
    this.reportService.getData().subscribe({
      next: (data) => {
        this.registrationData = data || [];
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
    const labels = this.registrationData.map(item => item.registrationYear);
    const data = this.registrationData.map(item => item.totalRegistrations);

    this.chartData = {
      labels: labels,
      datasets: [
        {
          label: 'Total Registrations',
          data: data,
          backgroundColor: ['rgba(255, 159, 64, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(153, 102, 255, 0.2)'],
          borderColor: ['rgb(255, 159, 64)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)', 'rgb(153, 102, 255)'],
          borderWidth: 1
        }
      ]
    };
  }
}
