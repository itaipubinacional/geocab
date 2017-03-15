import {Component, OnInit, EventEmitter, Output} from "@angular/core";
import {OAuthService} from "angular-oauth2-oidc";
import {HelloService} from "../shared/hello.service";

@Component({
    selector: 'app-button-menu',
    templateUrl: './button-menu.component.html',
    styleUrls: ['./button-menu.component.css'],
    providers: [HelloService]
})
export class ButtonMenuComponent implements OnInit {

    private name: string;
    private baseUrl: string = 'http://localhost:8080/api'

    constructor(private oAuthService: OAuthService, private helloService: HelloService) {
    }

    @Output() buttonClicked = new EventEmitter<string>();

    clicked(event) {
        this.helloService.getHelloMessage().then(value => alert(value))
        //this.buttonClicked.emit("slider");

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
