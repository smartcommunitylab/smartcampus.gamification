<!DOCTYPE html>
<html ng-app="cp">
<head id="myHead" lang="it">
<meta charset="utf-8">
<title>{{ 'app_tab-title' | i18n }}</title>

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/xeditable.css" rel="stylesheet">
<link href="css/modaldialog.css" rel="stylesheet">
<link href="img/myweb.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="lib/angular.js"></script>
<script src="js/localize.js" type="text/javascript"></script>
<script src="js/dialogs.min.js" type="text/javascript"></script>
<script src="lib/angular-route.js"></script>
<script src="lib/angular-sanitize.js"></script>

<script src="i18n/angular-locale_it-IT.js"></script>
<!-- <script src="i18n/angular-locale_en-EN.js"></script> -->

<script src="js/app.js?1001"></script>
<!-- <script src="js/controllers.js"></script> -->
<script src="js/controllers/ctrl.js?1001"></script>
<script src="js/controllers/ctrl_login.js?1000"></script>
<script src="js/controllers/ctrl_main.js?1000"></script>
<script src="js/controllers/ctrl_practice.js?1001"></script>
<script src="js/controllers/ctrl_info.js"></script>

<script src="js/filters.js?1001"></script>
<script src="js/services.js?1001"></script>
<script src="js/directives.js"></script>
<script src="lib/ui-bootstrap-tpls.min.js"></script>

<!-- <script type="text/javascript" src="js/jquery.min.js" /></script> -->
<!-- <script type="text/javascript" src="js/jquery-ui.custom.min.js" ></script> -->
<!-- <script type="text/javascript" src="js/ui.datepicker-it.js" ></script> -->

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
var user_name="<%=request.getAttribute("user_name")%>";
var user_surname="<%=request.getAttribute("user_surname")%>";
var user_mail="<%=request.getAttribute("e_mail")%>";
var nome="<%=request.getAttribute("nome")%>";
var cognome="<%=request.getAttribute("cognome")%>";
var sesso="<%=request.getAttribute("sesso")%>";
var dataNascita="<%=request.getAttribute("dataNascita")%>";
var provinciaNascita="<%=request.getAttribute("provinciaNascita")%>";
var luogoNascita="<%=request.getAttribute("luogoNascita")%>";
var indirizzoRes="<%=request.getAttribute("indirizzoRes")%>";
var capRes="<%=request.getAttribute("capRes")%>";
var cittaRes="<%=request.getAttribute("cittaRes")%>";
var provinciaRes="<%=request.getAttribute("provinciaRes")%>";
var codiceFiscale="<%=request.getAttribute("codiceFiscale")%>";
var cellulare="<%=request.getAttribute("cellulare")%>";
var email="<%=request.getAttribute("email")%>";
var issuerdn="<%=request.getAttribute("issuerdn")%>";
<%-- var subjectdn="<%=request.getAttribute("subjectdn")%>"; --%>
var base64="<%=request.getAttribute("base64")%>";

  
  
//   angular.module('cpControllers', [])
//   .controller('LangController', ['$scope', 'sharedDataService', function($scope, sharedDataService) {
    
// 	$scope.init_lang = function(){ 
// 		$scope.lang = sharedDataService.getUsedLanguage();
	    
// 		if($scope.lang == 'ita'){
// 			var locale = 'it-IT';
// 			$.getScript('i18n/angular-locale_it-IT.js');
// 	  	} else {
// 	  		//$("#lang_script").remove();
// 	  		$.getScript('i18n/angular-locale_en-EN.js');
// 		};

// 	};
//   }]);
  
// 	var language_script = document.createElement('script');
// 	language_script.type = 'text/javascript';
// 	language_script.id = 'lang_script';
	
// 	var appElement = document.querySelector('[ng-app=cp]');
// 	var $scope = angular.element(appElement).scope();
// 	console.log($scope.used_lang);
	
// 	var controllerElement = document.querySelector('html');
// 	var controllerScope = angular.element(controllerElement).scope();
// 	console.log(controllerScope);

	
// 	var language = JSON.parse(localStorage.getItem('language'));
  
	//var dom_el = document.querySelector('[ng-controller="MainCtrl"]');
	//var ng_el = angular.element(dom_el);
 	//var ng_el_scope = ng_el.scope();
	//var language = ng_el_scope.used_lang;
	//var language = $('[ng-controller="MainCtrl"]')).scope().used_lang;
	
//  	if(language == 'ita'){
//		language_script.src = 'i18n/angular-locale_it-IT.js';
	  //document.write('<script src=\'i18n/angular-locale_it-IT.js\'/>');
//  	} else {
//  		$("#lang_script").remove();
//		language_script.src = 'i18n/angular-locale_en-EN.js';
	  //document.write('<script src=\'i18n/angular-locale_en-EN.js\'/>');
//	};

  
// 	$("#myHead").append(language_script);

// 	var locale = JSON.parse(localStorage.getItem('language'));
// 	if (locale) {
//     	document.write('<script src="i18n/angular-locale_'+locale+'.js"><\/script>');
// 	}

	<%-- Prevent the backspace key from navigating back. --%>
	$(document).unbind('keydown').bind('keydown', function (event) {
	    var doPrevent = false;
	    if (event.keyCode === 8) {
	        var d = event.srcElement || event.target;
	        if ((d.tagName.toUpperCase() === 'INPUT' && 
	             (
	                 d.type.toUpperCase() === 'TEXT' ||
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

</head>

<body>
	<div id="myBody" ng-controller="MainCtrl" ng-init="setItalianLanguage()">
	
    <div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
      <div class="container">
      	<div class="row" align="center">
      	<div class="col-md-2"></div>
		<div class="col-md-8">
        <div class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
			<!-- <li class="active"><a href="#/" ng-click="home()">{{ 'menu_bar-home' | i18n }}</a></li> -->
            
            <li class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" >{{ 'left_menu-profile' | i18n }}</a></li>
            <li class="{{ isActiveClassification() }}"><a href="#/classification/{{ gameId }}" ng-click="showClassification()" >{{ 'left_menu-classification' | i18n }}</a></li>
            
          </ul>
          <ul class="nav navbar-nav navbar-right" >
<!--           	<li class="dropdown"> -->
<!--           		<a href="#" class="dropdown-toggle" data-toggle="dropdown">{{ 'guide' | i18n }} <span class="caret"></span></a> -->
<!--           		<ul class="dropdown-menu" role="menu"> -->
<!--             		<li><a href="http://www.trentinosociale.it/index.php/Servizi-ai-cittadini/Guida-ai-servizi/per-destinatari/Anziani/Abitare-o-disporre-di-un-alloggio-adeguato-e-sicuro/Locazione-alloggio-pubblico-a-canone-sociale" target="_blank">{{ 'document_link_edil' | i18n }}</a></li> -->
<!--             		<li><a href="http://www.trentinosociale.it/index.php/Servizi-ai-cittadini/Guida-ai-servizi/per-destinatari/Anziani/Abitare-o-disporre-di-un-alloggio-adeguato-e-sicuro/Contributo-sul-canone-di-affitto" target="_blank">{{ 'document_link_allowances' | i18n }}</a></li> -->
<!--             	</ul> -->
<!--           	</li> -->
<!--           	<li><a href="mailto:myweb.edilizia@comunitadellavallagarina.tn.it?Subject=Info%20MyWeb" target="_top" alt="myweb.edilizia@comunitadellavallagarina.tn.it" title="myweb.edilizia@comunitadellavallagarina.tn.it">{{ 'usefull_link'| i18n }}</a></li> -->
          	<li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()">IT</a></li>
          	<li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()">EN</a></li>
            <li class="active" >
            	<a>
            		<span class="glyphicon glyphicon-user"></span>
            		{{ getUserName() }} {{ getUserSurname() }}
            	</a>
            </li>
            
            <li><a href="logout" ng-click="logout()">{{ 'menu_bar-logout' | i18n }}</a></li><!-- ng-click="logout()" -->
          </ul>
        </div><!-- /.nav-collapse -->
        </div>
        <div class="col-md-2"></div>
    	</div>
      </div><!-- /.container -->
    </div><!-- /.navbar -->
	<div class="container">
<!-- 		<div class="row" style="margin-top:70px;"> -->
		<div class="row">
			<div class="col-md-2"></div>
			<div class="col-md-8">
				<div class="panel panel-default" style="margin-top:65px;">
			  		<div class="panel-body">
			  			<div style="margin:5px 15px;">
						<!-- Rights menu - List of links and other services (menu mensa etc) style="margin: 50px 20px 10px 0;" -->
	<!-- 				<div class="col-md-2" style="margin-top:135px;" ng-show="!frameOpened"> -->
	<!-- 					<div class="panel panel-default" style="height: 230px"> -->
	<!-- 						<div class="panel-heading"> -->
	<!-- 							<h4 class="panel-title">{{ 'left_menu-availableServices_eu' | i18n }}</h4> -->
	<!-- 						</div> -->
	<!-- 						<div class="panel-body"> -->
	<!-- 							<ul class="nav nav-pills nav-stacked" style="font-size: 14px"> -->
	<!-- 			            		<li class="{{ isActiveLinkEdil() }}"><a href="#/PracticeList/edil/1" ng-click="showPractices(1, true)">{{ 'left_menu-bildings' | i18n }}</a></li> -->
	<!-- 			            		<li class="{{ isActiveLinkAss() }}"><a href="#/PracticeList/ass/1" ng-click="showPractices(2, true)">{{ 'left_menu-allowances' | i18n }}</a></li> -->
	<!-- 			        		</ul> -->
	<!-- 			        	</div> -->
	<!-- 			        </div> -->
	<!-- 			        <div class="panel panel-default" style="height: 230px"> -->
	<!-- 						<div class="panel-heading"> -->
	<!-- 							<h4 class="panel-title">{{ 'left_menu-availableServices_extraeu' | i18n }}</h4> -->
	<!-- 						</div> -->
	<!-- 						<div class="panel-body"> -->
	<!-- 							<ul class="nav nav-pills nav-stacked" style="font-size: 14px"> -->
	<!-- 			            		<li class="{{ isActiveLinkEdilExtra() }}"><a href="#/PracticeList/edil/2" ng-click="showPractices(1, false)">{{ 'left_menu-bildings' | i18n }}</a></li> -->
	<!-- 			            		<li class="{{ isActiveLinkAssExtra() }}"><a href="#/PracticeList/ass/2" ng-click="showPractices(2, false)">{{ 'left_menu-allowances' | i18n }}</a></li> -->
	<!-- 			        		</ul> -->
	<!-- 			        	</div> -->
	<!-- 			        </div> -->
	<!-- 				</div> -->
				<!-- Main section with informations and practices -->
	<!-- 		<div ng-class="{col-md-7:!frameOpened, col-md-9:frameOpened}"> -->
	<!-- 				<div ng-class="{'col-md-8':!frameOpened, 'col-md-10':frameOpened}"> -->
	<!-- 					<div class="col-md-10"> -->
							<div class="row" align="center" style="height: 85px"><!-- ; margin-top: 20px; -->
								<div><!-- "text-align: center" -->
									<table>
										<tr>
											<td width="35%" align="center" valign="middle"><img src="img/gamification_small.jpeg" alt="Logo gamification" title="Logo gamification" /></td>
											<td width="65%" align="center" valign="middle"><h1>{{ 'app_home-title' | i18n }}</h1></td>
										</tr>
									</table>
								</div>
							</div>
							
<!-- 							<div class="row"> -->
<!-- 								<div class="well">style="height: 250px" -->
<!-- 									<table class="table">ng-init="retrieveUserData()" style="width: 98%" -->
<!-- 										<tr> -->
<!-- 											<th colspan="3" align="center"> -->
<!-- 												<strong>{{ 'citizen_info' | i18n }}</strong> -->
<!-- 											</th> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<td width="10%" rowspan="4" align="center"> -->
<!-- 												<a href="#" -->
<!-- 													class="thumbnail"><img -->
<!-- 													src="img/user.jpg" alt=""> -->
<!-- 												</a> -->
<!-- 											</td> -->
<!-- 											<td width="45%">{{ 'citizen_name' | i18n }}: <strong>{{ getUserName() }}</strong></td><span id="user_name"></span> -->
<!-- 											<td width="45%">{{ 'citizen_address' | i18n }}: <strong>{{ utenteCS.indirizzoRes }},{{ utenteCS.capRes }},{{ utenteCS.cittaRes }}-{{ utenteCS.provinciaRes }}</strong></td>{{ translateUserGender(user.gender) }} -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<td>{{ 'citizen_surname' | i18n }}: <strong>{{ getUserSurname() }}</strong></td><span id="user_surname"></span> -->
<!-- 											<td>{{ 'citizen_mail' | i18n }}: <strong>{{ getMail() }}</strong></td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<td>{{ 'citizen_gender' | i18n }}: <strong>{{ utenteCS.sesso }}</strong></td> -->
<!-- 											<td>{{ 'citizen_birth_municipality' | i18n }}: <strong>{{ utenteCS.luogoNascita }} ({{ utenteCS.provinciaNascita }})</strong></td> -->
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<td>{{ 'citizen_phone' | i18n }}: <strong>{{ utenteCS.cellulare }}</strong></td> -->
<!-- 											<td>{{ 'citizen_date_of_birth' | i18n }}: <strong>{{ utenteCS.dataNascita }}</strong></td> -->
<!-- 										</tr> -->
<!-- 									</table> -->
<!-- 								</div> -->
<!-- 							</div> -->
							
							<div class="row" style="height: 30px;" ng-show="!frameOpened">&nbsp;</div>
							<div ng-view class="row" >{{ 'loading_text'| i18n }}...</div>
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
	</div>	
</body>

</html>