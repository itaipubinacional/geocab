import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {TreeViewComponent} from "./tree-view/tree-view.component";
import {LogoComponent} from "./logo/logo.component";
import {ConfirmModalDialogComponent} from "./confirm-modal-dialog/confirm-modal-dialog.component";
import {ModalWindowComponent} from "./modal-window/modal-window.component";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        HttpModule
    ],
    exports: [
        CommonModule,
        RouterModule,
        HttpModule,
        NavComponent,
        TreeViewComponent,
        LogoComponent,
        ConfirmModalDialogComponent
    ],
    declarations: [
        NavComponent,
        TreeViewComponent,
        LogoComponent,
        ConfirmModalDialogComponent,
        ModalWindowComponent
    ]
})
export class SharedModule {
}
