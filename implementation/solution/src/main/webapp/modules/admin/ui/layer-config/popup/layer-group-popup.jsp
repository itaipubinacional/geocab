<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<div class="modal-content">
	<div class="modal-header">
        <button type="button" class="close" ng-click="close(true)"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
		<h3 class="modal-title">Associar grupo de camada</h3>
	</div>
	<div class="modal-body" ng-init="initialize();"
		style="overflow: visible">
		<table>
			<tr>
				<td style="vertical-align: top">
					<table>
						<tr>
							<td style="vertical-align: top">
								<div
									style="width: 719px;">
									<span ng-if="doing_async">...loading...</span>
									
									<abn-tree tree-data="groups" tree-control="my_tree"
										on-select="my_tree_handler(branch)" expand-level="{{currentGroup.level}}"
										initial-selection="{{currentGroup.label}}"></abn-tree>
										
								</div>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>

	</div>

	<div class="modal-footer">
		<button id="buttonClose" ng-disabled="currentEntity == null" class="btn btn-primary" ng-click="close(false)">Selecionar</button>
        <button class="btn btn-default" ng-click="close(true)">Fechar</button>
	</div>
</div>

<script type="text/javascript">
    /**
     * Jquery usado para nao permitir que a popup seja aberta novamente toda vez que o usuario teclar enter
     */
    $(document).ready(function(){
        $("#hide").focus();
        $("#hide").hide();
    });
</script>

</html>