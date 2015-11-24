<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="overflow:hidden" xmlns="http://www.w3.org/1999/html">
<div style="width:36%; height: 100%; float:left; margin: 20px;">
    <div style="height:76vh; overflow:auto">
        <div>
            <span style="cursor:pointer" ng-click="changeToList(currentPage)"
                  class="icon itaipu-icon-arrow-left-1">
            </span>

            <span style=" font-weight: bold; font-size: 18px; margin-left: 20px">
                {{currentEntity.layer.title }}
            </span>

            <div style="margin-left: 35px">
                <span><b>{{ currentEntity.user.name}} ({{ currentEntity.user.email}})</b></span>
                <span>em {{ currentEntity.created | date:'dd/MM/yyyy' }}</span>
            </div>

        </div>


        <form>

            <div id="left-content" style="float:left; margin-bottom: 70px">

                <span style="font-weight: bold; font-size: 18px; margin: 20px 0 20px 0; float: left;">Informações</span>

                </br>


                <div class="col-md-10" style="float:left">
                    <span><b>Status</b></span>
                    {{currentEntity.status}}

                    <div ng-repeat="markerAttribute in attributesByMarker track by $index"
                         style="position: relative;margin-bottom:15px;">

                        <label ng-style="$index > 0 ? {'margin-top':'15px'} : '' " ng-if="!markerAttribute.value == ''">{{
                            markerAttribute.attribute.name }}</label>
                        <input
                                type="number" name="number1"
                                ng-if="markerAttribute.attribute.type == 'NUMBER' && !markerAttribute.value == '' "
                                class="form-control" ng-model="markerAttribute.value"
                                required ng-disabled="true">

                        <input
                                name="date1"
                                ng-if="markerAttribute.attribute.type == 'DATE' && !markerAttribute.value == ''"
                                class="form-control datepicker" ng-model="markerAttribute.value"
                                required
                                ng-disabled="true">

                        <div ng-if="markerAttribute.attribute.type == 'BOOLEAN' && !markerAttribute.value == ''">
                            <input
                                    ng-disabled="true" type="radio"
                                    ng-checked="markerAttribute.value == 'Yes'"
                                    ng-model="markerAttribute.value"
                                    value="Yes">
                            <spring:message code="map.Yes"/>

                            <input
                                    ng-disabled="true" type="radio"
                                    ng-checked="markerAttribute.value == 'No'"
                                    ng-model="markerAttribute.value"
                                    value="No">

                            <spring:message code="map.No"/>
                        </div>

                        <div
                                ng-if="markerAttribute.attribute.type == 'TEXT' && !markerAttribute.value == ''"
                                name="texto"
                                class="form-control" ng-bind="markerAttribute.value"
                                required
                                style="resize: none;max-height: 127px;min-height: 30px;height: auto; width:404px "
                                ng-disabled="true" style="resize:none">
                        </div>

                    </div>
                </div>

                <div style="text-align:center;">
                    <img ng-click="openImgModal()" ng-show="imgResult" class="marker-image" ng-src="{{ imgResult }}"
                         style="width: 100%; height: 200px; margin-top: 12px; cursor: pointer;max-width:360px"> <br>

                </div>
            </div>

        </form>


        <!--BUTTONS-->
        <div class="row" style="position:fixed;background-color: #FFFFFF ; height: 80px;bottom: 25px; width: 37%;
            padding-top: 20px; border-top: 1px solid #a4a4a4">
            <div class=" col-md-2">
                <a class="btn btn-default icon itaipu-icon-book" ng-click="changeToHistory(row.entity.marker)"
                   style="width:75px; height:58px;">
                    </br>
                    <spring:message code="admin.marker-moderation.History"/>
                </a>
            </div>
            <div class="col-md-2">
                <div class="btn btn-default icon itaipu-icon-like-filled" name="btnApprove"
                     ng-click="approveMarker()"
                     style="width:75px; height:58px;">
                    </br>
                    <a style="font-size:14px;text-decoration:none;color:black">
                        <spring:message code="admin.marker-moderation.Approve"/>
                    </a>

                </div>
            </div>
            <div class="col-md-2">
                <div class="btn btn-default icon itaipu-icon-dislike" name="btnRefuse" ng-click="refuseMarker()"
                     style="width:75px; height:58px">
                    </br>
                    <a style="font-size:14px;text-decoration:none;color:black">
                        <spring:message code="admin.marker-moderation.Refuse"/>
                    </a>
                </div>
            </div>
            <div class="col-md-2">
                <div class="btn btn-default icon itaipu-icon-close" name="btnRefuse"
                     ng-click="cancelMarker()"
                     style="width:75px; height:58px">
                    </br>
                    <a style="font-size:14px;text-decoration:none;color:black">
                        <spring:message code="admin.marker-moderation.Cancel"/>
                    </a>
                </div>
            </div>
        </div>
        </br>

    </div>
</div>
</html>