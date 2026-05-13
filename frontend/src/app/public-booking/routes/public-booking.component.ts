import { Component } from '@angular/core';

@Component({
  selector: 'app-public-booking',
  templateUrl: './public-booking.component.html',
  styleUrls: ['./public-booking.component.scss']
})
export class PublicBookingComponent {
  authenticatedUser: any = null;

  onUserAuthenticated(user: any) {
    this.authenticatedUser = user;
  }
}
