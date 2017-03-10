import {Component, OnInit, Input} from '@angular/core';

@Component({
  selector: 'app-map-nav',
  templateUrl: './map-nav.component.html',
  styleUrls: ['./map-nav.component.css']
})
export class MapNavComponent implements OnInit {

  @Input()
  isToggleButtonHidden: boolean;

  @Input()
  isHidden: boolean;

  //this[this.$element.hasClass('open') ? 'hide' : 'show']()

  constructor() { }

  ngOnInit() {
  }

}
