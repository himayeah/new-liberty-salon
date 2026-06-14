import { Injectable } from '@angular/core';
import { Role } from 'src/app/models/role.enum';

/**
 * Manages the stylist authentication session.
 * Uses sessionStorage so that closing the browser tab logs the stylist out.
 */
@Injectable({
  providedIn: 'root'
})
export class StylistAuthService {

  private readonly STORAGE_KEY = 'stylist_session';

  /**
   * Stores the authenticated stylist data in sessionStorage.
   */
  login(stylistData: any): void {
    const session = {
      stylist: stylistData,
      role: Role.STYLIST,
      loginTime: new Date().toISOString()
    };
    sessionStorage.setItem(this.STORAGE_KEY, JSON.stringify(session));
  }

  /**
   * Clears the stylist session from sessionStorage.
   */
  logout(): void {
    sessionStorage.removeItem(this.STORAGE_KEY);
  }

  /**
   * Checks whether a stylist is currently authenticated.
   */
  isAuthenticated(): boolean {
    return sessionStorage.getItem(this.STORAGE_KEY) !== null;
  }

  /**
   * Returns the authenticated stylist's employee data, or null if not logged in.
   */
  getStylistData(): any | null {
    const session = sessionStorage.getItem(this.STORAGE_KEY);
    if (session) {
      return JSON.parse(session).stylist;
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
    return null;
  }
}
