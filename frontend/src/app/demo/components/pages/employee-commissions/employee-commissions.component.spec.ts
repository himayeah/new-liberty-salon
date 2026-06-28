import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeCommissionsComponent } from './employee-commissions.component';

describe('EmployeeCommissionsComponent', () => {
  let component: EmployeeCommissionsComponent;
  let fixture: ComponentFixture<EmployeeCommissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeCommissionsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EmployeeCommissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
