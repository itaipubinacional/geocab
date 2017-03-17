import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {MaterialModule} from "@angular/material";
import {FlexLayoutModule} from "@angular/flex-layout";
import {UserService} from "./user.service";
import {UserRouteGuard, ModeratorUserRouteGuard, AdminUserRouteGuard} from "./user.route-guards";

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
    declarations: [
        NavComponent
    ],
    providers: [
        UserService,
        UserRouteGuard,
        ModeratorUserRouteGuard,
        AdminUserRouteGuard
    ]
})
export class SharedModule {
}
