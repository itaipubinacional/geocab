import { Component, OnInit, Output } from '@angular/core';
import { EventEmitter } from '@angular/core';

declare var $: any;

@Component({
  selector: 'app-confirm-modal-dialog',
  templateUrl: './confirm-modal-dialog.component.html',
  styleUrls: ['./confirm-modal-dialog.component.css']
})
export class ConfirmModalDialogComponent implements OnInit {

  constructor() { }

  private title: string;
  private message: string;
  private callback: any;

  setTitle(title: string) {
    this.title = title;
  }

  setMessage(message: string) {
    this.message = message;
  }

  confirm() {    
    this.callback();
    $('#confirm-modal-dialog').modal('hide');
  }

  show(callback: any) {    
    this.callback = callback;
    $('#confirm-modal-dialog').modal();
  }

  ngOnInit() {
  }

}
