import {Component, OnInit, ViewChild} from "@angular/core";
import {DataSource} from "../../shared/model/data-source";
import {DataSourceService} from "../data-source.service";
import {ConfirmModalDialogComponent} from "../../shared/confirm-modal-dialog/confirm-modal-dialog.component";

@Component({
    selector: 'geocab-list-data-sources',
    templateUrl: './list-data-sources.component.html',
    styleUrls: ['./list-data-sources.component.css']
})
export class ListDataSourcesComponent implements OnInit {
    dataSources: DataSource[] = [];

    @ViewChild(ConfirmModalDialogComponent)
    private confirmDialog: ConfirmModalDialogComponent;

    constructor(private dataSourceService: DataSourceService) {
    }

    ngOnInit() {
        // pega os datasources
        this.dataSourceService.getDataSources()
            .then(ds => this.dataSources = ds,
                  error => alert(error));
    }

    remove(dataSource: DataSource): void {
        this.confirmDialog.title = 'Remover fonte de dados';
        this.confirmDialog.message = `Tem certeza que deseja remover o grupo de camadas: ${dataSource.name}`;
        this.confirmDialog.show(() => {
            // apaga a fonte de dados
            this.dataSourceService.removeDataSource(dataSource)
                .then(() => {
                    // busca a posição da fonte na lista e remove
                    let idx = this.dataSources.indexOf(dataSource);
                    if (idx !== -1) {
                        this.dataSources.splice(idx, 1);
                    }
                })
                .catch(error => alert(error));
        });
    }

}
