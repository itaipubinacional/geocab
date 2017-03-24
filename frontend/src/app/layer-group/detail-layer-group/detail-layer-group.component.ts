import { LayerGroup } from '../../shared/model/layer-group';
import { LayerGroupService } from '../layer-group.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-detail-layer-group',
  templateUrl: './detail-layer-group.component.html',
  styleUrls: ['./detail-layer-group.component.css'],
  providers: [LayerGroupService]
})
export class DetailLayerGroupComponent implements OnInit {

    model: LayerGroup;
    title: string = "Novo grupo de camadas";

    constructor(private activatedRoute: ActivatedRoute,
                private router: Router,
                private layerGroupService: LayerGroupService) {
    }

    ngOnInit() {
        this.model = new LayerGroup();
        // verifica se é para criar ou editar um group de camadas
        let requestType = this.activatedRoute.snapshot.params['id'];
        if (requestType != 'new') {
            // faz a leitura
            let id = parseInt(requestType, 10);
            this.layerGroupService.getLayerGroupById(id)
                .then((ds) => {
                    console.log(ds);
                  this.model = ds;                  
                });
        }

    }

    onSubmit() {
        let res;

        // verifica se é edição de um grupo existente ou um novo grupo de camadas.
        console.log(this.model); 
        if (!this.model.id) {
            // salva o grupo de camadas
            res = this.layerGroupService.createLayerGroup(this.model);                            
        } else {
            // atualiza o grupo de camadas
            console.log('model: '+this.model);
            res = this.layerGroupService.updateLayerGroup(this.model);
        }
        
        // retorna para a lista de grupos de camadas
        res.then(() => this.router.navigate(['./layer-group']))
        .catch(error => console.log(error));
    }

}
