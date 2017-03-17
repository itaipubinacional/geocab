import {Component, OnInit} from "@angular/core";
import {UserService} from "../user.service";

@Component({
    selector: 'app-nav',
    templateUrl: './nav.component.html',
    styleUrls: ['./nav.component.scss']
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
