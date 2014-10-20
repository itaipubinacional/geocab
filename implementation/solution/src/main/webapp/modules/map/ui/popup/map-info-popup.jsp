<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html>

<div class="modal-content">

	<div class="modal-header">
		<button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
	    <h3 class="modal-title">Informações</h3>
	</div>

	<div class="modal-body" ng-init="initialize();" style="overflow-y: auto; max-height: 550px;">

        <accordion close-others="true" class="accordion-popup">
            <accordion-group ng-repeat="feature in features">
                <accordion-heading>
                    <div style="cursor:pointer; padding: 10px 0;">
                        {{feature.layer.titulo}}
                    </div>
                </accordion-heading>
               <span ng-repeat="(key, value) in feature.campos">
                   <b>{{key}}</b> - {{value}}
                   </br>
               </span>
            </accordion-group>

        </accordion>
        
	</div>
	<div class="modal-footer">
        <button class="btn btn-default" ng-click="close(true)">Fechar</button>
	</div>

</div>

</html>