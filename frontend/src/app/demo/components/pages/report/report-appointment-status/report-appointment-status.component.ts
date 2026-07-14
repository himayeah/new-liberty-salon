import { Component, OnInit } from '@angular/core';
import { ReportAppointmentStatusService } from 'src/app/services/report-appointment-status/report-appointment-status.service';

@Component({
  selector: 'app-report-appointment-status',
  templateUrl: './report-appointment-status.component.html',
  styleUrl: './report-appointment-status.component.scss'
})
export class ReportAppointmentStatusComponent implements OnInit {

  appointmentCancellationData: any[] = [];
  loading: boolean = true;
  chartData: any;
  chartOptions: any;
  data: number;
  appointmentsBySourceData: any;
  appointmentsBySourceOptions: any;
  appointmentCountByStatusData: any;

  constructor(private reportService: ReportAppointmentStatusService) { }

  ngOnInit(): void {
    this.initChartOptions();
    this.fetchData();
    this.loadAppointmentCancellationData();
    this.loadAppointmentsBySourcePieChart();
    this.loadAppointmentCountByStatus();
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

  // appointment cancellation count chart
  fetchData(): void {
    this.loading = true;
    this.reportService.getAppointmentCancellationData().subscribe({
      next: (data) => {
        console.log('Cancellation details:', data); 
        this.prepareChartData(data); 
        this.loading = false;
      },
      error: (err) => {
        console.error('Failed to load report data', err);
        this.loading = false;
      }
    });
  }

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


  // appointmentcancellation details table
  loadAppointmentCancellationData(): void {
    this.reportService.getAppointmentCancellationDetails().subscribe({
      next: (data) => {
        console.log('cancellation table details:', data);
        this.appointmentCancellationData = data;
      },
      error: (err) => {
        console.error('Failed to load table data', err);
      }
    });
  }

  // appointmentsBySource (Pie Chart)
  loadAppointmentsBySourcePieChart() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.reportService.getAppointmentsBySource().subscribe({

      next: (data) => {
        // map the JSON data you receive from backend. use console.log to check the data field
        const labels = data.map(item => item.bookingSource);
        const counts = data.map(item => item.totalCount); //Appointment count as the data

        this.appointmentsBySourceData = {
          labels: labels,
          datasets: [
            {
              data: counts,
              backgroundColor: [
                '#B6C787',
                '#ABD5FF',
                '#FFCDCF'
              ],
              hoverBackgroundColor: [
                '#A5B676',
                '#9CC4EE',
                '#EEBCC0'
              ]
            },
          ],
        };
      },
      error: (error) => {
        console.error('Failed to load chart data', error);
        this.appointmentsBySourceData = { labels: [], datasets: [] };
      },
    });

    this.appointmentsBySourceOptions = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor
          }
        }
      }
    };
  }

  // Appointment Count By Status
  loadAppointmentCountByStatus() {
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    this.reportService.getAppointmentCountByStatus().subscribe({

      next: (data) => {
        console.log("Appointment count by Status data:", data);
         // map the JSON data you receive from backend. use console.log to check the data field
        const labels = data.map(item => item.appointmentStatus);
        const counts = data.map(item => item.appointmentCount); //Appointment count as the data

        this.appointmentCountByStatusData = {
          labels: labels,
          datasets: [
            {
              data: counts,
              backgroundColor: [
                '#B6C787',
                '#ABD5FF',
                '#FFCDCF'
              ],
              hoverBackgroundColor: [
                '#A5B676',
                '#9CC4EE',
                '#EEBCC0'
              ]
            },
          ],
        };
      },
      error: (error) => {
        console.error('Failed to load chart data', error);
        this.appointmentsBySourceData = { labels: [], datasets: [] };
      },
    });

    this.appointmentsBySourceOptions = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
            color: textColor
          }
        }
      }
    };
  }



}