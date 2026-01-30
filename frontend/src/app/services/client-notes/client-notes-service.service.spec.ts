import { TestBed } from '@angular/core/testing';

import { ClientNotesServiceService } from './client-notes-service.service';

describe('ClientNotesServiceService', () => {
  let service: ClientNotesServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientNotesServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
