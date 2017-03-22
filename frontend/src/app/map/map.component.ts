import {Component, OnInit} from "@angular/core";

@Component({
    selector: 'app-map',
    templateUrl: './map.component.html',
    styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

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
