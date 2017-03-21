webpackJsonp([0,7],{

/***/ 1017:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__list_layer_group_list_layer_group_component__ = __webpack_require__(1026);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__detail_layer_group_detail_layer_group_component__ = __webpack_require__(1023);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__layer_group_routes__ = __webpack_require__(1032);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__layer_group_component__ = __webpack_require__(1024);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__shared_shared_module__ = __webpack_require__(508);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "LayerGroupModule", function() { return LayerGroupModule; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};






var LayerGroupModule = (function () {
    function LayerGroupModule() {
    }
    LayerGroupModule = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["NgModule"])({
            imports: [
                __WEBPACK_IMPORTED_MODULE_5__shared_shared_module__["a" /* SharedModule */],
                __WEBPACK_IMPORTED_MODULE_3__layer_group_routes__["a" /* LayerGroupRoutingModule */]
            ],
            declarations: [
                __WEBPACK_IMPORTED_MODULE_4__layer_group_component__["a" /* LayerGroupComponent */],
                __WEBPACK_IMPORTED_MODULE_1__list_layer_group_list_layer_group_component__["a" /* ListLayerGroupComponent */],
                __WEBPACK_IMPORTED_MODULE_2__detail_layer_group_detail_layer_group_component__["a" /* DetailLayerGroupComponent */]
            ]
        }), 
        __metadata('design:paramtypes', [])
    ], LayerGroupModule);
    return LayerGroupModule;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/layer-group.module.js.map

/***/ }),

/***/ 1023:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__shared_model_layer_group__ = __webpack_require__(1030);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__layer_group_service__ = __webpack_require__(1025);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__angular_router__ = __webpack_require__(191);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return DetailLayerGroupComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var DetailLayerGroupComponent = (function () {
    function DetailLayerGroupComponent(activatedRoute, router, layerGroupService) {
        this.activatedRoute = activatedRoute;
        this.router = router;
        this.layerGroupService = layerGroupService;
    }
    DetailLayerGroupComponent.prototype.ngOnInit = function () {
        var _this = this;
        // verifica se é para criar ou editar um group de camadas
        var requestType = this.activatedRoute.snapshot.params['id'];
        if (requestType != 'new') {
            // faz a leitura
            var id = parseInt(requestType, 10);
            this.layerGroupService.getLayerGroupById(id)
                .then(function (ds) { return _this.model = ds; });
        }
        else
            this.model = new __WEBPACK_IMPORTED_MODULE_0__shared_model_layer_group__["a" /* LayerGroup */]();
    };
    DetailLayerGroupComponent.prototype.onSubmit = function () {
        var _this = this;
        // salva o grupo de camadas
        this.layerGroupService.createLayerGroup(this.model)
            .then(function () { return _this.router.navigate(['/layer-group']); })
            .catch(function (error) { return alert(error); });
    };
    DetailLayerGroupComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_2__angular_core__["Component"])({
            selector: 'app-detail-layer-group',
            template: __webpack_require__(1045),
            styles: [__webpack_require__(1037)],
            providers: [__WEBPACK_IMPORTED_MODULE_1__layer_group_service__["a" /* LayerGroupService */]]
        }), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["a" /* ActivatedRoute */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_3__angular_router__["a" /* ActivatedRoute */]) === 'function' && _a) || Object, (typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__angular_router__["b" /* Router */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_3__angular_router__["b" /* Router */]) === 'function' && _b) || Object, (typeof (_c = typeof __WEBPACK_IMPORTED_MODULE_1__layer_group_service__["a" /* LayerGroupService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_1__layer_group_service__["a" /* LayerGroupService */]) === 'function' && _c) || Object])
    ], DetailLayerGroupComponent);
    return DetailLayerGroupComponent;
    var _a, _b, _c;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/detail-layer-group.component.js.map

/***/ }),

/***/ 1024:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_core__ = __webpack_require__(0);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LayerGroupComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};

var LayerGroupComponent = (function () {
    function LayerGroupComponent() {
    }
    LayerGroupComponent.prototype.ngOnInit = function () {
    };
    LayerGroupComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_0__angular_core__["Component"])({
            selector: 'app-layer-group',
            template: __webpack_require__(1046),
            styles: [__webpack_require__(1038)]
        }), 
        __metadata('design:paramtypes', [])
    ], LayerGroupComponent);
    return LayerGroupComponent;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/layer-group.component.js.map

/***/ }),

/***/ 1025:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__shared_model_layer_group__ = __webpack_require__(1030);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_http__ = __webpack_require__(95);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__shared_user_service__ = __webpack_require__(192);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LayerGroupService; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};




var LayerGroupService = (function () {
    function LayerGroupService(http, userService) {
        this.http = http;
        this.userService = userService;
    }
    LayerGroupService.prototype.getLayerGroupById = function (id) {
        return Promise.resolve(new __WEBPACK_IMPORTED_MODULE_0__shared_model_layer_group__["a" /* LayerGroup */]());
    };
    LayerGroupService.prototype.createLayerGroup = function (layerGroup) {
        var _this = this;
        // cria o header de autorização
        var headers = this.userService.createAuthorizationHeaders();
        var options = new __WEBPACK_IMPORTED_MODULE_2__angular_http__["RequestOptions"]({ headers: headers });
        return this.http.post('http://localhost:8080/api/layer-group', layerGroup, options)
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(function (res) { return _this.handleError(res); });
    };
    LayerGroupService.prototype.getLayerGroups = function () {
        var _this = this;
        // cria o header de autorização
        var headers = this.userService.createAuthorizationHeaders();
        var options = new __WEBPACK_IMPORTED_MODULE_2__angular_http__["RequestOptions"]({ headers: headers });
        return this.http.get('http://localhost:8080/api/layer-group', options)
            .toPromise()
            .then(function (res) { return res.json(); })
            .catch(function (res) { return _this.handleError(res); });
    };
    LayerGroupService.prototype.handleError = function (error) {
        // in a real world app, we might use a remote logging infrastructure
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
    LayerGroupService = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Injectable"])(), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_2__angular_http__["Http"] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_2__angular_http__["Http"]) === 'function' && _a) || Object, (typeof (_b = typeof __WEBPACK_IMPORTED_MODULE_3__shared_user_service__["a" /* UserService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_3__shared_user_service__["a" /* UserService */]) === 'function' && _b) || Object])
    ], LayerGroupService);
    return LayerGroupService;
    var _a, _b;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/layer-group.service.js.map

/***/ }),

/***/ 1026:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__layer_group_service__ = __webpack_require__(1025);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__angular_core__ = __webpack_require__(0);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return ListLayerGroupComponent; });
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};


var ListLayerGroupComponent = (function () {
    function ListLayerGroupComponent(layerGroupService) {
        this.layerGroupService = layerGroupService;
        this.layerTree = {};
        this.layerTree.data = null;
        this.layerTree.children = [];
    }
    ListLayerGroupComponent.prototype.ngOnInit = function () {
        var _this = this;
        this.layerGroupService.getLayerGroups()
            .then(function (lgs) {
            _this.layerGroups = lgs;
            console.log(_this.layerGroups);
            _this.layerTree.children = _this.convertLayerTree(_this.layerGroups);
            console.log(_this.layerTree);
        }, function (error) { return alert(error); });
    };
    ListLayerGroupComponent.prototype.convertLayerTree = function (layerGroups) {
        var tree = [];
        for (var _i = 0, layerGroups_1 = layerGroups; _i < layerGroups_1.length; _i++) {
            var group = layerGroups_1[_i];
            var node = { children: null, data: null };
            node.data = group;
            if (group.groups !== null)
                node.children = this.convertLayerTree(group.groups);
            tree.push(node);
        }
        return tree;
    };
    ListLayerGroupComponent = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__angular_core__["Component"])({
            selector: 'app-list-layer-group',
            template: __webpack_require__(1047),
            styles: [__webpack_require__(1039)],
            providers: [__WEBPACK_IMPORTED_MODULE_0__layer_group_service__["a" /* LayerGroupService */]]
        }), 
        __metadata('design:paramtypes', [(typeof (_a = typeof __WEBPACK_IMPORTED_MODULE_0__layer_group_service__["a" /* LayerGroupService */] !== 'undefined' && __WEBPACK_IMPORTED_MODULE_0__layer_group_service__["a" /* LayerGroupService */]) === 'function' && _a) || Object])
    ], ListLayerGroupComponent);
    return ListLayerGroupComponent;
    var _a;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/list-layer-group.component.js.map

/***/ }),

/***/ 1030:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LayerGroup; });
var LayerGroup = (function () {
    function LayerGroup() {
    }
    return LayerGroup;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/layer-group.js.map

/***/ }),

/***/ 1032:
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__angular_router__ = __webpack_require__(191);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__list_layer_group_list_layer_group_component__ = __webpack_require__(1026);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__angular_core__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__layer_group_component__ = __webpack_require__(1024);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__shared_user_route_guards__ = __webpack_require__(509);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__detail_layer_group_detail_layer_group_component__ = __webpack_require__(1023);
/* harmony export (binding) */ __webpack_require__.d(__webpack_exports__, "a", function() { return LayerGroupRoutingModule; });
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
        component: __WEBPACK_IMPORTED_MODULE_3__layer_group_component__["a" /* LayerGroupComponent */],
        canActivateChild: [__WEBPACK_IMPORTED_MODULE_4__shared_user_route_guards__["a" /* AdminUserRouteGuard */]],
        children: [
            { path: '', component: __WEBPACK_IMPORTED_MODULE_1__list_layer_group_list_layer_group_component__["a" /* ListLayerGroupComponent */] },
            { path: 'new', component: __WEBPACK_IMPORTED_MODULE_5__detail_layer_group_detail_layer_group_component__["a" /* DetailLayerGroupComponent */] }
        ]
    },
];
var LayerGroupRoutingModule = (function () {
    function LayerGroupRoutingModule() {
    }
    LayerGroupRoutingModule = __decorate([
        __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_2__angular_core__["NgModule"])({
            imports: [__WEBPACK_IMPORTED_MODULE_0__angular_router__["c" /* RouterModule */].forChild(routes)],
            exports: [__WEBPACK_IMPORTED_MODULE_0__angular_router__["c" /* RouterModule */]]
        }), 
        __metadata('design:paramtypes', [])
    ], LayerGroupRoutingModule);
    return LayerGroupRoutingModule;
}());
//# sourceMappingURL=C:/Users/lcvmelo/projetos/geocab/frontend/src/layer-group.routes.js.map

/***/ }),

/***/ 1037:
/***/ (function(module, exports) {

module.exports = ""

/***/ }),

/***/ 1038:
/***/ (function(module, exports) {

module.exports = ".nav-heading {\r\n    border-bottom: 1px solid #eee;\r\n}"

/***/ }),

/***/ 1039:
/***/ (function(module, exports) {

module.exports = "ul[md-treeview] {\r\n    width: 100%;\r\n    text-align: left;\r\n    border: 0;\r\n    border-collapse: collapse;\r\n    padding: 0px 0px 0px 0px;\r\n    margin-top: 0px;\r\n    margin-bottom: 0px;\r\n}\r\n\r\nul[md-treeview-child] {\r\n    width: 100%;\r\n    text-align: left;\r\n    border: 0;\r\n    border-collapse: collapse;\r\n    padding-left: 32px;\r\n}\r\n\r\nli[md-treeview] {\r\n    list-style-type: none;\r\n    color: rgba(0,0,0,0.87);\r\n    padding: 15px 0px 15px 32px\r\n    \r\n}\r\n\r\nli[md-treeview]:hover {\r\n    background-color: #eeeeee;\r\n}\r\n\r\nli[md-treeview] {\r\n    border-top: 1px solid rgba(0,0,0,0.12);\r\n}\r\n"

/***/ }),

/***/ 1045:
/***/ (function(module, exports) {

module.exports = "<!-- \r\n<h1>Novo Grupo de Camadas</h1>\r\n<hr/>\r\n<form (ngSubmit)=\"onSubmit()\" target=\"_parent\">\r\n    <div class=\"panel\">\r\n\t    <div class=\"panel-body\">\r\n\t        <button type=\"button\" class=\"btn btn-warning pull-right\" routerLink=\"/layer-group\">Voltar</button>\r\n\t        <button class=\"btn btn-primary pull-right\" routerLink=\"/layer-group/new\">Salvar</button>\r\n\t    </div>\r\n\t</div>    \r\n\r\n    <div class=\"content\">\r\n        <div class=\"form-group\">\r\n            <label for=\"name\">Nome</label>\r\n            <input type=\"text\" class=\"form-control\" id=\"name\" name=\"name\" required [(ngModel)]=\"model.name\">\r\n        </div>\r\n        \r\n    </div>\r\n</form>\r\n -->"

/***/ }),

/***/ 1046:
/***/ (function(module, exports) {

module.exports = "<md-sidenav-container class=\"viewport\">\r\n    <md-sidenav #sidenav mode=\"side\" opened=\"true\">\r\n        <app-nav></app-nav>\r\n    </md-sidenav>\r\n\r\n    <router-outlet></router-outlet>\r\n</md-sidenav-container>"

/***/ }),

/***/ 1047:
/***/ (function(module, exports) {

module.exports = "<md-toolbar color=\"primary\" class=\"mat-elevation-z4\">\r\n    <span>Grupos de Camadas</span>\r\n    <span spacer></span>\r\n\r\n</md-toolbar>\r\n\r\n<section class=\"screen-actions\">\r\n    <a md-fab routerLink=\"/layer-group/new\">\r\n        <md-icon>add</md-icon>\r\n    </a>\r\n</section>\r\n\r\n<section class=\"screen-content\">\r\n   \r\n    <tree-view [node]=layerTree></tree-view>\r\n\r\n</section>\r\n"

/***/ })

});
//# sourceMappingURL=0.bundle.map