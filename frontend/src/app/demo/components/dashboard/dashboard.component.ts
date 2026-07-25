import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Product } from '../../api/product';
import { ProductService } from '../../service/product.service';
import { Subscription, debounceTime, forkJoin } from 'rxjs';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { ClientRegServiceService } from 'src/app/services/client-reg/client-reg-service.service';
import { BillingService } from 'src/app/services/billing/billing.service';
import { ToastrService } from 'ngx-toastr';
import { EmployeeAuthService } from '../../../employee-workspace/services/employee-auth.service';
import { Role } from '../../../models/role.enum';
import { InventoryServiceService } from 'src/app/services/inventory/inventory-service.service';
import { EmployeeAttendanceServiceService } from 'src/app/services/employee-attendance/employee-attendance-service.service';
import { EmployeeRegServicesService } from 'src/app/services/employee-reg/employee-reg-services.service';

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
    notificationInterval: any;
    //NEWLYADDED
    topEmployeeData: any = null;

    isReceptionist: boolean = false;
    todayAppointmentsCount: number = 0;
    appointmentsReadyForBillingCount: number = 0;
    completedAppointmentsTodayCount: number = 0;
    newClientsTodayCount: number = 0;
    activeTasksCount: number = 0;
    todayScheduleList: any[] = [];

    isManager: boolean = false;
    managerTodayAppointmentsCount: number = 0;
    managerCompletedAppointmentsCount: number = 0;
    managerCancelledAppointmentsCount: number = 0;
    employeesPresentTodayCount: number = 0;
    employeeWorkloadList: any[] = [];

    constructor(
        private productService: ProductService,
        public layoutService: LayoutService,
        private appointmentService: AppointmentSchedulingServiceService,
        private clientRegService: ClientRegServiceService,
        private billingService: BillingService,
        private toastr: ToastrService,
        private employeeAuthService: EmployeeAuthService,
        private inventoryService: InventoryServiceService,
        private router: Router,
        private employeeAttendanceService: EmployeeAttendanceServiceService,
        private employeeRegService: EmployeeRegServicesService
    ) {
        this.subscription = this.layoutService.configUpdate$
            .pipe(debounceTime(25))
            .subscribe((config) => {
                this.initChart();
                this.loadTop3ServicesPieChart();
            });
    }

    ngOnInit() {
        this.isReceptionist = this.employeeAuthService.getRole() === Role.RECEPTIONIST;
        this.isManager = this.employeeAuthService.getRole() === Role.MANAGER;
        if (this.isReceptionist) {
            this.loadReceptionistMetrics();
        } else if (this.isManager) {
            this.loadManagerMetrics();
        } else {
            this.loadActiveTasks();
        }

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
        this.startNotificationPolling();
        this.checkInventoryReorderAlerts();
        this.getTopEmployeeData();
    }

    loadActiveTasks() {
        const today = new Date();
        const todayStr = new Date(today.getTime() - (today.getTimezoneOffset() * 60000))
            .toISOString().split('T')[0];

        this.appointmentService.getData().subscribe({
            next: (appointments: any[]) => {
                const list = appointments || [];
                // Active tasks: appointments scheduled for today that are not CANCELLED or COMPLETED
                const activeStatuses = ['BOOKED', 'CHECK_IN', 'CHECKED_IN', 'IN PROGRESS'];
                this.activeTasksCount = list.filter(app => 
                    app.appointmentDate === todayStr && activeStatuses.includes(app.appointmentStatus)
                ).length;
            },
            error: (error) => {
                console.error('Failed to load active tasks count', error);
            }
        });
    }

    loadReceptionistMetrics() {
        const today = new Date();
        const todayStr = new Date(today.getTime() - (today.getTimezoneOffset() * 60000))
            .toISOString().split('T')[0];

        this.appointmentService.getData().subscribe({
            next: (appointments: any[]) => {
                const list = appointments || [];
                // 1. Today's Appointments count: count of all appointments where date is today and status is NOT CANCELLED
                this.todayAppointmentsCount = list.filter(app => 
                    app.appointmentDate === todayStr && app.appointmentStatus !== 'CANCELLED'
                ).length;

                // 2. Appointments ready for Billing: count of all appointments where status is 'READY FOR BILLING'
                this.appointmentsReadyForBillingCount = list.filter(app => 
                    app.appointmentStatus === 'READY FOR BILLING'
                ).length;

                // 3. Completed Appointments today: count of all appointments where date is today and status is 'COMPLETED'
                this.completedAppointmentsTodayCount = list.filter(app => 
                    app.appointmentDate === todayStr && app.appointmentStatus === 'COMPLETED'
                ).length;

                // Today's schedule list (excluding cancelled, sorted by start time)
                this.todayScheduleList = list.filter(app => 
                    app.appointmentDate === todayStr && app.appointmentStatus !== 'CANCELLED'
                ).sort((a, b) => {
                    const timeA = a.appointmentStartTime || '';
                    const timeB = b.appointmentStartTime || '';
                    return timeA.localeCompare(timeB);
                });
            },
            error: (error) => {
                console.error('Failed to load receptionist appointment metrics', error);
            }
        });

        this.clientRegService.getData().subscribe({
            next: (clients: any[]) => {
                const list = clients || [];
                // 4. New Clients today: count of all clients registered today
                this.newClientsTodayCount = list.filter(client => {
                    if (!client.registrationDate) return false;
                    return client.registrationDate.startsWith(todayStr);
                }).length;
            },
            error: (error) => {
                console.error('Failed to load receptionist client metrics', error);
            }
        });
    }

    loadManagerMetrics() {
        const today = new Date();
        const todayStr = new Date(today.getTime() - (today.getTimezoneOffset() * 60000))
            .toISOString().split('T')[0];

        forkJoin({
            appointments: this.appointmentService.getData(),
            attendances: this.employeeAttendanceService.getData(),
            employees: this.employeeRegService.getData()
        }).subscribe({
            next: (res: any) => {
                const appointmentsList = res.appointments || [];
                const attendancesList = res.attendances || [];
                const employeesList = res.employees || [];

                // 1. Today's All Appointments: count of all appointments scheduled for today
                this.managerTodayAppointmentsCount = appointmentsList.filter(app => 
                    app.appointmentDate === todayStr
                ).length;

                // 2. Today's completed Appointments: count of appointments where date is today and status is 'COMPLETED'
                this.managerCompletedAppointmentsCount = appointmentsList.filter(app => 
                    app.appointmentDate === todayStr && app.appointmentStatus === 'COMPLETED'
                ).length;

                // 3. Appointments cancelled: count of appointments where date is today and status is 'CANCELLED'
                this.managerCancelledAppointmentsCount = appointmentsList.filter(app => 
                    app.appointmentDate === todayStr && app.appointmentStatus === 'CANCELLED'
                ).length;

                // 4. Employees Present today: count of check-ins with status 'Present' or 'Late' where checkInTime matches today
                this.employeesPresentTodayCount = attendancesList.filter(att => {
                    if (!att.checkInTime) return false;
                    const recordDate = new Date(att.checkInTime);
                    const recordDateStr = new Date(recordDate.getTime() - (recordDate.getTimezoneOffset() * 60000))
                        .toISOString().split('T')[0];
                    return recordDateStr === todayStr && (att.status === 'Present' || att.status === 'Late');
                }).length;

                // 5. Stylists workload computation
                const stylists = employeesList.filter((emp: any) => 
                    emp.designation && emp.designation.toLowerCase().includes('stylist')
                );

                this.employeeWorkloadList = stylists.map((stylist: any) => {
                    const stylistApps = appointmentsList.filter((app: any) => 
                        app.appointmentDate === todayStr && 
                        app.employeeId === stylist.id && 
                        app.appointmentStatus !== 'CANCELLED'
                    );

                    const todayAtt = attendancesList.find((att: any) => {
                        if (!att.checkInTime || att.employeeId !== stylist.id) return false;
                        const recordDate = new Date(att.checkInTime);
                        const recordDateStr = new Date(recordDate.getTime() - (recordDate.getTimezoneOffset() * 60000))
                            .toISOString().split('T')[0];
                        return recordDateStr === todayStr;
                    });

                    return {
                        stylistName: stylist.employeeName,
                        appointmentCount: stylistApps.length,
                        status: todayAtt ? todayAtt.status : 'Not Checked In'
                    };
                });
            },
            error: (error) => {
                console.error('Failed to load manager metrics', error);
            }
        });
    }
    // Runs startNotificationPolling() method at every 60,000 ms intervals ( equals to 1 minute)
    startNotificationPolling() {
        this.checkUpcomingNotifications();
        this.checkInventoryReorderAlerts();
        this.notificationInterval = setInterval(() => {
            this.checkUpcomingNotifications();
            this.checkInventoryReorderAlerts();
        }, 60000); // Check every minute
    }

    // Runs getUpcomingNotifications method at every 60,000 ms intervals ( equals to 1 minute) 
    checkUpcomingNotifications() {
        this.appointmentService.getUpcomingNotifications().subscribe({
            next: (notifications) => {
                notifications.forEach((notif) => {
                    const message = `
                        <strong>Service:</strong> ${notif.serviceName}<br/>
                        <strong>Phone:</strong> ${notif.clientPhone || 'N/A'}<br/>
                        <strong>Time:</strong> ${notif.appointmentStartTime}
                    `;
                    this.toastr.info(message, `Reminder: ${notif.clientName}`, {
                        timeOut: 15000,
                        progressBar: true,
                        enableHtml: true,
                        positionClass: 'toast-top-right',
                        toastClass: 'ngx-toastr appointment-reminder-toast'
                    });
                });
            },
            error: (error) => {
                console.error('Failed to load upcoming notifications', error);
            },
        });
    }

    // Checks for products in inventory that have reached re-order limits
    checkInventoryReorderAlerts() {
        const role = this.employeeAuthService.getRole();
        if (role === Role.OWNER || role === Role.MANAGER) {
            const username = window.localStorage.getItem('user_name') || this.employeeAuthService.getEmployeeData()?.email || 'guest';
            this.inventoryService.getReorderAlerts().subscribe({
                next: (alerts: any[]) => {
                    const dismissed = JSON.parse(localStorage.getItem('dismissedReorderAlerts_' + username) || '[]');

                    alerts.forEach((alert) => {
                        // Check if this alert has already been dismissed for the current stock level
                        const alreadyDismissed = dismissed.some((d: any) => d.id === alert.id && d.currentStock === alert.currentStock);
                        if (alreadyDismissed) {
                            return;
                        }

                        const productName = alert.product?.productName || 'Product';
                        const toast = this.toastr.warning(
                            `${productName} currently at the re-order limit`,
                            'Inventory Alert',
                            {
                                timeOut: 0,
                                extendedTimeOut: 0,
                                closeButton: true,
                                positionClass: 'toast-top-right'
                            }
                        );

                        // Save dismissal preference once closed by the user
                        toast.onHidden.subscribe(() => {
                            const currentDismissed = JSON.parse(localStorage.getItem('dismissedReorderAlerts_' + username) || '[]');
                            const filtered = currentDismissed.filter((d: any) => d.id !== alert.id);
                            filtered.push({ id: alert.id, currentStock: alert.currentStock });
                            localStorage.setItem('dismissedReorderAlerts_' + username, JSON.stringify(filtered));
                        });
                    });
                },
                error: (error) => {
                    console.error('Failed to load inventory alerts', error);
                }
            });
        }
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

                console.log("This is Pie Chart Data:", data);

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
        if (this.notificationInterval) {
            clearInterval(this.notificationInterval);
        }
    }

    // //NEWLYADDED
    // getTopEmployeeData() {
    //     this.appointmentService.getTopEmployeeData().subscribe({
    //         next: (data) => {
    //             console.log('Top Employee Data', data);
    //             this.topEmployeeData = data;
    //         },
    //         error: (err) => {
    //             console.error('Failed to load table data', err);
    //         }
    //     });
    // }

    // Dashboard card (Get Most Used Service)
    getTopEmployeeData() {
        this.appointmentService.getTopEmployeeData().subscribe({
            next: (employeeData) => {
                console.log("This is Top EMployee Data NEWLY ADDED:", employeeData);
                // backend return array data[0] so taking the first element
                this.topEmployeeData = employeeData[0];
            },
            error: (error) => {
                console.error('Failed to load Top Employee Data', error);
            },
        });
    }

    navigateToRegisterClient() {
        this.router.navigate(['/pages/client-reg'], { queryParams: { openAddModal: 'true' } });
    }

    navigateToNewAppointment() {
        this.router.navigate(['/pages/appointment-schedule'], { queryParams: { openAddModal: 'true' } });
    }

    navigateToCreateBilling() {
        this.router.navigate(['/pages/billing'], { queryParams: { openAddModal: 'true' } });
    }

}
