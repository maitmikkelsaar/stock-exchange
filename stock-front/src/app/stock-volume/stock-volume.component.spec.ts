import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StockVolumeComponent } from './stock-volume.component';

describe('StockVolumeComponent', () => {
  let component: StockVolumeComponent;
  let fixture: ComponentFixture<StockVolumeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StockVolumeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StockVolumeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
