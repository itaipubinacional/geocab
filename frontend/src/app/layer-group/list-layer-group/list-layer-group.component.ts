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
  layerTree: any = {};

  constructor(private layerGroupService: LayerGroupService) {
      this.layerTree.data = null;
      this.layerTree.children = [];
  
   }

  ngOnInit() {
    this.layerGroupService.getLayerGroups()
    .then((lgs) => {
        this.layerGroups = lgs;
        console.log(this.layerGroups);
        this.layerTree.children = this.convertLayerTree(this.layerGroups);

        console.log(this.layerTree);
      },
          error => alert(error));

  }

  private convertLayerTree(layerGroups: LayerGroup[]) {
    let tree = [];

    for (let group of layerGroups) {
      let node = { children : null, data : null};

      node.data = group;
      if (group.groups !== null)
         node.children = this.convertLayerTree(group.groups);

      tree.push(node);
    }

    return tree;

  }

}
