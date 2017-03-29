import {LayerGroup} from "../shared/model/layer-group";
import {Injectable} from "@angular/core";
import {Http, Response, RequestOptions} from "@angular/http";
import {UserService} from "../core/user.service";

@Injectable()
export class LayerGroupService {

    constructor(private http: Http, private userService: UserService) {
    }

    getLayerGroupById(id: number): Promise<LayerGroup> {

        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get('/api/layer-group/' + id, options)
            .toPromise()
            .then(
                res => {
                    console.log(res);
                    return res.json();
                })
            .catch(res => this.handleError(res));
    }

    deleteLayerGroup(layerGroup: LayerGroup): Promise<any> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.delete('/api/layer-group/' + layerGroup.id, options)
            .toPromise()
            .then()
            .catch(res => this.handleError(res));
    }

    createLayerGroup(layerGroup: LayerGroup): Promise<LayerGroup> {

        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.post('/api/layer-group', layerGroup, options)
            .toPromise()
            .then(res => res.json())
            .catch(res => this.handleError(res));
    }

    updateLayerGroup(layerGroup: LayerGroup): Promise<LayerGroup> {

        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.put('/api/layer-group', layerGroup, options)
            .toPromise()
            .then(res => res.json())
            .catch(res => this.handleError(res));
    }

    getLayerGroups(): Promise<LayerGroup[]> {
        // cria o header de autorização
        let headers = this.userService.createAuthorizationHeaders();
        let options = new RequestOptions({headers: headers});

        return this.http.get('/api/layer-group', options)
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
