import {Routes, RouterModule} from '@angular/router';

import {NgModule} from '@angular/core';
import {MapComponent} from "./map.component";
import {MapViewComponent} from "./map-view/map-view.component";

const routes: Routes = [
    {
        path: '',
        component: MapComponent,
        children: [
            {path: '', component: MapViewComponent}
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class MapRoutingModule {
}
