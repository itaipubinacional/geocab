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
					style="width: 300px; margin: 12px 0px 4px 0;">
					<div class="col-md-2">
						<input ng-model="configurationCurrentEntity.stopSendEmail" type="checkbox">
					</div>
					<div class="col-md-10">
						<label class="detail-label">Interromper envio de emails?</label>
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
