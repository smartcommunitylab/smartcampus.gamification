<html>
<head lang="it">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<title>GameficationConsole - Login</title>

<link href="../css/bootstrap.min.css" rel="stylesheet" />
<link href="../css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="../css/xeditable.css" rel="stylesheet" />
<link href="../css/modaldialog.css" rel="stylesheet" />
<link href="../css/angular-socialshare.css" rel="stylesheet">
<link href="../css/gg_style.css" rel="stylesheet" />
<link href="../img/gamification.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<!-- <script src="https://apis.google.com/js/platform.js" async defer>
    {lang: 'it'}
</script> -->
<script src="../js/jquery.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script src="../lib/angular.js"></script>
<script src="../js/localize.js" type="text/javascript"></script>
<script src="../lib/angular-route.js"></script>
<script src="../lib/angular-sanitize.js"></script>
<script src="../lib/angular-socialshare.js"></script>
<script src="../lib/ui-bootstrap-tpls.min.js"></script>
<script src="../js/dialogs.min.js" type="text/javascript"></script>
<script src="../js/app.js"></script>
<script src="../js/controllers/ctrl.js"></script>
<script src="../js/controllers/ctrl_login.js"></script>
<script src="../js/controllers/ctrl_main.js"></script>
<script src="../js/filters.js"></script>
<script src="../js/services.js"></script>
<script src="../js/directives.js"></script>

<!-- optional libraries -->
<script src="../lib/angular-resource.min.js"></script>
<script src="../lib/angular-cookies.min.js"></script>
<script src="../lib/angular-route.min.js"></script>
<script src="../lib/xeditable.min.js"></script>
<script src="../lib/angular-base64.min.js"></script>
<base href="/gamificationweb/" /> <!-- <%=request.getContextPath()%>/ -->

<script type="text/javascript">
 	function get(name){
 	   if(name=(new RegExp('[?&]'+encodeURIComponent(name)+'=([^&]*)')).exec(location.search))
 	      return decodeURIComponent(name[1]);
 	}
</script>
<style type="text/css">
	.panel {
		background: url(img/paginaAccesso-56-56-56.svg) no-repeat center center fixed;
	}
</style>

<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

</head>
	<body>
		<div class="container">
			<div class="row" style="margin-top: 20px">
				<div id="my-big-login" ng-class="col-md-8">
					<div class="panel panel-default" align="center" background="img/paginaAccesso-56-56-56.svg">
		  				<div class="panel-body">
		<!-- 				<div class="row" style="height: 800px"> -->
							<div class="row" style="margin: 10px 10px 10px 10px; height: 400px">
								<!--[if lt IE 9]>
								<div class="row" style="height: 20px" align="center" ng-init="hideLogin()">
									<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer 8 e versioni inferiori. Aggiorna Internet Explorer ad un versione successiva o utilizza un altro browser per accedere al portale.</font></h4>
								</div>
								<![endif]-->
								<div class="row" style="font-size: 18px; color: red" align="center" id="cookies">
								</div>
								<div style="margin-top: 130px" align="center">
								</div>
								<!-- <div class="well" align="center"> -->
										<form name="f" action="console/login.do" method="POST" accept-charset="utf-8"><!-- j_spring_security_check -->
											<table style="width: 45%;">
												<tr>
													<td colspan="2" id="err_login">
														<script>
															if(get('error') == 'true'){
																var err_div = document.createElement('div');
															 	err_div.className = 'alert alert-danger';
															 	err_div.id = 'err_log_text';
															 	err_div.innerHTML = 'Errore Autenticazione: username o password non corretti';
															 	$("#err_login").append(err_div);
															}
														</script>							
													</td>
												</tr>
												<tr>
													<td width="30%"><label for="input_user">Username:</label></td>
													<td width="70%"><input id="input_user" class="form-control" type='text' name='j_username' /></td>
												</tr>
												<tr>
													<td><label for="input_pwd">Password:</label></td>
													<td><input id="input_pwd" class="form-control" type='password' name='j_password' /></td>
												</tr>
												<tr>
													<td colspan="2">&nbsp;</td>
												</tr>
												<tr>
													<td colspan="2" align="center">
														<input value="Login" name="submit" type="submit" class="btn btn-success btn-lg buttonaccess">&nbsp;
														<input value="Cancella" name="reset" type="reset" class="btn btn-error btn-lg">
													</td>
												</tr>
											</table>
										</form>
								<!-- </div> -->
							</div>
						</div>
					</div>	
				</div>
				<div id="my-small-login" ng-class="col-sm-10">
							<div style="margin: 10px 10px 10px 10px">
								<!--[if lt IE 9]>
								<div class="row" style="height: 20px" align="center" ng-init="hideLogin()">
									<h4><font color="red">Alcune funzionalit&agrave; del portale non sono supportate in Internet Explorer 8 e versioni inferiori. Aggiorna Internet Explorer ad un versione successiva o utilizza un altro browser per accedere al portale.</font></h4>
								</div>
								<![endif]-->
								<div class="row" style="font-size: 18px; color: red" align="center" id="cookies">
								</div>
								<div style="margin: 10px" align="center">
								</div>
								<div class="well" align="center">
										<form name="f" action="console/login.do" method="POST" accept-charset="utf-8"><!-- j_spring_security_check -->
											<table style="width: 45%;">
												<tr>
													<td colspan="2" id="err_login_cel">
														<script>
															if(get('error') == 'true'){
																var err_div = document.createElement('div');
															 	err_div.className = 'alert alert-danger';
															 	err_div.id = 'err_log_text';
															 	err_div.innerHTML = 'Errore Autenticazione: username o password non corretti';
															 	$("#err_login_cel").append(err_div);
															}
														</script>							
													</td>
												</tr>
												<tr>
													<td width="30%"><label for="input_user">Username:</label></td>
													<td width="70%"><input id="input_user" class="form-control" type='text' name='j_username' /></td>
												</tr>
												<tr>
													<td><label for="input_pwd">Password:</label></td>
													<td><input id="input_pwd" class="form-control" type='password' name='j_password' /></td>
												</tr>
												<tr>
													<td colspan="2">&nbsp;</td>
												</tr>
												<tr>
													<td colspan="2" align="center">
														<input value="Login" name="submit" type="submit" class="btn btn-primary">&nbsp;
														<input value="Cancella" name="reset" type="reset" class="btn btn-default">
													</td>
												</tr>
											</table>
										</form>
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
		
	</body>
</html>