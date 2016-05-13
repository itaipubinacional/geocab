<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<!-- Grupo de camadas - List -->

<head>
    <link href="<c:url value="/static/style/tree/tree-theme.css"/>" type="text/css" rel="stylesheet" />
</head>


<body>
    <div class="row tree">
        <div class="col-lg-6">

            <script type="text/ng-template" id="nodes_renderer.html">
                <div ui-tree-handle class="tree-node tree-node-content" ng-mouseenter="hover = true" ng-mouseleave="hover = false" style="padding: 11px 10px;">

                    <a ng-if="node.icon == null" class="open-tree" data-nodrag ng-click="toggle(this);listLayersGroupByLayerGroupId(this, node)">
                        <span class="glyphicon"
                                ng-class="{'glyphicon-chevron-right': !node.collapsed,
                                            'glyphicon-chevron-down': node.collapsed}">
                        </span>
                    </a>

                    <!--<span class="glyphicon glyphicon-chevron-right glyphicon-without-nodes"></span>-->
                    <span ng-if="node.icon == null" ng-class="'icon-child-node-tree'"></span>

                    <a ng-if="node.icon"><img ng-src="{{node.icon}}" ng-if="!node.nodes.length" style="margin-right: 5px; width: 20px; height: 20px; border: solid 1px #c9c9c9;"/></a>
					<a ng-if="node.legend"><img ng-src="{{node.legend}}" ng-if="node.nodes.length" style="margin-right: 5px; width: 20px; height: 20px; border: solid 1px #c9c9c9;"/></a>

                    {{node.name ? node.name : node.title}}

                    <a ng-if="node.nodes.length || node.nodes.length == 0"
                        ng-show="hover"
                        data-nodrag
                        class="icon itaipu-icon-edit tree-itaipu-icon"
                        ng-if="node.nodes != null"
                        data-nodrag
                        ng-click="editItem(this)"
                        title="<spring:message code="layer-group-popup.Update"/>"
                        style="position: absolute;margin-left: 18px;"></a>

                    <a ng-if="node.nodes.length || node.nodes.length == 0"
                        ng-show="hover"
                        data-nodrag
                        id="delete"
                        class="icon itaipu-icon-delete tree-itaipu-icon"
                        data-nodrag
                        ng-click="removeItem(this)"
                        title="<spring:message code="layer-group-popup.Delete" />"
                        style="position: absolute;margin-left: 48px;"></a>
                </div>
                <ol ui-tree-nodes="" class="children-nodes" ng-model="node.nodes" ng-class="{hidden: collapsed}">
                    <li ng-repeat="node in node.nodes" ui-tree-node ng-include="'nodes_renderer.html'" ng-show="visible(node)">
                    </li>
                </ol>
            </script>
            <div ui-tree="treeOptions" id="tree-root">
                <ol ui-tree-nodes ng-model="groupsUpper">
                    <li ng-repeat="node in groupsUpper" ui-tree-node ng-include="'nodes_renderer.html'" ng-show="visible(node)"></li>
                </ol>
            </div>
        </div>

    </div>

    <%--{{ groupsUpper | json }}--%>

</body>

</html>