<!DOCTYPE html>
<html ng-app="cp">
<head lang="it">
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<!-- <title>{{ 'app_tab-title' | i18n }}</title> -->
<title>Green Game</title>

<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="css/xeditable.css" rel="stylesheet" />
<link href="css/modaldialog.css" rel="stylesheet" />
<link href="img/gamification.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="lib/angular.js"></script>
<script src="js/localize.js" type="text/javascript"></script>
<script src="lib/angular-route.js"></script>
<script src="lib/angular-sanitize.js"></script>
<script src="lib/ui-bootstrap-tpls.min.js"></script>
<script src="js/dialogs.min.js" type="text/javascript"></script>
<script src="js/app.js"></script>
<script src="js/controllers/ctrl.js"></script>
<script src="js/controllers/ctrl_login.js"></script>
<script src="js/controllers/ctrl_main.js"></script>
<script src="js/filters.js"></script>
<script src="js/services.js"></script>
<script src="js/directives.js"></script>

<!-- optional libraries -->
<script src="lib/angular-resource.min.js"></script>
<script src="lib/angular-cookies.min.js"></script>
<script src="lib/angular-route.min.js"></script>
<script src="lib/xeditable.min.js"></script>
<script src="lib/angular-base64.min.js"></script>
<base href="/gamificationweb/" />

<script type="text/javascript">
	
	var cookieEnabled = (navigator.cookieEnabled) ? true : false;	
	if (typeof navigator.cookieEnabled == "undefined" && !cookieEnabled)
	{ 
		document.cookie="testcookie";
		cookieEnabled = (document.cookie.indexOf("testcookie") != -1) ? true : false;
	}
	
</script>
<style type="text/css">
	.panel {
		background: url(img/paginaAccesso-56-56-56.svg) no-repeat center center fixed;
	}
</style>

</head>

<body>
	<div class="container" align="center"><!-- ng-controller="LoginCtrl" ng-init="checkLogin()" -->
		<div class="row" style="margin-top: 20px">
			<div class="col-md-8 col-md-offset-2">
				<div class="panel panel-default" background="img/paginaAccesso-56-56-56.svg">
	  				<div class="panel-body">
	<!-- 				<div class="row" style="height: 800px"> -->
						<div style="margin: 5px 3px 5px 3px">
							<!--[if lt IE 9]>
							<div class="row" style="height: 20px" align="center" ng-init="hideLogin()">
								<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer 8 e versioni inferiori. Aggiorna Internet Explorer ad un versione successiva o utilizza un altro browser per accedere al portale.</font></h4>
							</div>
							<![endif]-->
							<div class="row" style="font-size: 18px; color: red" align="center" id="cookies">
							</div>
							<div class="row" style="height: 20px" align="center" ng-show="isIe10==true">
								<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer. Utilizza un altro browser per accedere al portale.</font></h4>
							</div>
							<div style="margin: 50px 5px 5px 5px" align="center">
								<h1>
									<font face="Raleway-bold" size="48" color="gray"><strong>GreenGame</strong></font>
								</h1>
								<h2>
									<font face="Raleway">con ViaggiaRovereto</font>
								</h2>
								<!-- <img src="img/gamification_med.jpeg" alt="Logo gamification" title="Logo gamification" /> -->
							</div>
							<div class="row" style="height: 150px; margin-top: 80px" align="center">
								&nbsp;
								&nbsp;
								<a id="btn_login_test" href="login" class="btn btn-success btn-lg" role="button" ng-click="getOldLogin()" disabled="false"><font face="Raleway" size="4"><strong>Accedi con Google</strong></font></a>
							</div>
							<div class="row" align="center">
								<img src="img/lineaPaginaAccesso-57.svg" width="95%" alt="" title="" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<!-- <hr> -->
				<footer>
			<!-- <p>&copy; SmartCampus 2013</p> -->
				</footer>
			</div>
		</div>
	</div>

<script type="text/javascript">
	if(!cookieEnabled){
		document.getElementById("cookies").innerHTML = "Il tuo browser sembra non avere i cookie attivi. E' necessario attivarli per utilizzare il portale. Clicca <a href='cookie_info'>QUI</a> per maggiori informazioni";
		$("#btn_login_prod").attr("disabled", "disabled");
	 	$("#btn_login_console").attr("disabled", "disabled");
	 	$("#btn_login_test").attr("disabled", "disabled");
	} else {
		$("#btn_login_prod").removeAttr("disabled");
	 	$("#btn_login_console").removeAttr("disabled");
	 	$("#btn_login_test").removeAttr("disabled");
	}
</script>

</body>

</html>