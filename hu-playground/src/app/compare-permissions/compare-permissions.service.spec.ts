import { TestBed } from '@angular/core/testing';

import { ComparePermissionsService } from './compare-permissions.service';

describe('ComparePermissionsService', () => {
  let service: ComparePermissionsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ComparePermissionsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
