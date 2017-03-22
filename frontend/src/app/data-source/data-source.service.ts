import {Injectable} from "@angular/core";
import {DataSource, DataSourceType} from "../shared/model/data-source";
import {Http, Response, RequestOptions} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {UserService} from "../shared/user.service";

@Injectable()
export class DataSourceService {

    constructor(private http: Http, private userService: UserService) {
    }

    getDataSources(): Promise<DataSource[]> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get("http://localhost:8080/api/data-source", options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    getDataSourceById(id: number): Promise<DataSource> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get("http://localhost:8080/api/data-source/" + id, options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    createDataSource(dataSource: DataSource): Promise<DataSource> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.post("http://localhost:8080/api/data-source", dataSource, options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    private handleError(error: Response | any) {
        // In a real world app, we might use a remote logging infrastructure
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