import {Routes, RouterModule} from "@angular/router";

import {ListDataSourcesComponent} from "./list-data-sources/list-data-sources.component";
import {NgModule} from "@angular/core";
import {DataSourceComponent} from "./data-source.component";

const routes:Routes = [
    {
        path: 'data-source',
        component: DataSourceComponent,
        children: [
            {path: '', component: ListDataSourcesComponent}
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DataSourceRoutingModule {
}