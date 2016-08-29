<!DOCTYPE html>
<html ng-app="cp">
<head id="myHead" lang="it">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Play&Go - Premi</title>
<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/xeditable.css" rel="stylesheet">
<link href="css/modaldialog.css" rel="stylesheet">
<link href="css/gg_style.css" rel="stylesheet">
<link href="css/angular-socialshare.css" rel="stylesheet">
<link href="img/gamification.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<script src="lib/platform_twitter_widgets.js"></script>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="lib/angular.js"></script>
<script src="js/localize.js" type="text/javascript"></script>
<script src="js/dialogs.min.js" type="text/javascript"></script>
<script src="lib/angular-route.js"></script>
<script src="lib/angular-sanitize.js"></script>
<script src="lib/angular-socialshare.js"></script>

<script src="i18n/angular-locale_it-IT.js"></script>
<!-- <script src="i18n/angular-locale_en-EN.js"></script> -->

<script src="js/app.js?1001"></script>
<script src="js/controllers/ctrl.js?1001"></script>
<script src="js/controllers/ctrl_login.js?1000"></script>
<script src="js/controllers/ctrl_main.js?1000"></script>

<script src="js/filters.js?1001"></script>
<script src="js/services.js?1001"></script>
<script src="js/directives.js"></script>
<script src="lib/ui-bootstrap-tpls.min.js"></script>

<!-- optional libraries -->
<script src="lib/angular-resource.min.js"></script>
<script src="lib/angular-cookies.min.js"></script>
<script src="lib/angular-route.min.js"></script>
<script src="lib/xeditable.min.js"></script>
<script src="lib/angular-base64.min.js"></script>
<base href="/gamificationweb/" />

<script>
var token="<%=request.getAttribute("token")%>";
var userId="<%=request.getAttribute("user_id")%>";
var conf_gameid="<%=request.getAttribute("gameid")%>";
var conf_point_types="<%=request.getAttribute("point_types")%>";
var conf_chall_messages="<%=request.getAttribute("challenge_desc_messages")%>";
var conf_week_sponsor_data="<%=request.getAttribute("week_sponsor_data")%>";
var conf_is_test="<%=request.getAttribute("isTest")%>";
var conf_is_short_classification="<%=request.getAttribute("isShortClassification")%>";
var conf_short_classification_size="<%=request.getAttribute("short_classification_size")%>";
var conf_wsresult="<%=request.getAttribute("wsresult")%>";
var conf_prizes="<%=request.getAttribute("prizes")%>";
</script>
</head>
<body>
	<div class="container" ng-controller="MainCtrl">
		<div id="big-prizes-page">
			<div class="row">
				<div ng-class="col-md-10">
					<div id="my-rules-head-title" align="center">
						{{ 'left_menu-prizes' | i18n }}
					</div>
					<div class="panel panel-success-trasparent" align="justify" ng-init="checkWeekPrizeList()" ng-show="p_WeekNum > 0">
					   <div class="panel-body">
						<h4>PREMI SETTIMANALI</h4>
						<div align="justify">
							I premi per la {{ p_WeekNum }}^a settimana di gioco sono:<br/>
							<ul ng-repeat="p in corrPrizesList">
								<li><strong>{{ p.prize }}</strong>: {{ p.classification }};</li>
							</ul>
						</div>
					   </div>		
					</div>
					<div class="panel panel-success-trasparent" align="justify">
					   <div class="panel-body">
						<h4>PREMI FINALI</h4>
						<div align="justify">
							Alla fine del Gioco, ogni Giocatore ricever&agrave; un attestato che riporter&agrave; i risultati raggiunti.
							I nove giocatori Giocatori in testa alla <strong>classifica globale a punti Green Leaves</strong> (vedasi "FUNZIONAMENTO DEL GIOCO") alla fine del Gioco riceveranno i seguenti premi:<br/>
							<ul>
								<li>1) primo classificato: <strong>n. 1 soggiorno di tre giorni e due notti per due persone in una struttura alberghiera di 4 stelle del territorio;</strong>;</li>
								<li>2) secondo classificato: <strong>n. 1 buono di quattro biglietti per lo spettacolo di Natale al Teatro Sociale</strong>;</li>
								<li>3) terzo classificato: <strong>n.1 abbonamento Trentino Volley</strong>.</li>
								<li>4) quarto classificato: <strong>n.1 abbonamento Trentino Rosa</strong>.</li>
								<li>5) quinto classificato: <strong>maglia e pallone autografati dai giocatori dell'Aquila basket</strong>.</li>
								<li>6) sesto classificato: <strong>n.1 tessera individuale ingresso al Muse con profilo MiniTrib&ugrave;' (1 genitore + figli)</strong>.</li>
								<li>7) settimo classificato: <strong>n.1 abbonamento singolo al car sharing</strong>.</li>
								<li>8) ottavo classificato: <strong>n.1 volo in aereo di 10 min sulla citt&agrave; di Trento</strong>.</li>
								<li>9) nono classificato: <strong>n.1 abbonamento giornaliero per sciare sul Bondone</strong>.</li>
							</ul>
							Una premiazione, con consegna degli attestati e dei premi avverr&agrave; presso e a cura del Comune di Trento al termine del Gioco.<br/><br/>
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
		<div id="small-prizes-page">
			<div class="row">
				<div ng-class="col-xs-12">
					<div id="my-rules-head-title" align="center">
						{{ 'left_menu-prizes' | i18n }}
					</div>
					<div class="panel panel-success-trasparent" align="justify" ng-init="checkWeekPrizeList()" ng-show="p_WeekNum > 0">
					   <div class="panel-body">
						<h4>PREMI SETTIMANALI</h4>
						<div align="justify">
							I premi per la {{ p_WeekNum }}^a settimana di gioco sono:<br/>
							<ul ng-repeat="p in corrPrizesList">
								<li><strong>{{ p.prize }}</strong>: {{ p.classification }};</li>
							</ul>
						</div>
					   </div>		
					</div>
					<div class="panel panel-success-trasparent" align="justify">
					   <div class="panel-body panel-body-nopadding">
						<h4>PREMI FINALI</h4>
						<div align="justify">
							Alla fine del Gioco, ogni Giocatore ricever&agrave; un attestato che riporter&agrave; i risultati raggiunti.
							I nove giocatori Giocatori in testa alla <strong>classifica globale a punti Green Leaves</strong> (vedasi "FUNZIONAMENTO DEL GIOCO") alla fine del Gioco riceveranno i seguenti premi:<br/>
							<ul>
								<li>1) primo classificato: <strong>n. 1 soggiorno di tre giorni e due notti per due persone in una struttura alberghiera di 4 stelle del territorio;</strong>;</li>
								<li>2) secondo classificato: <strong>n. 1 buono di quattro biglietti per lo spettacolo di Natale al Teatro Sociale</strong>;</li>
								<li>3) terzo classificato: <strong>n.1 abbonamento Trentino Volley</strong>.</li>
								<li>4) quarto classificato: <strong>n.1 abbonamento Trentino Rosa</strong>.</li>
								<li>5) quinto classificato: <strong>maglia e pallone autografati dai giocatori dell'Aquila basket</strong>.</li>
								<li>6) sesto classificato: <strong>n.1 tessera individuale ingresso al Muse con profilo MiniTrib&ugrave;' (1 genitore + figli)</strong>.</li>
								<li>7) settimo classificato: <strong>n.1 abbonamento singolo al car sharing</strong>.</li>
								<li>8) ottavo classificato: <strong>n.1 volo in aereo di 10 min sulla citt&agrave; di Trento</strong>.</li>
								<li>9) nono classificato: <strong>n.1 abbonamento giornaliero per sciare sul Bondone</strong>.</li>
							</ul>
							Una premiazione, con consegna degli attestati e dei premi avverr&agrave; presso e a cura del Comune di Trento al termine del Gioco.<br/><br/>
							<!-- I premi finali sono gentilmente offerti da:<br/><br/>
							<div class="row" align="center">
								<div class="col-xs-12">
									<a href="http://www.2001team.com/rovereto/" title="centro natatorio leno" alt="centro natatorio leno" target="_blank">
										<img src="img/centro_natatorio.png" width="200px">
									</a>
								</div>
								<div class="col-xs-12">
									<a href="http://www.tettamantibike.it" title="tettamanti bike" alt="tettamanti bike" target="_blank">
										<img src="img/tettamanti.png" width="250px">
									</a>
								</div>
							</div> -->
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
	</div>
</body>
</html>