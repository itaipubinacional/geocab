/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { DetailLayerGroupComponent } from './detail-layer-group.component';

describe('DetailLayerGroupComponent', () => {
  let component: DetailLayerGroupComponent;
  let fixture: ComponentFixture<DetailLayerGroupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DetailLayerGroupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailLayerGroupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
