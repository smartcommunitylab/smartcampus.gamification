<!DOCTYPE html>
<html ng-app="cp" itemscope itemtype="http://schema.org/Article">
<head id="myHead" lang="it">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">	<!-- the last two parameters solve the modal resize problem -->
<title>Play&Go</title>

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
<!-- <script src="js/controllers.js"></script> -->
<script src="js/controllers/ctrl.js?1001"></script>
<script src="js/controllers/ctrl_login.js?1000"></script>
<script src="js/controllers/ctrl_main.js?1000"></script>

<script src="js/filters.js?1001"></script>
<script src="js/services.js?1001"></script>
<script src="js/directives.js"></script>
<script src="lib/ui-bootstrap-tpls.min.js"></script>

<!-- optional libraries -->
<!-- <script src="lib/underscore-min.js"></script> -->
<!-- <script src="lib/moment.min.js"></script> -->
<!-- <script src="lib/fastclick.min.js"></script> -->
<!-- <script src="lib/prettify.js"></script> -->
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

	<%-- Prevent the backspace key from navigating back. --%>
	$(document).unbind('keydown').bind('keydown', function (event) {
	    var doPrevent = false;
	    if (event.keyCode === 8) {
	        var d = event.srcElement || event.target;
	        if ((d.tagName.toUpperCase() === 'INPUT' && 
	             (
	                 d.type.toUpperCase() === 'TEXT' ||
	                 d.type.toUpperCase() === 'NUMBER' ||
	                 d.type.toUpperCase() === 'PASSWORD' || 
	                 d.type.toUpperCase() === 'FILE' || 
	                 d.type.toUpperCase() === 'EMAIL' || 
	                 d.type.toUpperCase() === 'SEARCH' || 
	                 d.type.toUpperCase() === 'DATE' )
	             ) || 
	             d.tagName.toUpperCase() === 'TEXTAREA') {
	            doPrevent = d.readOnly || d.disabled;
	        }
	        else {
	            doPrevent = true;
	        }
	    }
	
	    if (doPrevent) {
	        event.preventDefault();
	    }
	});
  </script>
	<meta itemprop="name" content="Play&Go Rovereto">
	<meta itemprop="image" content="img/foglia.svg">
</head>

<body>
	<div id="myBody" ng-controller="MainCtrl" ng-init="setItalianLanguage()">
	
    <div id="my-big-menu" class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
      	<div class="row" align="center">
		<div class="col-md-8 col-md-offset-2">
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
			<!-- <li class="active"><a href="#/" ng-click="home()">{{ 'menu_bar-home' | i18n }}</a></li> -->
            <li>
            	<a>
            		<img height="22" src="img/foglia.svg">
            	</a>
            </li> 
            
            <!-- <li ng-if="!disableAllLinks" class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" >{{ 'left_menu-profile' | i18n }}</a></li> -->
           <!--  <li ng-if="!disableAllLinks" class="{{ isActiveChalleng() }}"><a href="#/challeng/{{ gameId }}" ng-click="showChalleng()" ><strong>{{ 'left_menu-challeng' | i18n }}</strong></a></li>
            <li ng-if="!disableAllLinks" class="{{ isActiveClassification() }}"><a href="#/classification/{{ gameId }}" ng-click="showClassification()" >{{ 'left_menu-classification' | i18n }}</a></li> -->
            <li class="{{ isActivePrizes() }}"><a href="#/prizes" ng-click="showPrizes()" >{{ 'left_menu-prizes' | i18n }}</a></li>
            <li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" >{{ 'left_menu-rules' | i18n }}</a></li>
            <li class="{{ isActivePrivacy() }}"><a href="#/privacy" ng-click="showPrivacy()" >{{ 'left_menu-privacy' | i18n }}</a></li>
            <!-- <li class="{{ isActiveCredits() }}"><a href="#/credits" ng-click="showCredits()" >{{ 'left_menu-credits' | i18n }}</a></li> -->
            
          </ul>
          <ul class="nav navbar-nav navbar-right" >
			<li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()">IT</a></li>
			<li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()">EN</a></li>
            <!-- <li><a href="logout" ng-click="logout()">{{ 'menu_bar-logout' | i18n }}</a></li> -->
          </ul>
        </div><!-- /.nav-collapse -->
        </div>
    	</div>
      </div><!-- /.container -->
    </div><!-- /.navbar -->
    
    <div id="my-small-menu" class="navbar navbar-default navbar-fixed-top" role="navigation">
      <div class="container">
      	<div class="row">
			<div class="col-md-8 col-md-offset-2">
			    <div class="navbar-header">	
				    <ul class="nav navbar-nav">
<!-- 						<li class="active"><a href="#/" ng-click="home()">{{ 'menu_bar-home' | i18n }}</a></li> -->
			            <li class="dropdown">
			            	<a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" >
			            		<img height="22" src="img/navMobile.svg">
			            	</a>
			            	<ul class="dropdown-menu" role="menu">
			            		<!-- <li class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" ><strong>{{ 'left_menu-profile' | i18n }}</strong></a></li> -->
			            		<li class="{{ isActivePrizes() }}"><a href="#/prizes" ng-click="showPrizes()" ><strong>{{ 'left_menu-prizes' | i18n }}</strong></a></li>
			            		<li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" ><strong>{{ 'left_menu-rules' | i18n }}</strong></a></li>
            					<li class="{{ isActivePrivacy() }}"><a href="#/privacy" ng-click="showPrivacy()" ><strong>{{ 'left_menu-privacy' | i18n }}</strong></a></li>
								<!-- <li class="{{ isActiveCredits() }}"><a href="#/credits" ng-click="showCredits()" ><strong>{{ 'left_menu-credits' | i18n }}</strong></a></li> -->
								<li class="divider"></li>
								<li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()"><strong>IT</strong></a></li>
								<li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()"><strong>EN</strong></a></li>
			          			<li class="divider"></li>
			          			<!-- <li><a href="logout" ng-click="logout()"><strong>{{ 'menu_bar-logout' | i18n }}</strong></a></li> -->
			            	</ul>
			            </li>
			         </ul>
			    </div>
	    	</div>
	    </div>
      </div><!-- /.container -->
    </div><!-- /.navbar -->
    
	<div class="container">
<!-- 		<div class="row" style="margin-top:70px;"> -->
		<div class="row">
			<div class="col-md-8 col-md-offset-2 nopadding">
				<div class="panel panel-default homepanel">
			  		<div class="panel-body nopadding">
			  			<div class="view_body_wrapper">
							<ng-view class="row">{{ 'loading_text'| i18n }}...</ng-view>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>	
</body>



</html>