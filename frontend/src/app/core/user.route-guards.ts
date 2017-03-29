import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateChild, Router} from "@angular/router";
import {UserService} from "./user.service";
import {Injectable} from "@angular/core";

/**
 * Classe abstrata que representa o guard que fará a checagem se um usuário
 * pode acessar uma determinada rota do Angular. As  implementações desta classe
 * deverão representar os perfis de usuários utilizados pela aplicação.
 */
export abstract class AbstractUserRouteGuard implements CanActivate, CanActivateChild {

    constructor(protected userService: UserService, protected router: Router) {
    }

    /**
     * Verifica se o usuário está autenticado. Caso não estiver, redireciona
     * para o servidor de autenticação.
     * @returns {boolean} Retorna <code>true</code> se o usuário estiver
     * autenticado. Caso contrário, retorna <code>false</code> e redireciona
     * para a página de login do servidor de autenticação.
     */
    private authenticatedOrRedirect(): boolean {
        // se não estiver autenticado redireciona para a página de login
        if (!this.userService.authenticated) {
            this.userService.login();
            return false;
        }

        return true;
    }

    canActivate(activatedRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        // verifica se o usuário está autenticado
        if (!this.authenticatedOrRedirect())
            return false;

        // libera a rota caso o usuário possuir a atribuição desejada
        let hasRole = this.userService.hasRole(this.userRole);
        if (!hasRole)
            // redireciona para a página principal caso não possuir a role
            this.router.navigate(['/']);

        return hasRole;
    }

    canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        return this.canActivate(childRoute, state);
    }

    /**
     * Retorna a atribuição do usuário a ser checada por este guard.
     */
    abstract get userRole(): string;
}

/**
 * Classe que representa o guard que verificará se o usuário possui o
 * perfil padrão ao acessar uma determinada rota.
 */
@Injectable()
export class UserRouteGuard extends AbstractUserRouteGuard {
    constructor(protected userService: UserService, protected router: Router) {
        super(userService, router);
    }

    /**
     * @inheritDoc
     */
    get userRole(): string {
        return "usuario";
    }
}

/**
 * Classe que representa o guard que verificará se o usuário possui o
 * perfil de moderador ao acessar uma determinada rota.
 */
@Injectable()
export class ModeratorUserRouteGuard extends AbstractUserRouteGuard {
    constructor(protected userService: UserService, protected router: Router) {
        super(userService, router);
    }

    /**
     * @inheritDoc
     */
    get userRole(): string {
        return "moderador";
    }
}

/**
 * Classe que representa o guard que verificará se o usuário possui o
 * perfil de administrador ao acessar uma determinada rota.
 */
@Injectable()
export class AdminUserRouteGuard extends AbstractUserRouteGuard {
    constructor(protected userService: UserService, protected router: Router) {
        super(userService, router);
    }

    /**
     * @inheritDoc
     */
    get userRole(): string {
        return "admin";
    }
}
