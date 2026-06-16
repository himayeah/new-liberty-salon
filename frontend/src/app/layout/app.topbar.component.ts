import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { Router } from '@angular/router';
import { CacheService } from '../services/CacheService';
import { HttpService } from '../services/http.service';
import { EmployeeAuthService } from '../employee-workspace/services/employee-auth.service';
import { Role } from '../models/role.enum';

@Component({
    selector: 'app-topbar',
    templateUrl: './app.topbar.component.html',
})
export class AppTopBarComponent {
    items!: MenuItem[];

    @ViewChild('menubutton') menuButton!: ElementRef;

    @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;

    @ViewChild('topbarmenu') menu!: ElementRef;

    constructor(
        public layoutService: LayoutService,
        private httpService: HttpService,
        private router: Router,
        private cacheService: CacheService,
        private employeeAuthService: EmployeeAuthService
    ) {}

    public logOutUser(): void {
        if (this.employeeAuthService.isAuthenticated()) {
            this.employeeAuthService.logout();
            this.router.navigate(['/employee-workspace']);
        } else {
            this.cacheService.clear(this.httpService.getUserId()!);
            this.httpService.removeToken();
            this.router.navigate(['/auth/login']);
        }
    }

    get currentRoleViewName(): string | null {
        const role = this.employeeAuthService.getRole();
        if (!role) {
            return null;
        }
        const names: { [key: string]: string } = {
            OWNER: 'Owner View',
            MANAGER: 'Manager View',
            RECEPTIONIST: 'Receptionist View',
            STYLIST: 'Stylist View',
            CLIENT: 'Client View'
        };
        return names[role] || `${role} View`;
    }
}
