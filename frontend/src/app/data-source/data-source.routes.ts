import {Routes, RouterModule} from "@angular/router";

import {ListDataSourcesComponent} from "./list-data-sources/list-data-sources.component";
import {NgModule} from "@angular/core";
import {DataSourceComponent} from "./data-source.component";
import {DetailDataSourceComponent} from "./detail-data-source/detail-data-source.component";
import {AdminUserRouteGuard} from "../shared/user.route-guards";

const routes:Routes = [
    {
        path: '',
        component: DataSourceComponent,
        canActivateChild: [AdminUserRouteGuard],
        children: [
            {path: '', component: ListDataSourcesComponent},
            {path: ':id', component: DetailDataSourceComponent},
            {path: 'new', component: DetailDataSourceComponent}
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class DataSourceRoutingModule {
}