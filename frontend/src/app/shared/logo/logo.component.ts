import {Component, OnInit, Input} from '@angular/core';

@Component({
    selector: 'geocab-logo',
    templateUrl: './logo.component.html',
    styleUrls: ['./logo.component.css']
})
export class LogoComponent implements OnInit {

    @Input('fontColor') fontColor: string = 'inherit';

    constructor() {
    }

    ngOnInit() {
    }

}
