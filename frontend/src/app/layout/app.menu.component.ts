import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { Subscription } from 'rxjs';
import { CacheService } from '../services/CacheService';
import { NavigationModel } from './app.menu';
import { EmployeeAuthService } from '../employee-workspace/services/employee-auth.service';
import { Role } from '../models/role.enum';

@Component({
    selector: 'app-menu',
    templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {
    model: any[] = [];
    private cacheSubscription!: Subscription;
    data!: number[];
    windowWidth: number;

    constructor(
        public layoutService: LayoutService,
        private cacheService: CacheService,
        public navigationMenu: NavigationModel,
        private employeeAuthService: EmployeeAuthService
    ) {
        this.cacheSubscription = this.cacheService.cache$.subscribe((data) => {
            this.data = data;

            this.setAuthStatusInNavItems(this.data);
        });
    }

    ngOnInit() {}

    public setEntitlements(
        navigationArray: any[],
        isAdmin: boolean,
        privilegeArray?: number[]
    ): void {
        navigationArray.forEach((element) => {
            if (isAdmin) {
                element.isVisible = true;
            } else if (privilegeArray.includes(element.auth)) {
                element.isVisible = true;
            } else {
                element.isVisible = false;
            }

            if (element.items && element.items.length > 0) {
                this.setEntitlements(element.items, isAdmin, privilegeArray);
            }

            if (!element.items || element.items.length === 0) {
                if (isAdmin) {
                    element.isVisible = true;
                    return;
                } else {
                    if (privilegeArray.includes(element.auth)) {
                        element.isVisible = true;
                    } else {
                        element.isVisible = false;
                    }
                }

                if (privilegeArray && privilegeArray.length === 0) {
                    element.isVisible = false;
                }
                return;
            }
        });
    }

    private setReceptionistEntitlements(menuArray: any[]): void {
        const allowedLinks = [
            '/pages/client-reg',
            '/pages/appointment-schedule',
            '/pages/employee-attendance',
            '/pages/employee-schedule',
            '/pages/employee-leave',
            '/pages/billing',
            '/pages/employee-reg',
            '/pages/service',
            '/pages/service-category',
            '/pages/product',
            '/pages/product-category'
        ];

        menuArray.forEach(group => {
            let hasVisibleItems = false;
            if (group.items && group.items.length > 0) {
                group.items.forEach((item: any) => {
                    const link = item.routerLink && item.routerLink[0];
                    if (link && allowedLinks.includes(link)) {
                        item.isVisible = true;
                        hasVisibleItems = true;
                    } else {
                        item.isVisible = false;
                    }
                });
                group.isVisible = hasVisibleItems;
            } else {
                group.isVisible = false;
            }
        });
    }

    private setManagerEntitlements(menuArray: any[]): void {
        const allowedLinks = [
            '/dashboard',
            '/pages/client-reg',
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
            '/pages/employee-reg'
        ];

        menuArray.forEach(group => {
            let hasVisibleItems = false;
            if (group.items && group.items.length > 0) {
                group.items.forEach((item: any) => {
                    const link = item.routerLink && item.routerLink[0];
                    if (link && allowedLinks.includes(link)) {
                        item.isVisible = true;
                        hasVisibleItems = true;
                    } else {
                        item.isVisible = false;
                    }
                });
                group.isVisible = hasVisibleItems;
            } else {
                group.isVisible = false;
            }
        });
    }

    public setAuthStatusInNavItems(authId: number[]) {
        if (this.employeeAuthService.isAuthenticated()) {
            const role = this.employeeAuthService.getRole();
            if (role === Role.RECEPTIONIST) {
                this.model = this.navigationMenu.get();
                this.setReceptionistEntitlements(this.model);
                return;
            } else if (role === Role.MANAGER) {
                this.model = this.navigationMenu.get();
                this.setManagerEntitlements(this.model);
                return;
            }
        }

        this.model = this.navigationMenu.get();
        let isAdmin = false;
        if (authId && authId.length > 0) {
            if (authId.includes(1)) {
                isAdmin = true;
                this.setEntitlements(this.model, isAdmin);
                return;
            }

            this.setEntitlements(this.model, isAdmin, authId);
        } else if (
            JSON.parse(window.localStorage.getItem('privileges')!)?.length! > 0
        ) {
            const privilegeArray = JSON.parse(
                window.localStorage.getItem('privileges')!
            );
            if (privilegeArray.includes(1)) {
                isAdmin = true;
                this.setEntitlements(this.model, isAdmin, privilegeArray);
                return;
            }
            isAdmin = false;
            this.setEntitlements(this.model, isAdmin, privilegeArray);
        } else if (authId && authId.length === 0) {
            isAdmin = false;
            this.setEntitlements(this.model, isAdmin, authId);
        }

        console.log(this.model);
    }
}
