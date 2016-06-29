<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<div>
	<form name="form" novalidate default-button="buttonUpdate">

		<div class="content-tab" style="padding-bottom: 14px;">

			<!-- BACKGROUND MAP -->

			<div class="column" style="margin-top: -7px;">

				<div class="form-item position-relative"
					style="margin: 12px 0px 4px 0;">
					<div class="col-md-12">
						<label style="cursor:pointer;">
							<input style="width: 50px;" ng-model="configurationCurrentEntity.stopSendEmail" type="checkbox"> 
							<span style="position: fixed; margin-top: -19px;margin-left: 40px;">Interromper envio de emails?</span>
						</label>
					</div>
				</div>
<!-- 				<button style="float: right; margin-top: 6px;" class="btn btn-success" -->
<%-- 					id="buttonUpdate" title="<spring:message code='admin.users.Save'/>" --%>
<!-- 					ng-click="updateConfiguration()"> -->
<%-- 					<spring:message code="admin.users.Save" /> --%>
<!-- 				</button> -->

			</div>
		</div>
	</form>
</div>
</html>
