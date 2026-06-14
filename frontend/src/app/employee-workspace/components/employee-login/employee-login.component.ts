import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { EmployeeWorkspaceService } from '../../services/employee-workspace.service';
import { EmployeeAuthService } from '../../services/employee-auth.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-employee-login',
  templateUrl: './employee-login.component.html',
  styleUrls: ['./employee-login.component.scss'],
})
export class EmployeeLoginComponent {
  loginForm: FormGroup;
  forgotPasswordForm: FormGroup;
  isButtonDisabled = false;
  isForgotPasswordMode = false;
  isSendingReset = false;
  hidePassword = true;

  constructor(
    private fb: FormBuilder,
    private employeeWorkspaceService: EmployeeWorkspaceService,
    private employeeAuthService: EmployeeAuthService,
    private messageService: MessageServiceService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]]
    });

    this.forgotPasswordForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      this.isButtonDisabled = true;
      const { email, password } = this.loginForm.value;
      this.employeeWorkspaceService.login(email, password).subscribe({
        next: (employee: any) => {
          this.isButtonDisabled = false;
          this.messageService.showSuccess('Hello, welcome back!');

          // Store session
          this.employeeAuthService.login(employee);

          // Navigate based on designation/role
          if (employee && employee.designation) {
            const designation = employee.designation.trim().toUpperCase();
            if (designation === 'RECEPTIONIST') {
              this.router.navigate(['/pages/appointment-schedule']);
            } else if (designation === 'MANAGER') {
              this.router.navigate(['/dashboard']);
            } else {
              this.router.navigate(['/employee-workspace/dashboard']);
            }
          } else {
            this.router.navigate(['/employee-workspace/dashboard']);
          }
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
      this.employeeWorkspaceService.forgotPassword(email).subscribe({
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
