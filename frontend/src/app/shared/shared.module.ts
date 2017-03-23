import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {UserService} from "./user.service";
import {UserRouteGuard, ModeratorUserRouteGuard, AdminUserRouteGuard} from "./user.route-guards";
import {TreeViewComponent} from "./tree-view/tree-view.component";
import { LogoComponent } from './logo/logo.component';
import { ConfirmModalDialogComponent } from './confirm-modal-dialog/confirm-modal-dialog.component';

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
        NavComponent,
        TreeViewComponent,
        LogoComponent,
        ConfirmModalDialogComponent
    ],
    declarations: [
        NavComponent,
        TreeViewComponent,
        LogoComponent,
        ConfirmModalDialogComponent
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
