import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportAppointmentStatusComponent } from './report-appointment-status.component';

describe('ReportAppointmentStatusComponent', () => {
  let component: ReportAppointmentStatusComponent;
  let fixture: ComponentFixture<ReportAppointmentStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportAppointmentStatusComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReportAppointmentStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
