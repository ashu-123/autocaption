import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaptionsGeneratorComponent } from './captions-generator.component';

describe('CaptionsGeneratorComponent', () => {
  let component: CaptionsGeneratorComponent;
  let fixture: ComponentFixture<CaptionsGeneratorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CaptionsGeneratorComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CaptionsGeneratorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
