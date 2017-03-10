import { Component, OnInit, ViewChild } from '@angular/core';
import { ButtonMenuComponent } from '../button-menu/button-menu.component';
import { SidebarComponent } from '../sidebar/sidebar.component';

declare var google: any;

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {

  private map;
  private mapElement: Element;
  private mapOptions;
  public name: string;

  @ViewChild(ButtonMenuComponent)
  private buttonMenuComponent : ButtonMenuComponent;

  @ViewChild(SidebarComponent)
  private sidebarComponent : SidebarComponent;

  constructor() {
    
   }
   
  ngOnInit() {
    this.mapElement = document.getElementById('map-main-container');
    this.mapOptions  = {
      center: {lat: -25.420762, lng: -54.588844},
      mapTypeId: google.maps.MapTypeId.MAP,
      mapTypeControl: false,
      zoom: 10
    };

    this.map = new google.maps.Map(this.mapElement, this.mapOptions);
  }

  onButtonClicked(event: string) {
    //alert(event);
    this.sidebarComponent.toggle();
  }
}
