import {NgModule} from "@angular/core";
import {ListLayerGroupComponent} from "./list-layer-group/list-layer-group.component";
import {DetailLayerGroupComponent} from './detail-layer-group/detail-layer-group.component';
import {LayerGroupRoutingModule} from "./layer-group.routes";
import {LayerGroupComponent} from "./layer-group.component";
import {SharedModule} from "../shared/shared.module";
import {FormsModule} from '@angular/forms';

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
    ]
})
export class LayerGroupModule {
}
