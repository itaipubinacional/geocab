import {NgModule} from "@angular/core";
import {SharedModule} from "../shared/shared.module";
import {MapComponent} from "./map.component";
import {MapViewComponent} from "./map-view/map-view.component";
import {MapRoutingModule} from "./map.routes";

@NgModule({
    imports: [
        SharedModule,
        MapRoutingModule
    ],
    declarations: [
        MapComponent,
        MapViewComponent
    ]
})
export class MapModule {
}
