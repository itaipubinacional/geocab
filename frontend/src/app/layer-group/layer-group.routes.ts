import {Routes, RouterModule} from '@angular/router';

import {ListLayerGroupComponent} from './list-layer-group/list-layer-group.component';
import {NgModule} from '@angular/core';
import {LayerGroupComponent} from './layer-group.component';

const routes: Routes = [
    {
        path: '',
        component: LayerGroupComponent,
        children: [
            {path: '', component: ListLayerGroupComponent}
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})

export class LayerGroupRoutingModule {
}
