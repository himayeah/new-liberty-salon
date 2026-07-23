import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportProductSalesComponent } from './report-product-sales.component';

describe('ReportProductSalesComponent', () => {
  let component: ReportProductSalesComponent;
  let fixture: ComponentFixture<ReportProductSalesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportProductSalesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportProductSalesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
