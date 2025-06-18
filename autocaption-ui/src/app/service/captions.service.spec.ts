import { TestBed } from '@angular/core/testing';

import { CaptionsService } from './captions.service';

describe('CaptionsService', () => {
  let service: CaptionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CaptionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
