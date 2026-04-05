import { Component } from '@angular/core';
import { ReportAppointmentStatusService } from 'src/app/services/report-appointment-status/report-appointment-status.service';

@Component({
  selector: 'app-report-appointment-status',
  templateUrl: './report-appointment-status.component.html',
  styleUrl: './report-appointment-status.component.scss'
})
export class ReportAppointmentStatusComponent {

  appointmentCancellationData: any[] = [];
  loading: boolean = true;
  chartData: any;
  chartOptions: any;
  data: number;

  constructor(private reportService: ReportAppointmentStatusService) { }

  ngOnInit(): void {
    this.initChartOptions();
    this.fetchData();
    this.loadAppointmentCancellationData();
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

  // OLD VERSION (ARRAY BASED)
  // fetchData(): void {
  //   this.loading = true;
  //   this.reportService.getAppointmentCancellationData().subscribe({
  //     next: (data) => {
  //       this.appointmentCancellationData = data || [];
  //       this.prepareChartData();
  //       this.loading = false;
  //     },
  //     error: (err) => {
  //       console.error('Failed to load report data', err);
  //       this.loading = false;
  //     }
  //   });
  // }

  // NEW VERSION (COUNT BASED)
  fetchData(): void {
    this.loading = true;

    this.reportService.getAppointmentCancellationData().subscribe({
      next: (data) => {
        console.log('API DATA:', data); // should be a number

        this.prepareChartData(data); // pass count directly
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load report data', err);
        this.loading = false;
      }
    });
  }

  // prepareChartData(): void {
  //   const labels = this.appointmentCancellationData.map(item => item.registrationYear);
  //   const data = this.appointmentCancellationData.map(item => item.totalRegistrations);

  //   this.chartData = {
  //     labels: labels,
  //     datasets: [
  //       {
  //         label: 'Total Appointment Cancellations',
  //         data: data,
  //         backgroundColor: ['rgba(255, 159, 64, 0.2)', 'rgba(75, 192, 192, 0.2)', 'rgba(54, 162, 235, 0.2)', 'rgba(153, 102, 255, 0.2)'],
  //         borderColor: ['rgb(255, 159, 64)', 'rgb(75, 192, 192)', 'rgb(54, 162, 235)', 'rgb(153, 102, 255)'],
  //         borderWidth: 1
  //       }
  //     ]
  //   };
  // }

  prepareChartData(count: number): void {
    this.chartData = {
      labels: ['Cancelled'],
      datasets: [
        {
          label: 'Total Appointment Cancellations',
          data: [count],
          backgroundColor: ['rgba(255, 159, 64, 0.2)'],
          borderColor: ['rgb(255, 159, 64)'],
          borderWidth: 1
        }
      ]
    };
  }


  //Table Data
  loadAppointmentCancellationData(): void {
    this.reportService.getAppointmentCancellationDetails().subscribe({
      next: (data) => {
        console.log('TABLE DATA:', data);
        this.appointmentCancellationData = data;
      },
      error: (err) => {
        console.error('Failed to load table data', err);
      }
    });
  }

}