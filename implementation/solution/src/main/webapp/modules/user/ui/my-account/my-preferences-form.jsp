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
        <!-- coordinates -->

        <label class="detail-label">
            <spring:message code="admin.users.Account-coordinates"/>
        </label>
        <br>
        <label>
            <input name="<spring:message code='admin.users.coordinatesDMS'/>" type="radio"
                   ng-model="currentEntity.coordinates" value="DEGRESS_MINUTES_SECONDS">
            <spring:message code='admin.users.coordinatesDMS'/>
        </label>
        <br>
        <label>
            <input name="<spring:message code='admin.users.coordinatesDMS'/>" type="radio"
                   ng-model="currentEntity.coordinates" value="DEGRESS_DECIMAL">
            <spring:message code='admin.users.coordinatesDegree'/>
        </label>
        <br>
        <hr>

        <!-- BACKGROUND MAP -->
        <label class="detail-label" required>
            <spring:message code="admin.users.backgroundMap"/>
        </label>
        <br>

        <div class="row">
            <div class="col-md-2">
                <label>
                    <input name="Google Maps" type="radio"
                           ng-model="currentEntity.backgroundMap" value="GOOGLE">
                    Google Maps
                </label>
                <br>
                <label>
                    <input name="MapQuest" type="radio"
                           ng-model="currentEntity.backgroundMap" value="MAP_QUEST">
                    MapQuest
                </label>

                <br>
                <label>
                    <input name="Open Street View" type="radio"
                           ng-model="currentEntity.backgroundMap" value="OPEN_STREET_MAP">
                    Open Street
                </label>
            </div>

            <div class="col-md-2" ng-if="currentEntity.backgroundMap == 'GOOGLE'">
                <div>
                    <input name="Map" type="radio"
                           ng-model="currentEntity.backgroundType" value="GOOGLE_MAP">
                    Map

                    <input name="Satelite" type="radio" style="margin-left: 20px "
                           ng-model="currentEntity.backgroundType" ng-value="GOOGLE_SATELITE">
                    Satelite
                </div>
                <div>
                    <input name="Terrain" type="checkbox"
                           ng-model="currentEntity.backgroundTypeTerrain" value="GOOGLE_MAP_TERRAIN">
                    Terrain

                    <input name="Labels" type="checkbox" style="margin-left: 20px "
                           ng-model="currentEntity.backgroundTypeLabels" value="GOOGLE_SATELITE_LABELS">
                    Labels
                </div>
            </div>

            <div class="col-md-2" ng-if="currentEntity.backgroundMap == 'MAP_QUEST'">
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
