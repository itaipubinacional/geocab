<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Access Group - Form -->
<div>
    <form novalidate name="form"
          default-button="{{ (currentState == INSERT_STATE) && 'buttonInsert' || 'buttonUpdate' }}">

        <div class="content-tab">
            <tabset>
                <tab heading="<spring:message code="admin.access-group.Group"/>">
                    <div style="margin-top: 12px;">
                        <div class="form-item position-relative" style="width: 300px;">
                            <label class="detail-label" required><spring:message code="admin.access-group.Name"/></label>
                            <input name="name" type="text" class="form-control"
                                   ng-model="currentEntity.name"
                                   placeholder="<spring:message code="admin.access-group.Enter-the-name"/>"
                                   required maxlength="144" ng-minlength="1"
                                   ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
                                   autofocus
                                   ng-hover>

                            <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)" class="tooltip-validation"><spring:message code="admin.access-group.Name-Required"/></span>
                        </div>
                        <br>

                        <div class="form-item position-relative" style="width: 500px;">
                            <label class="detail-label" required><spring:message code="admin.access-group.Description"/></label>
                            <textarea ng-model="currentEntity.description" class="form-control"
                                      placeholder="<spring:message code="admin.access-group.Enter-the-description"/>"
                                      name="description"
                                      required ng-minlength="1"
                                      ng-class="{ ngInvalid: form.description.$error.required && (form.$submitted || form.description.$dirty) }"
                                      ng-hover>
                            <span ng-show="form.description.$error.required && (form.$submitted || form.description.$dirty)" class="tooltip-validation"><spring:message code="admin.access-group.Description-required"/></span>
                            </textarea>
                        </div>

                        <div>
                        
                            <!-- State Create -->
                            <button ng-show="currentState == INSERT_STATE"
                                    class="btn btn-success top-space"
                                    ng-click="insertAccessGroup()"><spring:message code="admin.access-group.Save-and-continue"/>
                            </button>
                            
                            <!-- State Edit -->
                            <button ng-show="currentState == UPDATE_STATE"
                                    class="btn btn-success top-space"
                                    ng-click="updateAccessGroup()"><spring:message code="admin.access-group.Save"/>
                            </button>

                            <button ng-show="currentState == INSERT_STATE || currentState == UPDATE_STATE"
                                    class="btn btn-default top-space"
                                    ui-sref="access-group.list"><spring:message code="admin.access-group.Cancel"/>
                            </button>
                        </div>
                    </div>
                </tab>
                <tab heading="<spring:message code="admin.access-group.User"/>" active="usuarioTab" ng-if="currentEntity.id != 1 && currentEntity.id != null">
                    <div style="margin-bottom: 20px; float: right;">
                        <button class="btn btn-primary" type="button" ng-click="associateUsers()"><spring:message code="admin.access-group.Associate-user"/></button>
                        <button class="btn btn-success larger-btn" ng-click="saveAssotiations()"><spring:message code="admin.access-group.Save"/></button>
                    </div>
                    <br style="clear: both">
                    <div ng-grid="gridUsers" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212);">
                    </div>
                    <div>
                        <!-- State Criar -->

                    </div>
                </tab>
                <tab heading="Camadas" ng-if="currentEntity.id != null">
                    <div style="margin-bottom: 20px; float: right;">
                        <button class="btn btn-primary" type="button" ng-click="associateLayer()"><spring:message code="admin.access-group.Associate-layer"/></button>
                        <button class="btn btn-success larger-btn" ng-click="saveAssotiations()"><spring:message code="admin.access-group.Save"/></button>
                    </div>
                    <br style="clear: both">
                    <div ng-grid="gridLayers" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212);">
                    </div>
                </tab>
                <tab heading="Pesquisas Personalizadas" ng-if="currentEntity.id != null">
                    <div style="margin-bottom: 20px; float: right;">
                        <button class="btn btn-primary" ng-click="associateSearch()" type="button"><spring:message code="admin.access-group.Associate-custom-search"/></button>
                        <button class="btn btn-success larger-btn" ng-click="saveAssotiations()"><spring:message code="admin.access-group.Save"/></button>
                    </div>
                    <br style="clear: both">
                    <div ng-grid="gridCustomSearch" style="height: 300px; width: 100%; border: 1px solid rgb(212,212,212);">
                    </div>
                </tab>
                 <tab heading="Ferramentas" ng-if="currentEntity.id != null">
                    <div style="margin-bottom: 20px; float: right;">
                        <button class="btn btn-primary" ng-click="associateTools()" type="button"><spring:message code="admin.access-group.Associate-tools"/></button>
                        <button class="btn btn-success larger-btn" ng-click="saveAssotiations()"><spring:message code="admin.access-group.Save"/></button>
                    </div>
                    <br style="celear: both">
                    
                    <div ng-grid="gridTools" style=" margin-top: 35px; height: 300px; width: 100%; border: 1px solid rgb(212,212,212);">
                    </div>
                </tab>
            </tabset>
        </div>
    </form>
</div>
</html>
