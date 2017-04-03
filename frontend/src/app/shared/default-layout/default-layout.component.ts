import {Component, OnInit} from "@angular/core";

@Component({
    selector: 'geocab-default-layout',
    templateUrl: './default-layout.component.html',
    styleUrls: ['./default-layout.component.css']
})
export class DefaultLayoutComponent implements OnInit {

    navOpened: boolean = false;

    constructor() {
    }

    ngOnInit() {
    }

    clickMenuButton() {
        this.navOpened = !this.navOpened;
    }

    closeNavbar() {
        this.navOpened = false;
    }
}
