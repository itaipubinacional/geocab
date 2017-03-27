import {Routes, RouterModule} from "@angular/router";
import {ListLayerGroupComponent} from "./list-layer-group/list-layer-group.component";
import {NgModule} from "@angular/core";
import {LayerGroupComponent} from "./layer-group.component";
import {DetailLayerGroupComponent} from "./detail-layer-group/detail-layer-group.component";
import {AdminUserRouteGuard} from "../core/user.route-guards";

const routes: Routes = [
    {
        path: '',
        component: LayerGroupComponent,
        canActivateChild: [AdminUserRouteGuard],
        children: [
            {path: '', component: ListLayerGroupComponent},
            {path: 'new', component: DetailLayerGroupComponent},
            {path: ':id', component: DetailLayerGroupComponent},
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class LayerGroupRoutingModule {
}
