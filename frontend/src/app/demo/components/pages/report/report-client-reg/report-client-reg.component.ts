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
  pieChartOptions: any;
  pieChartData: any;

  selectedDuration: string = 'allTime';
  customStartDate: Date | null = null;
  customEndDate: Date | null = null;
  maxDate: Date = new Date();

  constructor(private reportService: ReportClientRegService) { }

  ngOnInit(): void {
    this.initChartOptions();
    this.fetchData();
    this.fetchTableData();
    this.loadClientRegistrationsByAgeGroup();
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

  // Registration Breakdown by Gender Table
  fetchData(): void {
    this.loading = true;
    this.reportService.getRegistrationData().subscribe({
      next: (data) => {
        this.clientsData = data || [];
        this.loading = false;
        console.log("Registration Breakdown by Gender:", this.clientsData);
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

  // Client Registrations by Age Group (Pie Chart)
  // After creating the method, make sure to call it in ngOnInit()
  loadClientRegistrationsByAgeGroup(startDate?: string, endDate?: string) {
    this.reportService.getRegistrationsByAgeGroup(startDate, endDate).subscribe({
      next: (data) => {

        const labels = data.map((item) => item.clientAgeGroup);
        const counts = data.map((item) => item.totalClientCount);
        console.log("Registration Breakdown by Age Group:", data);


        this.pieChartData = {
          labels: labels,
          datasets: [
            {
              data: counts,
              backgroundColor: [
                '#B6C787', // 1st Place
                '#ABD5FF', // 2nd Place
                '#FFCDCF'  // 3rd Place
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
        this.pieChartData = { labels: [], datasets: [] };
      },
    });

    this.pieChartOptions = {
      plugins: {
        legend: {
          labels: {
            usePointStyle: true,
          }
        }
      }
    };
  }

  // Below formatDate(), onDurationCHange(), onDateRangeChange() are related to the Date Filter of the Pie Chart (Client registration by Age Group)
  // format date changes the dates to yyyy-mm-dd format which is required by the backend API for filtering
  formatDate(date: Date): string {
    return date.toISOString().split('T')[0];
  }

  // set startDate & endDate to null for allTime, last 1 month and last 3 months
  onDurationChange(): void {
    if (this.selectedDuration === 'allTime') {
      this.customStartDate = null;
      this.customEndDate = null;
      this.loadClientRegistrationsByAgeGroup();
    } else if (this.selectedDuration === 'last1Month') {
      // newDate() gives today date
      // getMonth gets the month's number (ex: Januaru -> 0, Feb -> 1 etc)
      const today = new Date();
      const pastDate = new Date();
      // setMonth changes the pastDate month value
      pastDate.setMonth(today.getMonth() - 1);
      // past date: // 28 May 2026
      // today: // 28 June 2026
      // after sending these to 'formatDate()' function the dates changes to '2026-05-28' & '2026-06-28' respectively
      this.loadClientRegistrationsByAgeGroup(this.formatDate(pastDate), this.formatDate(today));
    } else if (this.selectedDuration === 'last3Months') {
      const today = new Date();
      const pastDate = new Date();
      pastDate.setMonth(today.getMonth() - 3);
      this.loadClientRegistrationsByAgeGroup(this.formatDate(pastDate), this.formatDate(today));
    } else if (this.selectedDuration === 'custom') {
      this.customStartDate = null;
      this.customEndDate = null;
    }
  }

  onDateRangeChange(): void {
    if (this.selectedDuration === 'custom' && this.customStartDate && this.customEndDate) {
      this.loadClientRegistrationsByAgeGroup(
        this.formatDate(this.customStartDate),
        this.formatDate(this.customEndDate)
      );
    }
  }


}
