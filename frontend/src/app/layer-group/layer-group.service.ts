import { LayerGroup } from '../shared/model/layer-group';
import { Injectable } from '@angular/core';
import {Http, Response, Headers, RequestOptions} from '@angular/http';

import {OAuthService} from 'angular-oauth2-oidc/dist/index';

@Injectable()
export class LayerGroupService {

    constructor(private http: Http, private oAuthService: OAuthService) {
    }

    getLayerGroupById(id: number): Promise<LayerGroup> {
      return Promise.resolve(new LayerGroup());
    }

    createLayerGroup(layerGroup: LayerGroup): Promise<LayerGroup> {

        let headers = new Headers({
            'Authorization': 'Bearer ' + this.oAuthService.getAccessToken(),
            'Content-Type': 'application/json'
        });

        let options = new RequestOptions({headers: headers});

        return this.http.post('http://localhost:8080/api/layer-group', layerGroup, options)
            .toPromise()
            .then(res => res.json())
            .catch(res => this.handleError(res));
    }

    getLayerGroups(): Promise<LayerGroup[]> {

        let headers = new Headers({
            'Authorization': 'Bearer ' + this.oAuthService.getAccessToken(),
            'Content-Type': 'application/json'
        });

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
