// All the components of the module are imported here, on it's separate ts file
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { StylistWorkspaceRoutingModule } from './routes/stylist-workspace-routing.module';
import { StylistWorkspaceComponent } from './routes/stylist-workspace.component';
import { StylistLoginComponent } from './components/stylist-login/stylist-login.component';
import { StylistDashboardComponent } from './components/stylist-dashboard/stylist-dashboard.component';

@NgModule({
  declarations: [
    StylistWorkspaceComponent,
    StylistLoginComponent,
    StylistDashboardComponent
  ],
  imports: [
    CommonModule,
    StylistWorkspaceRoutingModule
  ]
})
export class StylistWorkspaceModule { }


