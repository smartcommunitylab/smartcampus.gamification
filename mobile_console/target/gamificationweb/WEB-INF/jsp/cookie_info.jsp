<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<html ng-app="cp">
<head lang="it">
<meta charset="utf-8" />
<!-- <title>{{ 'app_tab-title' | i18n }}</title> -->
<title>Cookie Info</title>

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
<base href="/myweb/" />
</head>
<body>
	<div class="container">
		<div class="row" style="margin-top: 50px;">
			<div ng-class="col-md-10">
				<div class="panel panel-default" >
	  				<div class="panel-body">
						<div style="margin: 10px 10px 10px 10px">
							<div style="margin: 20px" align="center">
								<table width="100%">
							    	<tr>
							    		<td width="45%" align="right"><img src="img/myweb4_small.png" alt="Logo myWeb" title="Logo myWeb" height="90" width="180" /></td>
							    		<td width="55%" valign="middle"><h2>Come abilitare i cookie</h2></td>
							    	</tr>
							    </table>
							</div>
							<div class="well">
								
								<div class="panel panel-primary">
									<div class="panel-heading">
							    		<table width="100%">
							    			<tr>
							    				<td width="5%" align="center"><img src="img/icona_ie.png" alt="Logo ie" title="Logo ie" height="34" width="34" /></td>
							    				<td width="95%" valign="middle"><h3 class="panel-title">Cookie in Internet Explorer</h3></td>
							    			</tr>
							    		</table>
							  		</div>
									<div class="panel-body">
										<ol>
											<li>Accedere al browser <b>Internet Explorer</b>.</li>
											<li>Fare clic sul pulsante <b>Strumenti</b> e quindi su <b>Opzioni Internet</b>.</li>
											<li>Fare clic sulla scheda <b>Privacy</b> e in <b>Impostazioni</b> spostare il dispositivo di scorrimento verso l'alto per bloccare tutti i cookie oppure verso il basso per consentirli tutti e quindi fare clic su <b>OK</b>.</li>
										</ol>
									</div>
								</div>
								<div class="panel panel-primary">
									<div class="panel-heading">
										<table width="100%">
							    			<tr>
							    				<td width="5%" align="center"><img src="img/icona_chrome.png" alt="Logo chrome" title="Logo chrome" /></td>
							    				<td width="95%" valign="middle"><h3 class="panel-title">Cookie in Chrome</h3></td>
							    			</tr>
							    		</table>
							  		</div>
									<div class="panel-body">
										<ol>
											<li>Fare clic sull'icona del menu Chrome e selezionare <b>Impostazioni</b>.</li>
											<li>Nella parte inferiore della pagina, fare clic su <b>Mostra</b> impostazioni avanzate.</li>
											<li>Nella sezione "Privacy", fare clic su <b>Impostazioni contenuti</b>.</li>
											<li>Per attivare i cookie, selezionare <b>Consenti il salvataggio dei dati in locale (consigliata)</b>.</li>
											<li>Fare clic su <b>Fine</b> per salvare.</li>
										</ol>
									</div>
								</div>
								<div class="panel panel-primary">
									<div class="panel-heading">
										<table width="100%">
							    			<tr>
							    				<td width="5%" align="center"><img src="img/icona_firefox.png" alt="Logo firefox" title="Logo firefox" height="34" width="34" /></td>
							    				<td width="95%" valign="middle"><h3 class="panel-title">Cookie in Firefox</h3></td>
							    			</tr>
							    		</table>
							  		</div>
									<div class="panel-body">
										<ol>
											<li>Fare clic sul pulsante del menu di Firefox e selezionare <b>Preferenze</b>.</li>
											<li>Selezionare il pannello "Privacy".</li>
											<li>Alla voce <b>Impostazioni cronologia</b>: selezionare utilizza <b>impostazioni personalizzate</b>. </li>
											<li>Per attivare i cookie, contrassegnare la voce <b>Accetta i cookie dai siti</b>.</li>
										</ol>
									</div>
								</div>
								<div class="panel panel-primary">
									<div class="panel-heading">
										<table width="100%">
							    			<tr>
							    				<td width="5%" align="center"><img src="img/icona_safari.png" alt="Logo Safari" title="Logo Safari" /></td>
							    				<td width="95%" valign="middle"><h3 class="panel-title">Cookie in Safari</h3></td>
							    			</tr>
							    		</table>
							  		</div>
									<div class="panel-body">
										<ol>
											<li>Avviare il Browser Safari:</b>.</li>
											<li>Selezionare la voce <b>Preferenze</b> dal menu.</li>
											<li>Dalla finestra di dialogo che segue, selezionare il tab <b>Sicurezza</b>.</li>
											<li>Dalla voce <b>Accetta Cookie</b> selezionare l'opzione <b>Solo dai siti che stai consultando</b>.</li>
										</ol>
									</div>
								</div>	
							</div>
							<table class="table" style="width: 100%">
								<tr>
									<td align="left" width="33%"></td>
									<td align="center" width="33%"></td>
									<td align="right" width="33%"><a id="btn_login_test" href="prelogin" class="btn btn-primary" role="button" ng-click="getOldLogin()">Indietro</a></td>
								</tr>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<hr>
				<footer>
				</footer>
			</div>
		</div>
	</div>
</body>
</html>