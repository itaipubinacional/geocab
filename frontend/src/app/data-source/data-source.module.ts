import {NgModule} from "@angular/core";
import {ListDataSourcesComponent} from "./list-data-sources/list-data-sources.component";
import {DataSourceRoutingModule} from "./data-source.routes";
import {DataSourceComponent} from "./data-source.component";
import {SharedModule} from "../shared/shared.module";
import {HttpModule} from "@angular/http";

@NgModule({
    imports: [
        SharedModule,
        HttpModule,
        DataSourceRoutingModule
    ],
    declarations: [
        DataSourceComponent,
        ListDataSourcesComponent
    ]
})
export class DataSourceModule {
}
