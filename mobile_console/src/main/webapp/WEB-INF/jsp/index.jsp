<!DOCTYPE html>
<html ng-app="cp">
<head id="myHead" lang="it">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Green Game</title>

<link href="css/bootstrap.css" rel="stylesheet">
<link href="css/bootstrap-theme.min.css" rel="stylesheet">
<link href="css/xeditable.css" rel="stylesheet">
<link href="css/modaldialog.css" rel="stylesheet">
<link href="css/gg_style.css" rel="stylesheet">
<link href="img/gamification.ico" rel="shortcut icon" type="image/x-icon" />

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
            
            <li class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" >{{ 'left_menu-profile' | i18n }}</a></li>
            <li class="{{ isActiveChalleng() }}"><a href="#/challeng/{{ gameId }}" ng-click="showChalleng()" ><strong>{{ 'left_menu-challeng' | i18n }}</strong></a></li>
            <li class="{{ isActiveClassification() }}"><a href="#/classification/{{ gameId }}" ng-click="showClassification()" >{{ 'left_menu-classification' | i18n }}</a></li>
            <li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" >{{ 'left_menu-rules' | i18n }}</a></li>
            
          </ul>
          <ul class="nav navbar-nav navbar-right" >
			<!-- <li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()">IT</a></li> -->
			<!-- <li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()">EN</a></li> -->
<!--             <li class="active" > -->
<!--             	<a> -->
<!--             		<span class="glyphicon glyphicon-user"></span> -->
<!--             		{{ getUserName() }} {{ getUserSurname() }} -->
<!--             	</a> -->
<!--             </li> --> 
            <li><a href="logout" ng-click="logout()">{{ 'menu_bar-logout' | i18n }}</a></li><!-- ng-click="logout()" -->
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
			            		<li class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" ><strong>{{ 'left_menu-profile' | i18n }}</strong></a></li>
			            		<li class="{{ isActiveChalleng() }}"><a href="#/challeng/{{ gameId }}" ng-click="showChalleng()" ><strong>{{ 'left_menu-challeng' | i18n }}</strong></a></li>
			            		<li class="{{ isActiveClassification() }}"><a href="#/classification/{{ gameId }}" ng-click="showClassification()" ><strong>{{ 'left_menu-classification' | i18n }}</strong></a></li>
			            		<li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" ><strong>{{ 'left_menu-rules' | i18n }}</strong></a></li>
								<!-- <li class="divider"></li> -->
								<!-- <li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()"><strong>IT</strong></a></li> -->
								<!-- <li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()"><strong>EN</strong></a></li> -->
			          			<li class="divider"></li>
			          			<li><a href="logout" ng-click="logout()"><strong>{{ 'menu_bar-logout' | i18n }}</strong></a></li>
			            	</ul>
			            </li>
			         </ul>
			    </div>
<!-- 			    <div id="navbar_col" class="collapse navbar-collapse"> -->
<!-- 		          <ul class="nav navbar-nav"> -->
<!-- 		            <li class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" >{{ 'left_menu-profile' | i18n }}</a></li> -->
<!-- 		            <li class="{{ isActiveClassification() }}"><a href="#/classification/{{ gameId }}" ng-click="showClassification()" >{{ 'left_menu-classification' | i18n }}</a></li> -->
<!-- 		            <li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" >{{ 'left_menu-rules' | i18n }}</a></li>  -->
<!-- 		          </ul> -->
<!-- 		          <ul class="nav navbar-nav navbar-right" > -->
<!-- 		          	<li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()">IT</a></li> -->
<!-- 		          	<li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()">EN</a></li> -->
<!-- 		            <li><a href="logout" ng-click="logout()">{{ 'menu_bar-logout' | i18n }}</a></li> -->
<!-- 		          </ul> -->
<!--         		</div> -->
	    	</div>
	    </div>
      </div><!-- /.container -->
    </div><!-- /.navbar -->
    
	<div class="container">
<!-- 		<div class="row" style="margin-top:70px;"> -->
		<div class="row">
			<div class="col-md-8 col-md-offset-2">
				<div class="panel panel-default" style="margin-top:65px;">
			  		<div class="panel-body">
			  			<div style="margin:5px 5px;">
			  			
				<!-- Main section with informations and practices -->
	<!-- 		<div ng-class="{col-md-7:!frameOpened, col-md-9:frameOpened}"> -->
	<!-- 				<div ng-class="{'col-md-8':!frameOpened, 'col-md-10':frameOpened}"> -->
	<!-- 					<div class="col-md-10"> -->
<!-- 							<div class="row" align="center" style="height: 85px">; margin-top: 20px; -->
<!-- 								<div>"text-align: center" -->
<!-- 									<table> -->
<!-- 										<tr> -->
<!-- 											<td width="35%" align="center" valign="middle"><img src="img/gamification_small.jpeg" alt="Logo gamification" title="Logo gamification" /></td> -->
<!-- 											<td width="65%" align="center" valign="middle"><h1>{{ 'app_home-title' | i18n }}</h1></td> -->
<!-- 										</tr> -->
<!-- 									</table> -->
<!-- 								</div> -->
<!-- 							</div> -->
							
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
								<ng-view class="row">{{ 'loading_text'| i18n }}...</ng-view>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div id="my-big-footer" class="row">
				<div class="col-md-8 col-md-offset-2" align="center">
					
					<h5><font face="Raleway-bold" size="5" color="gray"><strong>Green Game</strong></font> con ViaggiaRovereto</h5>
					&egrave; un progetto di:
					<br>
					<footer>
						<!-- <p>&copy; SmartCampus 2013</p> -->
						<img src="img/footer/footer.png" width="90%" alt="" title="" />
					</footer>
				</div>
			</div>
			<div id="my-small-footer" class="row">
				<div class="col-md-4 col-md-offset-2" align="center">
					<h5><font face="Raleway-bold" size="5" color="gray"><strong>Green Game</strong></font></h5>
				</div>
				<div class="col-md-4" align="center">	
					<h5> con ViaggiaRovereto</h5>
				</div>
				<div class="col-md-4" align="center">	
					&egrave; un progetto di:
				</div>	
					<footer>
						<br>
						<div class="col-md-4" align="center">	
							<img src="img/footer/streetLife.svg" width="120" alt="" title="" />
						</div>
						<br>
						<div class="col-md-4" align="center">	
							<img src="img/footer/fbk.svg" width="85" alt="" title="" />
						</div>
						<br>
						<div class="col-md-4" align="center">	
							<img src="img/footer/comuneRV.svg" width="150" alt="" title="" />
						</div>
						<br>
						<div class="col-md-4" align="center">	
							<img src="img/footer/caire.svg" width="150" alt="" title="" />
						</div>
						<br>
						<!-- <p>&copy; SmartCampus 2013</p> -->
					</footer>
			</div>
		</div>
	</div>	
</body>
<script type="text/ng-template" id="/dialogs/nickinput.html">
<div class="modal" id="variablesModal" role="dialog"> <!-- aria-labelledby="modalTitle" aria-hidden="true" -->
	<form role="form" name="form">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;Benvenuto</h4>
				</div>
				<div class="modal-body">
					<div>
					Rispondi a queste veloci e semplici domande per accedere alla console. Questo permetter&agrave; al sistema di recuperare informazioni utili per offrire
					un servizio pi&ugrave; personalizzato e adatto alle tue abitudini.
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.nickname.$dirty && form.nickname.$invalid]">
						<label class="control-label" for="username">Nick name:</label>
						<input type="text" class="form-control" name="nickname" id="nickname" placeholder="Inserisci un nickname che ti rappresenti nel gioco" ng-model="user.nickname" ng-click="clearErroMessages()" ng-keyup="hitEnter($event)" required>
						<div ng-show="showMessages" class="alert alert-danger" role="alert">{{ errorMessages }}</div>
					</div>
					<div ng-class="{true: 'has-error'}[form.age.$dirty && form.age.$invalid]"><!-- class="form-group" -->
						<label class="control-label">Et&agrave;:</label>
						<select type="text" name="age" class="form-control" ng-model="user.age" required><!--  ng-options="a as a.label for a in ages" required -->
							<option value="">Seleziona una facia d'eta'</option>
							<option value="1">< 20 anni</option>
							<option value="2">20 - 40 anni</option>
							<option value="3">40 - 70 anni</option>
							<option value="4">> 70 anni</option>
						</select>
						<div ng-show="submitNumber && form.age.$error.required" class="alert alert-danger" role="alert">Valore eta' oggligatorio. Selezionare un valore</div>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.transport.$dirty && form.transport.$invalid]">
						<label class="control-label" for="username">Utilizzi quotidianamente i mezzi pubblici? </label>
						<table width="100%">
							<tr><td><label><input type="radio" name="transport" value="yes" ng-model="user.transport" ng-change="clearVehicle()" required> Si</label></td></tr>
							<tr><td><label><input type="radio" name="transport" value="no" ng-model="user.transport" ng-change="clearVehicle()" required> No</label></td></tr>
						</table>
						<div ng-show="submitNumber && form.transport.$error.required" class="alert alert-danger" role="alert">Valore obbligatorio, selezionare si o no</div>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.vehicle.$dirty && form.vehicle.$invalid || form.vehicle.$error.required]">
						<label class="control-label" for="username">Mezzi usati abitualmente per gli spostamenti: </label>
						<table width="100%">
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="0" name="vehicle" ng-model="user.vehicle[0]" ng-required="!someSelectedTrans(user.vehicle)"> treno</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="1" name="vehicle" ng-model="user.vehicle[1]" ng-required="!someSelectedTrans(user.vehicle)"> autobus</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="2" name="vehicle" ng-model="user.vehicle[2]" ng-required="!someSelectedTrans(user.vehicle)"> auto condivisa</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="3" name="vehicle" ng-model="user.vehicle[3]" ng-required="!someSelectedTrans(user.vehicle)"> bici condivisa</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='no'"><input type="checkbox" value="4" name="vehicle" ng-model="user.vehicle[4]" ng-required="!someSelectedPrivat(user.vehicle)"> auto privata</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='no'"><input type="checkbox" value="5" name="vehicle" ng-model="user.vehicle[5]" ng-required="!someSelectedPrivat(user.vehicle)"> bici privata</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='no'"><input type="checkbox" value="6" name="vehicle" ng-model="user.vehicle[6]" ng-required="!someSelectedPrivat(user.vehicle)"> a piedi</label></td>
							</tr>
							<tr>
								<td><div ng-show="submitNumber && form.vehicle.$error.required" class="alert alert-danger" role="alert">Veicolo obbligatorio. Selezionare almeno un elemento</div></td>
							</tr>
						</table>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.averagekm.$dirty && form.averagekm.$invalid]">
						<label class="control-label" for="averagekm">Km medi percorsi giornalmente </label>
						<input id="averagekm" type="number" class="form-control" min="0" name="averagekm" ng-model="user.averagekm" required>
						<div ng-show="submitNumber && form.averagekm.$error.min" class="alert alert-danger" role="alert">Valore non permesso nel campo km</div>
						<div ng-show="submitNumber && form.averagekm.$error.required" class="alert alert-danger" role="alert">Valore km medi obbligatorio</div>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.invitation_person.$dirty && form.invitation_person.$invalid]">
						<label class="control-label" for="invitation">Chi ti ha proposto il link a questo gioco?</label>
						<input type="text" class="form-control" name="invitation_person" id="invitation" placeholder="Inserisci il nome di chi ti ha invitato al gioco" ng-model="user.invitation">
					</div>
				</div>
				<div class="modal-footer">
					<!-- <button type="button" class="btn btn-default" ng-click="cancel()">Annulla</button> -->
					<button type="button" class="btn btn-primary" ng-click="submitNumber=1;save(form)" ng-disabled="(form.$dirty && form.$invalid) || form.$pristine" >OK</button>
				</div>
			</div>
		</div>
	</form>
</div>
</script>

</html>