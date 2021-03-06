<!DOCTYPE html>
<html ng-app="pm">
<head id="myHead" lang="it">
<meta charset="utf-8">
<title>{{ 'app_tab-title' | i18n }}</title>

<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/bootstrap-theme.min.css" rel="stylesheet" />
<link href="css/xeditable.css" rel="stylesheet" />
<link href="css/modaldialog.css" rel="stylesheet" />
<link href="css/colorpicker.css" rel="stylesheet" />
<link href="imgs/carpark.ico" rel="shortcut icon" type="image/x-icon" />

<!-- required libraries -->
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="lib/angular.js"></script>
<script src="js/localize.js" type="text/javascript"></script>
<script src="js/dialogs.min.js" type="text/javascript"></script>
<script src="lib/angular-route.js"></script>
<script src="lib/angular-sanitize.js"></script>

<script src="i18n/angular-locale_it-IT.js"></script>
<script src="i18n/angular-locale_en-EN.js"></script>

<script src="js/app.js?1001"></script>
<!-- <script src="js/controllers.js"></script> -->
<script src="js/controllers/ctrl.js?1001"></script>
<script src="js/controllers/ctrl_login.js?1000"></script>
<script src="js/controllers/ctrl_main.js"></script>
<script src="js/controllers/ctrl_park.js"></script>
<script src="js/controllers/ctrl_bike.js"></script>
<script src="js/controllers/ctrl_view.js"></script>
<script src="js/controllers/ctrl_view_gmap.js"></script>

<script src="js/filters.js?1001"></script>
<script src="js/services.js?1001"></script>
<script src="js/directives.js"></script>
<script src="lib/ui-bootstrap-tpls.min.js"></script>

<!-- <script type="text/javascript" src="js/jquery.min.js" /></script> -->
<!-- <script type="text/javascript" src="js/jquery-ui.custom.min.js" ></script> -->
<!-- <script type="text/javascript" src="js/ui.datepicker-it.js" ></script> -->

<!-- optional libraries -->
<script src="lib/angular-resource.min.js"></script>
<script src="lib/angular-cookies.min.js"></script>
<script src="lib/angular-route.min.js"></script>
<script src="lib/xeditable.min.js"></script>
<script src="lib/angular-base64.min.js"></script>
<script src="lib/bootstrap-colorpicker-module.js"></script>

<script src="lib/lodash.js"></script>
<!-- <script src="lib/angular-google-maps.js"></script> -->
<script src="https://maps.google.com/maps/api/js?key=AIzaSyBmKVWmFzh2JHT7q1MLmQRQ7jC4AhkRBDs&sensor=false&v=3.exp"></script>
<script src="lib/ng-map.min.js"></script>

<base href="/parking-management/" />

<script>
<%-- var token="<%=request.getAttribute("token")%>"; --%>
<%-- var userId="<%=request.getAttribute("user_id")%>"; --%>
var user_name="<%=request.getAttribute("user_name")%>";
var user_surname="<%=request.getAttribute("user_surname")%>";
var no_sec="<%=request.getAttribute("no_sec")%>";
var conf_app_id="<%=request.getAttribute("app_id")%>";
var conf_map_center="<%=request.getAttribute("map_center")%>";
var conf_map_zoom="<%=request.getAttribute("map_zoom")%>";
var object_to_show="<%=request.getAttribute("object_showed")%>";

<%-- Part for google analytics --%>

  (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
  (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
  })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

  ga('create', 'UA-54947160-1', 'auto');
  ga('send', 'pageview');

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
  
  <style>
  
  	.borderless td{
	    border: 0;
	}
	
	div.tablewrapper {
		height: 490px; 
		overflow-y: auto;
		margin-bottom: 10px;
	}
	
	.angular-google-map-container {
		height: 610px; 
	}
	
	.colorBox {   
    	float: left;
    	width: 15px;
    	height: 15px;
    	margin: 2px;
    	border-width: 1px;
    	border-style: solid;
    	border-color: rgba(0,0,0,.2);
	}
	
  </style>

</head>

<body>
	<div id="myBody" ng-controller="MainCtrl" ng-init="setItalianLanguage()">
    <div class="navbar navbar-fixed-top navbar-inverse" role="navigation">
      <div class="container-fluid" style="margin-left:160px; margin-right:160px">
        <div class="collapse navbar-collapse">
          <div class="navbar-brand"><strong>{{ 'app_home-title' | i18n }}&nbsp;&nbsp;&nbsp;</strong></div>
          <ul class="nav navbar-nav">
<!--             <li class="{{ isHomeActive() }}"><a href="#/" ng-click="home()">{{ 'menu_bar-home' | i18n }}</a></li> -->
            <li class="{{ isHomeParkActive() }}"><a href="#/park/home" ng-click="setHomeParkActive()">{{ 'menu_bar-homepark' | i18n }}</a></li>
            <li class="{{ isHomeAuxActive() }}"><a href="#/aux/home" ng-click="setHomeAuxActive()">{{ 'menu_bar-homeaux' | i18n }}</a></li>
<!--             <li class="{{ isEditingParkActive() }}"><a href="#/edit/park" ng-click="setEditingParkActive()">{{ 'menu_bar-parkediting' | i18n }}</a></li> -->
<!--             <li class="{{ isEditingBikeActive() }}"><a href="#/edit/bike" ng-click="setEditingBikeActive()">{{ 'menu_bar-bikeediting' | i18n }}</a></li> -->
<!--           	<li class="{{ isViewAllActive() }}"><a href="#/view" ng-click="setViewAllActive()">{{ 'menu_bar-parkview' | i18n }}</a></li> -->
          </ul>
          <ul class="nav navbar-nav navbar-right" >
<!--           	<li class="dropdown"> -->
<!--           		<a href="#" class="dropdown-toggle" data-toggle="dropdown">{{ 'guide' | i18n }} <span class="caret"></span></a> -->
<!--           		<ul class="dropdown-menu" role="menu"> -->
<!--             		<li><a href="http://www.trentinosociale.it/index.php/Servizi-ai-cittadini/Guida-ai-servizi/per-destinatari/Anziani/Abitare-o-disporre-di-un-alloggio-adeguato-e-sicuro/Locazione-alloggio-pubblico-a-canone-sociale" target="_blank">{{ 'document_link_edil' | i18n }}</a></li> -->
<!--             		<li><a href="http://www.trentinosociale.it/index.php/Servizi-ai-cittadini/Guida-ai-servizi/per-destinatari/Anziani/Abitare-o-disporre-di-un-alloggio-adeguato-e-sicuro/Contributo-sul-canone-di-affitto" target="_blank">{{ 'document_link_allowances' | i18n }}</a></li> -->
<!--             	</ul> -->
<!--           	</li> -->
          	<li><a href="mailto:myweb.edilizia@comunitadellavallagarina.tn.it?Subject=Info%20MyWeb" target="_top" alt="myweb.edilizia@comunitadellavallagarina.tn.it" title="myweb.edilizia@comunitadellavallagarina.tn.it">{{ 'usefull_link'| i18n }}</a></li>
          	<li class="{{ isActiveItaLang() }}"><a href ng-click="setItalianLanguage()">IT</a></li>
          	<li class="{{ isActiveEngLang() }}"><a href ng-click="setEnglishLanguage()">EN</a></li>
            <li><a href="" ng-click="logout()">{{ 'menu_bar-logout' | i18n }}</a></li> <!-- href="logout" -->
          </ul>
        </div><!-- /.nav-collapse -->
      </div><!-- /.container -->
    </div><!-- /.navbar -->
	<div class="container-fluid">
<!-- 		<div class="row" style="margin-top:70px;"> -->
		<div class="row">
			<div class="col-md-1"></div>
			<div class="col-md-10">
				<div class="panel panel-default" style="margin-top:65px;">
			  		<div class="panel-body">
			  			<div style="margin:5px 15px;">
<!-- 							<div class="row" align="center" style="height: 100px"> -->
<!-- 								<div> -->
<!-- 									<table> -->
<!-- 										<tr> -->
<!-- 											<td width="100%" align="center" valign="middle"><h1>{{ 'app_home-title' | i18n }}</h1></td> -->
<!-- 										</tr> -->
<!-- 									</table> -->
									
<!-- 								</div> -->
<!-- 							</div> -->
							<div class="row" ng-show="isHomeParkActive() == 'active'"><!-- style="height: 150px;" -->
								<div class="col-md-2" >
									<div class="panel panel-primary" align="left">
										<div class="panel-heading">
											<h5 class="panel-title">{{ 'park_menu_list' | i18n }}</h5>
										</div>
										<div class="panel-body">
											<ul class="nav nav-pills nav-stacked" style="font-size: 14px">
<!-- 												<li class="{{ isHomeSubParkActive() }}"><a href="#/park/home" ng-click="setHomeSubParkActive()">{{ 'menu_bar-home' | i18n }}</a></li> -->
												<li class="{{ isEditingParkActive() }}"><a href="#/edit/park" ng-click="setEditingParkActive()">{{ 'menu_bar-parkediting' | i18n }}</a></li>
						            			<li class="{{ isEditingBikeActive() }}" ng-show="showBikeMenuLink"><a href="#/edit/bike" ng-click="setEditingBikeActive()">{{ 'menu_bar-bikeediting' | i18n }}</a></li>
						          				<li class="{{ isViewAllActive() }}"><a href="#/view" ng-click="setViewAllActive()">{{ 'menu_bar-parkview' | i18n }}</a></li>
											</ul>
										</div>
									</div>
								</div>
								<div class="col-md-10">
									<div ng-view class="row" ng-hide="isNewPractice()" >{{ 'loading_text'| i18n }}...</div>
								</div>
							</div>
							<div ng-show="isHomeParkActive() != 'active'">
								<div ng-view class="row" ng-hide="isNewPractice()" >{{ 'loading_text'| i18n }}...</div>
							</div>
						</div>
						</div>
					</div>
				</div>
				<div class="col-md-1"></div>
			</div>
			<div class="row">
				<div class="col-md-1"></div>
				<div class="col-md-10">
					<hr>
					<footer>
		<!-- 				<p>&copy; SmartCampus 2013</p> -->
					</footer>
				</div>
				<div class="col-md-1"></div>	
			</div>
		</div>
	</div>	
</body>



</html>