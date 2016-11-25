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
										<font face="Raleway-bold" size="48" color="gray"><strong>Play&amp;Go</strong></font>
									</h1>
									<h2>
										<font face="Raleway">{{ 'text_subtitle_survey_page' | i18n }}</font>
									</h2>
									<h2>&nbsp;</h2>
									<p class="big-text" align="left" ng-show="surveyComplete">{{ 'text_survey_ok' | i18n }}<br /><br />
									&emsp;<em>{{ 'text_staff_survey_page' | i18n }}</em>
									</p>
									<p class="big-text" align="left" ng-show="!surveyComplete">{{ 'text_survey_ko' | i18n }}<br /><br />
									&emsp;<em>{{ 'text_staff_survey_page' | i18n }}</em>
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
										<font face="Raleway-bold" size="48" color="gray"><strong>Play&amp;Go</strong></font>
									</h1>
									<h2>
										<font face="Raleway">{{ 'text_subtitle_survey_page' | i18n }}</font>
									</h2>
									<h2>&nbsp;</h2>
									<p class="big-text" align="left" ng-show="surveyComplete">{{ 'text_survey_ok' | i18n }}<br /><br />
									&emsp;<em>{{ 'text_staff_survey_page' | i18n }}</em>
									</p>
									<p class="big-text" align="left" ng-show="!surveyComplete">{{ 'text_survey_ko' | i18n }}<br /><br />
									&emsp;<em>{{ 'text_staff_survey_page' | i18n }}</em>
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
						</span>&nbsp;&nbsp;{{ 'final_survey_title' | i18n }}&nbsp;&nbsp;
					</h4>
					<button type="button" class="btn btn-danger btn-sm btn-left" ng-click="cancel()"><i class="glyphicon glyphicon-remove"></i></button>
				</div>
				<div class="modal-body">
					<div align="justify">
						{{ 'final_survey_subtitle' | i18n }}<br/><br/>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.residence_tn.$dirty && form.residence_tn.$invalid]">
						<label class="control-label">1) {{ 'final_survey_question1' | i18n }}</label>
						<select ng-if="showSelect" type="text" name="residence_tn" class="form-control" ng-model="user.surveyData.user_residence_tn" required>
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="yes_val">{{ 'fs_yes_value' | i18n }}</option>
							<option ng-value="no_val">{{ 'fs_no_value' | i18n }}</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="residence_tn" ng-model="user.surveyData.user_residence_tn" ng-value="yes_val" required> {{ 'fs_yes_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="residence_tn" ng-model="user.surveyData.user_residence_tn" ng-value="no_val" required> {{ 'fs_no_value' | i18n }}</label></td>
							</tr>
						</table>
						<div ng-show="form.residence_tn.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.commute_tn.$dirty && form.commute_tn.$invalid]">
						<label class="control-label">2) {{ 'final_survey_question2' | i18n }}</label>
						<select ng-if="showSelect" type="text" name="commute_tn" class="form-control" ng-model="user.surveyData.user_commute_tn" required>
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="yes_val">{{ 'fs_yes_value' | i18n }}</option>
							<option ng-value="no_val">{{ 'fs_no_value' | i18n }}</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="commute_tn" ng-model="user.surveyData.user_commute_tn" ng-value="yes_val" required> {{ 'fs_yes_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="commute_tn" ng-model="user.surveyData.user_commute_tn" ng-value="no_val" required> {{ 'fs_no_value' | i18n }}</label></td>
							</tr>
						</table>
						<div ng-show="form.commute_tn.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.game_discover.$dirty && form.game_discover.$invalid]">
						<label class="control-label">3) {{ 'final_survey_question2a' | i18n }}</label>
						<select ng-if="showSelect" type="text" name="game_discover" class="form-control" ng-model="user.surveyData.game_discover_from_user" required>
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="from_newspaper">{{ 'fs_newspaper_value' | i18n }}</option>
							<option ng-value="from_radio">{{ 'fs_radio_value' | i18n }}</option>
							<option ng-value="from_bus_leaflet">{{ 'fs_bus_leaflet_value' | i18n }}</option>
							<option ng-value="from_poster">{{ 'fs_poster_value' | i18n }}</option>
							<option ng-value="from_facebook">facebook</option>
							<option ng-value="from_information_event">{{ 'fs_info_event_value' | i18n }}</option>
							<option ng-value="from_a_friend">{{ 'fs_friend_value' | i18n }}</option>
							<option ng-value="from_other">{{ 'fs_other_value' | i18n }}</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_newspaper" required> {{ 'fs_newspaper_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_radio" required> {{ 'fs_radio_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_bus_leaflet" required> {{ 'fs_bus_leaflet_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_poster" required> {{ 'fs_poster_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_facebook" required> Facebook</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_information_event" required> {{ 'fs_info_event_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_a_friend" required> {{ 'fs_friend_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="game_discover" ng-model="user.surveyData.game_discover_from_user" ng-value="from_other" required> {{ 'fs_other_value' | i18n }}</label></td>
							</tr>
						</table>
						<textarea ng-show="user.surveyData.game_discover_from_user == from_other" class="form-control" name="game_discover_other" id="game_discover_other" placeholder="{{ 'fs_text_placeholder_discover_value' | i18n }}" ng-model="user.surveyData.game_discover_other_value"></textarea>
						<div ng-show="form.game_discover.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.gaming_exp.$dirty && form.gaming_exp.$invalid]">
						<label class="control-label">4) {{ 'final_survey_question3' | i18n }}</label>
						<select ng-if="showSelect" type="text" name="gaming_exp" class="form-control" ng-model="user.surveyData.gamimg_experience" required>
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="negative_exp">{{ 'fs_negative_value' | i18n }}</option>
							<option ng-value="satisfying_exp">{{ 'fs_enough_value' | i18n }}</option>
							<option ng-value="good_exp">{{ 'fs_good_value' | i18n }}</option>
							<option ng-value="excellent_exp">{{ 'fs_excellent_value' | i18n }}</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="negative_exp" required> {{ 'fs_negative_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="satisfying_exp" required> {{ 'fs_enough_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="good_exp" required> {{ 'fs_good_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="gaming_exp" ng-model="user.surveyData.gamimg_experience" ng-value="excellent_exp" required> {{ 'fs_excellent_value' | i18n }}</label></td>
							</tr>
						</table>
						<div ng-show="form.gaming_exp.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.change_of_habits.$dirty && form.change_of_habits.$invalid]">
						<label class="control-label">5) {{ 'final_survey_question4' | i18n }}</label>
						<select ng-if="showSelect" type="text" name="change_of_habits" class="form-control" ng-model="user.surveyData.change_of_habits" required>
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="nothing_val">{{ 'fs_nothing_value' | i18n }}</option>
							<option ng-value="little_val">{{ 'fs_little_value' | i18n }}</option>
							<option ng-value="enough_val">{{ 'fs_quite_value' | i18n }}</option>
							<option ng-value="much_val">{{ 'fs_very_value' | i18n }}</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="nothing_val" required> {{ 'fs_nothing_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="little_val" required> {{ 'fs_little_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="enough_val" required> {{ 'fs_quite_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="change_of_habits" ng-model="user.surveyData.change_of_habits" ng-value="much_val" required> {{ 'fs_very_value' | i18n }}</label></td>
							</tr>
						</table>
						<div ng-show="form.change_of_habits.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.new_habits_maintaining.$dirty && form.new_habits_maintaining.$invalid]">
						<label class="control-label">6) {{ 'final_survey_question5' | i18n }}</label>
						<select ng-if="showSelect" type="text" name="new_habits_maintaining" class="form-control" ng-model="user.surveyData.new_habits_maintaining" ng-required="user.surveyData.change_of_habits!=nothing_val">
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="no_val">{{ 'fs_absolutely_no_value' | i18n }}</option>
							<option ng-value="maybe_no_val">{{ 'fs_think_not_value' | i18n }}</option>
							<option ng-value="maybe_yes_val">{{ 'fs_probably_yes_value' | i18n }}</option>
							<option ng-value="yes_val">{{ 'fs_certain_yes_value' | i18n }}</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="no_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> {{ 'fs_absolutely_no_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="maybe_no_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> {{ 'fs_think_not_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="maybe_yes_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> {{ 'fs_probably_yes_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="yes_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> {{ 'fs_certain_yes_value' | i18n }}</label></td>
							</tr>
						</table>
						<div ng-show="form.new_habits_maintaining.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.new_mode_type.$dirty && form.new_mode_type.$invalid]">
						<label class="control-label">7) {{ 'final_survey_question6' | i18n }}</label>
						<!-- <select ng-if="showSelect" type="text" name="new_mode_type" class="form-control" ng-model="user.surveyData.new_mode_type">
							<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
							<option ng-value="no_mode">Nessuno</option>
							<option ng-value="bike_sharing_mode">Bike Sharing</option>
							<option ng-value="park_and_ride_mode">Park and Ride</option>
							<option ng-value="bike_mode">{{ 'fs_bike_value' | i18n }}</option>
							<option ng-value="transport_mode">{{ 'fs_public_transport_value' | i18n }}</option>
							<option ng-value="cable_mode">{{ 'fs_cable_value' | i18n }}</option>
						</select> -->
						<table width="100%" ng-if="!showSelect">
							<!-- <tr>
								<td><label><input type="checkbox" name="new_mode_type_no" ng-model="user.surveyData.new_mode_type.no_mode"> Nessuno</label></td>
							</tr> -->
							<tr>
								<td><label><input type="checkbox" name="new_mode_type_bs" ng-model="user.surveyData.new_mode_type.bike_sharing_mode"> Bike Sharing</label></td>
							</tr>
							<tr>
								<td><label><input type="checkbox" name="new_mode_type_pr" ng-model="user.surveyData.new_mode_type.park_and_ride_mode"> Park and Ride</label></td>
							</tr>
							<tr>
								<td><label><input type="checkbox" name="new_mode_type_b" ng-model="user.surveyData.new_mode_type.bike_mode"> {{ 'fs_bike_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="checkbox" name="new_mode_type_t" ng-model="user.surveyData.new_mode_type.transport_mode"> {{ 'fs_public_transport_value' | i18n }}</label></td>
							</tr>
							<tr>
								<td><label><input type="checkbox" name="new_mode_type_f" ng-model="user.surveyData.new_mode_type.cable_mode"> {{ 'fs_cable_value' | i18n }}</label></td>
							</tr>
						</table>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.new_mode_type.$dirty && form.new_mode_type.$invalid]">
						<label class="control-label">8) {{ 'final_survey_question7' | i18n }}</label>
						<ul>
							<li>
								<label>{{ 'final_survey_question7_sub1' | i18n }}</label>
								<select ng-if="showSelect" type="text" name="point_interest_in_game" class="form-control" ng-model="user.surveyData.point_interest_in_game" required>
									<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
									<option ng-value="nothing_val">{{ 'fs_nothing_value' | i18n }}</option>
									<option ng-value="little_val">{{ 'fs_little_value' | i18n }}</option>
									<option ng-value="enough_val">{{ 'fs_quite_value' | i18n }}</option>
									<option ng-value="much_val">{{ 'fs_very_value' | i18n }}</option>
								</select>	
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="nothing_val" required> {{ 'fs_nothing_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="little_val" required> {{ 'fs_little_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="enough_val" required> {{ 'fs_quite_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="point_interest_in_game" ng-model="user.surveyData.point_interest_in_game" ng-value="much_val" required> {{ 'fs_very_value' | i18n }}</label></td>
									</tr>
								</table>
								<div ng-show="form.point_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
							</li>
							<li>
								<label>{{ 'final_survey_question7_sub2' | i18n }}</label>
								<select ng-if="showSelect" type="text" name="badges_interest_in_game" class="form-control" ng-model="user.surveyData.badges_interest_in_game" required>
									<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
									<option ng-value="nothing_val">{{ 'fs_nothing_value' | i18n }}</option>
									<option ng-value="little_val">{{ 'fs_little_value' | i18n }}</option>
									<option ng-value="enough_val">{{ 'fs_quite_value' | i18n }}</option>
									<option ng-value="much_val">{{ 'fs_very_value' | i18n }}</option>
								</select>
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="nothing_val" required> {{ 'fs_nothing_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="little_val" required> {{ 'fs_little_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="enough_val" required> {{ 'fs_quite_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="badges_interest_in_game" ng-model="user.surveyData.badges_interest_in_game" ng-value="much_val" required> {{ 'fs_very_value' | i18n }}</label></td>
									</tr>
								</table>
								<div ng-show="form.badges_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
							</li>
							<li>
								<label>{{ 'final_survey_question7_sub3' | i18n }}</label>
								<select ng-if="showSelect" type="text" name="challenges_interest_in_game" class="form-control" ng-model="user.surveyData.challenges_interest_in_game" required>
									<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
									<option ng-value="nothing_val">{{ 'fs_nothing_value' | i18n }}</option>
									<option ng-value="little_val">{{ 'fs_little_value' | i18n }}</option>
									<option ng-value="enough_val">{{ 'fs_quite_value' | i18n }}</option>
									<option ng-value="much_val">{{ 'fs_very_value' | i18n }}</option>
								</select>
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="nothing_val" required> {{ 'fs_nothing_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="little_val" required> {{ 'fs_little_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="enough_val" required> {{ 'fs_quite_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="challenges_interest_in_game" ng-model="user.surveyData.challenges_interest_in_game" ng-value="much_val" required> {{ 'fs_very_value' | i18n }}</label></td>
									</tr>
								</table>
								<div ng-show="form.challenges_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
							</li>
							<li>
								<label>{{ 'final_survey_question7_sub4' | i18n }}</label>
								<select ng-if="showSelect" type="text" name="prize_interest_in_game" class="form-control" ng-model="user.surveyData.prize_interest_in_game" required>
									<option value="">{{ 'fs_select_defalut_value' | i18n }}</option>
									<option ng-value="nothing_val">{{ 'fs_nothing_value' | i18n }}</option>
									<option ng-value="little_val">{{ 'fs_little_value' | i18n }}</option>
									<option ng-value="enough_val">{{ 'fs_quite_value' | i18n }}</option>
									<option ng-value="much_val">{{ 'fs_very_value' | i18n }}</option>
								</select>
								<table width="100%" ng-if="!showSelect">
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="nothing_val" required> {{ 'fs_nothing_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="little_val" required> {{ 'fs_little_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="enough_val" required> {{ 'fs_quite_value' | i18n }}</label></td>
									</tr>
									<tr>
										<td><label><input type="radio" name="prize_interest_in_game" ng-model="user.surveyData.prize_interest_in_game" ng-value="much_val" required> {{ 'fs_very_value' | i18n }}</label></td>
									</tr>
								</table>
								<div ng-show="form.prize_interest_in_game.$error.required && submitNumber" class="alert alert-danger" role="alert">{{ 'fs_text_required_field_value' | i18n }}</div>
							</li>
						</ul>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.game_liked_features.$dirty && form.game_liked_features.$invalid]">
						<label class="control-label" for="game_liked_features">9) {{ 'final_survey_question8' | i18n }}</label>
						<textarea class="form-control" name="game_liked_features" id="game_liked_features" placeholder="{{ 'fs_text_placeholder_value' | i18n }}" ng-model="user.surveyData.game_liked_features"></textarea>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.game_not_liked_features.$dirty && form.game_not_liked_features.$invalid]">
						<label class="control-label" for="game_not_liked_features">10) {{ 'final_survey_question9' | i18n }}</label>
						<textarea class="form-control" name="game_not_liked_features" id="game_suggestion" placeholder="{{ 'fs_text_placeholder_value' | i18n }}" ng-model="user.surveyData.game_not_liked_features"></textarea>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.improve_suggestion.$dirty && form.app_improve_suggestion.$invalid]">
						<label class="control-label" for="app_suggestion">11) {{ 'final_survey_question10' | i18n }}</label>
						<textarea class="form-control" name="improve_suggestion" id="app_suggestion" placeholder="{{ 'fs_text_placeholder_value' | i18n }}" ng-model="user.surveyData.improve_suggestion"></textarea>
					</div>
					<div class="required_desc"><p>{{ 'final_survey_required_field' | i18n }}</p></div>
					<div align="justify">
					{{ 'final_survey_thanks_description' | i18n }}<br/><br/>
					{{ 'final_survey_staff' | i18n }} 
					</div>
				</div>

				<div class="modal-footer">
					<button type="button" class="btn btn-danger" ng-click="cancel()">{{ 'final_survey_not_now_button' | i18n }}</button>
					<button type="button" class="btn btn-primary" ng-disabled="form.$invalid || form.$pristine" ng-click="submitNumber=1;save(form)" >OK</button><!-- ng-disabled="form.$invalid || form.$pristine" -->
				</div>
			</div>
		</div>
	</form>
</div>
	</script>
</body>
</html>