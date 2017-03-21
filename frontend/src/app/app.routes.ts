import {Routes, RouterModule} from '@angular/router';

import {MapComponent} from './map/map.component';
import {NgModule} from "@angular/core";

const routes:Routes = [
    {path: '', redirectTo: 'map', pathMatch: 'full'},
    {path: 'map', loadChildren: 'app/map/map.module#MapModule'},
    {path: 'data-source', loadChildren: 'app/data-source/data-source.module#DataSourceModule'},
    {path: 'layer-group', loadChildren: 'app/layer-group/layer-group.module#LayerGroupModule'}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {}