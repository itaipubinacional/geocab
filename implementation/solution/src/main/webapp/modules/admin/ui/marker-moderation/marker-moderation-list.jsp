<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Users - List -->
<div>

	<!-- Posting evaluation - List -->
    <div style="width:38%; height: 100%; float:left; padding: 20px 20px 0 20px;z-index: 1; position: relative;
            box-shadow: 8px -6px 5px -5px #999, -6px 0 5px -5px #999;">

		<!-- Filter Bar -->
        <div class="search-div" style="margin-bottom:10px">
			<form class="form-inline" role="form">
                <div class="form-inline row">
                    <div class="form-group col-md-12">
                        <div class="col-md-5 row">
                            <!--<input auto-complete autocomplete="off" type="text" class="form-control"-->
                                   <!--typeahead-wait-ms="500" ng-model="filter.layer"-->
                                   <!--placeholder="<spring:message code='admin.marker-moderation.Layer' />"-->
                                   <!--typeahead="layer.title for layer in listAllInternalLayerGroups($viewValue) | limitTo:2"/>-->

                            <select data-placeholder="<spring:message code='admin.marker-moderation.Layer'/>" name="camada"
                                    ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
                                    ng-model="filter.layer.title" chosen class="form-control">
                                <option value=""></option>
                            </select>
                        </div>



                        <div class="col-md-2">
                            <input type="button" style="margin-right:5px" ng-click="bindFilter()"
                                   value="<spring:message code='Filter'/>" title="<spring:message code='Search'/>"
                                   class="btn btn-default" ng-disabled="currentPage == null"/>
                        </div>

                        <div class="col-md-4">
                            <input type="button" ng-click="clearFilters()" value="Limpar Filtros"
                                   class="btn btn-default" ng-disabled="currentPage == null"/>
                        </div>

                        <div class="col-md-1">
                            <a class="btn btn-mini" ng-show="visible" ng-click="visible = false">
                                <i class="glyphicon glyphicon-chevron-up"></i>
                            </a>
                            <a class="btn btn-mini" ng-show="!visible" ng-click="visible = true">
                                <i class="glyphicon glyphicon-chevron-down"></i>
                            </a>
                        </div>

                    </div>
                </div>

                <div class="col-md-12" ng-if="visible">
                    <div class="row">
                        <!--<div class="form-group">-->
                            <select class="form-control" ng-model="filter.status">
                                <option value=""><spring:message code="admin.marker-moderation.All-status" /></option>
                                <option value="SAVED"><spring:message code="admin.marker-moderation.Saved"/></option>
                                <option value="PENDING" ><spring:message code="admin.marker-moderation.Pending" /></option>
                                <option value="ACCEPTED"><spring:message code="admin.marker-moderation.Approved" /></option>
                                <option value="REFUSED"><spring:message code="admin.marker-moderation.Refused"/></option>
                                <option value="CANCELED"><spring:message code="admin.marker-moderation.Canceled"/></option>
                             </select>
                        <!--</div>-->

                        <div class="form-group">
                            <input ng-model="filter.dateStart" class="form-control datepicker"
                                   placeholder="<spring:message code='admin.marker-moderation.Beginning'/>"/>
                        </div>

                        <div class="form-group">
                            <input ng-model="filter.dateEnd" class="form-control datepicker"
                                   placeholder="<spring:message code='admin.marker-moderation.Ending'/>"/>
                        </div>
                    </div>
                </div>
                <div class="form-inline row" ng-if="visible">
                    <div class="form-group col-md-12">
                        <select data-placeholder="<spring:message code='admin.marker-moderation.Users'/>" name="layer"
                                ng-options="user.email for user in selectUsers "
                                ng-model="filter.user" chosen class="form-control">
                            <option value="">
                                <spring:message code="admin.marker-moderation.All-users"/>
                            </option>
                        </select>
                    </div>
                </div>

            </form>

		</div>

        <div ng-grid="gridOptions" style="height: 499px;border: 1px solid rgb(212,212,212);"></div>

        <div class="gridFooterDiv">
            <pagination style="text-align: center; margin-top:45px"
                        total-items="currentPage.total" rotate="false"
                        items-per-page="currentPage.size"
                        max-size="5"
                        ng-change="changeToPage(data.filter, currentPage.pageable.pageNumber)"
                        ng-model="currentPage.pageable.pageNumber" boundary-links="true"
                        previous-text="‹" next-text="›" first-text="«" last-text="»">
            </pagination>
        </div>

        <div class="grid-elements-count">
            {{currentPage.numberOfElements}}
            <spring:message code="admin.users.of"/>
            {{currentPage.totalElements}}
            <spring:message code="admin.users.items"/>
        </div>

	</div>

</div>
</html>