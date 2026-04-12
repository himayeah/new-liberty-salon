import { Component, OnInit, OnDestroy } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Product } from '../../api/product';
import { ProductService } from '../../service/product.service';
import { Subscription, debounceTime } from 'rxjs';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';

@Component({
    templateUrl: './dashboard.component.html',
    styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
    items!: MenuItem[];

    products!: Product[];

    chartData: any;

    chartOptions: any;

    subscription!: Subscription;

    appointmentCountLast30Days: number = 0;

    newClientCountLast30Days: number = 0;

    mostUsedService: string;

    constructor(
        private productService: ProductService,
        public layoutService: LayoutService,
        private appointmentService: AppointmentSchedulingServiceService,
        private clientRegService: ClientRegServiceService
    ) {
        this.subscription = this.layoutService.configUpdate$
            .pipe(debounceTime(25))
            .subscribe((config) => {
                this.initChart();
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

                this.chartData = {
                    labels: labels,
                    datasets: [
                        {
                            label: 'Appointments',
                            data: counts,
                            fill: false,
                            backgroundColor:
                                documentStyle.getPropertyValue('--bluegray-700'),
                            borderColor:
                                documentStyle.getPropertyValue('--bluegray-700'),
                            tension: 0.4,
                        },
                    ],
                };
            },
            error: (error) => {
                console.error('Failed to load chart data', error);
                this.chartData = { labels: [], datasets: [] };
            },
        });

        this.chartOptions = {
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

    //ngOnDestroy is an Angular lifecycle hook that is triggered when a component is destroyed. 
    //It is mainly used to clean up resources like unsubscribing from Observables to prevent memory leaks
    ngOnDestroy() {
        if (this.subscription) {
            this.subscription.unsubscribe();
        }
    }
}
