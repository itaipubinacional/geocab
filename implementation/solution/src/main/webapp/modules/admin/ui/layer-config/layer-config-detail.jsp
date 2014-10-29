<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<style>
	.icon{
		padding-right:3px;
	}
	
</style>

<script>
	$("input[type=radio]").attr("disabled","true")
</script>
<!-- Layer config - Detail -->
<div>
    <form>
        <div class="content-tab">
            <div class="form-item">
                <b class="detail-label"><spring:message code="admin.layer-config.Geographic-Data-Source"/></b>
                <br>
                <span class="detail-value">{{currentEntity.dataSource.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="Layer"/></b>
                <br>
                <span class="detail-value">{{currentEntity.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="Title"/></b>
                <br>
                <span class="detail-value">{{currentEntity.title}}</span>
            </div>
            <br>
            
            <div ng-grid="gridAttributesDetail" style="height: 320px; border: 1px solid rgb(212,212,212);"></div>

			<label class="detail-label" style="margin: 15px 0 5px 0;" required><spring:message code="admin.layer-config.Icon" /></label>
            
            
            <table style="text-align: center; background: #E6E6E6;width:80px" >
            
           		<tr>
	           			<td class="icon"><img src="<c:url value="/static/icons/alpinehut.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/camping.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/hotel2.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/shelter2.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/youth_hostel.png"/>" width="25" height="25"></td>	  
	           			
	           			<td class="icon" ><img src="<c:url value="/static/icons/firestation.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/fountain.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/playground.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/recycling.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/telephone.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/toilets.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/waste_bin.png"/>" width="25" height="25"></td>
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/gate.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/toll_booth.png"/>" width="25" height="25"></td>
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/nursery.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/school.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/university.png"/>" width="25" height="25"></td>
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/cafe.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/fastfood.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/restaurant.png"/>" width="25" height="25"></td>
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/doctors.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/hospital.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/pharmacy.png"/>" width="25" height="25"></td>
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/deciduous.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/grass.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/hills.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/scrub.png"/>" width="25" height="25"></td>
	           			<td class="icon"><img src="<c:url value="/static/icons/swamp.png"/>" width="25" height="25"></td>	           			
	           			     
	           			<td class="icon"><img src="<c:url value="/static/icons/atm.png"/>" width="25" height="25"></td>	     
	           			<td class="icon"><img src="<c:url value="/static/icons/bank.png"/>" width="25" height="25"></td>
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/christian3.png"/>" width="25" height="25"></td>	  	     
	           			<td class="icon"><img src="<c:url value="/static/icons/islamic3.png"/>" width="25" height="25"></td>	  	     
	           			<td class="icon"><img src="<c:url value="/static/icons/jewish3.png"/>" width="25" height="25"></td>	  	     
	           			<td class="icon"><img src="<c:url value="/static/icons/unknown.png"/>" width="25" height="25"></td>	 
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/cave.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/crane.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/mine.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/peak2.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/place_city.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/tower_communications.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/tower_lookout.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/tower_power.png"/>" width="25" height="25"></td>     
	           			<td class="icon"><img src="<c:url value="/static/icons/tower_water.png"/>" width="25" height="25"></td>     
	           			
	           			<td class="icon"><img src="<c:url value="/static/icons/station_coal.png"/>" width="25" height="25"></td>  
	           			<td class="icon"><img src="<c:url value="/static/icons/station_gas.png"/>" width="25" height="25"></td>  
	           			<td class="icon"><img src="<c:url value="/static/icons/station_solar.png"/>" width="25" height="25"></td>  
	           			<td class="icon"><img src="<c:url value="/static/icons/station_wind.png"/>" width="25" height="25"></td>
	           		</tr>
	           		
	           		<tr>
	           			<td><input type="radio" value="static/icons/alpinehut.png" ng-checked="currentEntity.icon == 'static/icons/alpinehut.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/camping.png" ng-checked="currentEntity.icon == 'static/icons/camping.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/hotel2.png" ng-checked="currentEntity.icon == 'static/icons/hotel2.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/shelter2.png" ng-checked="currentEntity.icon == 'static/icons/shelter2.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/youth_hostel.png" ng-checked="currentEntity.icon == 'static/icons/youth_hostel.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/firestation.png" ng-checked="currentEntity.icon == 'static/icons/firestation.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/fountain.png" ng-checked="currentEntity.icon == 'static/icons/fountain.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/playground.png" ng-checked="currentEntity.icon == 'static/icons/playground.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/recycling.png" ng-checked="currentEntity.icon == 'static/icons/recycling.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/telephone.png" ng-checked="currentEntity.icon == 'static/icons/telephone.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/toilets.png" ng-checked="currentEntity.icon == 'static/icons/toilets.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/waste_bin.png" ng-checked="currentEntity.icon == 'static/icons/waste_bin.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/gate.png" ng-checked="currentEntity.icon == 'static/icons/gate.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/toll_booth.png" ng-checked="currentEntity.icon == 'static/icons/toll_booth.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/nursery.png" ng-checked="currentEntity.icon == 'static/icons/nursery.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/school.png" ng-checked="currentEntity.icon == 'static/icons/school.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/university.png" ng-checked="currentEntity.icon == 'static/icons/university.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/cafe.png" ng-checked="currentEntity.icon == 'static/icons/cafe.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/fastfood.png" ng-checked="currentEntity.icon == 'static/icons/fastfood.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/restaurant.png" ng-checked="currentEntity.icon == 'static/icons/restaurant.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           		
	           			<td><input type="radio" value="static/icons/doctors.png" ng-checked="currentEntity.icon == 'static/icons/doctors.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/hospital.png" ng-checked="currentEntity.icon == 'static/icons/hospital.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/pharmacy.png" ng-checked="currentEntity.icon == 'static/icons/pharmacy.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/deciduous.png" ng-checked="currentEntity.icon == 'static/icons/deciduous.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/grasss.png" ng-checked="currentEntity.icon == 'static/icons/grass.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/hills.png" ng-checked="currentEntity.icon == 'static/icons/hills.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/scrub.png" ng-checked="currentEntity.icon == 'static/icons/scrub.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/swamp.png" ng-checked="currentEntity.icon == 'static/icons/swamp.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/atm.png" ng-checked="currentEntity.icon == 'static/icons/atm.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/bank.png" ng-checked="currentEntity.icon == 'static/icons/bank.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/christian3.png" ng-checked="currentEntity.icon == 'static/icons/christian3.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/islamic3.png" ng-checked="currentEntity.icon == 'static/icons/islamic3.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/jewish3.png" ng-checked="currentEntity.icon == 'static/icons/jewish3.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/unknow.png" ng-checked="currentEntity.icon == 'static/icons/unknow.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/cave.png" ng-checked="currentEntity.icon == 'static/icons/cave.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/crane.png" ng-checked="currentEntity.icon == 'static/icons/crane.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/mine.png" ng-checked="currentEntity.icon == 'static/icons/mine.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/peak2.png" ng-checked="currentEntity.icon == 'static/icons/peak2.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/place_city.png" ng-checked="currentEntity.icon == 'static/icons/place_city.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/tower_communication.png" ng-checked="currentEntity.icon == 'static/icons/tower_communications.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/tower_lookout.png" ng-checked="currentEntity.icon == 'static/icons/tower_lookout.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/tower_power.png" ng-checked="currentEntity.icon == 'static/icons/tower_power.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/tower_water.png" ng-checked="currentEntity.icon == 'static/icons/tower_water.png'" name="layerIcon" ng-model="currentEntity.icon"></td>

	           			<td><input type="radio" value="static/icons/station_coal.png" ng-checked="currentEntity.icon == 'static/icons/station_coal.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/station_gas.png" ng-checked="currentEntity.icon == 'static/icons/station_gas.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/station_solar.png" ng-checked="currentEntity.icon == 'static/icons/station_solar.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/station_wind.png" ng-checked="currentEntity.icon == 'static/icons/station_wind.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
					</tr>
	           		
	           		<tr>
	           			<td class="icon"><img src="<c:url value="/static/icons/car_repair.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/car.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/clothes.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/computer.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/diy.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/fish.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/florist.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/garden_centre.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/jewelry2.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/motorcycle.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/music.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/pet2.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/photo.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/tackle.png"/>" width="25" height="25"></td>    
	           			<td class="icon"><img src="<c:url value="/static/icons/tobacco.png"/>" width="25" height="25"></td>    

						<td class="icon"><img src="<c:url value="/static/icons/canoe.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/diving.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/gym.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/hillclimbing.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/jetski.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/motorracing.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/sailing.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/soccer.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/swimming_outdoor.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/tennis.png"/>" width="25" height="25"></td>  
						<td class="icon"><img src="<c:url value="/static/icons/windsurfing.png"/>" width="25" height="25"></td>  
	           			       			
	           		</tr>
					
					<tr>
	           			<td><input type="radio" value="static/icons/car_repair.png" ng-checked="currentEntity.icon == 'static/icons/car_repair.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/car.png" ng-checked="currentEntity.icon == 'static/icons/car.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/clothes.png" ng-checked="currentEntity.icon == 'static/icons/clothes.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/computer.png" ng-checked="currentEntity.icon == 'static/icons/computer.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/diy.png" ng-checked="currentEntity.icon == 'static/icons/diy.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/fish.png" ng-checked="currentEntity.icon == 'static/icons/fish.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/florist.png" ng-checked="currentEntity.icon == 'static/icons/florist.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/garden_centre.png" ng-checked="currentEntity.icon == 'static/icons/garden_centre.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/jewelry2.png" ng-checked="currentEntity.icon == 'static/icons/jewelry.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/motorcycle.png" ng-checked="currentEntity.icon == 'static/icons/motorcycle.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/music.png" ng-checked="currentEntity.icon == 'static/icons/music.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/pet2.png" ng-checked="currentEntity.icon == 'static/icons/pet2.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/photo.png" ng-checked="currentEntity.icon == 'static/icons/photo.png'" name="layerIcon" ng-model="currentEntity.icon"></td>	           			
	           			<td><input type="radio" value="static/icons/tackle.png" ng-checked="currentEntity.icon == 'static/icons/tackle.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/tobacco.png" ng-checked="currentEntity.icon == 'static/icons/tobacco.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			
	           			<td><input type="radio" value="static/icons/canoe.png" ng-checked="currentEntity.icon == 'static/icons/canoe.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/diving.png" ng-checked="currentEntity.icon == 'static/icons/diving.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/gym.png" ng-checked="currentEntity.icon == 'static/icons/gym.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/hillclimbing.png" ng-checked="currentEntity.icon == 'static/icons/hillclimbing.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/jetski.png" ng-checked="currentEntity.icon == 'static/icons/jetski.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/motorracing.png" ng-checked="currentEntity.icon == 'static/icons/motorracing.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/sailing.png" ng-checked="currentEntity.icon == 'static/icons/sailing.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/soccer.png" ng-checked="currentEntity.icon == 'static/icons/soccer.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           			<td><input type="radio" value="static/icons/swimming_outdoor.png" ng-checked="currentEntity.icon == 'static/icons/swimming_outdoor.png'" name="layerIcon" ng-model="currentEntity.icon"></td>	           			
	           			<td><input type="radio" value="static/icons/tennis.png" ng-checked="currentEntity.icon == 'static/icons/tennis.png'" name="layerIcon" ng-model="currentEntity.icon"></td>	           		
	           			<td><input type="radio" value="static/icons/windsurfing.png" ng-checked="currentEntity.icon == 'static/icons/windsurfing.png'" name="layerIcon" ng-model="currentEntity.icon"></td>
	           		</tr>
           		
           	</table>
           	
           	<br>

			<div class="form-item">
                <b class="detail-label"><spring:message code="admin.layer-config.Symbology"/></b>
                <br>
                <img ng-if="currentEntity.dataSource.url"style="border: solid 1px #c9c9c9;" ng-src="{{currentEntity.legend}}"/>
                <img style="border: 1px solid #000" ng-if="!currentEntity.dataSource.url && currentEntity.dataSource.id " src="<c:url value="/static/images/symbology.image"/>">
            </div>
            <br>
            
			<div class="form-item"  ng-if="currentEntity.grupoCamadas != null" style="width: 500px;">
                <b class="detail-label"><spring:message code="admin.layer-config.Layer-group"/></b>
                <br>
                <span class="detail-value">{{currentEntity.layerGroup.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label" style="margin-bottom: 10px; display: inline-block;"><spring:message code="admin.layer-config.Viewing-scale"/></b>
                <br>
                <div class="position-relative" scale-slider slider-disabled="true" ng-model="layers" style="width: 350px;">
                </div>
                <div style="width: 350px;">
                    <label style="float: left">{{layers.values[0]}}</label>
                    <label style="float: right;">{{layers.values[1]}}</label>
                </div>
            </div>
            <br>

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" style="width: 20px;"
                       ng-model="currentEntity.startEnabled"
                       ng-disabled="true"> <label><spring:message code="admin.layer-config.Start-allowed-in-map"/></label>
            </div>

            <br />

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.startVisible"
                       ng-disabled="currentState == DETAIL_STATE"> <label><spring:message code="admin.layer-config.Available-in-the-layers-menu"/></label>

            </div>
            
            <br />

            <div class="form-item position-relative" style="width: 300px;"
                 ng-if="currentState">
                <input type="checkbox" id="grupo" style="width: 20px;"
                       ng-model="currentEntity.enabled"
                       ng-disabled="currentState == DETAIL_STATE"> <label>Disponível para receber postagens</label>

            </div>

     
        </div>
    </form>
</div>
</html>
