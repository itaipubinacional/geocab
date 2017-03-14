import {NgModule} from "@angular/core";
import {ListDataSourcesComponent} from "./list-data-sources/list-data-sources.component";
import {DataSourceRoutingModule} from "./data-source.routes";
import {DataSourceComponent} from "./data-source.component";
import {DetailDataSourceComponent} from "./detail-data-source/detail-data-source.component";
import {FormsModule} from "@angular/forms";
import {SharedModule} from "../shared/shared.module";

@NgModule({
    imports: [
        SharedModule,
        FormsModule,
        DataSourceRoutingModule
    ],
    declarations: [
        DataSourceComponent,
        ListDataSourcesComponent,
        DetailDataSourceComponent
    ]
})
export class DataSourceModule {
}
