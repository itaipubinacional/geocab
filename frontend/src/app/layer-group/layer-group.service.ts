import {LayerGroup} from "../shared/model/layer-group";
import {Injectable} from "@angular/core";
import {Http, Response, RequestOptions} from "@angular/http";
import {UserService} from "../shared/user.service";

@Injectable()
export class LayerGroupService {

    constructor(private http: Http, private userService: UserService) {
    }

    getLayerGroups(): Promise<LayerGroup[]> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get('http://localhost:8080/api/layer-group', options)
            .toPromise()
            .then(res => res.json())
            .catch(res => this.handleError(res));
    }

    private handleError(error: Response | any) {
        // in a real world app, we might use a remote logging infrastructure
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
