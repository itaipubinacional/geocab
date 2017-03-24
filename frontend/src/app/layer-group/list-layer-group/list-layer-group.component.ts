import { LayerGroup } from '../../shared/model/layer-group';
import { LayerGroupService } from '../layer-group.service';
import { ConfirmModalDialogComponent } from '../../shared/confirm-modal-dialog/confirm-modal-dialog.component';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { Router } from '@angular/router';

declare var $: any;

@Component({
  selector: 'app-list-layer-group',
  templateUrl: './list-layer-group.component.html',
  styleUrls: ['./list-layer-group.component.css'],
  providers: [LayerGroupService]
})

export class ListLayerGroupComponent implements OnInit {

  layerGroups: LayerGroup[];
  layerTree: any = {};
  
  @ViewChild(ConfirmModalDialogComponent)
  confirmModal: ConfirmModalDialogComponent;

  constructor(private layerGroupService: LayerGroupService, private router: Router) {
      this.layerTree.data = null;
      this.layerTree.children = [];
   }

  ngOnInit() {
    this.refresh();
  }

  remove(event) {
    this.confirmModal.setTitle('Remover grupo de camadas');
    this.confirmModal.setMessage('Tem certeza que deseja remover o grupo de camadas: ' + event.name);
    this.confirmModal.show(() => {
      let group = new LayerGroup();
      group.id = event.id;
      this.layerGroupService.deleteLayerGroup(group)
        .then(() => this.refresh());            
    });    
  }
  
  edit(event) {
    this.router.navigate(['./layer-group/' + event.id]);
    console.log(event);
  }

  private refresh() {
    this.layerGroupService.getLayerGroups()
    .then((lgs) => {
        this.layerGroups = lgs;
        this.layerTree.children = this.convertLayerTree(this.layerGroups);
      },
          error => alert(error));
  }

  /***
   *  converte a estrutura recursiva do grupo de camadas para a estrutura de árvore
   *  esperada pelo componente TreeView.
   */
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
