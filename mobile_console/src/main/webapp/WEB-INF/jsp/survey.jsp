<!DOCTYPE html>
<html ng-app="cp">
<head id="myHead" lang="it">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Play&Go - Questionario finale</title>
<link href="../css/bootstrap.css" rel="stylesheet">
<link href="../css/bootstrap-theme.min.css" rel="stylesheet">
<link href="../css/xeditable.css" rel="stylesheet">
<link href="../css/modaldialog.css" rel="stylesheet">
<link href="../css/gg_style.css" rel="stylesheet">
<link href="../css/angular-socialshare.css" rel="stylesheet">
<link href="../img/gamification.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<script src="../lib/platform_twitter_widgets.js"></script>
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../lib/angular.js"></script>
<script src="../js/localize.js" type="text/javascript"></script>
<script src="../js/dialogs.min.js" type="text/javascript"></script>
<script src="../lib/angular-route.js"></script>
<script src="../lib/angular-sanitize.js"></script>
<script src="../lib/angular-socialshare.js"></script>

<script src="../i18n/angular-locale_it-IT.js"></script>
<!-- <script src="i18n/angular-locale_en-EN.js"></script> -->

<script src="../js/app.js"></script>
<script src="../js/controllers/ctrl.js"></script>
<script src="../js/controllers/ctrl_login.js?1000"></script>
<script src="../js/controllers/ctrl_main.js?1000"></script>

<script src="../js/filters.js?1001"></script>
<script src="../js/services.js?1001"></script>
<script src="../js/directives.js"></script>
<script src="../lib/ui-bootstrap-tpls.min.js"></script>

<!-- optional libraries -->
<script src="../lib/angular-resource.min.js"></script>
<script src="../lib/angular-cookies.min.js"></script>
<script src="../lib/angular-route.min.js"></script>
<script src="../lib/xeditable.min.js"></script>
<script src="../lib/angular-base64.min.js"></script>
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
var conf_lang="<%=request.getAttribute("language")%>";
var conf_prizes="<%=request.getAttribute("prizes")%>";
var conf_prizes_eng="<%=request.getAttribute("prizes_eng")%>";
</script>
</head>
<body>
<div class="container" ng-controller="MainCtrl" ng-init="retrieveSurveyDataForPlayer()">
		<div id="big-privacy-page">
			<div class="row" style="margin-top: 20px">
				<div class="col-md-8 col-md-offset-2">
					<div class="panel panel-default" background="../img/paginaAccesso-56-56-56.svg">
		  				<div class="panel-body">
							<div style="margin: 5px 3px 5px 3px">
								<div class="row" style="font-size: 18px; color: red" align="center" id="cookies">
								</div>
								<!-- <div class="row" style="height: 20px" align="center" ng-if="isIe10==true">
									<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer. Utilizza un altro browser per accedere al portale.</font></h4>
								</div> -->
								<div style="margin: 50px 5px 5px 5px" align="center">
									<h1>
										<font face="Raleway-bold" size="48" color="gray"><strong>Play&Go</strong></font>
									</h1>
									<h2>
										<font face="Raleway">con ViaggiaTrento</font>
									</h2>
									<h2>&nbsp;</h2>
									<p class="big-text" align="left" ng-show="surveyComplete">{{ 'text_survey_ok' | i18n }}<br /><br />
									&emsp;<em>Lo staff di Play&amp;Go</em>
									</p>
									<p class="big-text" align="left" ng-show="!surveyComplete">{{ 'text_survey_ko' | i18n }}<br /><br />
									&emsp;<em>Lo staff di Play&amp;Go</em>
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
				<div class="col-md-12">
					<hr>
					<footer>
					</footer>
				</div>
			</div>
		</div>
		<div id="small-privacy-page">
			<div class="row" style="margin-top: 20px">
				<div class="col-sm-8 col-sm-offset-2">
					<div class="panel panel-default" background="img/paginaAccesso-56-56-56.svg">
		  				<div class="panel-body">
							<div style="margin: 5px 3px 5px 3px">
								<div class="row" style="font-size: 18px; color: red" align="center" id="cookies">
								</div>
								<!-- <div class="row" style="height: 20px" align="center" ng-if="isIe10==true">
									<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer. Utilizza un altro browser per accedere al portale.</font></h4>
								</div> -->
								<div style="margin: 50px 5px 5px 5px" align="center">
									<h1>
										<font face="Raleway-bold" size="48" color="gray"><strong>Play&Go</strong></font>
									</h1>
									<h2>
										<font face="Raleway">con ViaggiaTrento</font>
									</h2>
									<h2>&nbsp;</h2>
									<p class="big-text" align="left" ng-show="surveyComplete">{{ 'text_survey_ok' | i18n }}<br /><br />
									&emsp;<em>Lo staff di Play&amp;Go</em>
									</p>
									<p class="big-text" align="left" ng-show="!surveyComplete">{{ 'text_survey_ko' | i18n }}<br /><br />
									&emsp;<em>Lo staff di Play&amp;Go</em>
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
				<div class="col-xs-12">
					<hr>
					<footer>
					</footer>
				</div>
			</div>
		</div>
	</div>
	<script type="text/ng-template" id="/dialogs/surveyinput.html">
<div class="modal" id="variablesModal" role="dialog">
	<form role="form" name="form">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title"><span class="glyphicon glyphicon-user">
						</span>&nbsp;&nbsp;Questionario di fine gioco&nbsp;&nbsp;
					</h4>
					<button type="button" class="btn btn-danger btn-sm btn-left" ng-click="cancel()"><i class="glyphicon glyphicon-remove"></i></button>
				</div>
				<div class="modal-body">
					<div align="justify">
					Rispondi a queste veloci e semplici domande per offrire un tuo parere sul gioco 'Play&Go'. <br/><br/>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.gaming_exp.$dirty && form.gaming_exp.$invalid]">
						<label class="control-label">1) Come valuteresti la tua esperienza di gioco in queste settimane?</label>
						<select ng-if="showSelect" type="text" name="gaming_exp" class="form-control" ng-model="user.surveyData.gamimg_experience" required>
							<option value="">Seleziona un valore</option>
							<option ng-value="negative_exp">Negativa</option>
							<option ng-value="satisfying_exp">Soddisfacente</option>
							<option ng-value="good_exp">Buona</option>
							<option ng-value="excellent_exp">Ottima</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="negative_exp" required> Negativa</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="satisfying_exp" required> Soddisfacente</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="good_exp" required> Buona</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="excellent_exp" required> Ottima</label></td>
							</tr>
						</table>
						<div ng-show="form.gaming_exp.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.change_of_habits.$dirty && form.change_of_habits.$invalid]">
						<label class="control-label">2) In quale misura il gioco ti ha indotto a cambiare le tue abitudini di mobilità?</label>
						<select ng-if="showSelect" type="text" name="change_of_habits" class="form-control" ng-model="user.surveyData.change_of_habits" required>
							<option value="">Seleziona un valore</option>
							<option ng-value="nothing_val">Per niente</option>
							<option ng-value="little_val">Poco</option>
							<option ng-value="enough_val">Abbastanza</option>
							<option ng-value="much_val">Molto</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="nothing_val" required> Per niente</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="little_val" required> Poco</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="enough_val" required> Abbastanza</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="much_val" required> Molto</label></td>
							</tr>
						</table>
						<div ng-show="form.change_of_habits.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.new_habits_maintaining.$dirty && form.new_habits_maintaining.$invalid]">
						<label class="control-label">3) Se hai cambiato le tue abitudini, continueresti a muoverti come suggerito dalla App anche dopo il gioco?</label>
						<select ng-if="showSelect" type="text" name="new_habits_maintaining" class="form-control" ng-model="user.surveyData.new_habits_maintaining" ng-required="user.surveyData.change_of_habits!=nothing_val">
							<option value="">Seleziona un valore</option>
							<option ng-value="no_val">Assolutamente No</option>
							<option ng-value="maybe_no_val">Non credo</option>
							<option ng-value="maybe_yes_val">Probabilmente Sì</option>
							<option ng-value="yes_val">Sicuramente Sì</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="no_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> Assolutamente No</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="maybe_no_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> Non credo</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="maybe_yes_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> Probabilmente Sì</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="yes_val" ng-required="user.surveyData.change_of_habits!=nothing_val">Sicuramente Sì</label></td>
							</tr>
						</table>
						<div ng-show="form.new_habits_maintaining.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.job_transport_mode.$dirty && form.job_transport_mode.$invalid]">
						<label class="control-label">4) Quale modalità di trasporto hai usato prevalentemente durante il gioco per andare al lavoro / scuola?</label>
						<select ng-if="showSelect" type="text" name="job_transport_mode" class="form-control" ng-model="user.surveyData.job_transport_mode" required>
							<option value="">Seleziona un valore</option>
							<option ng-value="walk_mode">A piedi</option>
							<option ng-value="bike_mode">In bici</option>
							<option ng-value="public_transport_mode">Mezzi pubblici</option>
							<option ng-value="car_mode">Auto</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="job_transport_mode" ng-model="user.surveyData.job_transport_mode" ng-value="walk_mode" required> A piedi</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="job_transport_mode" ng-model="user.surveyData.job_transport_mode" ng-value="bike_mode" required> In bici</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="job_transport_mode" ng-model="user.surveyData.job_transport_mode" ng-value="public_transport_mode" required> Mezzi pubblici</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="job_transport_mode" ng-model="user.surveyData.job_transport_mode" ng-value="car_mode" required> Auto</label></td>
							</tr>
						</table>
						<div ng-show="form.job_transport_mode.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.free_time_transport_mode.$dirty && form.free_time_transport_mode.$invalid]">
						<label class="control-label">5) Quale modalità di trasporto hai usato prevalentemente durante il gioco per il tuo tempo libero?</label>
						<select ng-if="showSelect" type="text" name="free_time_transport_mode" class="form-control" ng-model="user.surveyData.free_time_transport_mode" required>
							<option value="">Seleziona un valore</option>
							<option ng-value="walk_mode">A piedi</option>
							<option ng-value="bike_mode">In bici</option>
							<option ng-value="public_transport_mode">Mezzi pubblici</option>
							<option ng-value="car_mode">Auto</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="free_time_transport_mode" ng-model="user.surveyData.free_time_transport_mode" ng-value="walk_mode" required> A piedi</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="free_time_transport_mode" ng-model="user.surveyData.free_time_transport_mode" ng-value="bike_mode" required> In bici</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="free_time_transport_mode" ng-model="user.surveyData.free_time_transport_mode" ng-value="public_transport_mode" required> Mezzi pubblici</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="free_time_transport_mode" ng-model="user.surveyData.free_time_transport_mode" ng-value="car_mode" required> Auto</label></td>
							</tr>
						</table>
						<div ng-show="form.free_time_transport_mode.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.trip_type.$dirty && form.trip_type.$invalid]">
						<label class="control-label">6) Per quale tipo di viaggio hai usato la App?</label>
						<select ng-if="showSelect" type="text" name="trip_type" class="form-control" ng-model="user.surveyData.trip_type" required>
							<option value="">Seleziona un valore</option>
							<option ng-value="all_trips">Per qualsiasi viaggio</option>
							<option ng-value="commuters_trips">Per viaggi pendolari</option>
							<option ng-value="long_trips">Per viaggi lunghi</option>
							<option ng-value="short_trips">Per viaggi corti</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="trip_type" ng-model="user.surveyData.trip_type" ng-value="all_trips" required> Per qualsiasi viaggio</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="trip_type" ng-model="user.surveyData.trip_type" ng-value="commuters_trips" required> Per viaggi pendolari</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="trip_type" ng-model="user.surveyData.trip_type" ng-value="long_trips" required> Per viaggi lunghi</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="trip_type" ng-model="user.surveyData.trip_type" ng-value="short_trips" required> Per viaggi corti</label></td>
							</tr>
						</table>
						<div ng-show="form.trip_type.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.new_mode_type.$dirty && form.new_mode_type.$invalid]">
						<label class="control-label">7) Quale modo hai provato per la prima volta grazie al gioco?</label>
						<select ng-if="showSelect" type="text" name="new_mode_type" class="form-control" ng-model="user.surveyData.new_mode_type" required>
							<option value="">Seleziona un valore</option>
							<option ng-value="no_mode">Nessuno</option>
							<option ng-value="bike_sharing_mode">Bike Sharing</option>
							<option ng-value="park_and_ride_mode">Park and Ride</option>
							<option ng-value="bike_mode">Bici</option>
							<option ng-value="transport_mode">Mezzi Pubblici</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="new_mode_type" ng-model="user.surveyData.new_mode_type" ng-value="no_mode" required> Nessuno</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_mode_type" ng-model="user.surveyData.new_mode_type" ng-value="bike_sharing_mode" required> Bike Sharing</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_mode_type" ng-model="user.surveyData.new_mode_type" ng-value="park_and_ride_mode" required> Park and Ride</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_mode_type" ng-model="user.surveyData.new_mode_type" ng-value="bike_mode" required> Bici</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_mode_type" ng-model="user.surveyData.new_mode_type" ng-value="transport_mode" required> Mezzi Pubblici</label></td>
							</tr>
						</table>
						<div ng-show="form.new_mode_type.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.new_mode_type.$dirty && form.new_mode_type.$invalid]">
						<label class="control-label">8) Quanto sono stati importanti questi elementi per rimanere attivo nel gioco e continuare a muoverti in modo sostenibile?</label>
						<ul>
							<li>
								<label>Punti e classifiche</label>
								<select ng-if="showSelect" type="text" name="point_interest_in_game" class="form-control" ng-model="user.surveyData.point_interest_in_game" required>
									<option value="">Seleziona un valore</option>
									<option ng-value="nothing_val">Per niente</option>
									<option ng-value="little_val">Poco</option>
									<option ng-value="enough_val">Abbastanza</option>
									<option ng-value="much_val">Molto</option>
								</select>	
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="nothing_val" required> Per niente</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="little_val" required> Poco</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="enough_val" required> Abbastanza</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="much_val" required> Molto</label></td>
									</tr>
								</table>
								<div ng-show="form.point_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
							</li>
							<li>
								<label>Badges</label>
								<select ng-if="showSelect" type="text" name="badges_interest_in_game" class="form-control" ng-model="user.surveyData.badges_interest_in_game" required>
									<option value="">Seleziona un valore</option>
									<option ng-value="nothing_val">Per niente</option>
									<option ng-value="little_val">Poco</option>
									<option ng-value="enough_val">Abbastanza</option>
									<option ng-value="much_val">Molto</option>
								</select>
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="nothing_val" required> Per niente</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="little_val" required> Poco</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="enough_val" required> Abbastanza</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="much_val" required> Molto</label></td>
									</tr>
								</table>
								<div ng-show="form.badges_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
							</li>
							<li>
								<label>Sfide</label>
								<select ng-if="showSelect" type="text" name="challenges_interest_in_game" class="form-control" ng-model="user.surveyData.challenges_interest_in_game" required>
									<option value="">Seleziona un valore</option>
									<option ng-value="nothing_val">Per niente</option>
									<option ng-value="little_val">Poco</option>
									<option ng-value="enough_val">Abbastanza</option>
									<option ng-value="much_val">Molto</option>
								</select>
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="nothing_val" required> Per niente</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="little_val" required> Poco</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="enough_val" required> Abbastanza</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="much_val" required> Molto</label></td>
									</tr>
								</table>
								<div ng-show="form.challenges_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
							</li>
							<li>
								<label>Premi</label>
								<select ng-if="showSelect" type="text" name="prize_interest_in_game" class="form-control" ng-model="user.surveyData.prize_interest_in_game" required>
									<option value="">Seleziona un valore</option>
									<option ng-value="nothing_val">Per niente</option>
									<option ng-value="little_val">Poco</option>
									<option ng-value="enough_val">Abbastanza</option>
									<option ng-value="much_val">Molto</option>
								</select>
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="nothing_val" required> Per niente</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="little_val" required> Poco</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="enough_val" required> Abbastanza</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="much_val" required> Molto</label></td>
									</tr>
								</table>
								<div ng-show="form.prize_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo obbligatorio. Scegliere un valore</div>
							</li>
						</ul>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.game_improve_suggestion.$dirty && form.game_improve_suggestion.$invalid]">
						<label class="control-label" for="game_suggestion">9) Hai qualche suggerimento o idea per migliorare il gioco (e.g., premi che potrebbero interessarti, miglioramenti nel funzionamento del gioco, nuovi concetti e dinamiche di gioco)?</label>
						<!-- <input type="text" class="form-control" name="game_improve_suggestion" id="game_suggestion" placeholder="Inserisci i tuoi suggerimenti sul gioco" ng-model="user.surveyData.game_improve_suggestion"> -->
						<textarea class="form-control" name="game_improve_suggestion" id="game_suggestion" placeholder="Inserisci i tuoi suggerimenti sul gioco" ng-model="user.surveyData.game_improve_suggestion"></textarea>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.app_improve_suggestion.$dirty && form.app_improve_suggestion.$invalid]">
						<label class="control-label" for="app_suggestion">10) Hai qualche suggerimento o idea per migliorare la App ViaggiaTrento Play&Go (e.g., nuovi servizi o funzionalità, miglioramento delle funzionalità esistenti)?</label>
						<!-- <input type="text" class="form-control" name="app_improve_suggestion" id="app_suggestion" placeholder="Inserisci i tuoi suggerimenti sulla app" ng-model="user.surveyData.app_improve_suggestion"> -->
						<textarea class="form-control" name="app_improve_suggestion" id="app_suggestion" placeholder="Inserisci i tuoi suggerimenti sulla app" ng-model="user.surveyData.app_improve_suggestion"></textarea>
					</div>
					<div class="required_desc"><p>Il simbolo * indica un campo obbligatorio</p></div>
					<div align="justify">
					Ti ringraziamo per aver compilato il questionario e di aver partecipato in modo attivo al miglioramento delle soluzioni proposte!<br/><br/>
					Gli organizzatori. 
					</div>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-danger" ng-click="cancel()">Non ora</button>
					<button type="button" class="btn btn-primary" ng-click="submitNumber=1;save(form)" ng-disabled="form.$invalid || form.$pristine" >OK</button><!-- ng-disabled="(form.$dirty && form.$invalid) || form.$pristine" -->
				</div>
			</div>
		</div>
	</form>
</div>
	</script>
</body>
</html>