import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        HttpModule
    ],
    exports: [
        CommonModule,
        RouterModule,
        HttpModule,
        NavComponent
    ],
    declarations: [NavComponent]
})
export class SharedModule {
}
