import {BrowserModule} from '@angular/platform-browser';
import {NgModule, APP_INITIALIZER} from '@angular/core';
import {AppComponent} from './app.component';
import {AppRoutingModule} from './app.routes';
import {SharedModule} from './shared/shared.module';
import {CoreModule} from "./core/core.module";
import {ConfigService} from "./core/config.service";
import {UserService} from "./core/user.service";

export function initApp(configService: ConfigService, userService: UserService) {
    /*
     * Tenta carregar a configuração e depois o serviço de usuário
     * (tem que retornar uma função que retorna um promise o.O)
     */
    return () => configService.load()
        .then(() => userService.load());
}

@NgModule({
    declarations: [
        AppComponent
    ],
    imports: [
        CoreModule,
        SharedModule,
        BrowserModule,
        AppRoutingModule
    ],
    providers: [{
        provide: APP_INITIALIZER,
        useFactory: initApp,
        deps: [ConfigService, UserService],
        multi: true
    }],
    bootstrap: [AppComponent]
})
export class AppModule {

    constructor() {
    }
}
