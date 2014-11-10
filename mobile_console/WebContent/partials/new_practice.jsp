<!DOCTYPE html>
<html lang="en" ng-app="cp">
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>

<head lang="en">
<meta charset="utf-8">
<title>Citizen Portal</title>

<link href="../css/bootstrap.min.css" rel="stylesheet">
<link href="../css/bootstrap-responsive.min.css" rel="stylesheet">
<link href="../css/prettify.css" rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.no-icons.min.css"
	rel="stylesheet">
<link
	href="//netdna.bootstrapcdn.com/font-awesome/4.0.0/css/font-awesome.css"
	rel="stylesheet">
<link href="../css/xeditable.css" rel="stylesheet">

<!-- required libraries -->
<script src="../lib/jquery.min.js"></script>
<script src="../lib/angular.js"></script>
<script src="../lib/angular-route.js"></script>
<script src="../lib/angular-sanitize.js"></script>
<script src="../lib/bootstrap.min.js"></script>
<script src="../lib/angular-strap.js"></script>
<script src="../lib/xeditable.js"></script>
<script src="../lib/ui-bootstrap-tpls.min.js"></script>
<script src="../js/services.js"></script>

<!-- optional libraries -->
<script src="../lib/underscore-min.js"></script>
<script src="../lib/moment.min.js"></script>
<script src="../lib/fastclick.min.js"></script>
<script src="../lib/prettify.js"></script>
<script src="../lib/angular-resource.min.js"></script>
<script src="../lib/angular-cookies.min.js"></script>
<script src="../lib/angular-route.min.js"></script>
<script src="../lib/xeditable.min.js"></script>

<!-- <script> -->
<%-- var token="<%=request.getAttribute("token")%>"; --%>
<%-- var userId="<%=request.getAttribute("user_id")%>"; --%>
<%-- var user_name="<%=request.getAttribute("user_name")%>"; --%>
<%-- var user_surname="<%=request.getAttribute("user_surname")%>"; --%>
<%-- var current_view="<%=request.getAttribute("view")%>"; --%>
<!-- </script> -->

</head>

<!-- ng-controller="PracticeCtrl" -->
<body>
	<div class="navbar navbar-fixed-top navbar-inverse">
		<div class="navbar-inner">
			<div class="container">
				<a class="btn btn-navbar" data-toggle="collapse"
					data-target=".nav-collapse"> <span class="icon-bar"></span> <span
					class="icon-bar"></span> <span class="icon-bar"></span>
				</a>
				<div class="btn-group pull-right">
					<a class="btn" href="../logout" ng-click="logout()"> <i class="icon-user"></i>
						Logout
					</a>
				</div>
				<div class="nav-collapse">
					<ul class="nav">
						<li class="active">
							<a href="../" ng-click="home()">Home</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="container" style="margin-top: 50px; text-align: center" >
	
<!-- <div class="container"  style="width: 120%; margin-top: 50px"> -->
		<div class="span11 well">
			<div class="row">
				<div class="span11">
					<h3>Nuova Pratica</h3>
				</div>
			</div>
			<iframe width="1000" height="800" ng-src="https://elixforms.anthesi.it/rwe2/module_preview.jsp?MODULE_TAG=domanda_alloggio">
				Loading Iframe...
			</iframe>
		</div>
		<div class="span12">
		<hr>
		<footer>
			<p>&copy; SmartCampus 2013</p>
		</footer>
	</div>
	</div>
</body>

</html>