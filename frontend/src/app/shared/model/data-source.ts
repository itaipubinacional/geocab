export enum DataSourceType {
    WMS = <any>"WMS",
    WFS = <any>"WFS"
}

export class DataSource {
    id:number;
    name:string;
    serviceType:DataSourceType;
    url:string;
    username:string;
    password:string;
}
