// All the components of the module are imported here, on it's separate ts file
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { StylistWorkspaceRoutingModule } from './routes/stylist-workspace-routing.module';
import { StylistWorkspaceComponent } from './routes/stylist-workspace.component';
import { StylistLoginComponent } from './components/stylist-login/stylist-login.component';
import { StylistDashboardComponent } from './components/stylist-dashboard/stylist-dashboard.component';
import { StylistInviteComponent } from './components/stylist-invite/stylist-invite.component';
import { StylistResetPasswordComponent } from './components/stylist-reset-password/stylist-reset-password.component';
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
    StylistWorkspaceComponent,
    StylistLoginComponent,
    StylistDashboardComponent,
    StylistInviteComponent,
    StylistResetPasswordComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    StylistWorkspaceRoutingModule,
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
export class StylistWorkspaceModule { }
