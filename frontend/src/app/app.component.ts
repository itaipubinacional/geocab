import { Component } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {

  constructor(private oauthService: OAuthService) {
        // Login-Url
        this.oauthService.loginUrl = "https://kchom.itaipu:9898/auth/realms/geocab/protocol/openid-connect/auth"; //Id-Provider?
 
        this.oauthService.redirectUri = window.location.origin;
        this.oauthService.clientId = "geocab-dev-becker";
        this.oauthService.scope = "";
        this.oauthService.oidc = true;
        this.oauthService.setStorage(sessionStorage);
        this.oauthService.logoutUrl ="https://kchom.itaipu:9898/auth/realms/geocab/protocol/openid-connect/logout";
        this.oauthService.tokenEndpoint = "https://kchom.itaipu:9898/auth/realms/geocab/protocol/openid-connect/token";
        
        this.oauthService.tryLogin({});      
  }
 
}
