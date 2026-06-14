import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { StylistAuthService } from '../stylist-workspace/services/stylist-auth.service';

@Injectable({
  providedIn: 'root'
})
export class StylistAuthGuard implements CanActivate {

  constructor(
    private stylistAuthService: StylistAuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    
    if (this.stylistAuthService.isAuthenticated()) {
      return true;
    }

    // Not logged in, redirect to stylist login
    this.router.navigate(['/stylist-workspace'], { queryParams: { returnUrl: state.url } });
    return false;
  }
  
}
