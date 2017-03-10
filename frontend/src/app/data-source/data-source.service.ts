import {Injectable} from "@angular/core";
import {DataSource, DataSourceType} from "../shared/model/data-source";
import {Http, Response, Headers, RequestOptions} from "@angular/http";

import 'rxjs/add/operator/toPromise';

@Injectable()
export class DataSourceService {

    constructor(private http: Http) {
    }

    getDataSources(): Promise<DataSource[]> {
        let token: string = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJDdmxlWDVWYnU5VjI0a0xSb1JjX1JxWXN2NmtRdWlrdkNsaXNwWnFFN3NnIn0.eyJqdGkiOiJiMTUyYzI2Mi1iMTRiLTQwNTQtOWQ4ZS0yYWY2MGJmZGEwMmEiLCJleHAiOjE0ODkxNTAxOTUsIm5iZiI6MCwiaWF0IjoxNDg5MTQ5ODk1LCJpc3MiOiJodHRwczovL2tjaG9tLml0YWlwdTo5ODk4L2F1dGgvcmVhbG1zL2dlb2NhYiIsImF1ZCI6Imdlb2NhYi1kZXYtYmlsbCIsInN1YiI6ImI0MjU3NTAwLTQxNTYtNDUwYi05Y2NjLTcwYzZiNzNhZDY4OCIsInR5cCI6IkJlYXJlciIsImF6cCI6Imdlb2NhYi1kZXYtYmlsbCIsImF1dGhfdGltZSI6MCwic2Vzc2lvbl9zdGF0ZSI6IjcwOTZhYzdiLWY2MzQtNGI5Zi1hY2M3LTIxYmVjZjcwYWM4YyIsImFjciI6IjEiLCJjbGllbnRfc2Vzc2lvbiI6IjcxOWYxMGE3LTk4MTYtNDcyNC1iNmNjLTQ4NTgzNGYyNDliZSIsImFsbG93ZWQtb3JpZ2lucyI6WyJodHRwOi8vbG9jYWxob3N0OjgwODAiLCJodHRwOi8vbG9jYWxob3N0OjQyMDAiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIm1vZGVyYWRvciIsImFkbWluIiwidXN1YXJpbyJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJ2aWV3LXByb2ZpbGUiXX19LCJuYW1lIjoiQWRtaW5pc3RyYWRvciAiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhZG1pbiIsImdpdmVuX25hbWUiOiJBZG1pbmlzdHJhZG9yIiwiZW1haWwiOiJhZG1pbkBnZW9jYWIuY29tLmJyIn0.QX1Cy_FYCFbZ1XmLuLVvsPPX1v7Uyyz2438xi__OqyPk4_ualoooSvMIdnJ07qnRe8F-tPUo9bx-K__v7tWN7s8i8e6DqtKAs6PInqFhZg4kbGAN7CZcr0hjZvMaXfjDmbsRRtmg6Z8YOWxb-VUTIcKdQGLjKg1gx_tDgYSiO6iFPVlaJtvxT4q2CScMoDTgHDWMX4d8zTyD3hG1q8qAyfscIDd_WQfbyST6CfI4rBAWbdbzBqZnN0nN9NVFzb34BsPTL68TGfaSdTEjJpuFBgSzVMmlyGGE9MFTIxsTHS3ueDHiLMdJBA3g6CTaWPcBNt7QLT2Lo12sneCA1IhexA";
        let headers = new Headers({
            "Authorization": token,
            "Content-Type": "application/json"
        });
        let options = new RequestOptions({ headers: headers });

        return this.http.get("http://localhost:8080/api/data-source", options)
            .toPromise()
            .then(res => this.extractData(res))
            .catch(res => this.handleError(res));
    }

    private extractData(res: Response): DataSource[] {
        let body = res.json();
        return body.map(this.toDataSource);
    }

    private toDataSource(p:any): DataSource {
        let dsTypeStr: string = p.serviceType;
        var dataServiceType = DataSourceType[dsTypeStr];

        let dataSource = <DataSource>({
            name: p.name,
            url: p.url,
            serviceType: dataServiceType
        });
        return dataSource;
    }

    private handleError (error: Response | any) {
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