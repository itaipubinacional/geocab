import {Injectable} from "@angular/core";
import {DataSource, DataSourceType} from "../shared/model/data-source";
import {Http, Response, Headers, RequestOptions} from "@angular/http";

import 'rxjs/add/operator/toPromise';
import {OAuthService} from "angular-oauth2-oidc/dist/index";

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
            .then(res => this.extractData(res))
            .catch(res => this.handleError(res));
    }

    private extractData(res:Response):DataSource[] {
        let body = res.json();
        return body.map(this.toDataSource);
    }

    private toDataSource(p:any):DataSource {
        let dsTypeStr:string = p.serviceType;
        var dataServiceType = DataSourceType[dsTypeStr];

        let dataSource = <DataSource>({
            name: p.name,
            url: p.url,
            serviceType: dataServiceType
        });
        return dataSource;
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