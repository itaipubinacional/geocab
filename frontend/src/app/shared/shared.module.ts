import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {MaterialModule} from "@angular/material";
import {FlexLayoutModule} from "@angular/flex-layout";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        HttpModule,
        MaterialModule,
        FlexLayoutModule
    ],
    exports: [
        CommonModule,
        RouterModule,
        HttpModule,
        MaterialModule,
        FlexLayoutModule,
        NavComponent
    ],
    declarations: [NavComponent]
})
export class SharedModule {
}
