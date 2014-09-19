<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Data source - Detail -->
<div>
    <form>
        <div class="content-tab">
            <div class="form-item" style="width: 500px;">
                <b class="detail-label"><spring:message code="Name"/></b>
                <br>
                <span class="detail-value">{{currentEntity.name}}</span>
            </div>
            <br>
            
            <div class="form-item">
                <b class="detail-label"><spring:message code="Address"/></b>
                <br>
                <span class="detail-value">{{currentEntity.url}}</span>
            </div>
            <br>

            <div class="form-item" ng-if="currentEntity.login">
                <b class="detail-label"><spring:message code="Username"/></b>
                <br>
                <span class="detail-value">{{currentEntity.login}}</span>
            </div>
            <br>
            
        </div>
    </form>
</div>
</html>
