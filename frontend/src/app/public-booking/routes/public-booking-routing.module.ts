import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { PublicBookingComponent } from './public-booking.component';

const routes: Routes = [
  { path: '', component: PublicBookingComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PublicBookingRoutingModule { }
