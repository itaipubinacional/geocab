<!DOCTYPE html>
<html>

<!-- header -->
<div id="header-page">
	<!-- navbar 1 -->
	<div class="navbar navbar-1" style="z-index: 1002;">
		<a class="logo" href="./">&nbsp;</a>
		<div class="nav-collapse collapse">
			<div class="left-side">
				<div style="float: left; margin-top: 23px">
					<span style="font-size: 17px"><b style="font-size: 17px">GEOCAB</b>
						- Cultivando Água Boa</span>
				</div>
			</div>

			<ul class="nav navbar-nav pull-right right-side">
				<li><a href="#" class="active box-separator"
					style="border: none; margin-top: 7px;"> <span
						style="color: #000000" ng-bind="usuarioAutenticado.nomeCompleto"></span>
				</a></li>
				<li class="box-separator"></li>
				<li>
					<div class="user-logout">
						<a onclick="autenticacaoService.deslogar();" href="./autenticacao">Logout</a>
					</div>
				</li>
			</ul>
		</div>
	</div>
	<!-- navbar 2 -->
	<!-- ng-if="usuarioAutenticado.papelUsuario == 'ADMINISTRADOR'" -->
	<div class="navbar navbar-2" style="z-index: 1001;">
		<div class="navbar-inner border-radius-0">

			<div class="nav-collapse collapse">
				<ul class="nav navbar-nav">

					<li class="position-relative"><a href="./"
						style="width: 50px;" ng-class="{active: menuActive == null}"><span
							class="icon-mapa-interativo"></span></a></li>

					<li class="position-relative"><a
						href="admin#/data-source"
						ng-class="{active: menuActive == 'data-source'}"
						style="width: 150px;">Data Source</a></li>

					<li class="position-relative"><a
						href="admin#/layer-group"
						ng-class="{active: menuActive == 'layer-group'}"
						style="width: 150px;">Layer group</a></li>

                    <li class="position-relative"><a
                            href="admin#/layers"
                            ng-class="{active: menuActive == 'layers'}"
                            style="width: 150px;">Layers</a></li>
                </ul>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="assets/libs/activity-dialog.js"></script>

</html>