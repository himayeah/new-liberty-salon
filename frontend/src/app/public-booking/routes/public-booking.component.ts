import { Component } from '@angular/core';

@Component({
  selector: 'app-public-booking',
  template: `
    <app-login-form *ngIf="!authenticatedUser" (userAuthenticated)="onUserAuthenticated($event)"></app-login-form>
    <app-booking-form *ngIf="authenticatedUser" [client]="authenticatedUser"></app-booking-form>
    <p-toast></p-toast>
  `
})
export class PublicBookingComponent {
  authenticatedUser: any = null;

  onUserAuthenticated(user: any) {
    this.authenticatedUser = user;
  }
}
