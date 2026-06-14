import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { EmployeeWorkspaceRoutingModule } from './routes/employee-workspace-routing.module';
import { EmployeeWorkspaceComponent } from './routes/employee-workspace.component';
import { EmployeeLoginComponent } from './components/employee-login/employee-login.component';
import { StylistDashboardComponent } from './components/stylist-dashboard/stylist-dashboard.component';
import { EmployeeInviteComponent } from './components/employee-invite/employee-invite.component';
import { EmployeeResetPasswordComponent } from './components/employee-reset-password/employee-reset-password.component';
import { SharedModule } from 'src/app/shared/shared.module';

// Material Imports
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatChipsModule } from '@angular/material/chips';
import { MatDividerModule } from '@angular/material/divider';

@NgModule({
  declarations: [
    EmployeeWorkspaceComponent,
    EmployeeLoginComponent,
    StylistDashboardComponent,
    EmployeeInviteComponent,
    EmployeeResetPasswordComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    EmployeeWorkspaceRoutingModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatChipsModule,
    MatDividerModule,
    SharedModule
  ]
})
export class EmployeeWorkspaceModule { }
