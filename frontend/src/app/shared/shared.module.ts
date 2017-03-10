import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavComponent} from "./nav/nav.component";
import {BrowserModule} from "@angular/platform-browser";
import {RouterModule} from "@angular/router";

@NgModule({
    imports: [CommonModule, BrowserModule, RouterModule],
    exports: [NavComponent, CommonModule, BrowserModule],
    declarations: [NavComponent]
})
export class SharedModule {
}
