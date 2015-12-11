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

            <fieldset
                    ng-disabled="!(currentEntity.status == 'SAVED' || currentEntity.status == 'REFUSED' || currentEntity.status == 'CANCELED' )">

                <div id="left-content" style="float:left; margin-bottom: 70px">

                    <span style="font-weight: bold; font-size: 18px; margin: 20px 0 20px 0; float: left;">Informações</span>

                    </br>


                    <div class="col-md-10" style="float:left">
                        <div>
                            <span><b>Status</b></span>
                            {{translateByStatus(currentEntity.status)}}

                            <i ng-if="currentEntity.status == 'SAVED' " class="icon itaipu-icon-save"></i>
                            <i ng-if="currentEntity.status == 'PENDING' " class="icon itaipu-icon-schedules"></i>
                            <i ng-if="currentEntity.status == 'ACCEPTED' " class="icon itaipu-icon-like-filled"></i>
                            <i ng-if="currentEntity.status == 'REFUSED' " class="icon itaipu-icon-dislike"></i>
                            <i ng-if="currentEntity.status == 'CANCELED' " class="icon itaipu-icon-close"></i>
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
                                   class="form-control datepicker"
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
                        <div
                                ng-if="!(currentEntity.status == 'SAVED' || currentEntity.status == 'REFUSED' || currentEntity.status == 'CANCELED' )"
                                style="text-align:center;">
                            <img ng-click="openImgModal(attributesByMarker)"
                                 ng-show="imgResult" class="marker-image" ng-src="{{ imgResult }}"
                                 style="width: 100%;margin-top: 12px;cursor: pointer;max-width:360px"> <br>
                        </div>
                    </div>

                    <div style="text-align:center;">


                    </div>
                </div>
            </fieldset>
        </form>


        <!--BUTTONS-->
        <div class="row"
             style=" position: fixed; background-color: #FFFFFF; height: 80px; bottom: 0; width: 36%; padding-top: 10px; border-top: 1px solid #a4a4a4">

            <div class="col-md-10">
                <div class="col-md-3">
                    <a class="btn btn-default icon itaipu-icon-book" ng-click="changeToHistory(row.entity.marker)"
                       style="width:75px; height:58px;">
                        </br>
                        <spring:message code="admin.marker-moderation.History"/>
                    </a>
                </div>
                <div class="col-md-3">
                    <div class="btn btn-default icon itaipu-icon-save" name="btnSave"
                         ng-click="saveMarkerModal()"
                         ng-disabled="currentEntity.status == PENDING ||currentEntity.status == ACCEPTED"
                         style="width:75px; height:58px;">
                        </br>
                        <a style="font-size:14px;text-decoration:none;color:black">
                            <spring:message code="admin.access-group.Save"/>
                        </a>

                    </div>
                </div>
                <div class="col-md-3">
                    <div class="btn btn-default icon itaipu-icon-export" name="btnRefuse"
                         ng-click="postMarkerModal()"
                         ng-disabled="currentEntity.status == PENDING ||currentEntity.status == ACCEPTED ||currentEntity.status == CANCELED"
                         style="width:75px; height:58px">
                        </br>
                        <a style="font-size:14px;text-decoration:none;color:black">
                            <spring:message code="layer-group-view.Post"/>
                        </a>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="btn btn-default icon itaipu-icon-delete" name="btnDelete"
                         ng-click="removeMarkerModal()"
                         style="width:75px; height:58px">
                        </br>
                        <a style="font-size:14px;text-decoration:none;color:black">
                            <spring:message code="map.Delete"/>
                        </a>
                    </div>
                </div>

            </div>
        </div>
        </br>

    </div>
</div>
</html>