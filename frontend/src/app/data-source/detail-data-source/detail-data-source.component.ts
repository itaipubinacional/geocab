import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {DataSourceService} from "../data-source.service";
import {DataSource, DataSourceType} from "../../shared/model/data-source";

@Component({
    selector: 'app-detail-data-source',
    templateUrl: './detail-data-source.component.html',
    styleUrls: ['./detail-data-source.component.css']
})
export class DetailDataSourceComponent implements OnInit {
    dataSourceTypes: any[];

    model: DataSource;

    externalDataSource: boolean;

    authenticationRequired: boolean;

    constructor(private activatedRoute: ActivatedRoute,
                private router: Router,
                private dataSourceService: DataSourceService) {
        this.dataSourceTypes = Object.keys(DataSourceType);
    }

    ngOnInit() {
        // verifica se é para criar ou editar uma fonte de dados
        let requestType = this.activatedRoute.snapshot.params['id'];
        if (requestType != 'new') {
            // faz a leitura
            let id = parseInt(requestType, 10);
            this.dataSourceService.getDataSourceById(id)
                .then(ds => this.model = ds);
        }
        else
            this.model = new DataSource();
    }

    onSubmit() {
        // salva o datasources
        this.dataSourceService.createDataSource(this.model)
            // e redireciona para a lista de datasources
            .then(() => this.router.navigate(['/data-source']))
            .catch(error => alert(error));

    }

}
