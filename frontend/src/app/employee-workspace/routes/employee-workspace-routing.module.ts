import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeeWorkspaceComponent } from './employee-workspace.component';
import { EmployeeLoginComponent } from '../components/employee-login/employee-login.component';
import { StylistDashboardComponent } from '../components/stylist-dashboard/stylist-dashboard.component';
import { EmployeeInviteComponent } from '../components/employee-invite/employee-invite.component';
import { EmployeeResetPasswordComponent } from '../components/employee-reset-password/employee-reset-password.component';
import { EmployeeAuthGuard } from 'src/app/guards/employee-auth.guard';

const routes: Routes = [
  { 
    path: '', 
    component: EmployeeWorkspaceComponent,
    children: [
      { path: '', component: EmployeeLoginComponent },
      { path: 'invite', component: EmployeeInviteComponent },
      { path: 'reset-password', component: EmployeeResetPasswordComponent },
      { 
        path: 'dashboard', 
        component: StylistDashboardComponent,
        canActivate: [EmployeeAuthGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class EmployeeWorkspaceRoutingModule { }
