import { Component, OnInit, OnDestroy } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Product } from '../../api/product';
import { ProductService } from '../../service/product.service';
import { Subscription, debounceTime } from 'rxjs';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { BillingService } from 'src/app/services/billing/billing.service';

@Component({
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
    items!: MenuItem[];

    products!: Product[];

    lineChartData: any;
    lineChartOptions: any;
    pieChartData: any;
    pieChartOptions: any;

    subscription!: Subscription;

    appointmentCountLast30Days: number = 0;

    newClientCountLast30Days: number = 0;

    mostUsedService: string;
    top5Employees: any[] = [];
    totalRevenue: number = 0;

    constructor(
        private productService: ProductService,
        public layoutService: LayoutService,
        private appointmentService: AppointmentSchedulingServiceService,
        private clientRegService: ClientRegServiceService,
        private billingService: BillingService
    ) {
        this.subscription = this.layoutService.configUpdate$
            .pipe(debounceTime(25))
            .subscribe((config) => {
                this.initChart();
                this.loadTop3ServicesPieChart();
            });
    }

    ngOnInit() {
        this.initChart();
        this.productService
            .getProductsSmall()
            .then((data) => (this.products = data));

        this.items = [
            { label: 'Add New', icon: 'pi pi-fw pi-plus' },
            { label: 'Remove', icon: 'pi pi-fw pi-minus' },
        ];

        this.loadAppointmentCount();
        this.loadNewClientCount();
        this.loadMostUsedService();
        this.loadTop3ServicesPieChart();
        this.loadTop5Employees();
        this.loadTotalRevenue();
    }

    //Dashboard card (Get Appointments in Last 30 Days)
    loadAppointmentCount() {
        this.appointmentService.getAppointmentCountLast30Days().subscribe({
            next: (count) => {
                this.appointmentCountLast30Days = count;
            },
            error: (error) => {
                console.error('Failed to load appointment count', error);
            },
        });
    }

    //Dashboard card (Get Most Used Service)
    loadMostUsedService() {
        this.appointmentService.getMostUsedService().subscribe({
            next: (serviceName) => {
                this.mostUsedService = serviceName;
            },
            error: (error) => {
                console.error('Failed to load most used service', error);
            },
        });
    }

    //Dashboard Card (Get New Clients in Last 30 Days)
    loadNewClientCount() {
        this.clientRegService.getNewClientRegistrationCountLast30Days().subscribe({
            next: (count) => {
                this.newClientCountLast30Days = count;
            },
            error: (error) => {
                console.error('Failed to load Client Registration Count', error);
            },
        });
    }

    //Appointment overview dashboard Chart
    initChart() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');
        const textColorSecondary = documentStyle.getPropertyValue(
            '--text-color-secondary'
        );
        const surfaceBorder =
            documentStyle.getPropertyValue('--surface-border');

        //Getting data from appointment-schedule
        //Gets a dataset like [["January",10],["February", 12],["March", 15],..]
        this.appointmentService.getAppointmentCountsByMonth().subscribe({
            //Splits data into 2 parts. 
            //labels = ["January", "February", "March",..]
            //counts = [10, 12, 15,...]
            next: (data) => {
                const labels = data.map((item) => item[0]);
                const counts = data.map((item) => item[1]);

                this.lineChartData = {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Appointments',
                            data: counts,
                            fill: false,
                            backgroundColor: '#ABD5FF',
                            borderColor: '#ABD5FF',
                            tension: 0.4,
                        },
                    ],
                };
            },
            error: (error) => {
                console.error('Failed to load chart data', error);
                this.lineChartData = { labels: [], datasets: [] };
            },
        });

        this.lineChartOptions = {
            plugins: {
                legend: {
                    labels: {
                        color: textColor,
                    },
                },
            },
            scales: {
                x: {
                    ticks: {
                        color: textColorSecondary,
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false,
                    },
                },
                y: {
                    ticks: {
                        color: textColorSecondary,
                    },
                    grid: {
                        color: surfaceBorder,
                        drawBorder: false,
                    },
                },
            },
        };
    }

    //Top 3 services - Pie chart
    loadTop3ServicesPieChart() {
        const documentStyle = getComputedStyle(document.documentElement);
        const textColor = documentStyle.getPropertyValue('--text-color');

        //Getting data from appointment-schedule
        this.appointmentService.getTop3Services().subscribe({
            //Splits data into 2 parts. 
            //labels = ["Female- Haircut", "Male-Haircut", "Nails",..]
            //counts = [10, 12, 15,...]
            next: (data) => {
                const labels = data.map((item) => item[0]);
                const counts = data.map((item) => item[1]);

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
                        color: textColor
                    }
                }
            }
        };
    }

    //Dashboard table (Top 5 Employees)
    loadTop5Employees() {
        this.appointmentService.getTop5Employees().subscribe({
            next: (data) => {
                this.top5Employees = data.map(item => ({
                    name: item[0],
                    count: item[1]
                }));
            },
            error: (error) => {
                console.error('Failed to load top 5 employees', error);
            },
        });
    }

    //Dashboard card (Calculate Total Revenue from Billings)
    loadTotalRevenue() {
        this.billingService.getData().subscribe({
            next: (billings: any[]) => {
                const data = Array.isArray(billings) ? billings : (billings as any)?.data || [];
                this.totalRevenue = data.reduce((total, billing) => {
                    // Each billing has a 'purchases' array
                    const billingTotal = (billing.purchases || []).reduce((subTotal, p) => {
                        return subTotal + ((p.quantity || 0) * (p.price || 0));
                    }, 0);
                    return total + billingTotal;
                }, 0);
            },
            error: (error) => {
                console.error('Failed to load revenue data', error);
            }
        });
    }

    //ngOnDestroy is an Angular lifecycle hook...
    //It is mainly used to clean up resources like unsubscribing from Observables to prevent memory leaks
    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}
