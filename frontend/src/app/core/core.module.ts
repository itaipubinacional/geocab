import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {UserService} from "./user.service";
import {UserRouteGuard, ModeratorUserRouteGuard, AdminUserRouteGuard} from "./user.route-guards";
import {OAuthModule} from "angular-oauth2-oidc";
import {HttpModule} from "@angular/http";
import {ConfigService} from "./config.service";

@NgModule({
    imports: [
        CommonModule,
        HttpModule,
        OAuthModule.forRoot()
    ],
    declarations: [],
    providers: [
        UserService,
        ConfigService,
        UserRouteGuard,
        ModeratorUserRouteGuard,
        AdminUserRouteGuard
    ]
})
export class CoreModule {
}
