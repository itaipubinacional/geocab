import {Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'geocab-toolbar',
    templateUrl: './toolbar.component.html',
    styleUrls: ['./toolbar.component.css']
})
export class ToolbarComponent implements OnInit {

    @Input('title') title: string;

    constructor() {
    }

    ngOnInit() {
    }

}
