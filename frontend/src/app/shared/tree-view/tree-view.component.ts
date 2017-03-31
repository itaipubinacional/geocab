import {Component, OnInit, Input, Output, EventEmitter} from '@angular/core';

@Component({
    selector: 'geocab-tree-view',
    templateUrl: './tree-view.component.html',
    styleUrls: ['./tree-view.component.css']
})
export class TreeViewComponent implements OnInit {

    @Input() node: any;
    @Output() removeEvent: EventEmitter<any> = new EventEmitter();
    @Output() editEvent: EventEmitter<any> = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
    }

    remove(data: any) {
        this.removeEvent.emit(data);
    }

    edit(data: any) {
        this.editEvent.emit(data);
    }
}
