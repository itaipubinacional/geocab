import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {MapComponent} from "./map/map.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {ButtonMenuComponent} from "./button-menu/button-menu.component";
import {AppRoutingModule} from "./app.routes";
import {SharedModule} from "./shared/shared.module";
import {MapNavComponent} from "./map-nav/map-nav.component";
import {OAuthModule} from "angular-oauth2-oidc";

@NgModule({
    declarations: [
        AppComponent,
        MapComponent,
        SidebarComponent,
        ButtonMenuComponent,
        MapNavComponent
    ],
    imports: [
        SharedModule,
        BrowserModule,
        AppRoutingModule,
        OAuthModule.forRoot()
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
