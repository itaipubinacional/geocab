import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {AppRoutingModule} from "./app.routes";
import {SharedModule} from "./shared/shared.module";
import {OAuthModule, OAuthService} from "angular-oauth2-oidc";

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
    providers: [
    ],
    bootstrap: [AppComponent]
})
export class AppModule {

    constructor(private oauthService: OAuthService) {
        /*
         * A configuração do serviço do OAuth2 deve ser realizada antes que
         * qualquer componentente/serviço da aplicação seja instanciado.
         */
        // Login-Url
        this.oauthService.loginUrl = "https://kchom.itaipu:9898/auth/realms/geocab/protocol/openid-connect/auth"; //Id-Provider?

        this.oauthService.redirectUri = window.location.origin;
        this.oauthService.clientId = "geocab-dev-becker";
        this.oauthService.scope = "";
        this.oauthService.oidc = true;
        this.oauthService.setStorage(sessionStorage);
        this.oauthService.logoutUrl = "https://kchom.itaipu:9898/auth/realms/geocab/protocol/openid-connect/logout";
        this.oauthService.tokenEndpoint = "https://kchom.itaipu:9898/auth/realms/geocab/protocol/openid-connect/token";

        this.oauthService.tryLogin({});
    }
}
