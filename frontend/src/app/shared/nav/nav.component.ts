import {Component, OnInit} from "@angular/core";
import {OAuthService} from "angular-oauth2-oidc/dist/index";

@Component({
    selector: 'app-nav',
    templateUrl: './nav.component.html',
    styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

    private name;

    constructor(private oAuthService:OAuthService) {
    }

    public login() {
        this.oAuthService.initImplicitFlow();
        this.get_name();
    }

    public logout() {
        this.oAuthService.logOut();
    }

    public get_name() {
        let claims = this.oAuthService.getIdentityClaims();
        if (!claims) return null;
        this.name = claims.given_name;
    }

    ngOnInit() {
        this.get_name();
    }

}
