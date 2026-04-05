import { TestBed } from '@angular/core/testing';

import { ReportProductSalesService } from './report-product-sales.service';

describe('ReportProductSalesService', () => {
  let service: ReportProductSalesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportProductSalesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
