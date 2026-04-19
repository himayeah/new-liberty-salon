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

  // If an existing client, calls the searchClient method of the publicBookingService to search for the client.
  // If the client is not found, calls the registerClient method of the publicBookingService to register the client.

  onSubmit() {
    //checks the vaidity of the loginForm, firstName and email entered correct.
    // Extracts the loginForm values and store them in variabes firstName and email.
    // Calls the searchClient method of the publicBookingService to search for the client.
    // If the client is found, it emits the userAuthenticated event with the client data.
    // If the client is not found, it sets the isRegistering flag to true to show the registration form.
    if (this.loginForm.valid) {
      const { firstName, email } = this.loginForm.value;

      //Since API calls are asynchronous, subscribe() waits for the response from backend
      this.publicBookingService.searchClient(firstName, email).subscribe({
        next: (client) => {
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
