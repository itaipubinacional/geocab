<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<style>
</style>
<!-- My account - Update -->
<div>


    <!--Mensagens-->

    <div class="alert"
         ng-class="{'alert-dismissable': msg.dismiss, 'danger' : (msg.type == 'danger'), 'info' : (msg.type == 'info'), 'warning' : (msg.type == 'warning'), 'success' : (msg.type == 'success')}"
         ng-show="msg != null">
        <button type="button" class="close" ng-click="close()" aria-hidden="true">&times;</button>
        {{msg.text}}
    </div>


    <form name="form" novalidate default-button="buttonUpdate">
        <!-- COORDENATES -->
        <label class="detail-label" required>
            <spring:message code="admin.users.Account-coordenates"/>
        </label>
        <br>
        <input name="<spring:message code='admin.users.coordenatesDMS'/>" type="radio"
               ng-model="currentEntity.coordenates" value="<spring:message code='admin.users.coordenatesDMS'/>">
        <spring:message code='admin.users.coordenatesDMS'/>
        <br>

        <input name="<spring:message code='admin.users.coordenatesDMS'/>" type="radio"
               ng-model="currentEntity.coordenates" value="<spring:message code='admin.users.coordenatesDMS'/>">
        <spring:message code='admin.users.coordenatesDegree'/>
        <br>
        <hr>

        <!-- BACKGROUND MAP -->
        <label class="detail-label" required>
            <spring:message code="admin.users.backgroundMap"/>
        </label>
        <br>

        <div class="row">
            <div class="col-md-2">
                <input name="Google Maps" type="radio"
                       ng-model="currentEntity.backgroundMap" value="Google Maps">
                Google Maps
                <br>

                <input name="MapQuest" type="radio"
                       ng-model="currentEntity.backgroundMap" value="MapQuest">
                MapQuest

                <br>
                <input name="Open Street View" type="radio"
                       ng-model="currentEntity.backgroundMap" value="Open Street View">
                Open Street View
            </div>

            <div class="col-md-2" ng-if="currentEntity.backgroundMap == 'Google Maps'">
                <div>
                    <input name="Map" type="radio"
                           ng-model="currentEntity.backgroundType" value="Map">
                    Map

                    <input name="Satelite" type="radio" style="margin-left: 20px "
                           ng-model="currentEntity.backgroundType" value="Satelite">
                    Satelite
                </div>
                <div>
                    <input name="Terrain" type="checkbox"
                           ng-model="currentEntity.backgroundTypeTerrain" value="Terrain">
                    Terrain

                    <input name="Labels" type="checkbox"  style="margin-left: 20px "
                           ng-model="currentEntity.backgroundTypeLabels" value="Labels">
                    Labels
                </div>
            </div>

            <div class="col-md-2" ng-if="currentEntity.backgroundMap == 'MapQuest'">
                <input name="Osm" type="radio"
                       ng-model="currentEntity.backgroundType" value="OSM">
                OSM

                <input name="Sat" type="radio"
                       ng-model="currentEntity.backgroundType" value="SAT">
                SAT
            </div>
        </div>
    </form>


</div>
</html>
