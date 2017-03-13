import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {HttpModule} from "@angular/http";
import {AppComponent} from "./app.component";
import {MapComponent} from "./map/map.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {ButtonMenuComponent} from "./button-menu/button-menu.component";
import {AppRoutingModule} from "./app.routes";
import {DataSourceModule} from "./data-source/data-source.module";
import {LayerGroupModule} from './layer-group/layer-group.module';
import {SharedModule} from "./shared/shared.module";
import { MapNavComponent } from './map-nav/map-nav.component';
import { OAuthModule } from 'angular-oauth2-oidc';

@NgModule({
    declarations: [
        AppComponent,
        MapComponent,
        SidebarComponent,
        ButtonMenuComponent,
        MapNavComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        HttpModule,
        SharedModule,
        LayerGroupModule,
        DataSourceModule,
        AppRoutingModule,
        OAuthModule.forRoot()
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
