import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {UserService} from "./user.service";
import {UserRouteGuard, ModeratorUserRouteGuard, AdminUserRouteGuard} from "./user.route-guards";
import {TreeViewComponent} from "./tree-view/tree-view.component";
import {MaterializeModule} from "angular2-materialize";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        HttpModule,
        MaterializeModule
    ],
    exports: [
        CommonModule,
        RouterModule,
        HttpModule,
        MaterializeModule,
        NavComponent,
        TreeViewComponent
    ],
    declarations: [
        NavComponent,
        TreeViewComponent
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
