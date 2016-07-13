<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!--class="content-tab"-->
<div style="width:38%;height: 100%; float:left;z-index: 1; position: relative;
            box-shadow: 8px -6px 5px -5px #999, -6px 0 5px -5px #999; ">
    <div class="col-md-12" style="padding: 15px; background-color: #f5f5f5">
        <div class="col-md-1" style="margin-top: 10px">
            <i style="cursor:pointer; font-size: 25px;" ng-click="changeToDetail(currentEntity)"
               class="icon itaipu-icon-arrow-left-1">
            </i>
        </div>

        <div class="col-md-10" style="margin-top: 10px;">
                <span style=" font-weight: bold; font-size: 18px;">
                    <spring:message code="admin.marker-moderation.History"/>
                </span>
        </div>
    </div>


    <div class="col-md-12" style="overflow:auto;height:78vh; padding: 30px">

        <div ng-repeat="markerModeration in markersModeration " style="width:97%">

            <!--<div class="{{markerModeration.status == 'PENDING' ? 'alert warning' : markerModeration.status == 'ACCEPTED' ? 'alert success' : 'alert danger'}}"-->
            <div class="alert" ng-style="{'background-color': verifyStatusColor(markerModeration.status)}"
                 style="text-align:center;height:75px">

                <div>
                    <span><b style="float:left"> {{ currentEntity.user.name }}</b></span>
                </div>

                <div style="position:relative">
                    <span><b style="position:absolute;top:25px;left:13px"> {{ $index + 1 }}</b></span>
                </div>

                <div style="position:relative">
                    <span><b style="position:absolute;top:15px;left:45%"> {{ translateStatus($index) }}</b></span>
                </div>

                <div style="position:relative" ng-if="!$index">
                    <span><b style="float:right;position:absolute;top:0;right:0"> {{ currentEntity.created | date:'dd/MM/yyyy' }}</b></span>
                </div>

                <div style="position:relative">
                    <span><b style="float:right;position:absolute;top:0;right:0"> {{ markerModeration.created | date:'dd/MM/yyyy'}}</b></span>
                </div>

                <div>
                   <span ng-show="visibleDescription && markerModeration.status == 'REFUSED'" id="up-arrow">
							<i style="float:right;cursor:pointer;left:-12px;top:40px"
                               class="glyphicon glyphicon-chevron-up"
                               ng-click="visibleDescription = false"></i>
                   </span>
                    <span ng-show="!visibleDescription && markerModeration.status == 'REFUSED'">
							<i style="float:right;cursor:pointer;top:40px" class="glyphicon glyphicon-chevron-down"
                               ng-click="visibleDescription = true; listMotivesByMarkerModeration(markerModeration.id)">

                            </i>
                    </span>
                </div>

            </div>

            <div class="alert" ng-style="{'background-color': verifyStatusColor(markerModeration.status)}"
                 style="text-align:center" ng-if="visibleDescription && markerModeration.status == 'REFUSED'">

                <span>
                    <b> {{motiveMarkerModeration[markerModeration.id][0].motive.name}} -
                    {{motiveMarkerModeration[markerModeration.id][0].description}}
                    </b>
                </span>
            </div>

        </div>
    </div>
</div>

</html>