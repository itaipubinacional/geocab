import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-button-menu',
  templateUrl: './button-menu.component.html',
  styleUrls: ['./button-menu.component.css']
})
export class ButtonMenuComponent implements OnInit {

  constructor() { }

  @Output() buttonClicked = new EventEmitter<string>();

  clicked(event) {
    this.buttonClicked.emit("slider");
  }

  ngOnInit() {

  }

}
