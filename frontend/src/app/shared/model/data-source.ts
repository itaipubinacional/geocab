export enum DataSourceType {
    WMS,
    WFS
}

export class DataSource {
    name:string;
    serviceType:DataSourceType;
    url:string;

    constructor(name:string, serviceType:DataSourceType, url:string) {
        this.name = name;
        this.serviceType = serviceType;
        this.url = url;
    }
}
