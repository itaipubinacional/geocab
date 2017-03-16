import {Component, OnInit} from "@angular/core";

declare var google: any;

@Component({
  selector: 'app-map-view',
  templateUrl: './map-view.component.html',
  styleUrls: ['./map-view.component.css']
})
export class MapViewComponent implements OnInit {

  private map;
  private mapElement: Element;
  private mapOptions;
  public name: string;
   
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
}
