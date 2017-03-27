import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app.routes';
import {SharedModule} from './shared/shared.module';
import {OAuthModule} from 'angular-oauth2-oidc';

@NgModule({
    declarations: [
        AppComponent
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

    constructor() {
    }
}
