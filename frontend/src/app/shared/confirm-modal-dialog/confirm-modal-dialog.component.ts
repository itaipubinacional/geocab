import {Component, OnInit, ViewChild} from "@angular/core";
import {ModalWindowComponent} from "../modal-window/modal-window.component";

@Component({
    selector: 'app-confirm-modal-dialog',
    templateUrl: './confirm-modal-dialog.component.html',
    styleUrls: ['./confirm-modal-dialog.component.css']
})
export class ConfirmModalDialogComponent implements OnInit {

    message: string;
    title: string;

    @ViewChild(ModalWindowComponent) private modalWin: ModalWindowComponent;


    private callback: any;

    constructor() {
    }

    ngOnInit() {
    }

    confirm() {
        this.callback();
        this.modalWin.hide();
    }

    cancel() {
        this.modalWin.hide();
    }

    show(callback: any) {
        this.callback = callback;
        this.modalWin.show();
    }

}
