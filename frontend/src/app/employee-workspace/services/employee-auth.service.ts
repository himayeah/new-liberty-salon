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

  /** What the login() function does
   * The login() method logs a user into the application by creating and saving their session.
        It:
        Checks the employee's designation.
        Assigns a role (Manager, Receptionist, or Stylist).
        Creates a session object with employee details and login time.
        Stores that session in sessionStorage.
   */
  login(employeeData: any): void { 
    let role = Role.STYLIST; //--> By default a user is set as Stylist
    if (employeeData && employeeData.designation) {
      const designation = employeeData.designation.trim().toUpperCase();
      if (designation === 'RECEPTIONIST') {
        role = Role.RECEPTIONIST;
      } else if (designation === 'MANAGER') {
        role = Role.MANAGER;
      } else if (designation === 'SENIOR STYLIST') {
        role = Role.SENIORSTYIST;
      } else if (designation === 'STYLIST') {
        role = Role.STYLIST;
      }
    }
    /**
     * The session stores:
        Who is logged in
        Their role (Manager, Receptionist, Stylist)
        Login time
        This lets the app remember the user across pages without making them log in again every time they navigate.
        sessionStorage lasts until the browser tab/window is closed.
     */
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
    if (sessionStorage.getItem(this.STORAGE_KEY) !== null) {
      return true;
    }
    const loginName = window.localStorage.getItem('user_name');
    if (loginName === 'libertysalonmanager@gmail.com') {
      return true;
    }
    return false;
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
    if (window.localStorage.getItem('auth_token')) {
      return Role.OWNER;
    }
    const session = sessionStorage.getItem(this.STORAGE_KEY);
    if (session) {
      return JSON.parse(session).role;
    }
    const loginName = window.localStorage.getItem('user_name');
    if (loginName === 'libertysalonmanager@gmail.com') {
      return Role.MANAGER;
    }
    return null;
  }
}
