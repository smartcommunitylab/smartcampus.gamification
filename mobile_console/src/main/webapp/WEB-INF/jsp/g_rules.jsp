<!DOCTYPE html>
<html ng-app="cp">
<head id="myHead" lang="it">
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Play&Go - Regole</title>
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
</head>
<body>
	<div class="container">
		<div id="big-rules-page">
			<div class="row">
				<div ng-class="col-md-10">
					<div class="panel panel-success-trasparent" >
		  				<div class="panel-body">
						    <h4>Benvenuto nel Gioco Play&Go con ViaggiaRovereto!</h4>
							<div align="justify">
								Partecipare &egrave; semplice: pianifica i tuoi viaggi con ViaggiaRovereto e scegli delle soluzioni di mobilit&agrave; sostenibile per guadagnare punti e avanzare nel gioco. 
								Divertiti a collezionare punti, badges e mettiti alla prova con le challenges settimanali. Sfida i tuoi amici a scalare le classifiche: ogni settimana puoi vincere ricchi premi e 
								contribuire, divertendoti, a rendere la tua citt&agrave; pi&ugrave; sostenibile e smart!
							</div>
							<br/>
							<h4>Come si gioca?</h4>
							<div align="left">
								Pianifica i tuoi viaggi con ViaggiaRovereto (<strong>"Pianifica viaggio"</strong> nella home), salva il tuo itinerario (<strong>"Salva itinerario"</strong> nella pagina di dettaglio) e ricordati di tracciare il tuo 
								percorso quando esegui il viaggio (<strong>"Inizia"</strong> e <strong>"Termina"</strong> in dettaglio viaggio ne "I miei viaggi"). Ogni viaggio ti permette di guadagnare <strong>punti Green Leaves</strong> in proporzione ai km fatti 
								con mezzi sostenibili (a piedi, in bici/bike sharing, e con i mezzi pubblici).
							</div>
							<div align="left">
								Puoi vincere dei bonus in punti Green Leaves:<br>
								<ul>
									<li>Scegliendo dei viaggi ad emissioni zero di CO2, cio&egrave; esclusivamente in bici o a piedi; </li>
									<li>Scegliendo viaggi sostenibili: suggeriti e marcati in verde da ViaggiaRovereto;</li>
									<li>Superando le tue sfide settimanali, quando ti vengono proposte;</li>
									<li>Invitando i tuoi amici a registrarsi al gioco: &egrave; sufficiente che il tuo amico, in fase di registrazione, indichi il tuo nickname Giocatore.</li>
								</ul>
							</div>
							<div align="justify">
								<h4>Incentivi e premi</h4>
								<div align="left">
									Durante il gioco sono previsti <strong>premi settimanali e finali</strong> che andranno a premiare i primi tre classificati nella classifica (settimanale e generale, rispettivamente) a punti Green Leaves.
									<br/>
									Al seguente link puoi scaricare il regolamento completo del gioco: <a href="pdf/regolamento.pdf" alt="regolamento" title="regolamento">regolamento Play&Go</a>.
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
			</div>
		</div>	
		<div id="small-rules-page">
			<div class="row">
				<div ng-class="col-xs-12">
					<div class="panel panel-success-trasparent" >
		  				<div class="panel-body">
						    <h4>Benvenuto nel Gioco Play&Go con ViaggiaRovereto!</h4>
							<div align="justify">
								Partecipare &egrave; semplice: pianifica i tuoi viaggi con ViaggiaRovereto e scegli delle soluzioni di mobilit&agrave; sostenibile per guadagnare punti e avanzare nel gioco. 
								Divertiti a collezionare punti, badges e mettiti alla prova con le challenges settimanali. Sfida i tuoi amici a scalare le classifiche: ogni settimana puoi vincere ricchi premi e 
								contribuire, divertendoti, a rendere la tua citt&agrave; pi&ugrave; sostenibile e smart!
							</div>
							<br/>
							<h4>Come si gioca?</h4>
							<div align="left">
								Pianifica i tuoi viaggi con ViaggiaRovereto (<strong>"Pianifica viaggio"</strong> nella home), salva il tuo itinerario (<strong>"Salva itinerario"</strong> nella pagina di dettaglio) e ricordati di tracciare il tuo 
								percorso quando esegui il viaggio (<strong>"Inizia"</strong> e <strong>"Termina"</strong> in dettaglio viaggio ne "I miei viaggi"). Ogni viaggio ti permette di guadagnare <strong>punti Green Leaves</strong> in proporzione ai km fatti 
								con mezzi sostenibili (a piedi, in bici/bike sharing, e con i mezzi pubblici).
							</div>
							<div align="left">
								Puoi vincere dei bonus in punti Green Leaves:<br>
								<ul>
									<li>Scegliendo dei viaggi ad emissioni zero di CO2, cio&egrave; esclusivamente in bici o a piedi; </li>
									<li>Scegliendo viaggi sostenibili: suggeriti e marcati in verde da ViaggiaRovereto;</li>
									<li>Superando le tue sfide settimanali, quando ti vengono proposte;</li>
									<li>Invitando i tuoi amici a registrarsi al gioco: &egrave; sufficiente che il tuo amico, in fase di registrazione, indichi il tuo nickname Giocatore.</li>
								</ul>
							</div>
							<div align="justify">
								<h4>Incentivi e premi</h4>
								<div align="left">
									Durante il gioco sono previsti <strong>premi settimanali e finali</strong> che andranno a premiare i primi tre classificati nella classifica (settimanale e generale, rispettivamente) a punti Green Leaves.
									<br/>
									Al seguente link puoi scaricare il regolamento completo del gioco: <a href="pdf/regolamento.pdf" alt="regolamento" title="regolamento">regolamento Play&Go</a>.
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
		</div>
	</div>		
</body>
</html>