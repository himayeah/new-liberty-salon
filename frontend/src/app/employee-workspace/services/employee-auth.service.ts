import { Injectable } from '@angular/core';
import { Role } from 'src/app/models/role.enum';

/**
 * Manages the employee authentication session.
 * Uses sessionStorage so that closing the browser tab logs the employee out.
 */
@Injectable({
  providedIn: 'root'
})
export class EmployeeAuthService {

  private readonly STORAGE_KEY = 'employee_session';

  /**
   * Stores the authenticated employee data in sessionStorage.
   */
  login(employeeData: any): void {
    let role = Role.STYLIST;
    if (employeeData && employeeData.designation) {
      const designation = employeeData.designation.trim().toUpperCase();
      if (designation === 'RECEPTIONIST') {
        role = Role.RECEPTIONIST;
      } else if (designation === 'MANAGER') {
        role = Role.MANAGER;
      }
    }
    const session = {
      employee: employeeData,
      role: role,
      loginTime: new Date().toISOString()
    };
    sessionStorage.setItem(this.STORAGE_KEY, JSON.stringify(session));
  }

  /**
   * Clears the employee session from sessionStorage.
   */
  logout(): void {
    sessionStorage.removeItem(this.STORAGE_KEY);
  }

  /**
   * Checks whether an employee is currently authenticated.
   */
  isAuthenticated(): boolean {
    return sessionStorage.getItem(this.STORAGE_KEY) !== null;
  }

  /**
   * Returns the authenticated employee's data, or null if not logged in.
   */
  getEmployeeData(): any | null {
    const session = sessionStorage.getItem(this.STORAGE_KEY);
    if (session) {
      return JSON.parse(session).employee;
    }
    return null;
  }

  /**
   * Returns the current user's role, or null if not logged in.
   */
  getRole(): Role | null {
    const session = sessionStorage.getItem(this.STORAGE_KEY);
    if (session) {
      return JSON.parse(session).role;
    }
    if (window.localStorage.getItem('auth_token')) {
      return Role.OWNER;
    }
    return null;
  }
}
