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
import {SharedModule} from "./shared/shared.module";
import { MapNavComponent } from './map-nav/map-nav.component';

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
        DataSourceModule,
        AppRoutingModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule {
}
