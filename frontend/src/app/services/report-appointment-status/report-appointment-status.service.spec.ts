import { TestBed } from '@angular/core/testing';

import { ReportAppointmentStatusService } from './report-appointment-status.service';

describe('ReportAppointmentStatusService', () => {
  let service: ReportAppointmentStatusService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportAppointmentStatusService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
