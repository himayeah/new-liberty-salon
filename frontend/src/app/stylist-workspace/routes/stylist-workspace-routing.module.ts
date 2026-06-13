import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StylistWorkspaceComponent } from './stylist-workspace.component';

const routes: Routes = [
  { path: '', component: StylistWorkspaceComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class StylistWorkspaceRoutingModule { }
