<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Users - Detail -->
<div>
    <form>
        <div class="content-tab">
            <div class="form-item">
                <b class="detail-label"><spring:message code="admin.users.Name"/></b>
                <br>
                <span class="detail-value">{{currentEntity.name}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="admin.users.E-mail"/></b>
                <br>
                <span class="detail-value">{{currentEntity.email}}</span>
            </div>
            <br>

            <div class="form-item">
                <b class="detail-label"><spring:message code="admin.users.Access-profile"/></b>
                <br>
                <span class="detail-value">{{roleTranslated()}}</span>
            </div>
            <br>
             <div class="form-item">
                <b class="detail-label"><spring:message code="admin.users.Status"/></b>
                <br>
                <span class="detail-value">{{ stateTranslated() }}</span>
            </div>
    </form>
</div>
</html>
