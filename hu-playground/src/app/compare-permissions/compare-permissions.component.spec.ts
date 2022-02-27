import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ComparePermissionsComponent } from './compare-permissions.component';

describe('ComparePermissionsComponent', () => {
  let component: ComparePermissionsComponent;
  let fixture: ComponentFixture<ComparePermissionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ComparePermissionsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ComparePermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
