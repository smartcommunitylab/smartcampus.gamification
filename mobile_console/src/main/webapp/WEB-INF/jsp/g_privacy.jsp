<!DOCTYPE html>
<html ng-app="cp">
<head id="myHead" lang="it">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Play&Go - Privacy</title>
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
var conf_lang="<%=request.getAttribute("language")%>";
var conf_prizes="<%=request.getAttribute("prizes")%>";
var conf_prizes_eng="<%=request.getAttribute("prizes_eng")%>";
</script>
</head>
<body>
	<div class="container" ng-controller="MainCtrl">
		<div id="big-privacy-page">
			<div class="row">
				<div ng-class="col-md-10">
					<div class="panel panel-success-trasparent" >
						<div class="panel-body">
					    <h4>{{ 'privacy_page_title' | i18n }}</h4>
						<div align="justify">
							{{ 'privacy_page_text' | i18n }} <a href="pdf/normativa.pdf" target="_blank" alt="normativa privacy" title="normativa privacy" >{{ 'privacy_page_link' | i18n }}</a>.
						</div>
						<br/>
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
			<div class="row">
				<div ng-class="col-xs-12">
					<div class="panel panel-success-trasparent" >
						<div class="panel-body">
					    <h4>{{ 'privacy_page_title' | i18n }}</h4>
						<div align="justify">
							{{ 'privacy_page_text' | i18n }} <a href="pdf/normativa.pdf" target="_blank" alt="normativa privacy" title="normativa privacy" >{{ 'privacy_page_link' | i18n }}</a>.
						</div>
						<br/>
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