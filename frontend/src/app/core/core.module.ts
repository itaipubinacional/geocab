import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UserService} from "./user.service";
import {UserRouteGuard, ModeratorUserRouteGuard, AdminUserRouteGuard} from "./user.route-guards";
import {OAuthModule} from "angular-oauth2-oidc";

@NgModule({
    imports: [
        CommonModule,
        OAuthModule.forRoot()
    ],
    declarations: [],
    providers: [
        UserService,
        UserRouteGuard,
        ModeratorUserRouteGuard,
        AdminUserRouteGuard
    ]
})
export class CoreModule {
}
