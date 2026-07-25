import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EmployeeAuthService } from '../../services/employee-auth.service';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-stylist-dashboard',
  templateUrl: './stylist-dashboard.component.html',
  styleUrls: ['./stylist-dashboard.component.scss']
})
export class StylistDashboardComponent implements OnInit {

  stylist: any;
  appointments: any[] = [];
  filteredAppointments: any[] = [];

  todayAppointmentsCount: number = 0;
  completedAppointmentsCount: number = 0;

  filterDate: Date | null = null;
  filterStatus: string = '';

  constructor(
    private employeeAuthService: EmployeeAuthService,
    private appointmentService: AppointmentSchedulingServiceService,
    private messageService: MessageServiceService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.stylist = this.employeeAuthService.getEmployeeData();
    if (this.stylist) {
      this.loadAppointments();
    }
  }

  loadAppointments(): void {
    this.appointmentService.getData().subscribe({
      next: (res: any[]) => {
        console.log("Stylist Dashboard Loaded Appointments:", res);
        console.log("Logged In Stylist:", this.stylist);
        // Filter appointments only for this specific stylist by matching name
        this.appointments = res.filter(app => {
          const match = app.employeeId === this.stylist.id || app.employeeName === this.stylist.employeeName;
          console.log("Match check:", app.employeeName, "vs", this.stylist.employeeName, "->", match);
          return match;
        });

        const today = new Date();
        const todayStr = new Date(today.getTime() - (today.getTimezoneOffset() * 60000))
          .toISOString().split('T')[0];

        this.todayAppointmentsCount = this.appointments.filter(app => 
          app.appointmentDate === todayStr && app.appointmentStatus !== 'CANCELLED'
        ).length;

        this.completedAppointmentsCount = this.appointments.filter(app => 
          app.appointmentStatus === 'COMPLETED'
        ).length;

        this.applyFilters();
      },
      error: (err) => {
        this.messageService.showError('Failed to load appointments');
        console.error(err);
      }
    });
  }

  applyFilters(): void {
    this.filteredAppointments = this.appointments.filter(app => {
      // Date filter (matching exact date string format: YYYY-MM-DD or similar)
      let matchesDate = true;
      if (this.filterDate) {
        // Create a local date string YYYY-MM-DD to compare
        const localDateStr = new Date(this.filterDate.getTime() - (this.filterDate.getTimezoneOffset() * 60000))
          .toISOString().split('T')[0];

        matchesDate = app.appointmentDate === localDateStr;
      }

      // Status filter
      let matchesStatus = true;
      if (this.filterStatus) {
        matchesStatus = app.appointmentStatus === this.filterStatus;
      }

      return matchesDate && matchesStatus;
    });

    // Sort by Date and Time
    this.filteredAppointments.sort((a, b) => {
      const dateA = new Date(`${a.appointmentDate}T${a.appointmentStartTime}`);
      const dateB = new Date(`${b.appointmentDate}T${b.appointmentStartTime}`);
      return dateA.getTime() - dateB.getTime();
    });
  }

  clearFilters(): void {
    this.filterDate = null;
    this.filterStatus = '';
    this.applyFilters();
  }

  markAsComplete(appointment: any): void {
    const updatedAppointment = { ...appointment, appointmentStatus: 'READY FOR BILLING' };
    console.log("Stylist marked as complete", appointment)

    this.appointmentService.editData(appointment.id, updatedAppointment).subscribe({
      next: () => {
        this.messageService.showSuccess(`Appointment for ${appointment.clientName} marked as Ready for Billing.`);
        this.loadAppointments(); // Refresh the list
      },
      error: (err) => {
        this.messageService.showError('Failed to update status');
        console.error(err);
      }
    });
  }

  // markAsInProgress(appointment: any): void {
  //   const updatedAppointment = { ...appointment, appointmentStatus: 'IN PROGRESS' };
  //   this.appointmentService.editData(appointment.id, updatedAppointment).subscribe({
  //     next: () => {
  //       this.messageService.showSuccess(`Appointment for ${appointment.clientName} marked as In Progress.`);
  //       this.loadAppointments(); // Refresh the list
  //     },
  //     error: (err) => {
  //       this.messageService.showError('Failed to update status');
  //       console.error(err);
  //     }
  //   });
  // }

  logout(): void {
    this.employeeAuthService.logout();
    this.router.navigate(['/employee-workspace']);
  }

}
