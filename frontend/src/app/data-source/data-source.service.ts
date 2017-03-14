import {Injectable} from "@angular/core";
import {DataSource, DataSourceType} from "../shared/model/data-source";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import "rxjs/add/operator/toPromise";
import {OAuthService} from "angular-oauth2-oidc";

@Injectable()
export class DataSourceService {

    constructor(private http:Http, private oAuthService:OAuthService) {
    }

    getDataSources():Promise<DataSource[]> {
        let token:string = this.oAuthService.getAccessToken();
        let headers = new Headers({
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        });
        let options = new RequestOptions({headers: headers});

        return this.http.get("http://localhost:8080/api/data-source", options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    getDataSourceById(id:number):Promise<DataSource> {
        return new Promise<DataSource>(resolve => {
            resolve(<DataSource>{
                id: id,
                name: "teste",
                serviceType: DataSourceType.WMS,
                url: "teste"
            });
        });
    }

    createDataSource(dataSource: DataSource): Promise<DataSource> {
        let token:string = this.oAuthService.getAccessToken();
        let headers = new Headers({
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        });
        let options = new RequestOptions({headers: headers});

        return this.http.post("http://localhost:8080/api/data-source", dataSource, options)
            .toPromise()
            .then(res => res.json()) // o objeto tem um enum
            .catch(res => this.handleError(res));
    }

    private handleError(error:Response | any) {
        // In a real world app, we might use a remote logging infrastructure
        let errMsg:string;
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