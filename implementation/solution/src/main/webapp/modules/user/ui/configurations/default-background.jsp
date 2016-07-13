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
    <form name="form" novalidate default-button="buttonUpdate">

      <div class="content-tab">
<!-- 			<button style="float: right;" -->
<!--                     class="btn btn-success" -->
<!--                     id="buttonUpdate" -->
<%--                     title="<spring:message code='admin.users.Save'/>" --%>
<!--                     ng-click="updateConfiguration()"> -->
<%--                 <spring:message code="admin.users.Save"/> --%>
<!--             </button> -->
            
            <!-- BACKGROUND MAP -->

            <div class="row">
                <div class="col-md-2">

					<div class="form-item-horizontal radio" style="margin-left: 0; margin-top: 15px">
                        <input type="radio" id="CONFIGURATION_OPEN_STREET_MAP" ng-click="setConfigurationBackgroundMap('CONFIGURATION_OPEN_STREET_MAP')" ng-model="configurationBackgroundMap.map" value="CONFIGURATION_OPEN_STREET_MAP"
                               name="CONFIGURATION_OPEN_STREET_MAP">
                        <label class="radio-label" for="CONFIGURATION_OPEN_STREET_MAP"> Open Street </label>
                    </div>
                    
                    <br />
                    
                    <div class="form-item-horizontal radio" style="margin-left: 0">
                        <input type="radio" id="CONFIGURATION_GOOGLE" ng-click="setConfigurationBackgroundMap('CONFIGURATION_GOOGLE_MAP')" ng-model="configurationBackgroundMap.map" value="CONFIGURATION_GOOGLE"
                               name="CONFIGURATION_GOOGLE">
                        <label class="radio-label" for="CONFIGURATION_GOOGLE"> Google Maps </label>
                    </div>

                    <br />

                    <div class="form-item-horizontal radio" style="margin-left: 0">
                        <input type="radio" id="CONFIGURATION_MAP_QUEST" ng-click="setConfigurationBackgroundMap('CONFIGURATION_MAP_QUEST')" ng-model="configurationBackgroundMap.map" value="CONFIGURATION_MAP_QUEST"
                               name="CONFIGURATION_MAP_QUEST">
                        <label class="radio-label" for="CONFIGURATION_MAP_QUEST"> MapQuest </label>
                    </div>

                </div>

                <div style="margin-top: 12px; padding-left:35px;border-left: 1px solid #ccc;" class="col-md-8" ng-if="configurationBackgroundMap.map == 'CONFIGURATION_GOOGLE'">

                    <div>
                        <div class="form-item-horizontal radio" style="margin-left: 0;">
                            <input type="radio" id="CONFIGURATION_Map" ng-click="setConfigurationBackgroundMap('CONFIGURATION_GOOGLE_MAP')" ng-model="configurationBackgroundMap.subType" value="CONFIGURATION_GOOGLE_MAP"
                                   name="CONFIGURATION_Map">
                            <label class="radio-label" for="CONFIGURATION_Map"> <spring:message code='admin.users.Map'/> </label>
                        </div>

                        <div class="form-item-horizontal radio" style="margin-left: 0;">
                            <input type="radio" id="CONFIGURATION_Satellite" ng-click="setConfigurationBackgroundMap('CONFIGURATION_GOOGLE_SATELLITE')" ng-model="configurationBackgroundMap.subType" value="CONFIGURATION_GOOGLE_SATELLITE"
                                   name="CONFIGURATION_Satellite">
                            <label class="radio-label" for="CONFIGURATION_Satellite"> <spring:message code='admin.users.Satellite'/> </label>
                        </div>
                    </div>

                    <div style="margin-left: 30px" ng-if="configurationBackgroundMap.subType == 'CONFIGURATION_GOOGLE_MAP'">
                        <label><input ng-change="setConfigurationType('CONFIGURATION_GOOGLE_MAP_TERRAIN', configurationBackgroundMap.type.CONFIGURATION_GOOGLE_MAP_TERRAIN)" name="CONFIGURATION_GOOGLE_MAP_TERRAIN" type="checkbox"
                               ng-model="configurationBackgroundMap.type.CONFIGURATION_GOOGLE_MAP_TERRAIN" value="CONFIGURATION_GOOGLE_MAP_TERRAIN">
                            <spring:message code='admin.users.Terrain'/>
                        </label>
                    </div>
                    <div style="margin-left: 130px" ng-if="configurationBackgroundMap.subType == 'CONFIGURATION_GOOGLE_SATELLITE'">
                        <label><input ng-change="setConfigurationType('CONFIGURATION_GOOGLE_SATELLITE_LABELS', configurationBackgroundMap.type.CONFIGURATION_GOOGLE_SATELLITE_LABELS)" name="CONFIGURATION_GOOGLE_SATELLITE_LABELS" type="checkbox" style="margin-left: 20px "
                               ng-model="configurationBackgroundMap.type.CONFIGURATION_GOOGLE_SATELLITE_LABELS" value="CONFIGURATION_GOOGLE_SATELLITE_LABELS">
                            <spring:message code='admin.users.Labels'/>
                        </label>
                    </div>
                </div>

                <div style="margin-top: 12px; padding-left:35px;border-left: 1px solid #ccc;" class="col-md-8" ng-if="configurationBackgroundMap.map == 'CONFIGURATION_MAP_QUEST'">

                    <div class="form-item-horizontal radio" style="margin-left: 0;">
                        <input type="radio" id="CONFIGURATION_OSM" ng-click="setConfigurationBackgroundMap('CONFIGURATION_MAP_QUEST_OSM')" ng-model="configurationBackgroundMap.subType" value="CONFIGURATION_MAP_QUEST_OSM"
                               name="CONFIGURATION_OSM">
                        <label class="radio-label" for="CONFIGURATION_OSM"> OSM </label>
                    </div>

                    <div class="form-item-horizontal radio" style="margin-left: 0;">
                        <input type="radio" id="CONFIGURATION_SAT" ng-click="setConfigurationBackgroundMap('CONFIGURATION_MAP_QUEST_SAT')" ng-model="configurationBackgroundMap.subType" value="CONFIGURATION_MAP_QUEST_SAT"
                               name="CONFIGURATION_SAT">
                        <label class="radio-label" for="CONFIGURATION_SAT"> SAT </label>
                    </div>

                </div>
            </div>
        </div>
    </form>
</div>
</html>
