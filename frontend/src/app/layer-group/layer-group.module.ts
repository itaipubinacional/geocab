import {NgModule} from "@angular/core";
import {ListLayerGroupComponent} from "./list-layer-group/list-layer-group.component";
import {DetailLayerGroupComponent} from './detail-layer-group/detail-layer-group.component';
import {LayerGroupRoutingModule} from "./layer-group.routes";
import {LayerGroupComponent} from "./layer-group.component";
import {SharedModule} from "../shared/shared.module";
import {FormsModule} from '@angular/forms';
import { RemoveLayerGroupModalComponent } from './remove-layer-group-modal/remove-layer-group-modal.component';

@NgModule({
    imports: [
        SharedModule,
        LayerGroupRoutingModule,
        FormsModule
    ],
    declarations: [
        LayerGroupComponent,
        ListLayerGroupComponent,
        DetailLayerGroupComponent,
        RemoveLayerGroupModalComponent
    ]
})
export class LayerGroupModule {
}
