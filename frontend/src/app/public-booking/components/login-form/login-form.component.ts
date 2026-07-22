import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PublicBookingService } from '../../services/public-booking.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.scss']
})
export class LoginFormComponent implements OnInit {
  // Steps: 'EMAIL' | 'LOGIN' | 'REGISTER' | 'SET_PASSWORD' | 'FORGOT_PASSWORD_SENT' | 'RESET_PASSWORD'
  currentStep: string = 'EMAIL';
  resetToken: string | null = null;

  emailForm: FormGroup;
  loginForm: FormGroup;
  registerForm: FormGroup;
  setPasswordForm: FormGroup;
  resetPasswordForm: FormGroup;

  @Output() userAuthenticated = new EventEmitter<any>();

  constructor(
    private fb: FormBuilder,
    private publicBookingService: PublicBookingService,
    private messageService: MessageService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    // 1. Email check form
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    // 2. Login credentials form
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });

    // 3. Brand-new user registration form
    this.registerForm = this.fb.group({
      firstName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.passwordStrengthValidator]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });

    // 4. Existing client setting password form
    this.setPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, this.passwordStrengthValidator]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });

    // 5. Password reset form
    this.resetPasswordForm = this.fb.group({
      password: ['', [Validators.required, this.passwordStrengthValidator]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    // Listen for query parameters to handle password reset redirects
    this.route.queryParams.subscribe(params => {
      if (params['token']) {
        this.resetToken = params['token'];
        this.currentStep = 'RESET_PASSWORD';
      }
    });
  }

  // Custom password strength validator:
  // Must be more than 8 characters, contain letters, numbers, and at least one special character
  passwordStrengthValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const value = control.value;
    if (!value) return null;

    const hasMinLength = value.length >= 9; // > 8 means at least 9
    const hasLetter = /[a-zA-Z]/.test(value);
    const hasNumber = /\d/.test(value);
    const hasSpecial = /[^a-zA-Z0-9]/.test(value);

    const valid = hasMinLength && hasLetter && hasNumber && hasSpecial;
    return valid ? null : { passwordStrength: true };
  }

  // Custom validator to ensure password and confirmPassword fields match
  passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  // Template helpers for password strength checks
  hasMinLength(value: string): boolean {
    return value ? value.length >= 9 : false;
  }

  hasLetter(value: string): boolean {
    return value ? /[a-zA-Z]/.test(value) : false;
  }

  hasNumber(value: string): boolean {
    return value ? /\d/.test(value) : false;
  }

  hasSpecial(value: string): boolean {
    return value ? /[^a-zA-Z0-9]/.test(value) : false;
  }

  // Navigates back to the email entry step and resets forms
  goBackToEmail() {
    this.currentStep = 'EMAIL';
    this.emailForm.reset();
    this.loginForm.reset();
    this.registerForm.reset();
    this.setPasswordForm.reset();
    this.resetPasswordForm.reset();
    // Clear URL parameters
    this.router.navigate([], { queryParams: {} });
  }

  // Checks email status in the backend and routes user to the correct form
  onCheckEmail() {
    if (this.emailForm.valid) {
      const email = this.emailForm.value.email;
      this.publicBookingService.checkEmailStatus(email).subscribe({
        next: (status: string) => {
          if (status === 'REGISTER') {
            this.registerForm.patchValue({ email });
            this.currentStep = 'REGISTER';
          } else if (status === 'SET_PASSWORD') {
            this.setPasswordForm.patchValue({ email });
            this.currentStep = 'SET_PASSWORD';
          } else if (status === 'LOGIN') {
            this.loginForm.patchValue({ email });
            this.currentStep = 'LOGIN';
          }
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Could not verify email status. Please try again.' });
        }
      });
    }
  }

  // Submits email + password for verification
  onLoginSubmit() {
    if (this.loginForm.valid) {
      this.publicBookingService.loginClient(this.loginForm.value).subscribe({
        next: (client: any) => {
          this.messageService.add({ severity: 'success', summary: 'Welcome Back', detail: `Hello ${client.firstName}!` });
          this.userAuthenticated.emit(client);
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: 'Invalid email or password. Please try again.' });
        }
      });
    }
  }

  // Submits details to create a new client profile
  onRegisterSubmit() {
    if (this.registerForm.valid) {
      this.publicBookingService.registerClient(this.registerForm.value).subscribe({
        next: (client: any) => {
          this.messageService.add({ severity: 'success', summary: 'Registered', detail: 'Your profile has been created successfully!' });
          this.userAuthenticated.emit(client);
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error?.message || 'Registration failed.' });
        }
      });
    }
  }

  // Sets a password for an existing receptionist-created client
  onSetPasswordSubmit() {
    if (this.setPasswordForm.valid) {
      this.publicBookingService.setClientPassword(this.setPasswordForm.value).subscribe({
        next: (client: any) => {
          this.messageService.add({ severity: 'success', summary: 'Password Set', detail: 'Password configured successfully. Welcome!' });
          this.userAuthenticated.emit(client);
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error?.message || 'Failed to set password.' });
        }
      });
    }
  }

  // Requests a password reset link
  onForgotPassword() {
    const email = this.loginForm.get('email')?.value;
    if (email) {
      this.publicBookingService.forgotPassword(email).subscribe({
        next: () => {
          this.currentStep = 'FORGOT_PASSWORD_SENT';
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error?.message || 'Failed to dispatch reset link.' });
        }
      });
    }
  }

  // Resets user password using the token received in link
  onResetPasswordSubmit() {
    if (this.resetPasswordForm.valid && this.resetToken) {
      const resetData = {
        resetToken: this.resetToken,
        password: this.resetPasswordForm.value.password
      };
      this.publicBookingService.resetPassword(resetData).subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Success', detail: 'Password updated successfully! Please sign in.' });
          this.goBackToEmail();
        },
        error: (err) => {
          this.messageService.add({ severity: 'error', summary: 'Error', detail: err.error?.message || 'Failed to reset password.' });
        }
      });
    }
  }
}
