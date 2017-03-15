import {NgModule} from "@angular/core";
import {ListLayerGroupComponent} from "./list-layer-group/list-layer-group.component";
import {LayerGroupRoutingModule} from "./layer-group.routes";
import {LayerGroupComponent} from "./layer-group.component";
import {SharedModule} from "../shared/shared.module";
import {HttpModule} from "@angular/http";

@NgModule({
    imports: [
        SharedModule,
        HttpModule,
        LayerGroupRoutingModule
    ],
    declarations: [
        LayerGroupComponent,
        ListLayerGroupComponent
    ]
})
export class LayerGroupModule {
}
