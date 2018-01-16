<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html style="overflow:hidden" xmlns="http://www.w3.org/1999/html" xmlns:spring="http://www.w3.org/1999/XSL/Transform">
<div class="markers-menu">
    <div style="height:90vh; overflow:auto;">

        <div class="col-md-12" style="padding: 15px; background-color: #f5f5f5">
            <div class="col-md-1" style="margin-top: 10px">
                <i style="cursor:pointer; font-size: 25px;" ng-click="changeToListNoVectorMarkers(currentPage)"
                   class="icon itaipu-icon-arrow-left-1">
                </i>
            </div>

            <div class="row col-md-10">
                <span class="col-md-12 dynamic-text" title="{{currentEntity.layer.title}}"
                  style="padding: 0; font-weight: bold; font-size: 18px;">
                    {{currentEntity.layer.title}}
                </span>

                <div>
                    <span><spring:message code="PostedBy"/> <b>{{currentEntity.user.name}} ({{currentEntity.user.email}})</b> <spring:message code="in"/> {{currentEntity.created | date:'dd/MM/yyyy' }}</span>
                </div>
            </div>
        </div>

        <div class="col-md-12">
            <form role="form">

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

                    <div ng-repeat="markerAttribute in attributesByMarker track by $index"
                         style="position: relative;margin-bottom:15px;">

                        <label ng-style="$index > 0 ? {'margin-top':'15px'} : '' " ng-if="!markerAttribute.value == ''">
                            {{markerAttribute.attribute.name }}
                        </label>
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

				            <div  ng-if="markerAttribute.attribute.type == 'MULTIPLE_CHOICE'">
				            	<label>{{markerAttribute.attribute.name}}</label>
				            	</br>
				            	<div ng-repeat="option in markerAttribute.attribute.options">
					                <label class="radio-label" >
						            	<input id="role-adminstrator" ng-disabled="true" type="radio" ng-checked="markerAttribute.selectedAttribute.id == option.id" />
						            	{{ option.description }}
					                </label>
					                </br>
				            	</div>
				            </div>
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
                                style="resize: none;max-height: 127px;min-height: 30px;height: auto;"
                                ng-disabled="true" style="resize:none">
                        </div>

                    </div>
                </div>

                <div class="col-md-12 col-md-offset-2" style="text-align:center;">

                    <div ng-click="openImgModal(attributesByMarker)" ng-show="imgResult" class="marker-image" ng-style="{'background-image':'url(' + imgResult + ')'}">
                        <div class="overlay">
                            <i class="icon itaipu-icon-zoom"></i>
                            <h3 ng-if="countPhotos >= 1"> + {{countPhotos}} </h3>
                        </div>
                    </div>

                    <%--<img ng-click="openImgModal(attributesByMarker)" ng-show="imgResult" class="marker-image"--%>
                         <%--ng-src="{{ imgResult }}"--%>
                         <%--style="width: 100%;margin-top: 12px;cursor: pointer;max-width:360px"> <br>--%>
                </div>

                <div class="col-md-12" style="margin-bottom: 120px"></div>
            </form>
        </div>
        <!--BUTTONS-->

        <div class="col-md-12" style="position: fixed ;bottom: 0;width: 36%;
            padding-top: 21px; background-color: white; border-top: 1px solid #c5c5c5;
            height: 100px; ">

            <div class="btn-group col-md-12" role="group" aria-label="group buttons">
                <button ng-disabled="currentEntity.status == ACCEPTED" ng-click="approveMarker()" type="button"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Approve'/>"
                        class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 24px" class="icon itaipu-icon-like-filled"></i>
                </button>
                <button type="button" ng-disabled="currentEntity.status == REFUSED"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Refuse'/>"
                        ng-click="refuseMarker()" class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 24px" class="icon itaipu-icon-dislike"></i>
                </button>
                <button type="button" ng-click="cancelMarker()"
                        tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Cancel'/>"
                        ng-disabled="currentEntity.status == CANCELED" class="btn btn-secondary col-md-4 btn-icon">
                    <i style="font-size: 24px" class="icon itaipu-icon-close"></i>
                </button>

            </div>
        </div>

    </div>


</div>
</html>
