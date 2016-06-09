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
            
            <li ng-if="!disableAllLinks" class="{{ isActiveProfile() }}"><a href="#/profile/{{ gameId }}" ng-click="showProfile()" >{{ 'left_menu-profile' | i18n }}</a></li>
           <!--  <li ng-if="!disableAllLinks" class="{{ isActiveChalleng() }}"><a href="#/challeng/{{ gameId }}" ng-click="showChalleng()" ><strong>{{ 'left_menu-challeng' | i18n }}</strong></a></li>
            <li ng-if="!disableAllLinks" class="{{ isActiveClassification() }}"><a href="#/classification/{{ gameId }}" ng-click="showClassification()" >{{ 'left_menu-classification' | i18n }}</a></li> -->
            <li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" >{{ 'left_menu-rules' | i18n }}</a></li>
            <li class="{{ isActivePrivacy() }}"><a href="#/privacy" ng-click="showPrivacy()" >{{ 'left_menu-privacy' | i18n }}</a></li>
            <li class="{{ isActivePrizes() }}"><a href="#/prizes" ng-click="showPrizes()" >{{ 'left_menu-prizes' | i18n }}</a></li>
            <li class="{{ isActiveCredits() }}"><a href="#/credits" ng-click="showCredits()" >{{ 'left_menu-credits' | i18n }}</a></li>
            
          </ul>
          <ul class="nav navbar-nav navbar-right" >
			<!-- <li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()">IT</a></li> -->
			<!-- <li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()">EN</a></li> -->
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
			            		<li class="{{ isActiveRules() }}"><a href="#/rules" ng-click="showRules()" ><strong>{{ 'left_menu-rules' | i18n }}</strong></a></li>
            					<li class="{{ isActivePrivacy() }}"><a href="#/privacy" ng-click="showPrivacy()" ><strong>{{ 'left_menu-privacy' | i18n }}</strong></a></li>
            					<li class="{{ isActivePrizes() }}"><a href="#/prizes" ng-click="showPrizes()" ><strong>{{ 'left_menu-prizes' | i18n }}</strong></a></li>
								<li class="{{ isActiveCredits() }}"><a href="#/credits" ng-click="showCredits()" ><strong>{{ 'left_menu-credits' | i18n }}</strong></a></li>
								<!-- <li class="divider"></li> -->
								<!-- <li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()"><strong>IT</strong></a></li> -->
								<!-- <li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()"><strong>EN</strong></a></li> -->
			          			<li class="divider"></li>
			          			<li><a href="logout" ng-click="logout()"><strong>{{ 'menu_bar-logout' | i18n }}</strong></a></li>
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
<script type="text/ng-template" id="/dialogs/nickinput.html">
<div class="modal" id="variablesModal" role="dialog">
	<form role="form" name="form">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;Benvenuto/a</h4>
				</div>
				<div class="modal-body">
					<div align="justify">
					Rispondi a queste veloci e semplici domande per registrarti al gioco. 
					Questo permettera' al sistema di recuperare informazioni utili per offrire un servizio piu' personalizzato e adatto alle tue abitudini.<br/>
					<label><input type="checkbox" ng-model="accepted" ng-disabled="false"> Ho letto e accettato il regolamento di gioco e l'informativa sulla privacy:</label>
					<ul>
						<li>
							<a href="view_rules" target="_blank" >Regolamento di gioco</a>
						</li>
						<li>
							<a href="view_privacy" target="_blank" >Informativa privacy</a>
						</li>
						<li>
							<a href="view_prizes" target="_blank" >Premi</a>
						</li>
					</ul>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.mail.$dirty && form.mail.$invalid]">
						<label class="control-label" for="mail">e-mail:</label>
						<input type="text" class="form-control" name="mail" id="mail" ng-disabled="!accepted" placeholder="Inserisci il tuo indirizzo email" ng-model="user.mail" ng-click="clearErroMessages()" ng-keyup="hitEnter($event)" ng-pattern="mailPattern" required>
						<div ng-show="form.mail.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo mail obbligatorio.</div>
						<div ng-show="form.mail.$error.pattern && submitNumber" class="alert alert-danger" role="alert">Campo mail non corretto. Inserisci un indirizzo mail esistente.</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.nickname.$dirty && form.nickname.$invalid]">
						<label class="control-label" for="username">Nick name:</label>
						<input type="text" class="form-control" name="nickname" id="nickname" ng-disabled="!accepted" placeholder="Inserisci un nickname che ti rappresenti nel gioco" ng-model="user.nickname" ng-click="clearErroMessages()" ng-keyup="hitEnter($event)" required>
						<div ng-show="showMessages" class="alert alert-danger" role="alert">{{ errorMessages }}</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.age.$dirty && form.age.$invalid]"><!--  -->
						<label class="control-label">Eta':</label>
						<select type="text" name="age" class="form-control" ng-model="user.age" ng-disabled="!accepted" required><!--  ng-options="a as a.label for a in ages" required -->
							<option value="">Seleziona una fascia d'età</option>
							<option value="1">< 20 anni</option>
							<option value="2">20 - 40 anni</option>
							<option value="3">40 - 70 anni</option>
							<option value="4">> 70 anni</option>
						</select>
						<div ng-show="submitNumber && form.age.$error.required" class="alert alert-danger" role="alert">Valore eta' obbligatorio. Selezionare un valore</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.averagekm.$dirty && form.averagekm.$invalid]">
						<label class="control-label" for="averagekm">Km medi percorsi giornalmente: </label>
						<input id="averagekm" type="number" class="form-control" min="0" name="averagekm" ng-model="user.averagekm" ng-disabled="!accepted" required>
						<div ng-show="submitNumber && form.averagekm.$error.min" class="alert alert-danger" role="alert">Valore non permesso nel campo km</div>
						<div ng-show="submitNumber && form.averagekm.$error.required" class="alert alert-danger" role="alert">Valore km medi obbligatorio</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.transport.$dirty && form.transport.$invalid]">
						<label class="control-label" for="username">Utilizzi quotidianamente i mezzi pubblici? </label>
						<table width="100%">
							<tr><td><label><input type="radio" name="transport" value="yes" ng-model="user.transport" ng-change="clearVehicle()" ng-disabled="!accepted" required> Si'</label></td></tr>
							<tr><td><label><input type="radio" name="transport" value="no" ng-model="user.transport" ng-change="clearVehicle()" ng-disabled="!accepted" required> No</label></td></tr>
						</table>
						<div ng-show="submitNumber && form.transport.$error.required" class="alert alert-danger" role="alert">Valore obbligatorio, selezionare si' o no</div>
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.vehicle.$dirty && form.vehicle.$invalid || form.vehicle.$error.required]">
						<label class="control-label" for="username">Mezzi usati abitualmente per gli spostamenti: </label>
						<table width="100%">
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="0" name="vehicle" ng-model="user.vehicle[0]" ng-required="!someSelectedTrans(user.vehicle)" ng-disabled="!accepted"> treno</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="1" name="vehicle" ng-model="user.vehicle[1]" ng-required="!someSelectedTrans(user.vehicle)" ng-disabled="!accepted"> autobus</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="2" name="vehicle" ng-model="user.vehicle[2]" ng-required="!someSelectedTrans(user.vehicle)" ng-disabled="!accepted"> auto condivisa</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='yes'"><input type="checkbox" value="3" name="vehicle" ng-model="user.vehicle[3]" ng-required="!someSelectedTrans(user.vehicle)" ng-disabled="!accepted"> bici condivisa</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='no'"><input type="checkbox" value="4" name="vehicle" ng-model="user.vehicle[4]" ng-required="!someSelectedPrivat(user.vehicle)" ng-disabled="!accepted"> auto privata</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='no'"><input type="checkbox" value="5" name="vehicle" ng-model="user.vehicle[5]" ng-required="!someSelectedPrivat(user.vehicle)" ng-disabled="!accepted"> bici privata</label></td>
							</tr>
							<tr>
								<td><label ng-if="user.transport=='no'"><input type="checkbox" value="6" name="vehicle" ng-model="user.vehicle[6]" ng-required="!someSelectedPrivat(user.vehicle)" ng-disabled="!accepted"> a piedi</label></td>
							</tr>
							<tr>
								<td><div ng-show="submitNumber && form.vehicle.$error.required" class="alert alert-danger" role="alert">Veicolo obbligatorio. Selezionare almeno un elemento</div></td>
							</tr>
						</table>
					</div>
					<div class="form-group" ng-class="{true: 'has-error'}[form.invitation_person.$dirty && form.invitation_person.$invalid]">
						<label class="control-label" for="invitation">Chi ti ha invitato a questo gioco? (nickname)</label>
						<input type="text" class="form-control" name="invitation_person" id="invitation" placeholder="Inserisci il nickname di chi ti ha invitato al gioco" ng-model="user.invitation" ng-disabled="!accepted">
						<div ng-show="showInvitationMessages" class="alert alert-danger" role="alert">{{ errorInvitationMessages }}</div>
					</div>
				</div>
				<div class="required_desc"><p>Il simbolo * indica un campo obbligatorio</p></div>
				<div class="modal-footer">
					<!-- <button type="button" class="btn btn-default" ng-click="cancel()">Annulla</button> -->
					<button type="button" class="btn btn-primary" ng-click="submitNumber=1;save(form)" ng-disabled="(form.$dirty && form.$invalid) || form.$pristine || !accepted" >OK</button>
				</div>
			</div>
		</div>
	</form>
</div>
</script>
<script type="text/ng-template" id="/dialogs/mailinput.html">
<div class="modal" id="variablesModal" role="dialog">
	<form role="form" name="form">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title"><span class="glyphicon glyphicon-user"></span>&nbsp;&nbsp;Benvenuto/a</h4>
				</div>
				<div class="modal-body">
					<div align="justify">
					Per utilizzare tutte le funzionalita' del gioco e per essere sempre aggiornato sulle novita' e' necessario che tu inserisca la tua mail. 
					</div>
					<div class="form-group required" ng-class="{true: 'has-error'}[form.mail.$dirty && form.mail.$invalid]">
						<label class="control-label" for="mail">e-mail:</label>
						<input type="text" class="form-control" name="mail" id="mail" placeholder="Inserisci il tuo indirizzo email" ng-model="user.mail" ng-click="clearErroMessages()" ng-keyup="hitEnter($event)" ng-pattern="mailPattern" required>
						<div ng-show="form.mail.$error.required && submitNumber" class="alert alert-danger" role="alert">Campo mail obbligatorio.</div>
						<div ng-show="form.mail.$error.pattern && submitNumber" class="alert alert-danger" role="alert">Campo mail non corretto. Inserisci un indirizzo mail esistente.</div>
					</div>
				</div>
				<div class="required_desc"><p>Il simbolo * indica un campo obbligatorio</p></div>
				<div class="modal-footer">
					<!-- <button type="button" class="btn btn-default" ng-click="cancel()">Annulla</button> -->
					<button type="button" class="btn btn-primary" ng-click="submitNumber=1;save(form)" ng-disabled="(form.$dirty && form.$invalid) || form.$pristine" >OK</button>
				</div>
			</div>
		</div>
	</form>
</div>
</script>
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
							<option ng-value="no_val">No</option>
							<option ng-value="maybe_val">Forse</option>
							<option ng-value="yes_val">Sì</option>
						</select>
						<table width="100%" ng-if="!showSelect">
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="no_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> No</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="maybe_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> Forse</label></td>
							</tr>
							<tr>
								<td><label><input type="radio" name="new_habits_maintaining" ng-model="user.surveyData.new_habits_maintaining" ng-value="yes_val" ng-required="user.surveyData.change_of_habits!=nothing_val"> Sì</label></td>
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
						<label class="control-label" for="app_suggestion">10) Hai qualche suggerimento o idea per migliorare la App ViaggiaRovereto Play&Go (e.g., nuovi servizi o funzionalità, miglioramento delle funzionalità esistenti)?</label>
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
					<!-- <button type="button" class="btn btn-default" ng-click="cancel()">Annulla</button> -->
					<button type="button" class="btn btn-primary" ng-click="submitNumber=1;save(form)" ng-disabled="form.$invalid || form.$pristine" >OK</button><!-- ng-disabled="(form.$dirty && form.$invalid) || form.$pristine" -->
				</div>
			</div>
		</div>
	</form>
</div>
</script>

</html>