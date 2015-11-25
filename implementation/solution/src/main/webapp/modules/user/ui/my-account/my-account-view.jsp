<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Data source - Main View -->
<div>
    <div class="navbar">

        <!--Mensagens-->

        <div class="alert"
             ng-class="{'alert-dismissable': msg.dismiss, 'danger' : (msg.type == 'danger'), 'info' : (msg.type == 'info'), 'warning' : (msg.type == 'warning'), 'success' : (msg.type == 'success')}"
             ng-show="msg != null">
            <button type="button" class="close" ng-click="close()" aria-hidden="true">&times;</button>
            {{msg.text}}
        </div>

        <div class="navbar-inner navbar-container row" >
            <div class="col-md-5">
                <div class="navbar-title">
                    <p style="text-transform:uppercase;cursor:pointer;" class="ng-scope" ng-click="changeForm('my-account.form')">
                        <spring:message code="admin.my-account.My-account"/>
                    </p>
                </div>
                <!--<div class="navbar-title">
                    <p style="text-transform:uppercase;cursor:pointer;" class="ng-scope" ng-click="changeForm('my-preferences.form')">
                        <spring:message code="admin.users.Account-preferences"/>
                    </p>
                </div>-->
            </div>
            <button ng-show="currentState == UPDATE_STATE || currentState== 'my-preferences.form'" style="float: right;"
                    class="btn btn-success"
                    id="buttonUpdate"
                    title="<spring:message code='admin.users.Save'/>"
                    ng-click="updateUser()">
                <spring:message code="admin.users.Save"/>
            </button>
        </div>
    </div>

    <tabset>
        <tab heading="<spring:message code='admin.users.Account-information'/>">
            <div ng-include="'modules/user/ui/my-account/my-account-form.jsp'"></div>
        </tab>
        <tab heading="<spring:message code='admin.users.Account-preferences'/>">
            <div ng-include="'modules/user/ui/my-account/my-preferences-form.jsp'"></div>
        </tab>
    </tabset>
    <!-- Partial views of states -->
    <!--<div ng-switch on="currentState">
        <div ng-switch-when="my-account.form">
            <div ng-include="'modules/user/ui/my-account/my-account-form.jsp'"></div>
        </div>
        <div ng-switch-when="my-preferences.form">
            <div ng-include="'modules/user/ui/my-account/my-preferences-form.jsp'"></div>
        </div>
        <div ng-switch-default>
            <div ng-include="'modules/loading.html'"></div>
        </div>
    </div>-->
</div>
</html>