import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { PublicBookingService } from '../../services/public-booking.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-booking-form',
  templateUrl: './booking-form.component.html',
  styleUrls: ['./booking-form.component.scss']
})
export class BookingFormComponent implements OnInit {
  @Input() client: any;
  bookingForm: FormGroup;
  stylists: any[] = [];
  services: any[] = [];
  bookingConfirmed: boolean = false;
  minDate: Date = new Date();
  readonly salonOpeningTime = '10:00';
  readonly salonClosingTime = '18:00';

  constructor(
    private fb: FormBuilder,
    private publicBookingService: PublicBookingService,
    private messageService: MessageService
  ) {
    this.minDate.setHours(0, 0, 0, 0);
    this.bookingForm = this.fb.group({
      clientId: ['', Validators.required],
      employeeId: [null],
      serviceId: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      appointmentStartTime: ['', [Validators.required, this.timeFormatValidator.bind(this)]],
      appointmentStatus: ['PENDING'],
      bookingSource: ['ONLINE']
    }, { validators: this.validateSalonHours.bind(this) });
  }

  ngOnInit() {
    if (this.client) {
      this.bookingForm.patchValue({ clientId: this.client.id });
    }
    this.loadData();
  }

  loadData() {
    this.publicBookingService.getStylists().subscribe((data: any) => this.stylists = data);
    this.publicBookingService.getServices().subscribe((data: any) => this.services = data);
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
