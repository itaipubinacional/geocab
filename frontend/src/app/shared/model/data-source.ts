export enum DataSourceType {
    WMS,
    WFS
}

export class DataSource {
    name:string;
    type:DataSourceType;
    address:string;

    constructor(name:string, type:DataSourceType, address:string) {
        this.name = name;
        this.type = type;
        this.address = address;
    }
}
