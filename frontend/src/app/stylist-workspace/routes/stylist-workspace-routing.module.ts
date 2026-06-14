import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StylistWorkspaceComponent } from './stylist-workspace.component';
import { StylistLoginComponent } from '../components/stylist-login/stylist-login.component';
import { StylistDashboardComponent } from '../components/stylist-dashboard/stylist-dashboard.component';
import { StylistInviteComponent } from '../components/stylist-invite/stylist-invite.component';
import { StylistResetPasswordComponent } from '../components/stylist-reset-password/stylist-reset-password.component';
import { StylistAuthGuard } from 'src/app/guards/stylist-auth.guard';

const routes: Routes = [
  { 
    path: '', 
    component: StylistWorkspaceComponent,
    children: [
      { path: '', component: StylistLoginComponent },
      { path: 'invite', component: StylistInviteComponent },
      { path: 'reset-password', component: StylistResetPasswordComponent },
      { 
        path: 'dashboard', 
        component: StylistDashboardComponent,
        canActivate: [StylistAuthGuard]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StylistWorkspaceRoutingModule { }
