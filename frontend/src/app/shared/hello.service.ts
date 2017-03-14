import {Injectable} from "@angular/core";
import {Http, Headers, RequestOptions} from "@angular/http";
import {OAuthService} from "angular-oauth2-oidc";

@Injectable()
export class HelloService {
    private helloUrl = 'http://localhost:8080/api/hello';

    constructor(private http: Http, private oAuthService: OAuthService) {
    }

    getHelloMessage(): Promise<string> {

        let headers = new Headers({
            "Authorization": "Bearer " + this.oAuthService.getAccessToken()
        });
        let options = new RequestOptions({headers: headers});

        return this.http.get(this.helloUrl, options)
            .toPromise()
            .then(response => response.json().data)
            .catch(this.handleError);
    }

    private handleError(error: any): Promise<any> {
        console.error('An error occurred', error);
        return Promise.reject(error.message || error);
    }
}