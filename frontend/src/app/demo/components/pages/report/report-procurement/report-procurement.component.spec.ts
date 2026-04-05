import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportProcurementComponent } from './report-procurement.component';

describe('ReportProcurementComponent', () => {
  let component: ReportProcurementComponent;
  let fixture: ComponentFixture<ReportProcurementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportProcurementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportProcurementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
