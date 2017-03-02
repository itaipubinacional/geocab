<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Data source - List -->
<div>
	<!-- filter bar -->
	<div class="search-div">
		<form>
			<input type="text" ng-model="data.filter" class="form-control" placeholder="<spring:message code="admin.datasource.Name-or-address"/>" style="float:left; width:300px"/>
			<input type="submit" value="<spring:message code="Search"/>" title="<spring:message code="Search"/>" class="btn btn-default" ng-disabled="currentPage == null"
			       ng-click="listDataSourceByFilters(data.filter, currentPage.pageable)"/>
	    </form>
	</div>

	<div ng-grid="gridOptions" style="height: 320px; border: 1px solid rgb(212,212,212);"></div>
    <div class="gridFooterDiv">
       <pagination style="text-align: center;"
                   total-items="currentPage.total" rotate="false"
                   items-per-page="currentPage.size"
                   max-size="currentPage.totalPages"
                   ng-change="changeToPage(data.filter, currentPage.pageable.pageNumber)"
                   ng-model="currentPage.pageable.pageNumber" boundary-links="true"
                   previous-text="‹" next-text="›" first-text="«" last-text="»">
       </pagination>
    </div>

    <div class="grid-elements-count">
        {{currentPage.numberOfElements == null ? 0 : currentPage.numberOfElements }} <spring:message code="of"/> {{currentPage.totalElements == null ? 0 : currentPage.totalElements}} <spring:message code="items"/>
    </div>

</div>
</html>