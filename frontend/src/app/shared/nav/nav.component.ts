import {Component, OnInit} from "@angular/core";
import {UserService} from "../../core/user.service";

@Component({
    selector: 'geocab-nav',
    templateUrl: './nav.component.html',
    styleUrls: ['./nav.component.css']
})
export class NavComponent implements OnInit {

    constructor(private userService: UserService) {
    }

    ngOnInit() {
    }

    login() {
        this.userService.login();
    }

    logout() {
        this.userService.logout();
    }

    get authenticated(): boolean {
        return this.userService.authenticated;
    }

}
