<!DOCTYPE html>
<html ng-app="cp">
<head lang="it">
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Play&amp;Go - disiscrizione mail</title>

<link href="../../../css/bootstrap.min.css" rel="stylesheet" />
<link href="../../../css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="../../../css/xeditable.css" rel="stylesheet" />
<link href="../../../css/modaldialog.css" rel="stylesheet" />
<link href="../../../css/angular-socialshare.css" rel="stylesheet">
<link href="../../../css/gg_style.css" rel="stylesheet" />
<link href="../../../img/gamification.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<script src="../../../js/jquery.min.js"></script>
<script src="../../../js/bootstrap.min.js"></script>
<script src="../../../lib/angular.js"></script>
<script src="../../../js/localize.js" type="text/javascript"></script>
<script src="../../../lib/angular-route.js"></script>
<script src="../../../lib/angular-sanitize.js"></script>
<script src="../../../lib/angular-socialshare.js"></script>
<script src="../../../lib/ui-bootstrap-tpls.min.js"></script>
<script src="../../../js/dialogs.min.js" type="text/javascript"></script>
<script src="../../../js/app.js"></script>
<script src="../../../js/controllers/ctrl.js"></script>
<script src="../../../js/controllers/ctrl_login.js"></script>
<script src="../../../js/controllers/ctrl_main.js"></script>
<script src="../../../js/filters.js"></script>
<script src="../../../js/services.js"></script>
<script src="../../../js/directives.js"></script>

<!-- optional libraries -->
<script src="../../../lib/angular-resource.min.js"></script>
<script src="../../../lib/angular-cookies.min.js"></script>
<script src="../../../lib/angular-route.min.js"></script>
<script src="../../../lib/xeditable.min.js"></script>
<script src="../../../lib/angular-base64.min.js"></script>
<base href="/gamificationweb/" />

<script type="text/javascript">
	var conf_wsresult="<%=request.getAttribute("wsresult")%>";
	var conf_lang="<%=request.getAttribute("language")%>";
	var token="<%=request.getAttribute("token")%>";
	var userId="<%=request.getAttribute("user_id")%>";
	var conf_gameid="<%=request.getAttribute("gameid")%>";
	var conf_point_types="<%=request.getAttribute("point_types")%>";
	var conf_chall_messages="<%=request.getAttribute("challenge_desc_messages")%>";
	var conf_week_sponsor_data="<%=request.getAttribute("week_sponsor_data")%>";
	var conf_is_test="<%=request.getAttribute("isTest")%>";
	var conf_is_short_classification="<%=request.getAttribute("isShortClassification")%>";
	var conf_short_classification_size="<%=request.getAttribute("short_classification_size")%>";
	var conf_prizes="<%=request.getAttribute("prizes")%>";
	var conf_prizes_eng="<%=request.getAttribute("prizes_eng")%>";
	
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
	<div class="container" align="center" ng-controller="MainCtrl" ><!-- ng-controller="LoginCtrl" ng-init="checkLogin()" -->
		<div class="row" style="margin-top: 20px">
			<div class="col-md-8 col-md-offset-2">
				<div class="panel panel-default" background="img/paginaAccesso-56-56-56.svg">
	  				<div class="panel-body">
						<div style="margin: 5px 3px 5px 3px">
							<div class="row" style="font-size: 18px; color: red" align="center" id="cookies">
							</div>
							<!-- <div class="row" style="height: 20px" align="center" ng-if="isIe10==true">
								<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer. Utilizza un altro browser per accedere al portale.</font></h4>
							</div> -->
							<div style="margin: 50px 5px 5px 5px" align="center" ng-if="itaLang">
								<h1>
									<font face="Raleway-bold" size="48" color="gray"><strong>Play&Go</strong></font>
								</h1>
								<h2>
									<font face="Raleway">con ViaggiaTrento</font>
								</h2>
								<h2>&nbsp;</h2>
								<p class="big-text" align="left" ng-if="wsresult">La tua richiesta di disattivazione delle notifiche via mail <strong> &egrave; andata a buon fine</strong>. Per tanto non riceverai pi&ugrave; alcuna notifica.<br />
								Grazie per la tua partecipazione al gioco ViaggiaTrento Play&amp;Go, continua a muoverti in modo green e ad usare la app!<br /><br />
								&emsp;<em>Lo staff di Play&amp;Go</em>
								</p>
								<p class="big-text" align="left" ng-if="!wsresult">La tua richiesta di disattivazione delle notifiche via mail<strong> non ha avuto esito positivo</strong>. Riprova pi&ugrave; tardi.<br />
								Grazie per la tua partecipazione al gioco ViaggiaTrento Play&amp;Go, continua a muoverti in modo green e ad usare la app!<br /><br />
								&emsp;<em>Lo staff di Play&amp;Go</em>
								</p>
							</div>
							<div style="margin: 50px 5px 5px 5px" align="center" ng-if="!itaLang">
								<h1>
									<font face="Raleway-bold" size="48" color="gray"><strong>Play&Go</strong></font>
								</h1>
								<h2>
									<font face="Raleway">with ViaggiaTrento</font>
								</h2>
								<h2>&nbsp;</h2>
								<p class="big-text" align="left" ng-if="wsresult">Your request to off the mail notification service <strong> is successful</strong>. Therefore you will no longer receive any notification.<br />
								Thanks for your participation in the ViaggiaTrento Play&amp;Go game, keep moving so green and to use the app!<br /><br />
								&emsp;<em>Play&amp;Go staff</em>
								</p>
								<p class="big-text" align="left" ng-if="!wsresult">Your request to off the mail notification service <strong> has failed</strong>. Please try later.<br />
								Thanks for your participation in the ViaggiaTrento Play&amp;Go game, keep moving so green and to use the app!<br /><br />
								&emsp;<em>Play&amp;Go staff</em>
								</p>
							</div>
							<!-- <div class="row" style="height: 150px; margin-top: 80px" align="center">
								&nbsp;
								&nbsp;
								<a id="btn_login_test" href="login" class="btn btn-success btn-lg buttonaccess" role="button" ng-click="getOldLogin()" disabled="false"><font face="Raleway" size="4"><strong>Accedi con Google</strong></font></a>
								&nbsp;
								<a id="btn_login_facebook" href="loginfb" class="btn btn-success btn-lg buttonaccess" role="button" ng-click="getOldLogin()" disabled="false"><font face="Raleway" size="4"><strong>Accedi con Facebook</strong></font></a>
							</div>
							<div class="row" align="center">
								<img src="img/lineaPaginaAccesso-57.svg" width="95%" alt="" title="" />
							</div> -->
							<div class="row" style="margin-right: 10px" align="right">
								&nbsp;
								<a id="btn_login_test" href="enter" class="btn btn-success btn-lg" ng-click="goToApp()" role="button"><font face="Raleway" size="4"><strong>OK</strong></font></a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<footer>
				</footer>
			</div>
		</div>
	</div>

<script type="text/javascript">
	/* if(!cookieEnabled){
		document.getElementById("cookies").innerHTML = "Il tuo browser sembra non avere i cookie attivi. E' necessario attivarli per utilizzare il portale. Clicca <a href='cookie_info'>QUI</a> per maggiori informazioni";
		$("#btn_login_prod").attr("disabled", "disabled");
	 	$("#btn_login_facebook").attr("disabled", "disabled");
	 	$("#btn_login_test").attr("disabled", "disabled");
	} else {
		$("#btn_login_prod").removeAttr("disabled");
	 	$("#btn_login_facebook").removeAttr("disabled");
	 	$("#btn_login_test").removeAttr("disabled");
	} */
</script>

<!-- Codice per accettazione cookie - Inizio -->
<script src="js/cookiechoices.js"></script>
<script>
  document.addEventListener('DOMContentLoaded', function(event) {
    cookieChoices.showCookieConsentBar("I cookie ci aiutano ad erogare servizi di qualita'. Utilizzando i nostri servizi, l'utente accetta le nostre modalita' d'uso dei cookie.",
      'OK', 'MAGGIORI INFORMAZIONI', 'cookie_licence');
  });
</script>

</body>

</html>