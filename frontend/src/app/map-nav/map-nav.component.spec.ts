/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { MapNavComponent } from './map-nav.component';

describe('MapNavComponent', () => {
  let component: MapNavComponent;
  let fixture: ComponentFixture<MapNavComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapNavComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapNavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
