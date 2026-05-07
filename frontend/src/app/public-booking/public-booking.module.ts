import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { PublicBookingRoutingModule } from './routes/public-booking-routing.module';
import { PublicBookingComponent } from './routes/public-booking.component';
import { LoginFormComponent } from './components/login-form/login-form.component';
import { BookingFormComponent } from './components/booking-form/booking-form.component';

// PrimeNG Imports
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';

// Material Imports
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    PublicBookingComponent,
    LoginFormComponent,
    BookingFormComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    PublicBookingRoutingModule,
    InputTextModule,
    ButtonModule,
    DropdownModule,
    CalendarModule,
    ToastModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule
  ],
  providers: [MessageService]
})
export class PublicBookingModule { }
