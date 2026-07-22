import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PublicBookingService } from '../../services/public-booking.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent {
  loginForm: FormGroup;
  isRegistering: boolean = false;
  @Output() userAuthenticated = new EventEmitter<any>();

  // ngOnInit is not needed here because this component doesn't need to load any data when it initializes —
  // it's a form-driven component that only reacts to user actions.

  constructor(
    private fb: FormBuilder,
    private publicBookingService: PublicBookingService,
    private messageService: MessageService
  ) {
    this.loginForm = this.fb.group({
      firstName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const { firstName, email } = this.loginForm.value;

      this.publicBookingService.searchClient(firstName, email).subscribe({
        next: (client: any) => {
          this.messageService.add({ severity: 'success', summary: 'Welcome Back', detail: `Hello ${client.firstName}!` });
          this.userAuthenticated.emit(client);
        },
        error: (err) => {
          if (err.status === 404) {
            this.isRegistering = true;
          } else {
            this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Could not authenticate. Please try again.' });
          }
        }
      });
    }
  }

  onRegister() {
    if (this.loginForm.valid) {
      this.publicBookingService.registerClient(this.loginForm.value).subscribe({
        next: (newClient) => {
          this.messageService.add({ severity: 'success', summary: 'Registered', detail: 'Your profile has been created.' });
          this.userAuthenticated.emit(newClient);
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Registration failed.' });
        }
      });
    }
  }
}
