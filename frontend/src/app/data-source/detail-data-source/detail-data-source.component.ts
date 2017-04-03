import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {DataSourceService} from "../data-source.service";
import {DataSource, DataSourceType} from "../../shared/model/data-source";

@Component({
    selector: 'geocab-detail-data-source',
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
        this.model = new DataSource();
        // verifica se é para criar ou editar uma fonte de dados
        let requestType = this.activatedRoute.snapshot.params['id'];
        if (requestType != 'new') {
            // faz a leitura
            let id = parseInt(requestType, 10);
            // função assíncrona. Tem que instanciar o model antes de chama-la
            this.dataSourceService.getDataSourceById(id)
                .then(ds => {
                        this.model = ds;
                        this.parseModel();
                    },
                    error => alert(error));
        }
    }

    onSubmit() {
        // limpa o objeto antes de salvar
        this.cleanModel();

        // salva o datasources
        let res;
        if (this.model.id)
            res = this.dataSourceService.updateDataSource(this.model);
        else
            res = this.dataSourceService.createDataSource(this.model);

        // e redireciona para a lista de datasources
        res.then(() => this.router.navigate(['/data-source']))
            .catch(error => alert(error));
    }

    private cleanModel() {
        if (!this.externalDataSource) {
            this.model.serviceType = null;
            this.model.url = null;
            this.model.login = null;
            this.model.password = null;
        }

        if (this.externalDataSource && !this.authenticationRequired) {
            this.model.login = null;
            this.model.password = null;
        }
    }

    private parseModel() {
        this.externalDataSource = !!this.model.serviceType && !!this.model.url;
        this.authenticationRequired = this.externalDataSource && !!this.model.login; // pode ter senha em branco
    }
}
