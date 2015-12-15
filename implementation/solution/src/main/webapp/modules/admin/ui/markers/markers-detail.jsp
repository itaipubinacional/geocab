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
                <i style="cursor:pointer; font-size: 25px;" ng-click="changeToList(currentPage)"
                   class="icon itaipu-icon-arrow-left-1">
                </i>
            </div>

            <div class="col-md-10">
                <span style=" font-weight: bold; font-size: 18px;">
                    {{currentEntity.layer.title }}
                </span>

                <div>
                    <span>Postado por <b>{{ currentEntity.user.name}} ({{ currentEntity.user.email}})</b> em {{ currentEntity.created | date:'dd/MM/yyyy' }}</span>
                </div>
            </div>
        </div>

        <div class="col-md-12">
            <form role="form">

                <fieldset
                        ng-disabled="!(currentEntity.status == 'SAVED' || currentEntity.status == 'REFUSED' || currentEntity.status == 'CANCELED' )">

                    <div id="left-content" style="float:left;">

                        <span style="font-weight: bold; font-size: 18px; margin: 20px 0 20px 0; float: left;">Informações</span>

                        </br>


                        <div class="col-md-12" style="float:left">
                            <div>
                                <span><b>Status</b></span>
                                </br>
                                <span><b>{{translateByStatus(currentEntity.status)}}</b></span>
                            </div>
                            </br>

                            <div ng-repeat="markerAttribute in attributesByMarker track by $index"
                                 style="position: relative;">
                                <label ng-style="$index > 0 ? {'margin-top':'15px'} : ''"
                                       ng-if="markerAttribute.attribute.type != 'PHOTO_ALBUM'">
                                    {{markerAttribute.attribute.name }}
                                </label>


                                <input
                                        type="number" name="number1"
                                        ng-if="markerAttribute.attribute.type == 'NUMBER'"
                                        class="form-control"
                                        ng-model="markerAttribute.value"
                                        required="{{markerAttribute.attribute.required}}">

                                <input name="date1" type="date"
                                       ng-if="markerAttribute.attribute.type == 'DATE'"
                                       class="form-control"
                                       ng-model="markerAttribute.value"
                                       required="{{markerAttribute.attribute.required}}">


                                <input name="texto"
                                       ng-if="markerAttribute.attribute.type == 'TEXT'"
                                       ng-model="markerAttribute.value"
                                       required="{{markerAttribute.attribute.required}}"
                                       class="form-control">

                                <div ng-if="markerAttribute.attribute.type == 'BOOLEAN'">
                                    <input
                                            type="radio"
                                            ng-checked="markerAttribute.value == 'Yes'"
                                            ng-model="markerAttribute.value"
                                            value="Yes">
                                    <spring:message code="map.Yes"/>

                                    <input
                                            type="radio"
                                            ng-checked="markerAttribute.value == 'No'"
                                            ng-model="markerAttribute.value"
                                            value="No">
                                    <spring:message code="map.No"/>
                                </div>
                            </div>

                            <!-- PHOTO ALBUM-->

                            <div class="col-md-12" style="text-align:center;">
                                <img ng-click="openImgModal(attributesByMarker)" ng-show="imgResult"
                                     class="marker-image"
                                     ng-src="{{ imgResult }}"
                                     style="width: 100%;margin-top: 12px;cursor: pointer;max-width:360px"> <br>
                            </div>

                            <div class="col-md-12" style="margin-bottom: 120px"></div>

                        </div>
                    </div>

                </fieldset>
            </form>
        </div>



        <!--BUTTONS-->
        <div class="col-md-12" style="position: fixed ;bottom: 0;width: 36%;
            padding-top: 10px; background-color: white; border-top: 1px solid #c5c5c5;
            height: 100px; ">

            <div class="btn-group col-md-12" role="group" aria-label="group buttons" >


                <button ng-disabled="currentEntity.status == PENDING ||currentEntity.status == ACCEPTED" ng-click="saveMarkerModal()" type="button"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Save'/>"
                        class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 40px" class="icon itaipu-icon-save"></i>
                </button>
                <button type="button" ng-click="postMarkerModal()"
                        ng-disabled="currentEntity.status == PENDING ||currentEntity.status == ACCEPTED ||currentEntity.status == CANCELED"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Post'/>"
                        class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 40px" class="icon itaipu-icon-export"></i>
                </button>
                <button type="button" ng-click="removeMarkerModal()"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Delete'/>"
                        class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 40px" class="icon itaipu-icon-delete"></i>
                </button>

            </div>
        </div>
        </br>

    </div>
</div>
</html>