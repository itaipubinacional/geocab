import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {FrontEndConfiguration} from "./config";

/**
 * Classe que contém as configurações do front-end. Estas configurações são
 * consultadas do back-end.
 */
@Injectable()
export class ConfigService {

    /**
     * Variável que armazena as configurações do front-end.
     */
    config: FrontEndConfiguration;

    constructor(private http: Http) {
    }

    /**
     * Função que carrega a configuração do front-end a partir do back-end. Esta
     * deverá ser chamada antes que o sistema realize algum tipo de processamento.
     */
    load() {
        // faz a requisição para o backend para este retornar a configuração
        return this.http.get("/api/.config")
            .toPromise()
            .then(res => {
                this.config = res.json();
                return res;
            });
    }
}
