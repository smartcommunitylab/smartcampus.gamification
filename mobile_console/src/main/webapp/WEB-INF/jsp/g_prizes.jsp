<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<html ng-app="cp">
<head lang="it">
<meta charset="utf-8" />
<!-- <title>{{ 'app_tab-title' | i18n }}</title> -->
<title>Play&Go</title>

<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="css/xeditable.css" rel="stylesheet" />
<link href="css/modaldialog.css" rel="stylesheet" />
<link href="css/gg_style.css" rel="stylesheet">
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
</head>
<body>
	<div id="big-prizes-page" class="container">
		<div class="row" style="margin-top: 50px;">
			<div ng-class="col-md-10">
				<div class="panel panel-success" >
	  				<div class="panel-body">
					<h4>RISULTATI E INCENTIVI</h4>
					<div align="justify">
						Alla fine del Gioco, ogni Giocatore ricever&agrave; un attestato che riporter&agrave; i risultati raggiunti.
						I primi tre Giocatori <strong>nella classifica a punti Green Leaves</strong> (vedasi 
						"FUNZIONAMENTO DEL GIOCO" - al punto I.) alla fine del Gioco riceveranno i 
						seguenti premi, elargiti dal <strong>Comune di Rovereto</strong>:
						<ul>
							<li><strong>Tessera MITT a scalare con valore di 34 Euro (30 di ticket + 4 di attivazione)</strong></li>
						</ul>
						Una premiazione, con consegna degli attestati e dei premi summenzionati, avverr&agrave; presso 
						e a cura del Comune di Rovereto al termine del Gioco, in data luned&igrave; 15/12 p.v. alle ore 18:00.
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
	<div id="small-prizes-page" class="container">
		<div class="row" style="margin-top: 50px;">
			<div ng-class="col-xs-12">
				<div class="panel panel-success" >
	  				<div class="panel-body">
					<h4>RISULTATI E INCENTIVI</h4>
					<div align="justify">
						Alla fine del Gioco, ogni Giocatore ricever&agrave; un attestato che riporter&agrave; i risultati raggiunti.
						I primi tre Giocatori <strong>nella classifica a punti Grean Leaves</strong> (vedasi 
						"FUNZIONAMENTO DEL GIOCO" - al punto I.) alla fine del Gioco riceveranno i 
						seguenti premi, elargiti dal <strong>Comune di Rovereto</strong>:
						<ul>
							<li><strong>Tessera MITT a scalare con valore di 34 Euro (30 di ticket + 4 di attivazione)</strong></li>
						</ul>
						Una premiazione, con consegna degli attestati e dei premi summenzionati, avverr&agrave; presso 
						e a cura del Comune di Rovereto al termine del Gioco, in data luned&igrave; 15/12 p.v. alle ore 18:00.
					</div>
				   </div>	
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12">
				<hr>
				<footer>
				</footer>
			</div>
		</div>
	</div>
</body>
</html>