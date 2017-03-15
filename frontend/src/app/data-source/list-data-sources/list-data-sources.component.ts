import {Component, OnInit} from "@angular/core";
import {DataSource} from "../../shared/model/data-source";
import {DataSourceService} from "../data-source.service";

@Component({
    selector: 'app-list-data-sources',
    templateUrl: './list-data-sources.component.html',
    styleUrls: ['./list-data-sources.component.css']
})
export class ListDataSourcesComponent implements OnInit {
    dataSources:DataSource[] = [];

    constructor(private dataSourceService:DataSourceService) {
    }

    ngOnInit() {
        // pega os datasources
        this.dataSourceService.getDataSources()
            .then(ds => this.dataSources = ds,
                error => {
                    alert(error);
                });
    }

}
