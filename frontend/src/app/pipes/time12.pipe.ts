import { Pipe, PipeTransform } from '@angular/core';

/**
 * Converts a 24-hour time string (e.g. "13:45" or "09:30") to 12-hour AM/PM format
 * (e.g. "1:45 PM" or "9:30 AM").
 *
 * Usage: {{ '13:45' | time12 }}  →  "1:45 PM"
 */
@Pipe({
  name: 'time12'
})
export class Time12Pipe implements PipeTransform {

  transform(value: string | null | undefined): string {
    if (!value) {
      return '';
    }

    // Handle values that might already contain AM/PM
    if (/AM|PM/i.test(value)) {
      return value;
    }

    const parts = value.split(':');
    if (parts.length < 2) {
      return value;
    }

    let hours = parseInt(parts[0], 10);
    const minutes = parts[1].padStart(2, '0');

    if (isNaN(hours)) {
      return value;
    }

    const period = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    if (hours === 0) {
      hours = 12;
    }

    return `${String(hours).padStart(2, '0')}:${minutes} ${period}`;
  }
}
