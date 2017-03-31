import {Component, OnInit} from '@angular/core';

@Component({
    selector: 'geocab-modal-window',
    templateUrl: './modal-window.component.html',
    styleUrls: ['./modal-window.component.css']
})
export class ModalWindowComponent implements OnInit {

    visible = false;

    private visibleAnimate = false;

    constructor() {
    }

    ngOnInit() {
    }

    show(): void {
        this.visible = true;
        setTimeout(() => this.visibleAnimate = true);
    }

    hide(): void {
        this.visibleAnimate = false;
        setTimeout(() => this.visible = false, 300);
    }
}
