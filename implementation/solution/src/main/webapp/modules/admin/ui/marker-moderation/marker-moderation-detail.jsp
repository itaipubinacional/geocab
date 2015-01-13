<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<div class="content-tab" style="width:35%; height: 100%; float:left; margin: 20px;">		
		<form>
        	
        	    
        	 <div id="left-content" style="float:left">        	    									
				<span
					style="float: left; margin-top: 12px; font-weight: bold; font-size: 18px;">{{marker.layer.title }}
				</span> 
				
				</br>
				
				<div style="text-align:center">
					<img ng-click="openImgModal()" ng-show="imgResult" class="marker-image" ng-src="{{ imgResult }}"
						style="width: 100%; height: 200px; margin-top: 12px; cursor: pointer;max-width:360px"> <br>
					
				</div>

				
				<div style=" overflow: auto;">
					<div ng-repeat="markerAttribute in attributesByMarker track by $index" style="position: relative;margin-bottom:15px">
											
						<label ng-style="$index > 0 ? {'margin-top':'15px'} : '' " ng-if="!markerAttribute.value == ''">{{ markerAttribute.attribute.name }}</label> 
							<input
								type="number" name="number1"
								ng-if="markerAttribute.attribute.type == 'NUMBER' && !markerAttribute.value == '' "
								class="form-control" ng-model="markerAttribute.value"									
								required ng-disabled="true"
							> 
																						
							<input 
								name="date1" 
								ng-if="markerAttribute.attribute.type == 'DATE' && !markerAttribute.value == ''"
								class="form-control datepicker" ng-model="markerAttribute.value"									
								required 
								ng-disabled="true"
							>
				
							<div ng-if="markerAttribute.attribute.type == 'BOOLEAN' && !markerAttribute.value == ''">
								<input 
									ng-disabled="true" type="radio"
									ng-checked="markerAttribute.value == 'Yes'"
									ng-model="markerAttribute.value" 
									value="Yes"
								>
								<spring:message code="map.Yes" /> 
															
								<input
									ng-disabled="true" type="radio"
									ng-checked="markerAttribute.value == 'No'"
									ng-model="markerAttribute.value" 
									value="No"
															>
								<spring:message code="map.No" />
							</div>
				
							<input type="text"
								ng-if="markerAttribute.attribute.type == 'TEXT' && !markerAttribute.value == ''" 
								name="texto"
								class="form-control" ng-model="markerAttribute.value"
								required ng-disabled="true"> 
																		
						</div>
					</div>
        		</div>
        			
        		<div style="float: right" id="right-content">
        			<span><b>{{ marker.user.name}} - </b></span> 
        			<span>{{ marker.created | date:'dd/MM/yyyy' }}</span>
        			
        			</br>
        			
        			<a class="btn btn-default history" style="float:right; margin-top:40px; width:75px; height:58px; line-height : 75px"><spring:message code="admin.marker-moderation.History" /></a></br>
					<a class="btn btn-default approve" style="float:right; margin-top:15px; width:75px; height:58px; line-height : 75px" ng-click="aprroveMarker()" ><spring:message code="admin.marker-moderation.Approve" /></a></br>
					<a class="btn btn-default refuse"  style="float:right; margin-top:15px; width:75px; height:58px; line-height : 75px" ng-click="refuseMarker()" ><spring:message code="admin.marker-moderation.Refuse" /></a></br> 					       													
        		</div>
        			
			
			
		</form>	
	</div>
</html>