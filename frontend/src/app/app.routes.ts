import {Routes, RouterModule} from '@angular/router';

import {MapComponent} from './map/map.component';
import {NgModule} from "@angular/core";

const routes:Routes = [
    {path: '', redirectTo: 'map', pathMatch: 'full'},
    {path: 'map', component: MapComponent},
    {path: 'data-source', loadChildren: 'app/data-source/data-source.module#DataSourceModule'}
];

@NgModule({
    imports: [RouterModule.forRoot(routes, {useHash: true})],
    exports: [RouterModule]
})
export class AppRoutingModule {}