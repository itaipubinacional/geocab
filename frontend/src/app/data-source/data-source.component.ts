import {Component, OnInit} from "@angular/core";
import {DataSourceService} from "./data-source.service";

@Component({
    selector: 'geocab-data-source',
    templateUrl: './data-source.component.html',
    styleUrls: ['./data-source.component.css'],
    providers: [DataSourceService]
})
export class DataSourceComponent implements OnInit {

    constructor() {
    }

    ngOnInit() {
    }

}
