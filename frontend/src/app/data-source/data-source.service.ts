import {Injectable} from "@angular/core";
import {DataSource} from "../shared/model/data-source";
import {Http, Response, RequestOptions} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {UserService} from "../core/user.service";

@Injectable()
export class DataSourceService {

    constructor(private http: Http, private userService: UserService) {
    }

    getDataSources(): Promise<DataSource[]> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get("/api/data-source", options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    getDataSourceById(id: number): Promise<DataSource> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get(`/api/data-source/${id}`, options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    createDataSource(dataSource: DataSource): Promise<DataSource> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.post("/api/data-source", dataSource, options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    updateDataSource(dataSource: DataSource): Promise<DataSource> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.put(`/api/data-source/${dataSource.id}`, dataSource, options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    removeDataSource(dataSource: DataSource): Promise<boolean> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.delete(`/api/data-source/${dataSource.id}`, options)
            .toPromise()
            // a api do angular já trata os erros. Se chegou no then é porque apagou
            .then(res => true)
            .catch(res => this.handleError(res));
    }

    private handleError(error: Response | any) {
        let errMsg: string;
        if (error instanceof Response) {
            const body = error.json() || '';
            const err = body.error || JSON.stringify(body);
            errMsg = `${error.status} - ${error.statusText || ''} ${err}`;
        } else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error(errMsg);
        return Promise.reject(errMsg);
    }
}
