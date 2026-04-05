import { TestBed } from '@angular/core/testing';

import { ReportProcurementService } from './report-procurement.service';

describe('ReportProcurementService', () => {
  let service: ReportProcurementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportProcurementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
