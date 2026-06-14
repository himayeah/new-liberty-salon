import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeWorkspaceService } from '../../services/employee-workspace.service';
import { MessageServiceService } from 'src/app/services/message-service/message-service.service';

@Component({
  selector: 'app-employee-reset-password',
  templateUrl: './employee-reset-password.component.html',
  styleUrls: ['./employee-reset-password.component.scss'],
})
export class EmployeeResetPasswordComponent implements OnInit {
  resetForm: FormGroup;
  token: string = '';
  isSubmitting = false;
  hidePassword = true;
  hideConfirmPassword = true;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private employeeWorkspaceService: EmployeeWorkspaceService,
    private messageService: MessageServiceService
  ) {
    this.resetForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6), this.passwordStrengthValidator]],
      confirmPassword: ['', [Validators.required]]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || '';
      if (!this.token) {
        this.messageService.showError('Invalid reset link. Please request a new password reset.');
      }
    });
  }

  /**
   * Custom validator: password must contain at least one number.
   */
  passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;
    const hasNumber = /\d/.test(value);
    return hasNumber ? null : { noNumber: true };
  }

  /**
   * Cross-field validator: password and confirmPassword must match.
   */
  passwordMatchValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    if (password && confirmPassword && password !== confirmPassword) {
      return { passwordMismatch: true };
    }
    return null;
  }

  /**
   * Template helper: checks if the current password value contains at least one digit.
   */
  get passwordHasNumber(): boolean {
    const val = this.resetForm.get('password')?.value;
    return val ? /\d/.test(val) : false;
  }

  onSubmit(): void {
    if (this.resetForm.valid && this.token) {
      this.isSubmitting = true;
      const { email, password } = this.resetForm.value;
      this.employeeWorkspaceService.resetPassword(this.token, email, password).subscribe({
        next: (response: any) => {
          this.isSubmitting = false;
          this.messageService.showSuccess(response.message || 'Password reset successfully! You can now log in.');
          this.router.navigate(['/employee-workspace']);
        },
        error: (error) => {
          this.isSubmitting = false;
          let errorMsg = 'Could not reset password. Please try again.';
          if (error.error && error.error.message) {
            errorMsg = error.error.message;
          }
          this.messageService.showError(errorMsg);
        }
      });
    }
  }
}
