<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<!-- Data source - Main View -->
<div class="content">

  <div class="navbar">
      <!--Message -->
      <div class="msg" ng-include="'static/libs/eits-directives/alert/alert.html'"></div>

    <div class="navbar-inner navbar-container">
      <div class="navbar-title"><span class="ng-scope" style="text-transform:uppercase;"><spring:message
          code="contact.Contact"/></span></div>
    </div>
  </div>


  <form name="form" ng-submit="sendForm()" role="form">
   
    <div>

        <div class="form-item position-relative" style="width: 300px;">
        <label class="detail-label" required><spring:message code="Name"/></label>
        <input name="name" type="text" class="form-control"
               ng-model="contactForm.name"
               ng-disabled="user.enabled"
               required maxlength="144" ng-minlength="1"
               ng-class="{ ngInvalid: form.name.$error.required && (form.$submitted || form.name.$dirty) }"
               autofocus
               autocomplete="off"
               ng-hover>

                <span ng-show="form.name.$error.required && (form.$submitted || form.name.$dirty)"
                      class="tooltip-validation"><spring:message code="admin.users.Name"/> <spring:message
                    code="admin.users.required"/></span>
      </div>
      <div class="form-item position-relative" style="width: 300px;">
        <label class="detail-label" required>Email</label>
        <input name="email" type="email" class="form-control"
               ng-model="contactForm.email"
               ng-disabled="user.enabled"
               required maxlength="144" ng-minlength="1"
               ng-class="{ ngInvalid: form.email.$error.required && (form.$submitted || form.name.$dirty) }"
               autofocus
               autocomplete="off"
               ng-hover>
                   <span ng-show="form.email.$error.required && (form.$submitted || form.email.$dirty)"
                         class="tooltip-validation">Email <spring:message
                       code="admin.users.required"/></span>
      </div>
    </div>
    <div>
      <div class="form-item position-relative" style="width: 300px;">
        <label class="detail-label" required><spring:message code="contact.Subject"/></label>
        <input name="subject" type="text" class="form-control"
               ng-model="contactForm.subject"
               required maxlength="144" ng-minlength="1"
               ng-class="{ ngInvalid: form.subject.$error.required && (form.$submitted || form.name.$dirty) }"
               autofocus
               autocomplete="off"
               ng-hover>
                   <span ng-show="form.subject.$error.required && (form.$submitted || form.subject.$dirty)"
                         class="tooltip-validation"><spring:message code="contact.Subject"/> <spring:message
                       code="admin.users.required"/></span>
      </div>
    </div>

      <div class="form-item position-relative" style="width: 300px;">
          <label class="detail-label" required>
              <spring:message code="contact.Message"/>
          </label>
                <textarea name="message" type="text" class="form-control"
                          style="height: 100px"
                          ng-model="contactForm.message"
                          required maxlength="144" ng-minlength="1"
                          ng-class="{ ngInvalid: form.message.$error.required && (form.$submitted || form.name.$dirty) }"
                          autofocus
                          autocomplete="off"
                          ng-hover>
                 </textarea>
                  <span ng-show="form.message.$error.required && (form.$submitted || form.message.$dirty)"
                        class="tooltip-validation"><spring:message code="contact.Message"/> <spring:message
                          code="admin.users.required"/>
                  </span>
      </div>

    <div>
      
      <div ng-show="!user.email">
      <div class="form-item position-relative" style="width: 300px;">
        <label class="detail-label" required><spring:message code="contact.securityVerification"/></label>
        <input name="subject" type="text" class="form-control"
               ng-model="contactForm.answer"
               maxlength="144" ng-minlength="1"
               ng-class="{ ngInvalid: form.subject.$error.required && (form.$submitted || form.name.$dirty) }"
               autofocus
               autocomplete="off"
               ng-hover>
                   <span ng-show="form.subject.$error.required && (form.$submitted || form.subject.$dirty)"
                         class="tooltip-validation"><spring:message code="contact.securityVerification"/> <spring:message
                       code="admin.users.required"/></span>
      </div>
      <img ng-src="./stickyImg" />
    </div>
		 <button class="btn btn-success"
              id="buttonUpdate"
              title="<spring:message code='contact.Send'/>"
              type="submit">
        <spring:message code="contact.Send"/>
      </button>
    </div>
  </form>


</div>
</html>