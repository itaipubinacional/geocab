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
	<div style="width:35%; height: 100%; float:left; margin: 20px;">
	        
		<!-- Filter Bar -->
		<div class="search-div" style="margin-bottom:10px">
			<form>
				
				<div style="width:45%;display:inline-block">
				
					<input auto-complete autocomplete="off" type="text" class="form-control" 
                	   typeahead-wait-ms="500" ng-model="filter.layer" placeholder="<spring:message code="admin.marker-moderation.Layer" />"
                	   typeahead="layer.title for layer in listAllInternalLayerGroups($viewValue) | limitTo:2">	
				
				</div>					                        
								
				<a class="btn btn-mini" ng-show="visible"   ng-click="visible = false" ><i class="glyphicon glyphicon-chevron-up"></i></a>													    
		    	<a class="btn btn-mini" ng-show="!visible" 	ng-click="visible = true"  ><i class="glyphicon glyphicon-chevron-down"></i></a>
		    	
		    	<input type="button" style="margin-right:5px" ng-click="bindFilter()" value="<spring:message code="Filter"/>" title="<spring:message code="Search"/>" class="btn btn-default" ng-disabled="currentPage == null"
			       />		    			    	
		    	
		    	<input type="button" ng-click="clearFilters()" value="Limpar Filtros" class="btn btn-default" ng-disabled="currentPage == null"
			       />		    			    	
		
			<div style="margin-top:10px; display:flex" ng-show="visible">
			
       	 			<select class="form-control" ng-model="filter.status" style="width:30%;margin-right:10px">
                         <option value="" ng-selected="true"><spring:message code="admin.marker-moderation.All-status" /></option>
                         <option value="PENDING"><spring:message code="admin.marker-moderation.Pending" /></option>
                         <option value="ACCEPTED"><spring:message code="admin.marker-moderation.Approved" /></option>
                         <option value="REFUSED"><spring:message code="admin.marker-moderation.Refused"/></option>
                     </select>
					
					<!--  
					<input ng-model="filter.dateStart" class="form-control datepicker" style="width:35%;;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Beginning"/>" onfocus="(this.type='date')" onblur="(this.type='text')"  id="date" />
					
					<input ng-model="filter.dateEnd" class="form-control datepicker" style="width:35%;;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Ending"/>" onfocus="(this.type='date')" onblur="(this.type='text')" id="date"/>
					-->
					
					<input ng-model="filter.dateStart" class="form-control datepicker" style="width:35%;;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Beginning"/>"/>
					
					<input ng-model="filter.dateEnd" class="form-control datepicker" style="width:35%;;margin-right:10px" placeholder="<spring:message code="admin.marker-moderation.Ending"/>"/>
				</div>
				
				<div style="margin-top:10px; display:flex" ng-show="visible">
					 <select data-placeholder="<spring:message code="admin.marker-moderation.Users"/>" name="layer"
							ng-options="user.email for user in selectUsers "
							ng-model="filter.user" chosen class="form-control"
							>
					</select>
				</div>
			
			 </form>		
			
		</div>
		
		<div ng-grid="gridOptions" style="height: 499px;border: 1px solid rgb(212,212,212);"></div>					
		
		<div class="gridFooterDiv">
		       <pagination style="text-align: center"
		                   total-items="currentPage.total" rotate="false"
		                   items-per-page="currentPage.size"
		                   max-size="currentPage.totalPages"
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
</html>