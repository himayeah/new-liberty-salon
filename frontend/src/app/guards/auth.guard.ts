import { Injectable } from '@angular/core';
import {
    ActivatedRouteSnapshot,
    CanActivate,
    Router,
    RouterStateSnapshot,
} from '@angular/router';
import { HttpService } from '../services/http.service';
import { EmployeeAuthService } from '../employee-workspace/services/employee-auth.service';
import { Role } from '../models/role.enum';

@Injectable({
    providedIn: 'root',
})
export class AuthGuard implements CanActivate {
    constructor(
        private httpService: HttpService,
        private employeeAuthService: EmployeeAuthService,
        private router: Router
    ) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const role = this.employeeAuthService.getRole();
        const url = state.url;

        if (role === Role.OWNER) {
            return true;
        }

        if (role === Role.MANAGER) {
            const allowedManagerPrefixes = [
                '/dashboard',
                '/pages/client-reg',
                '/pages/client-profile',
                '/pages/appointment-schedule',
                '/pages/employee-attendance',
                '/pages/employee-schedule',
                '/pages/employee-leave',
                '/pages/billing',
                '/pages/report-client-reg',
                '/pages/report-product-sales',
                '/pages/report-appointment-status',
                '/pages/report-procurement',
                '/pages/inventory',
                '/pages/product',
                '/pages/service',
                '/pages/product-category',
                '/pages/service-category',
                '/pages/employee-reg',
                '/pages/employee-profile',
                '/notfound'
            ];
            const isAllowed = allowedManagerPrefixes.some(prefix => url === prefix || url.startsWith(prefix + '/') || url.startsWith(prefix + '?'));
            if (isAllowed) {
                return true;
            }
            this.router.navigate(['/dashboard']);
            return false;
        }

        if (role === Role.RECEPTIONIST) {
            const allowedReceptionistPrefixes = [
                '/pages/client-reg',
                '/pages/client-profile',
                '/pages/appointment-schedule',
                '/pages/employee-attendance',
                '/pages/employee-schedule',
                '/pages/employee-leave',
                '/pages/billing',
                '/pages/employee-reg',
                '/pages/employee-profile',
                '/pages/service',
                '/pages/service-category',
                '/pages/product',
                '/pages/product-category',
                '/notfound'
            ];
            const isAllowed = allowedReceptionistPrefixes.some(prefix => url === prefix || url.startsWith(prefix + '/') || url.startsWith(prefix + '?'));
            if (isAllowed) {
                return true;
            }
            this.router.navigate(['/pages/appointment-schedule']);
            return false;
        }

        this.router.navigate(['/auth/login'], {
            queryParams: { returnUrl: state.url },
        });
        return false;
    }
}
