import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {NavComponent} from "./nav/nav.component";
import {RouterModule} from "@angular/router";
import {HttpModule} from "@angular/http";
import {TreeViewComponent} from "./tree-view/tree-view.component";
import {LogoComponent} from "./logo/logo.component";
import {ConfirmModalDialogComponent} from "./confirm-modal-dialog/confirm-modal-dialog.component";
import {ModalWindowComponent} from "./modal-window/modal-window.component";
import {DefaultLayoutComponent} from './default-layout/default-layout.component';
import {ToolbarComponent} from './toolbar/toolbar.component';

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
        ConfirmModalDialogComponent,
        ModalWindowComponent,
        DefaultLayoutComponent,
        ToolbarComponent
    ],
    declarations: [
        NavComponent,
        TreeViewComponent,
        LogoComponent,
        ConfirmModalDialogComponent,
        ModalWindowComponent,
        DefaultLayoutComponent,
        ToolbarComponent
    ]
})
export class SharedModule {
}
