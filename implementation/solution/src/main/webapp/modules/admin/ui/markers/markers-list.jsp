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
	<div style="width:38%; height: 100%; float:left; padding: 20px;z-index: 1; position: relative;
            box-shadow: 2px 1px 7px 2px #999, -6px 0 5px -2px #999" id="markers-sidebar">

		<!-- Filter Bar -->
		<div class="search-div" style="margin-bottom:10px">
			<form>

				<div style="width:45%;display:inline-block">

					<!--<input auto-complete autocomplete="off" type="text" class="form-control"-->
					<!--typeahead-wait-ms="500" ng-model="filter.layer" placeholder="<spring:message code="admin.marker-moderation.Layer" />"-->
					<!--typeahead="layer.title for layer in listAllInternalLayerGroups($viewValue) | limitTo:2">-->

					<select data-placeholder="<spring:message code='admin.marker-moderation.Layer'/>" name="camada"
									ng-options="layer.layerTitle group by layer.group for layer in selectLayerGroup"
									ng-model="filter.layer.title" chosen class="form-control">
						<option value=""></option>
					</select>
				</div>


				<input type="button" style="margin-right:5px;margin-bottom:0px" ng-click="bindFilter()" value="<spring:message code="Filter"/>" title="<spring:message code="Search"/>" class="btn btn-default" ng-disabled="currentPage == null"
				/>

				<input type="button" ng-click="clearFilters()" style="margin-bottom:0px" value="<spring:message code='clear.Filters'/>" class="btn btn-default" ng-disabled="currentPage == null"
						/>


				<a class="btn btn-mini" ng-show="visible"   ng-click="visible = false" ><i class="glyphicon glyphicon-chevron-up"></i></a>
				<a class="btn btn-mini" ng-show="!visible" 	ng-click="visible = true"  ><i class="glyphicon glyphicon-chevron-down"></i></a>


				<div style="margin-top:10px; display:flex" ng-show="visible">

					<select class="form-control" ng-model="filter.status" style="width:30%;margin-right:10px">
						<option value="" ng-selected="true"><spring:message code="admin.marker-moderation.All-status" /></option>
						<option value="SAVED"><spring:message code="admin.marker-moderation.Saved"/></option>
						<option value="PENDING"><spring:message code="admin.marker-moderation.Pending" /></option>
						<option value="ACCEPTED"><spring:message code="admin.marker-moderation.Approved" /></option>
						<option value="REFUSED"><spring:message code="admin.marker-moderation.Refused"/></option>
						<option value="CANCELED"><spring:message code="admin.marker-moderation.Canceled"/></option>
					</select>

					<!--
					<input ng-model="filter.dateStart" class="form-control datepicker" style="width:35%;;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Beginning"/>" onfocus="(this.type='date')" onblur="(this.type='text')"  id="date" />

					<input ng-model="filter.dateEnd" class="form-control datepicker" style="width:35%;;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Ending"/>" onfocus="(this.type='date')" onblur="(this.type='text')" id="date"/>
					-->

					<input ng-model="filter.dateStart" class="form-control datepicker" style="width:35%;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Beginning"/>"/>

					<input ng-model="filter.dateEnd" class="form-control datepicker" style="width:35%;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Ending"/>"/>
				</div>


			</form>

		</div>

		<!--<div class="row">
			<div class="col-md-4">
				<h4 ng-if="itensMarcados.length > 1">{{itensMarcados.length}}
				<spring:message code="admin.marker-moderation.Selected-items"/>
			</h4>
				<h4 ng-if="itensMarcados.length == 1">{{itensMarcados.length}}
					<spring:message code="admin.marker-moderation.Selected-item"/>
				</h4></div>
			<div class="col-md-4 col-md-offset-4">
				<button type="button" class="btn btn-primary" name="btnSave"
								ng-click="postMarkersModal()" ng-disabled="!disableButtonPost">
					<spring:message code="admin.marker-moderation.Post"/>
				</button>
				<button type="button" class="btn btn-primary" name="btnSave" ng-click="removeMarkersModal()">
					<spring:message code="admin.marker-moderation.Delete"/>
				</button>
			</div>
		</div>-->

		<div class="row" ng-if="!itensMarcados.length">
			<div class="col-md-5">
				<h4 style="margin: 0;height: 42px;line-height: 42px"><spring:message code="admin.access-group.my-markers"/></h4>
			</div>
		</div>
		<div class="row" ng-if="itensMarcados.length" style="height: 42px;line-height: 42px">
			<div class="col-md-5">
				<h4 ng-if="itensMarcados.length > 1">{{itensMarcados.length}}
					<spring:message code="admin.marker-moderation.Selected-items"/>
				</h4>
				<h4 ng-if="itensMarcados.length == 1">{{itensMarcados.length}}
					<spring:message code="admin.marker-moderation.Selected-item"/>
				</h4>
			</div>

			<!-- BUTTONS-->
			<!--<div class="btn-group col-md-4" role="group" aria-label="group buttons">-->
			<!--<button type="button" ng-click="postMarkersModal()" ng-disabled="!disableButtonPost"-->
			<!--tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Post'/>"-->
			<!--style="float: right"-->
			<!--class="btn btn-secondary">-->
			<!--<i style="font-size: 18px" class="icon itaipu-icon-export"></i>-->
			<!--</button>-->
			<!--<button type="button" ng-click="removeMarkersModal()"-->
			<!--tooltip-placement="top" tooltip=" <spring:message code='admin.marker-moderation.Delete'/>"-->
			<!--style="float: right"-->
			<!--class="btn btn-secondary">-->
			<!--<i style="font-size: 18px" class="icon itaipu-icon-delete"></i>-->
			<!--</button>-->
			<!--</div>-->
			<div class="col-md-7">

				<button type="button" class="btn btn-default pull-right" name="btnSave"
							ng-click="removeMarkersModal()" ng-disabled="disableButtonDelete" >
					  <i class="itaipu-icon-delete"></i>
					  <spring:message code="admin.marker-moderation.Delete"/>
				</button>

				<button type="button" class="btn btn-primary pull-right" name="btnSave"
							ng-click="postMarkersModal()" ng-disabled="disableButtonPost" style="margin-right: 7px;">
					  <i class="itaipu-icon-export"></i>
					  <spring:message code='admin.marker-moderation.Post'/>
				</button>
			</div>
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
			{{currentPage.numberOfElements}} <spring:message code="admin.users.of"/> {{currentPage.totalElements}} <spring:message code="admin.users.items"/>
		</div>

	</div>

</div>

<script type="text/javascript">
function setSidebarHeight() {
  var height = $('#olmap').height();
  $('#markers-sidebar').height(height - 40);
  $('#markers-sidebar').css('overflow', 'auto');
  $('body').css('overflow', 'hidden');
}

setSidebarHeight();

$(window).resize(function() {
  setSidebarHeight();
});
</script>
</html>