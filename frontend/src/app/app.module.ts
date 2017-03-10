import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';
import { OAuthModule } from 'angular-oauth2-oidc';

import { AppComponent } from './app.component';
import { rootRouterConfig } from './app.routes';
import { MapComponent } from './map/map.component';
import { NavComponent } from './nav/nav.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { ButtonMenuComponent } from './button-menu/button-menu.component';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    NavComponent,
    SidebarComponent,
    ButtonMenuComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
        RouterModule.forRoot(rootRouterConfig, { useHash: true }),
    OAuthModule.forRoot()

  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
