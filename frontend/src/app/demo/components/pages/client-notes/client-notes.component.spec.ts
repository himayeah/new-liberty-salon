import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClientNotesComponent } from './client-notes.component';

describe('ClientNotesComponent', () => {
  let component: ClientNotesComponent;
  let fixture: ComponentFixture<ClientNotesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientNotesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ClientNotesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
