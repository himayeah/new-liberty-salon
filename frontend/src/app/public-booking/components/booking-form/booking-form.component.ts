import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
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

  constructor(
    private fb: FormBuilder,
    private publicBookingService: PublicBookingService,
    private messageService: MessageService
  ) {
    this.bookingForm = this.fb.group({
      clientId: ['', Validators.required],
      employeeId: ['', Validators.required],
      serviceId: ['', Validators.required],
      appointmentDate: ['', Validators.required],
      appointmentStartTime: ['', Validators.required],
      appointmentStatus: ['PENDING'],
      bookingSource: ['PUBLIC_WEB']
    });
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
      const bookingData = {
        ...this.bookingForm.value,
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

  formatDate(date: Date): string {
    const d = new Date(date);
    let month = '' + (d.getMonth() + 1);
    let day = '' + d.getDate();
    const year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }
}
