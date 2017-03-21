webpackJsonp([1,7],{

/***/ 1016:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__list_data_sources_list_data_sources_component__ = __webpack_require__(1022);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__data_source_routes__ = __webpack_require__(1031);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__data_source_component__ = __webpack_require__(1020);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__detail_data_source_detail_data_source_component__ = __webpack_require__(1021);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__angular_forms__ = __webpack_require__(42);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__shared_shared_module__ = __webpack_require__(508);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "DataSourceModule", function() { return DataSourceModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};







var DataSourceModule = (function () {
    function DataSourceModule() {
    }
    DataSourceModule = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            imports: [
                __WEBPACK_IMPORTED_MODULE_6__shared_shared_module__["a" /* SharedModule */],
                __WEBPACK_IMPORTED_MODULE_5__angular_forms__["a" /* FormsModule */],
                __WEBPACK_IMPORTED_MODULE_2__data_source_routes__["a" /* DataSourceRoutingModule */]
            ],
            declarations: [
                __WEBPACK_IMPORTED_MODULE_3__data_source_component__["a" /* DataSourceComponent */],
                __WEBPACK_IMPORTED_MODULE_1__list_data_sources_list_data_sources_component__["a" /* ListDataSourcesComponent */],
                __WEBPACK_IMPORTED_MODULE_4__detail_data_source_detail_data_source_component__["a" /* DetailDataSourceComponent */]
            ]
        }), 
        __metadata('design:paramtypes', [])
    ], DataSourceModule);
    return DataSourceModule;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/data-source.module.js.map

/***/ }),

/***/ 1019:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__shared_model_data_source__ = __webpack_require__(1029);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_http__ = __webpack_require__(95);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_toPromise__ = __webpack_require__(510);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_toPromise___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_rxjs_add_operator_toPromise__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__shared_user_service__ = __webpack_require__(192);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DataSourceService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};





var DataSourceService = (function () {
    function DataSourceService(http, userService) {
        this.http = http;
        this.userService = userService;
    }
    DataSourceService.prototype.getDataSources = function () {
        var _this = this;
        // cria o header de autorização
        var headers = this.userService.createAuthorizationHeaders();
        var options = new __WEBPACK_IMPORTED_MODULE_2__angular_http__["RequestOptions"]({ headers: headers });
        return this.http.get("http://localhost:8080/api/data-source", options)
            .toPromise()
            .then(function (res) { return res.json(); }) // o objeto tem um enum
            .catch(function (res) { return _this.handleError(res); });
    };
    DataSourceService.prototype.getDataSourceById = function (id) {
        return new Promise(function (resolve) {
            resolve({
                id: id,
                name: "teste",
                serviceType: __WEBPACK_IMPORTED_MODULE_1__shared_model_data_source__["a" /* DataSourceType */].WMS,
                url: "teste"
            });
        });
    };
    DataSourceService.prototype.createDataSource = function (dataSource) {
        var _this = this;
        // cria o header de autorização
        var headers = this.userService.createAuthorizationHeaders();
        var options = new __WEBPACK_IMPORTED_MODULE_2__angular_http__["RequestOptions"]({ headers: headers });
        return this.http.post("http://localhost:8080/api/data-source", dataSource, options)
            .toPromise()
            .then(function (res) { return res.json(); }) // o objeto tem um enum
            .catch(function (res) { return _this.handleError(res); });
    };
    DataSourceService.prototype.handleError = function (error) {
        // In a real world app, we might use a remote logging infrastructure
        var errMsg;
        if (error instanceof __WEBPACK_IMPORTED_MODULE_2__angular_http__["Response"]) {
            var body = error.json() || '';
            var err = body.error || JSON.stringify(body);
            errMsg = error.status + " - " + (error.statusText || '') + " " + err;
        }
        else {
            errMsg = error.message ? error.message : error.toString();
        }
        console.error(errMsg);
        return Promise.reject(errMsg);
    };
    DataSourceService = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Injectable"])(), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__angular_http__["Http"] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_2__angular_http__["Http"]) === 'function' && _a) || Object, (typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_4__shared_user_service__["a" /* UserService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_4__shared_user_service__["a" /* UserService */]) === 'function' && _b) || Object])
    ], DataSourceService);
    return DataSourceService;
    var _a, _b;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/data-source.service.js.map

/***/ }),

/***/ 1020:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__data_source_service__ = __webpack_require__(1019);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DataSourceComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var DataSourceComponent = (function () {
    function DataSourceComponent() {
    }
    DataSourceComponent.prototype.ngOnInit = function () {
    };
    DataSourceComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-data-source',
            template: __webpack_require__(1042),
            styles: [__webpack_require__(1034)],
            providers: [__WEBPACK_IMPORTED_MODULE_1__data_source_service__["a" /* DataSourceService */]]
        }), 
        __metadata('design:paramtypes', [])
    ], DataSourceComponent);
    return DataSourceComponent;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/data-source.component.js.map

/***/ }),

/***/ 1021:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_router__ = __webpack_require__(191);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__data_source_service__ = __webpack_require__(1019);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__shared_model_data_source__ = __webpack_require__(1029);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DetailDataSourceComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var DetailDataSourceComponent = (function () {
    function DetailDataSourceComponent(activatedRoute, router, dataSourceService) {
        this.activatedRoute = activatedRoute;
        this.router = router;
        this.dataSourceService = dataSourceService;
        this.dataSourceTypes = Object.keys(__WEBPACK_IMPORTED_MODULE_3__shared_model_data_source__["a" /* DataSourceType */]);
    }
    DetailDataSourceComponent.prototype.ngOnInit = function () {
        var _this = this;
        // verifica se é para criar ou editar uma fonte de dados
        var requestType = this.activatedRoute.snapshot.params['id'];
        if (requestType != 'new') {
            // faz a leitura
            var id = parseInt(requestType, 10);
            this.dataSourceService.getDataSourceById(id)
                .then(function (ds) { return _this.model = ds; });
        }
        else
            this.model = new __WEBPACK_IMPORTED_MODULE_3__shared_model_data_source__["b" /* DataSource */]();
    };
    DetailDataSourceComponent.prototype.onSubmit = function () {
        var _this = this;
        // salva o datasources
        this.dataSourceService.createDataSource(this.model)
            .then(function () { return _this.router.navigate(['/data-source']); })
            .catch(function (error) { return alert(error); });
    };
    DetailDataSourceComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-detail-data-source',
            template: __webpack_require__(1043),
            styles: [__webpack_require__(1035)]
        }), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* ActivatedRoute */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_1__angular_router__["a" /* ActivatedRoute */]) === 'function' && _a) || Object, (typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* Router */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_1__angular_router__["b" /* Router */]) === 'function' && _b) || Object, (typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_2__data_source_service__["a" /* DataSourceService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_2__data_source_service__["a" /* DataSourceService */]) === 'function' && _c) || Object])
    ], DetailDataSourceComponent);
    return DetailDataSourceComponent;
    var _a, _b, _c;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/detail-data-source.component.js.map

/***/ }),

/***/ 1022:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__data_source_service__ = __webpack_require__(1019);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ListDataSourcesComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var ListDataSourcesComponent = (function () {
    function ListDataSourcesComponent(dataSourceService) {
        this.dataSourceService = dataSourceService;
        this.dataSources = [];
    }
    ListDataSourcesComponent.prototype.ngOnInit = function () {
        var _this = this;
        // pega os datasources
        this.dataSourceService.getDataSources()
            .then(function (ds) { return _this.dataSources = ds; }, function (error) {
            alert(error);
        });
    };
    ListDataSourcesComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-list-data-sources',
            template: __webpack_require__(1044),
            styles: [__webpack_require__(1036)]
        }), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_1__data_source_service__["a" /* DataSourceService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_1__data_source_service__["a" /* DataSourceService */]) === 'function' && _a) || Object])
    ], ListDataSourcesComponent);
    return ListDataSourcesComponent;
    var _a;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/list-data-sources.component.js.map

/***/ }),

/***/ 1029:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DataSourceType; });
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "b", function() { return DataSource; });
var DataSourceType;
(function (DataSourceType) {
    DataSourceType[DataSourceType["WMS"] = "WMS"] = "WMS";
    DataSourceType[DataSourceType["WFS"] = "WFS"] = "WFS";
})(DataSourceType || (DataSourceType = {}));
var DataSource = (function () {
    function DataSource() {
    }
    return DataSource;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/data-source.js.map

/***/ }),

/***/ 1031:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_router__ = __webpack_require__(191);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__list_data_sources_list_data_sources_component__ = __webpack_require__(1022);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__data_source_component__ = __webpack_require__(1020);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__detail_data_source_detail_data_source_component__ = __webpack_require__(1021);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__shared_user_route_guards__ = __webpack_require__(509);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DataSourceRoutingModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






var routes = [
    {
        path: '',
        component: __WEBPACK_IMPORTED_MODULE_3__data_source_component__["a" /* DataSourceComponent */],
        canActivateChild: [__WEBPACK_IMPORTED_MODULE_5__shared_user_route_guards__["a" /* AdminUserRouteGuard */]],
        children: [
            { path: '', component: __WEBPACK_IMPORTED_MODULE_1__list_data_sources_list_data_sources_component__["a" /* ListDataSourcesComponent */] },
            { path: ':id', component: __WEBPACK_IMPORTED_MODULE_4__detail_data_source_detail_data_source_component__["a" /* DetailDataSourceComponent */] },
            { path: 'new', component: __WEBPACK_IMPORTED_MODULE_4__detail_data_source_detail_data_source_component__["a" /* DetailDataSourceComponent */] }
        ]
    },
];
var DataSourceRoutingModule = (function () {
    function DataSourceRoutingModule() {
    }
    DataSourceRoutingModule = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_2__angular_core__["NgModule"])({
            imports: [__WEBPACK_IMPORTED_MODULE_0__angular_router__["c" /* RouterModule */].forChild(routes)],
            exports: [__WEBPACK_IMPORTED_MODULE_0__angular_router__["c" /* RouterModule */]]
        }), 
        __metadata('design:paramtypes', [])
    ], DataSourceRoutingModule);
    return DataSourceRoutingModule;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/data-source.routes.js.map

/***/ }),

/***/ 1034:
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ 1035:
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ 1036:
/***/ (function(module, exports) {

module.exports = "table[md-data-table] {\r\n    width: 100%;\r\n    text-align: left;\r\n    border: 0;\r\n    border-collapse: collapse;\r\n}\r\n\r\n/* regras para o header */\r\ntable[md-data-table] th {\r\n    color: rgba(0,0,0,0.54);\r\n    height: 56px;\r\n    font-size: 0.9em;\r\n    padding-left: 56px;\r\n}\r\n\r\ntable[md-data-table] th:first-child {\r\n    padding-left: 0;\r\n}\r\n\r\ntable[md-data-table] th:nth-child(2) {\r\n    padding-left: 24px;\r\n}\r\n\r\n/* regras para o corpo da tabela */\r\ntable[md-data-table] tbody tr:hover {\r\n    background-color: #eeeeee;\r\n}\r\n\r\ntable[md-data-table] tbody tr {\r\n    border-top: 1px solid rgba(0,0,0,0.12);\r\n}\r\n\r\ntable[md-data-table] td {\r\n    color: rgba(0,0,0,0.87);\r\n    height: 48px;\r\n    padding-left: 56px;\r\n}\r\n\r\ntable[md-data-table] td:first-child {\r\n    padding-left: 0;\r\n}\r\n\r\ntable[md-data-table] td:nth-child(2) {\r\n    padding-left: 24px;\r\n}\r\n\r\ntable[md-data-table] td[md-number] {\r\n    text-align: right;\r\n}\r\n"

/***/ }),

/***/ 1042:
/***/ (function(module, exports) {

module.exports = "<md-sidenav-container class=\"viewport\">\r\n    <md-sidenav #sidenav mode=\"side\" opened=\"true\">\r\n        <app-nav></app-nav>\r\n    </md-sidenav>\r\n\r\n    <router-outlet></router-outlet>\r\n</md-sidenav-container>"

/***/ }),

/***/ 1043:
/***/ (function(module, exports) {

module.exports = "<h1>Nova Fonte de Dados Geográficos</h1>\r\n<hr/>\r\n<form (ngSubmit)=\"onSubmit()\" target=\"_parent\">\r\n    <div class=\"pull-right\">\r\n        <button type=\"submit\" class=\"btn btn-success\">Salvar</button>\r\n        <button type=\"button\" class=\"btn btn-warning\">Testar conexão</button>\r\n    </div>\r\n\r\n    <div class=\"content\">\r\n        <div class=\"form-group\">\r\n            <label for=\"name\">Nome</label>\r\n            <input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" required [(ngModel)]=\"model.name\">\r\n        </div>\r\n\r\n        <div class=\"checkbox\">\r\n            <label>\r\n                <input type=\"checkbox\" name=\"externalDataSourceCheckbox\" [(ngModel)]=\"externalDataSource\">\r\n                Fonte de dados externa\r\n            </label>\r\n        </div>\r\n\r\n        <div [hidden]=\"!externalDataSource\">\r\n            <div class=\"form-group\">\r\n                <label for=\"serviceType\">Tipo de serviço</label>\r\n                <select class=\"form-control\" id=\"serviceType\" name=\"serviceType\" [(ngModel)]=\"model.serviceType\">\r\n                    <option *ngFor=\"let dsType of dataSourceTypes\" [value]=\"dsType\">{{dsType}}</option>\r\n                </select>\r\n            </div>\r\n\r\n            <div class=\"form-group\">\r\n                <label for=\"url\">Endereço</label>\r\n                <input type=\"text\" class=\"form-control\" id=\"url\" name=\"url\" [(ngModel)]=\"model.url\">\r\n            </div>\r\n\r\n            <div class=\"checkbox\">\r\n                <label>\r\n                    <input type=\"checkbox\" name=\"authenticationRequiredCheckbox\" [(ngModel)]=\"authenticationRequired\">\r\n                    Autenticação obrigatória\r\n                </label>\r\n            </div>\r\n\r\n            <div [hidden]=\"!authenticationRequired\">\r\n                <div class=\"form-group\">\r\n                    <label for=\"username\">Usuário</label>\r\n                    <input type=\"text\" class=\"form-control\" id=\"username\" name=\"username\" [(ngModel)]=\"model.login\">\r\n                </div>\r\n                <div class=\"form-group\">\r\n                    <label for=\"password\">Senha</label>\r\n                    <input type=\"password\" class=\"form-control\" id=\"password\" name=\"password\" [(ngModel)]=\"model.password\">\r\n                </div>\r\n            </div>\r\n        </div>\r\n    </div>\r\n</form>"

/***/ }),

/***/ 1044:
/***/ (function(module, exports) {

module.exports = "<md-toolbar color=\"primary\" class=\"mat-elevation-z4\">\r\n    <span>Fontes de Dados Geográficos</span>\r\n    <span spacer></span>\r\n\r\n</md-toolbar>\r\n\r\n<section class=\"screen-actions\">\r\n    <a md-fab routerLink=\"/data-source/new\">\r\n        <md-icon>add</md-icon>\r\n    </a>\r\n</section>\r\n\r\n<section class=\"screen-content\">\r\n    <table md-data-table>\r\n        <thead>\r\n        <tr>\r\n            <th>Nome</th>\r\n            <th>Tipo</th>\r\n            <th>Endereço</th>\r\n        </tr>\r\n        </thead>\r\n        <tbody>\r\n        <tr *ngFor=\"let datasource of dataSources\">\r\n            <td>{{datasource.name}}</td>\r\n            <td>{{datasource.serviceType}}</td>\r\n            <td>{{datasource.url}}</td>\r\n        </tr>\r\n        </tbody>\r\n    </table>\r\n</section>"

/***/ })

});
//# sourceMappingURL=1.bundle.map