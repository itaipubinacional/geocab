import {Injectable} from "@angular/core";
import {OAuthService} from "angular-oauth2-oidc";
import {Headers} from "@angular/http";
import {ConfigService} from "./config.service";
import {isUndefined} from "util";

/**
 * Classe que contém os serviços relacionados ao usuário do sistema. Os
 * serviços fornecidos por esta classe permitem:
 * <ul>
 *     <li>Iniciar/finalizar a autorização do usuário para utilização do sistema;</li>
 *     <li>Retornar informações sobre o usuário (nome, e-mail, etc...);</li>
 *     <li>Informar se o usuário possui autorização para acessar determinada fincionalidade;</li>
 *     <li>Montar os headers necessários para chamar os serviços autenticados do backend.</li>
 * </ul>
 * Um objeto desta classe deverá ser injetado onde necessário via o contêiner de injeção
 * de dependência do próprio Angular.
 */
@Injectable()
export class UserService {

    constructor(private oauthService: OAuthService, private configService: ConfigService) {
    }

    /**
     * Função que carrega o serviço de usuário. Esta função deve ser chamada antes que
     * seja realizada quaisquer operação sobre o sistema.
     */
    load() {
        /*
         * A configuração do serviço do OAuth2 deve ser realizada antes que
         * qualquer componentente/serviço da aplicação seja instanciado.
         */
        // login-Url
        this.oauthService.loginUrl = this.configService.config.auth.userAuthorizationUrl;

        this.oauthService.clientId = this.configService.config.auth.clientId;
        this.oauthService.scope = '';
        this.oauthService.oidc = true;
        this.oauthService.setStorage(localStorage);

        this.oauthService.tokenEndpoint = this.configService.config.auth.accessTokenUrl;
        this.oauthService.userinfoEndpoint = this.configService.config.auth.userInfoUrl;

        // a URL de logout é opcional no backend
        if (!isUndefined(this.configService.config.auth.logoutUrl))
            this.oauthService.logoutUrl = this.configService.config.auth.logoutUrl;

        this.oauthService.tryLogin({
            validationHandler: context => {
                /*
                 * Tenta carregar as informações do usuário. Caso não conseguir é porque
                 * não está autenticado.
                 */
                return this.oauthService.loadUserProfile();
            }
        });
    }

    /**
     * Função que inicia o processo de autenticação de um usuário. O
     * comportamento deste método depende do servidor de autenticação
     * que estiver configurado no sistema.
     */
    login(): void {
        this.oauthService.redirectUri = window.location.href;
        this.oauthService.initImplicitFlow();
    }

    /**
     * Função que desloga o usuário atual.
     */
    logout(): void {
        this.oauthService.redirectUri = window.location.origin;
        this.oauthService.logOut();
    }

    /**
     * Cria um header com as informações de autorização do usuário logado.
     * @returns {Headers} O objeto do header criado com as informações de
     * autorização. Caso o usuário não estiver logado, será retornado um
     * header vazio.
     */
    createAuthorizationHeaders(): Headers {
        let headers = new Headers();
        this.appendAuthorizationHeaders(headers);
        return headers;
    }

    /**
     * Adiciona as informações de autorização do usuário logado em um header
     * previamente alocado.
     * @param headers O objeto do header previamente alocado. Caso o usuário
     * não estiver logado o objeto do header não será alterado.
     */
    appendAuthorizationHeaders(headers: Headers): void {
        // se o usuário estiver logado
        if (this.authenticated) {
            let token: string = this.oauthService.getAccessToken();
            headers.append('Authorization', `Bearer ${token}`);
        }
    }

    /**
     * Informa se o usuário possui a atribuição passada no parâmetro.
     * @param role A string com a identificação da atribuição.
     * @returns {boolean} Retorna <code>true</code> caso o usuário possuir a
     * atribuição. Caso contrário, retorna <code>false</code>.
     */
    hasRole(role: string): boolean {
        /*
         * O Spring requer que o objeto retornado do user_info tenha o claim
         * authorities com a lista de roles do usuário com o prefixo 'ROLE_'.
         */
        let claims = this.oauthService.getIdentityClaims();
        if (!claims && !('authorities' in claims)) return false;

        let authoritiesClaim = claims.authorities;

        // checa se o usuário possui a role desejada
        return authoritiesClaim.indexOf(`ROLE_${role}`) > -1;
    }

    /**
     * Informa se o usuário está autenticado.
     * @returns {boolean} Retorna <code>true</code> se o usuário estiver
     * autenticado. Caso contrário, retorna <code>false</code>.
     */
    get authenticated(): boolean {
        return this.oauthService.hasValidAccessToken();
    }

    /**
     * Retorna o nome do usuário autenticado.
     * @returns {string} O nome do usuário autenticado. Caso o servidor de
     * autenticação não estiver configurado para fornecer esta informação
     * será retornado <code>null</code>.
     */
    get name(): string {
        let claims = this.oauthService.getIdentityClaims();
        if (!claims) return null;
        return claims.given_name;
    }

}
