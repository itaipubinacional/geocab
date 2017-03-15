import { LayerGroup } from '../../shared/model/layer-group';
import { LayerGroupService } from '../layer-group.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-list-layer-group',
  templateUrl: './list-layer-group.component.html',
  styleUrls: ['./list-layer-group.component.css'],
  providers: [LayerGroupService]
})

export class ListLayerGroupComponent implements OnInit {

  layerGroups: LayerGroup[];

  constructor(private layerGroupService: LayerGroupService) { }

  ngOnInit() {
    this.layerGroupService.getLayerGroups()
    .then((lgs) => {
        this.layerGroups = lgs;
        console.log(this.layerGroups);
      },
          error => alert(error));
  }

}
