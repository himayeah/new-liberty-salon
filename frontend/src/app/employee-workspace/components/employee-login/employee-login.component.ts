import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { StylistWorkspaceService } from '../../services/stylist-workspace.service';
import { StylistAuthService } from '../../services/stylist-auth.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-stylist-login',
  templateUrl: './stylist-login.component.html',
  styleUrls: ['./stylist-login.component.scss'],
})
export class StylistLoginComponent {
  stylistLoginForm: FormGroup;
  forgotPasswordForm: FormGroup;
  isButtonDisabled = false;
  isForgotPasswordMode = false;
  isSendingReset = false;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private stylistWorkspaceService: StylistWorkspaceService,
    private stylistAuthService: StylistAuthService,
    private messageService: MessageServiceService,
    private router: Router
  ) {
    this.stylistLoginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });

    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.stylistLoginForm.valid) {
      this.isButtonDisabled = true;
      const { email, password } = this.stylistLoginForm.value;
      this.stylistWorkspaceService.login(email, password).subscribe({
        next: (stylist: any) => {
          this.isButtonDisabled = false;
          this.messageService.showSuccess('Hello, welcome back!');

          // Store session
          this.stylistAuthService.login(stylist);

          // Navigate to dashboard
          this.router.navigate(['/stylist-workspace/dashboard']);
        },
        error: (error) => {
          this.isButtonDisabled = false;
          let errorMsg = 'Could not authenticate. Please try again.';
          if (error.error && error.error.message) {
            errorMsg = error.error.message;
          }
          this.messageService.showError(errorMsg);
        }
      });
    }
  }

  toggleForgotPassword(): void {
    this.isForgotPasswordMode = !this.isForgotPasswordMode;
  }

  onForgotPasswordSubmit(): void {
    if (this.forgotPasswordForm.valid) {
      this.isSendingReset = true;
      const { email } = this.forgotPasswordForm.value;
      this.stylistWorkspaceService.forgotPassword(email).subscribe({
        next: (response: any) => {
          this.isSendingReset = false;
          this.messageService.showSuccess(response.message || 'A password reset link has been sent to your email.');
          this.isForgotPasswordMode = false;
          this.forgotPasswordForm.reset();
        },
        error: (error) => {
          this.isSendingReset = false;
          let errorMsg = 'Could not send reset link. Please try again.';
          if (error.error && error.error.message) {
            errorMsg = error.error.message;
          }
          this.messageService.showError(errorMsg);
        }
      });
    }
  }
}
