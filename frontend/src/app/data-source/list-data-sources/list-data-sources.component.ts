import {Component, OnInit} from '@angular/core';
import {DataSource, DataSourceType} from "../../shared/model/data-source";

@Component({
    selector: 'app-list-data-sources',
    templateUrl: './list-data-sources.component.html',
    styleUrls: ['./list-data-sources.component.css']
})
export class ListDataSourcesComponent implements OnInit {
    DataSourceType:typeof DataSourceType = DataSourceType;

    dataSources:DataSource[] = [
        new DataSource("teste", DataSourceType.WMS, "http://localhost:8080"),
        new DataSource("teste2", DataSourceType.WFS, "http://localhost:8081"),
        new DataSource("teste3", DataSourceType.WMS, "http://localhost:8082"),
    ];

    constructor() {
    }

    ngOnInit() {
    }

}
