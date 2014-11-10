<!DOCTYPE html>
<html ng-app="cp">
<head lang="it">
<meta charset="utf-8" />
<!-- <title>{{ 'app_tab-title' | i18n }}</title> -->
<title>Gamification</title>

<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="css/xeditable.css" rel="stylesheet" />
<link href="css/modaldialog.css" rel="stylesheet" />
<link href="img/myweb.ico" rel="shortcut icon" type="image/x-icon" />

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

</head>

<body>
	<div class="container" align="center"><!-- ng-controller="LoginCtrl" ng-init="checkLogin()" -->
		<div class="row" style="margin-top: 50px;">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="panel panel-default" >
	  				<div class="panel-body">
	<!-- 				<div class="row" style="height: 800px"> -->
						<div style="margin: 10px 10px 10px 10px">
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
							<div style="margin: 20px" align="center">
	<!-- 							<h2>MyWeb</h2> -->
								<img src="img/gamification_med.jpeg" alt="Logo gamification" title="Logo gamification" />
							</div>
							<div class="well">
							<p align="justify">Caro Utente, benvenuto in <b>Gamification WEB</b>, il Portale dove puoi tenere sotto controllo il tuo profilo e la classifica generale del gioco integrato nell' app di <b>Rovereto Explorer</b>. Effettua il login per visualizzare i dati.<br></p>
							<table class="table" style="width: 98%">
								<tr>
									<!--<td align="center"><a id="btn_login_prod" href="adc_login" class="btn btn-primary" role="button" ng-click="getLogin()" disabled="false">Procedi con l'autenticazione</a></td> ng-show="isIe10!=true && isLoginShowed!=false" -->						
									<td align="center"><a id="btn_login_test" href="login" class="btn btn-default" role="button" ng-click="getOldLogin()" disabled="false">Login</a></td>
								</tr>
								<tr>
									<td colspan="1">&nbsp;</td>
								</tr>
							</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-md-2"></div>
		</div>
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<hr>
				<footer>
<!-- 				<p>&copy; SmartCampus 2013</p> -->
				</footer>
			</div>
			<div class="col-md-2"></div>
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