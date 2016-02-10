<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="overflow:hidden" xmlns="http://www.w3.org/1999/html">
<div style="width:38%;height: 100%; float:left;z-index: 1; position: relative;
            box-shadow: 8px -6px 5px -5px #999, -6px 0 5px -5px #999; ">
    <div style="height:90vh; overflow:auto;">
        <div class="col-md-12" style="padding: 15px; background-color: #f5f5f5">
            <div class="col-md-1" style="margin-top: 10px">
                <i style="cursor:pointer; font-size: 25px;" ng-click="$state.go('markers'); changeToListNoVectorMarkers(currentPage);"
                   class="icon itaipu-icon-arrow-left-1">
                </i>
            </div>

            <div class="col-md-10">
                <span style=" font-weight: bold; font-size: 18px;">
                    {{currentEntity.layer.title }}
                </span>

                <div>
                    <span><spring:message code="PostedBy"/> <b>{{currentEntity.user.name}} ({{currentEntity.user.email}})</b> <spring:message code="in"/> {{currentEntity.created | date:'dd/MM/yyyy' }}</span>
                </div>
            </div>
        </div>

        <div class="col-md-12">
            <form name="sidebarMarkerUpdate" default-button="buttonUpdate" method="post" novalidate>

                <fieldset
                        ng-disabled="!(currentEntity.status == 'SAVED' || currentEntity.status == 'REFUSED' || currentEntity.status == 'CANCELED')">


                    <span style="font-weight: bold; font-size: 18px;padding-left: 15px; margin: 20px 0 20px 0; float: left;">
                        <spring:message code="Informations"/>
                    </span>

                    <span style="cursor: pointer; text-decoration: underline; float: right; margin-top: 25px; color: #cacaca"
                          ng-click="changeToHistory(row.entity.marker)">
                        <spring:message code="admin.marker-moderation.History"/>
                    </span>
                    </br>

                    <div class="col-md-12" style="float:left">
                        <div>
                            <span><b>Status</b></span>
                            </br>
                            <span><b>{{translateByStatus(currentEntity.status)}}</b></span>
                        </div>
                        </br>

                        <span class="tooltip-validation"
                        ng-show="sidebarMarker.$submitted && sidebarMarker.layer.$error.required"
                        style="top: -20px"><spring:message code="map.Field-required"/></span> <br>

                        <div ng-repeat="markerAttribute in attributesByMarker"
                        style="position: relative;margin-bottom:15px;">

                            <ng-form name="ngSideMarker" default-button="buttonUpdate">



                                <!--<label style="padding-top: 10px">{{ markerAttribute.attribute.name }}</label>-->
                                <div ng-if="(currentEntity.status == 'SAVED' || currentEntity.status == 'REFUSED' || currentEntity.status == 'CANCELED')">

                                <button ng-if="markerAttribute.attribute.type == 'PHOTO_ALBUM'" class="btn btn-default"
                                ng-click="showUpload(markerAttribute.attribute, attributesByMarker)"
                                style="float: left;margin-right: 5px"
                                title="<spring:message code='map.Picture'/>"><span class="glyphicon glyphicon-picture"></span>
                                </button>

                                 <label ng-if="markerAttribute.attribute.type != 'PHOTO_ALBUM'" style="margin-top: 10px">{{ markerAttribute.attribute.name }}</label>
                                 <label ng-if="markerAttribute.attribute.type == 'PHOTO_ALBUM'" style="height: 34px;line-height: 34px; margin-bottom: 15px;">{{ markerAttribute.attribute.name }}</label>
                                </div>

                                <input
                                type="number"
                                name="number1"
                                ng-if="markerAttribute.attribute.type == 'NUMBER'"
                                class="form-control"
                                ng-model="markerAttribute.value"
                                maxlength="255"
                                ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.number1.$error.required}"
                                ng-required="markerAttribute.attribute.required"
                                >

                                <input
                                name="date1"
                                ng-if="markerAttribute.attribute.type == 'DATE'"
                                class="form-control datepicker" ng-model="markerAttribute.value"
                                ng-class="{ngInvalid: ngSideMarker.$submitted && ngSideMarker.date1.$error.required}"
                                required
                                ng-required="markerAttribute.attribute.required"
                                >

                                <div ng-if="markerAttribute.attribute.type == 'BOOLEAN'" ng-required="attribute.required">

                                        <div class="required-boolean" >
                                        <input type="radio" name="boolean{{ $index }}" class="boolean-1 boolean" ng-model="attribute.value"
                                        value="Yes" onClick="isBooleanChecked(this)" ><spring:message code="map.Yes" />

                                        <input type="radio" name="boolean{{ $index }}" class="boolean-2 boolean" ng-model="attribute.value"
                                        value="No" onClick="isBooleanChecked(this)"><spring:message code="map.No" />
                                        </div>

                                </div>

                                <input type="text"
                                ng-if="markerAttribute.attribute.type == 'TEXT'" name="texto"
                                class="form-control" ng-model="markerAttribute.value"
                                ng-class="{ ngInvalid: ngSideMarker.$submitted && ngSideMarker.texto.$error.required }"
                                maxlength="255"
                                ng-required="markerAttribute.attribute.required"
                                >

                                <span class="tooltip-validation"
                                ng-show="  (ngSideMarker.texto.$error.required && ngSideMarker.$submitted)"
                                style="top: 13px"><spring:message code="map.Field-required"/>
                                </span>

                                <span
                                class="tooltip-validation"
                                ng-show="  (ngSideMarker.number1.$error.required && ngSideMarker.$submitted)"
                                style="top: 13px" ><spring:message code="map.Field-required"/>
                                </span>

                                <span
                                class="tooltip-validation"
                                ng-show="!(ngSideMarker.number1.$error.required && ngSideMarker.$submitted) && (ngSideMarker.number1.$error.number)"
                                style="top: 13px"><spring:message code="map.Must-be-a-number"/>
                                </span>

                                <span
                                class="tooltip-validation"
                                ng-show=" (ngSideMarker.date1.$error.required && ngSideMarker.$submitted)"
                                style="top: 13px"><spring:message code="map.Field-required"/>
                                </span>
                            <ng-form>

                        </div>


                        <!-- PHOTO ALBUM-->
                        <div class="col-md-12" style="text-align:center;"
                             ng-if="!(currentEntity.status == 'SAVED' || currentEntity.status == 'REFUSED' || currentEntity.status == 'CANCELED')">
                            <img ng-click="openImgModal(attributesByMarker)" ng-show="imgResult"
                                 class="marker-image"
                                 ng-src="{{ imgResult }}"
                                 style="width: 100%;margin-top: 12px;cursor: pointer;max-width:360px"> <br>
                        </div>

                        <div class="col-md-12" style="margin-bottom: 120px"></div>

                    </div>

                </fieldset>
            </form>
        </div>


        <!--BUTTONS-->
        <div class="col-md-12" style="position: fixed ;bottom: 0;width: 36%;
            padding-top: 21px; background-color: white; border-top: 1px solid #c5c5c5;
            height: 100px; ">

            <div class="btn-group col-md-12" role="group" aria-label="group buttons">


                <button ng-disabled="currentEntity.status == PENDING ||currentEntity.status == ACCEPTED"
                        ng-click="saveMarkerModal()" type="button"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Save'/>"
                        class="btn btn-secondary col-md-4 btn-icon"
                        id="buttonUpdate">
                    <i style="font-size: 24px" class="icon itaipu-icon-save"></i>
                </button>
                <button type="button" ng-click="postMarkerModal()"
                        ng-disabled="currentEntity.status == PENDING ||currentEntity.status == ACCEPTED ||currentEntity.status == CANCELED"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Post'/>"
                        class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 24px" class="icon itaipu-icon-export"></i>
                </button>
                <button type="button" ng-click="removeMarkerModal()"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Delete'/>"
                        class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 24px" class="icon itaipu-icon-delete"></i>
                </button>

            </div>
        </div>
        </br>

    </div>
</div>
</html>