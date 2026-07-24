import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { PublicBookingService } from '../../services/public-booking.service';
import { MessageService } from 'primeng/api';
import { AppointmentSchedulingServiceService } from 'src/app/services/appointment_scheduling/appointment-scheduling-service.service';
import { EmployeeLeaveServiceService } from 'src/app/services/employee-leave/employee-leave-service.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit, OnDestroy {
  @Input() client: any;
  bookingForm: FormGroup;
  stylists: any[] = [];
  services: any[] = [];
  bookingConfirmed: boolean = false;
  minDate: Date = new Date();
  readonly salonOpeningTime = '10:00';
  readonly salonClosingTime = '18:00';
  appointments: any[] = [];
  employeeLeaveData: any[] = [];
  timeSlots: { value: string, label: string, disabled: boolean }[] = [];
  private subs: Subscription[] = [];

  constructor(
    private fb: FormBuilder,
    private publicBookingService: PublicBookingService,
    private messageService: MessageService,
    private appointmentService: AppointmentSchedulingServiceService,
    private employeeLeaveService: EmployeeLeaveServiceService
  ) {
    this.minDate.setHours(0, 0, 0, 0);
    this.bookingForm = this.fb.group({
      clientId: ['', Validators.required],
      employeeId: [null, [this.stylistAvailabilityValidator.bind(this)]],
      serviceId: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      appointmentStartTime: ['', [Validators.required]],
      appointmentStatus: ['PENDING'],
      bookingSource: ['ONLINE']
    }, { validators: this.validateSalonHours.bind(this) });
  }

  ngOnInit() {
    if (this.client) {
      this.bookingForm.patchValue({ clientId: this.client.id });
    }
    this.loadData();
    this.generateTimeSlots();

    const triggerAvailabilityCheck = () => {
      const employeeIdCtrl = this.bookingForm.get('employeeId');
      if (employeeIdCtrl?.value) {
        employeeIdCtrl.markAsTouched();
        employeeIdCtrl.updateValueAndValidity();
      }
    };

    const dateCtrl = this.bookingForm.get('appointmentDate');
    const startTimeCtrl = this.bookingForm.get('appointmentStartTime');

    if (dateCtrl) {
      this.subs.push(dateCtrl.valueChanges.subscribe(() => triggerAvailabilityCheck()));
    }
    if (startTimeCtrl) {
      this.subs.push(startTimeCtrl.valueChanges.subscribe(() => triggerAvailabilityCheck()));
    }

    this.subs.push(this.bookingForm.valueChanges.subscribe(() => {
      this.updateTimeSlotsAvailability();
    }));
  }

  ngOnDestroy() {
    this.subs.forEach(s => s.unsubscribe());
  }

  loadData() {
    this.publicBookingService.getStylists().subscribe((data: any) => {
      this.stylists = data;
      this.updateTimeSlotsAvailability();
    });
    this.publicBookingService.getServices().subscribe((data: any) => {
      this.services = data;
      this.updateTimeSlotsAvailability();
    });
    this.loadAppointments();
    this.loadEmployeeLeaveData();
  }

  loadAppointments(): void {
    this.appointmentService.getData().subscribe(res => {
      this.appointments = (res as any[]) || [];
      this.updateTimeSlotsAvailability();
    });
  }

  loadEmployeeLeaveData(): void {
    this.employeeLeaveService.getData().subscribe({
      next: (response: any[]) => {
        this.employeeLeaveData = response || [];
        this.updateTimeSlotsAvailability();
      },
      error: (err) =>
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Employee Leave data retrieval failed: ' + err.message })
    });
  }

  onSubmit() {
    if (this.bookingForm.valid) {
      const startTime = this.parseTimeInput(this.bookingForm.value.appointmentStartTime);
      if (!startTime) {
        this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Please enter a valid time like 10:00 AM.' });
        return;
      }

      const selectedService = this.services.find(s => s.id === this.bookingForm.value.serviceId);
      const duration = selectedService?.duration || 60;
      const endTime = this.calculateEndTime(startTime, duration);

      const bookingData = {
        ...this.bookingForm.value,
        appointmentStartTime: startTime,
        appointmentEndTime: endTime,
        clientName: this.client.firstName + ' ' + (this.client.lastName || ''),
        appointmentDate: this.formatDate(this.bookingForm.value.appointmentDate)
      };

      this.publicBookingService.bookAppointment(bookingData).subscribe({
        next: (response) => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Appointment booked successfully!' });
          this.bookingConfirmed = true;
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Failed to book appointment.' });
        }
      });
    }
  }

  private calculateEndTime(startTime: string, durationMinutes: number): string {
    const [hours, minutes] = startTime.split(':').map(Number);
    const date = new Date();
    date.setHours(hours, minutes + durationMinutes, 0, 0);
    return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
  }

  formatDate(date: Date): string {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }

  private parseTimeInput(value: string | null | undefined): string | null {
    if (!value) {
      return null;
    }

    const trimmed = String(value).trim().toUpperCase();
    const explicitMatch = trimmed.match(/^(\d{1,2}):(\d{2})\s*(AM|PM)$/);
    if (explicitMatch) {
      let hours = Number(explicitMatch[1]);
      const minutes = explicitMatch[2];
      const suffix = explicitMatch[3];
      if (suffix === 'PM' && hours < 12) {
        hours += 12;
      }
      if (suffix === 'AM' && hours === 12) {
        hours = 0;
      }
      return `${String(hours).padStart(2, '0')}:${minutes}`;
    }

    const simpleMatch = trimmed.match(/^(\d{1,2}):(\d{2})$/);
    if (simpleMatch) {
      const hours = Number(simpleMatch[1]);
      const minutes = simpleMatch[2];
      return `${String(hours).padStart(2, '0')}:${minutes}`;
    }

    return null;
  }

  private timeFormatValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) {
      return null;
    }

    return this.parseTimeInput(value) ? null : { invalidTime: true };
  }

  private validateSalonHours(group: AbstractControl): ValidationErrors | null {
    const startValue = group.get('appointmentStartTime')?.value;
    if (!startValue) {
      return null;
    }

    const startTime = this.parseTimeInput(startValue);
    if (!startTime) {
      return null;
    }

    const startMinutes = this.toMinutes(startTime);
    const openingMinutes = this.toMinutes(this.salonOpeningTime);
    const closingMinutes = this.toMinutes(this.salonClosingTime);
    return startMinutes !== null && startMinutes >= openingMinutes! && startMinutes <= closingMinutes! ? null : { salonHours: true };
  }

  private getServiceDuration(serviceId: any): number {
    const s = this.services.find(x => x.id === serviceId);
    return s?.duration || 30;
  }

  generateTimeSlots(): void {
    const slots = [];
    const start = 10 * 60; // 10:00 AM in minutes
    const end = 18 * 60; // 06:00 PM in minutes
    for (let min = start; min < end; min += 30) {
      const h = Math.floor(min / 60);
      const m = min % 60;
      const hour12 = h > 12 ? h - 12 : (h === 0 ? 12 : h);
      const suffix = h >= 12 ? 'PM' : 'AM';
      const timeStr = `${String(hour12).padStart(2, '0')}:${String(m).padStart(2, '0')} ${suffix}`;
      slots.push({
        value: timeStr,
        label: timeStr,
        disabled: false
      });
    }
    this.timeSlots = slots;
  }

  updateTimeSlotsAvailability(): void {
    const clientId = this.bookingForm?.get('clientId')?.value;
    const employeeId = this.bookingForm?.get('employeeId')?.value;
    const serviceId = this.bookingForm?.get('serviceId')?.value;
    const dateVal = this.bookingForm?.get('appointmentDate')?.value;

    const duration = this.getServiceDuration(serviceId);
    const closingMinutes = this.toMinutes(this.salonClosingTime) || 1080;

    let dateStr = '';
    if (dateVal) {
      const d = new Date(dateVal);
      dateStr = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
    }

    const selectedEmployee = employeeId ? this.stylists.find(e => e.id === employeeId) : null;

    this.timeSlots.forEach(slot => {
      const startMin = this.toMinutes(this.parseTimeInput(slot.value)) || 0;
      const endMin = startMin + duration;

      // 1. Check if slot exceeds closing time
      if (endMin > closingMinutes) {
        slot.disabled = true;
        return;
      }

      // 2. Check client overlaps if date and client are selected
      if (clientId && dateStr) {
        const clientOverlap = this.appointments.some(apt => {
          if (apt.appointmentStatus === 'CANCELLED') {
            return false;
          }
          if (apt.clientId !== clientId || apt.appointmentDate !== dateStr) {
            return false;
          }
          const bookedStart = this.toMinutes(this.parseTimeInput(apt.appointmentStartTime)) || 0;
          const bookedEnd = this.toMinutes(this.parseTimeInput(apt.appointmentEndTime)) || 0;
          return startMin < bookedEnd && endMin > bookedStart;
        });

        if (clientOverlap) {
          slot.disabled = true;
          return;
        }
      }

      // 3. Check employee overlaps / leaves / off-days if date and employee are selected
      if (selectedEmployee && dateStr) {
        if (this.isEmployeeOnLeave(selectedEmployee) || this.isEmployeeUnavailable(selectedEmployee)) {
          slot.disabled = true;
          return;
        }

        const employeeOverlap = this.appointments.some(apt => {
          if (apt.appointmentStatus === 'CANCELLED') {
            return false;
          }
          if (apt.employeeId !== employeeId || apt.appointmentDate !== dateStr) {
            return false;
          }
          const bookedStart = this.toMinutes(this.parseTimeInput(apt.appointmentStartTime)) || 0;
          const bookedEnd = this.toMinutes(this.parseTimeInput(apt.appointmentEndTime)) || 0;
          return startMin < bookedEnd && endMin > bookedStart;
        });

        if (employeeOverlap) {
          slot.disabled = true;
          return;
        }
      }

      slot.disabled = false;
    });
  }

  isEmployeeUnavailable(employee: any): boolean {
    const selectedDate = this.bookingForm.get('appointmentDate')?.value;
    if (!selectedDate || !employee.weeklyOffDays) {
      return false;
    }

    const dateObj = new Date(selectedDate);
    const currentDayName = dateObj.toLocaleString('default', {
      weekday: 'long'
    });

    return employee.weeklyOffDays.includes(currentDayName);
  }

  isEmployeeBooked(employee: any): boolean {
    const selectedDate = this.bookingForm.get('appointmentDate')?.value;
    const selectedStart = this.toMinutes(this.parseTimeInput(this.bookingForm.get('appointmentStartTime')?.value));
    const selectedService = this.services.find(s => s.id === this.bookingForm.get('serviceId')?.value);
    const duration = selectedService?.duration || 30;

    if (!selectedDate || selectedStart == null) {
      return false;
    }

    const selectedEnd = selectedStart + duration;

    const d = new Date(selectedDate);
    const date = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;

    for (const appointment of this.appointments) {
      if (appointment.appointmentStatus === 'CANCELLED') {
        continue;
      }

      if (appointment.employeeId !== employee.id || appointment.appointmentDate !== date) {
        continue;
      }

      const bookedStart = this.toMinutes(this.parseTimeInput(appointment.appointmentStartTime)) || 0;
      const bookedEnd = this.toMinutes(this.parseTimeInput(appointment.appointmentEndTime)) || 0;

      if (selectedStart < bookedEnd && selectedEnd > bookedStart) {
        return true;
      }
    }

    return false;
  }

  isEmployeeOnLeave(employee: any): boolean {
    const selectedDate = this.bookingForm.get('appointmentDate')?.value;
    if (!selectedDate || !employee) {
      return false;
    }
    const selectedDateObject = new Date(selectedDate);
    const selectedDateFormatted = selectedDateObject.toISOString().split('T')[0];
    const employeeName = employee.employeeName;

    for (const leave of this.employeeLeaveData) {
      if (leave.employeeName !== employeeName) {
        continue;
      }

      if (selectedDateFormatted >= leave.startDate && selectedDateFormatted <= leave.endDate) {
        return true;
      }
    }
    return false;
  }

  private stylistAvailabilityValidator(control: AbstractControl): ValidationErrors | null {
    if (!this.bookingForm) {
      return null;
    }
    const employeeId = control.value;
    if (!employeeId) {
      return null;
    }

    const selectedEmployee = this.stylists.find(emp => emp.id === employeeId);
    if (!selectedEmployee) {
      return null;
    }

    if (this.isEmployeeOnLeave(selectedEmployee)) {
      return { stylistOnLeave: true };
    }

    if (this.isEmployeeUnavailable(selectedEmployee)) {
      return { stylistUnavailable: true };
    }

    if (this.isEmployeeBooked(selectedEmployee)) {
      return { stylistBooked: true };
    }

    return null;
  }

  private toMinutes(value: string | null | undefined): number | null {
    if (!value) {
      return null;
    }
    const [hours, minutes] = value.split(':').map(part => Number(part));
    if (Number.isNaN(hours) || Number.isNaN(minutes)) {
      return null;
    }
    return hours * 60 + minutes;
  }
}
