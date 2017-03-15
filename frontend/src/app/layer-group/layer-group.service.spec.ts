/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { LayerGroupService } from './layer-group.service';

describe('LayerGroupService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LayerGroupService]
    });
  });

  it('should ...', inject([LayerGroupService], (service: LayerGroupService) => {
    expect(service).toBeTruthy();
  }));
});
