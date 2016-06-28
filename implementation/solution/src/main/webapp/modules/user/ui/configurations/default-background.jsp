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
			<button style="float: right;"
                    class="btn btn-success"
                    id="buttonUpdate"
                    title="<spring:message code='admin.users.Save'/>"
                    ng-click="updateUser()">
                <spring:message code="admin.users.Save"/>
            </button>
            
            <!-- BACKGROUND MAP -->

            <div class="row">
                <div class="col-md-2">

					<div class="form-item-horizontal radio" style="margin-left: 0; margin-top: 15px">
                        <input type="radio" id="OPEN_STREET_MAP" ng-click="setBackgroundMap('OPEN_STREET_MAP')" ng-model="backgroundMap.map" value="OPEN_STREET_MAP"
                               name="OPEN_STREET_MAP">
                        <label class="radio-label" for="OPEN_STREET_MAP"> Open Street </label>
                    </div>
                    
                    <br />
                    
                    <div class="form-item-horizontal radio" style="margin-left: 0">
                        <input type="radio" id="GOOGLE" ng-click="setBackgroundMap('GOOGLE_MAP')" ng-model="backgroundMap.map" value="GOOGLE"
                               name="GOOGLE">
                        <label class="radio-label" for="GOOGLE"> Google Maps </label>
                    </div>

                    <br />

                    <div class="form-item-horizontal radio" style="margin-left: 0">
                        <input type="radio" id="MAP_QUEST" ng-click="setBackgroundMap('MAP_QUEST')" ng-model="backgroundMap.map" value="MAP_QUEST"
                               name="MAP_QUEST">
                        <label class="radio-label" for="MAP_QUEST"> MapQuest </label>
                    </div>

                </div>

                <div style="margin-top: 12px; padding-left:35px;border-left: 1px solid #ccc;" class="col-md-8" ng-if="backgroundMap.map == 'GOOGLE'">

                    <div>
                        <div class="form-item-horizontal radio" style="margin-left: 0;">
                            <input type="radio" id="Map" ng-click="setBackgroundMap('GOOGLE_MAP')" ng-model="backgroundMap.subType" value="GOOGLE_MAP"
                                   name="Map">
                            <label class="radio-label" for="Map"> <spring:message code='admin.users.Map'/> </label>
                        </div>

                        <div class="form-item-horizontal radio" style="margin-left: 0;">
                            <input type="radio" id="Satellite" ng-click="setBackgroundMap('GOOGLE_SATELLITE')" ng-model="backgroundMap.subType" value="GOOGLE_SATELLITE"
                                   name="Satellite">
                            <label class="radio-label" for="Satellite"> <spring:message code='admin.users.Satellite'/> </label>
                        </div>
                    </div>

                    <div style="margin-left: 30px" ng-if="backgroundMap.subType == 'GOOGLE_MAP'">
                        <label><input ng-change="setType('GOOGLE_MAP_TERRAIN', backgroundMap.type.GOOGLE_MAP_TERRAIN)" name="GOOGLE_MAP_TERRAIN" type="checkbox"
                               ng-model="backgroundMap.type.GOOGLE_MAP_TERRAIN" value="GOOGLE_MAP_TERRAIN">
                            <spring:message code='admin.users.Terrain'/>
                        </label>
                    </div>
                    <div style="margin-left: 130px" ng-if="backgroundMap.subType == 'GOOGLE_SATELLITE'">
                        <label><input ng-change="setType('GOOGLE_SATELLITE_LABELS', backgroundMap.type.GOOGLE_SATELLITE_LABELS)" name="GOOGLE_SATELLITE_LABELS" type="checkbox" style="margin-left: 20px "
                               ng-model="backgroundMap.type.GOOGLE_SATELLITE_LABELS" value="GOOGLE_SATELLITE_LABELS">
                            <spring:message code='admin.users.Labels'/>
                        </label>
                    </div>
                </div>

                <div style="margin-top: 12px; padding-left:35px;border-left: 1px solid #ccc;" class="col-md-8" ng-if="backgroundMap.map == 'MAP_QUEST'">

                    <div class="form-item-horizontal radio" style="margin-left: 0;">
                        <input type="radio" id="OSM" ng-click="setBackgroundMap('MAP_QUEST_OSM')" ng-model="backgroundMap.subType" value="MAP_QUEST_OSM"
                               name="OSM">
                        <label class="radio-label" for="OSM"> OSM </label>
                    </div>

                    <div class="form-item-horizontal radio" style="margin-left: 0;">
                        <input type="radio" id="SAT" ng-click="setBackgroundMap('MAP_QUEST_SAT')" ng-model="backgroundMap.subType" value="MAP_QUEST_SAT"
                               name="SAT">
                        <label class="radio-label" for="SAT"> SAT </label>
                    </div>

                </div>
            </div>
        </div>
    </form>
</div>
</html>
