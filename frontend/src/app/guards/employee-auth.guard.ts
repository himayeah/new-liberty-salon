import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from '@angular/router';
import { EmployeeAuthService } from '../employee-workspace/services/employee-auth.service';
import { Role } from '../models/role.enum';

@Injectable({
  providedIn: 'root'
})
export class EmployeeAuthGuard implements CanActivate {

  constructor(
    private employeeAuthService: EmployeeAuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): boolean {
    
    if (this.employeeAuthService.isAuthenticated()) {
      const role = this.employeeAuthService.getRole();
      if (role === Role.RECEPTIONIST) {
        this.router.navigate(['/pages/appointment-schedule']);
        return false;
      }
      if (role === Role.MANAGER) {
        this.router.navigate(['/dashboard']);
        return false;
      }
      return true;
    }

    // Not logged in, redirect to employee login
    this.router.navigate(['/employee-workspace'], { queryParams: { returnUrl: state.url } });
    return false;
  }
  
}
