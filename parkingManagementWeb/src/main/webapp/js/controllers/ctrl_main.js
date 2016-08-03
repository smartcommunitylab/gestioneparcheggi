'use strict';

/* Controllers */
var pmControllers = angular.module('pmControllers');

pm.controller('MainCtrl',['$scope', '$http', '$route', '$window', '$routeParams', '$rootScope', 'localize', '$locale', '$dialogs', 'sharedDataService', '$filter', 'invokeWSService', 'invokeDashboardWSService', 'invokeWSServiceProxy','invokePdfServiceProxy','initializeService','utilsService','getMyMessages','$timeout',
    function($scope, $http, $route, $window, $routeParams, $rootScope, localize, $locale,  $dialogs, sharedDataService, $filter, invokeWSService, invokeDashboardWSService, invokeWSServiceProxy, invokePdfServiceProxy, initializeService, utilsService, getMyMessages, $timeout) {
    
    $scope.setFrameOpened = function(value){
    	$rootScope.frameOpened = value;
    };
    
    $scope.setViewTabs = function(){
    	$scope.hideHome();
    	$scope.setNextButtonViewLabel("Chiudi");
    	$scope.setFrameOpened(true);
    };
    
    $scope.setNextButtonViewLabel = function(value){
    	$rootScope.buttonNextViewLabel = value;
    };
    
    $window.onresize = function() {
        changeTemplate();
        $scope.$apply();
    };
    changeTemplate();

    function changeTemplate() {
        var screenWidth = $window.innerWidth;
        if (screenWidth >= 1170) {
        	$scope.tabletWiew = false;
        } else {
        	$scope.tabletWiew = true;
        }	
    }

    $scope.$route = $route;
    //$scope.$location = $location;
    $scope.$routeParams = $routeParams;
    //this.params = $routeParams;
    
    $scope.userCF = sharedDataService.getUserIdentity(); 
    
    $scope.app ;
                  			
    //$scope.citizenId = userId;
    $scope.user_token = token;
                  			
    // new elements for view
    $scope.currentView;
    $scope.editMode;
    $scope.currentViewDetails;
                  			
    // max practices displayed in home list
    $scope.maxPractices = 10;
    $scope.practicesWSM = [];

    // for language icons
    var itaLanguage = "active";
    var engLanguage = "";
    
	// for localization
    $scope.setEnglishLanguage = function(){
    	$scope.used_lang = "i18n/angular-locale_en-EN.js";
    	itaLanguage = "";
    	engLanguage = "active";
    	//$scope.setUserLocale("en-US");
    	//$locale.id = "en-US";
    	localize.setLanguage('en-US');
    	sharedDataService.setUsedLanguage('eng');
    };
    
    $scope.setItalianLanguage = function(){
    	$scope.used_lang = "i18n/angular-locale_it-IT.js";
    	itaLanguage = "active";
    	engLanguage = "";
    	//$scope.setUserLocale("it-IT");
    	//$locale.id = "it-IT";
    	localize.setLanguage('it-IT');
    	sharedDataService.setUsedLanguage('ita');
    };
    
    if(sharedDataService.getUsedLanguage() == 'ita'){
    	// here I force ita for the first app access
    	$scope.setItalianLanguage();
    }
    
    $scope.setUserLocale = function(lan){
    	var lan_uri = '';
    	if(lan == "it-IT"){
    		lan_uri = 'i18n/angular-locale_it-IT.js';
    	} else if (lan == "en-US"){
    		lan_uri = 'i18n/angular-locale_en-EN.js';
    	}
    	$http.get(lan_uri)
    		.success(function(results){
    			console.log("Success get locale " + results);
    			$locale = results;
    			//angular.copy(results, $locale);
    			$locale.id;
    		})
    		.error(function(results) {
        	console.log("Error get locale " + results);
        });
    };
    
    $scope.isActiveItaLang = function(){
        return itaLanguage;
    };
                  			
    $scope.isActiveEngLang = function(){
    	return engLanguage;
    };
    
    // for services selection
    var homeShowed = true;
    // for menu manageing
    var home = "";
    var parkhome = "";
    var auxhome = "";
    
    var homeSubPark = "";
    var editingPark = "active";
    var editingBike = "";
    var viewingAll = "";
    
    // ----------------------- Dashboard section ----------------------
    var dashboard = "active";
    
    $scope.setHomeDashboardActive = function(){
    	home = "";
    	dashboard = "active";
    	parkhome = "";
    	auxhome = "";
    	viewingAll = "";
    	sharedDataService.setInGlobalLogPage(false);
    };
    
    $scope.isHomeDashboardActive = function(){
        return dashboard;
    };
    // ------------------- End of Dashboard section -------------------
        
    $scope.hideHome = function(){
    	homeShowed = false;   		
    };
    
    $scope.showHome = function(){
    	homeShowed = true;
    };            			
                  			
    $scope.isHomeShowed = function(){
    	return homeShowed;
    };
    
    $scope.isHomeActive = function(){
    	return home;
    };
      
    $scope.home = function() {
    	$scope.setFrameOpened(false);
    	// I refresh all the actived Link
    	home="active";
    	dashboard = "";	// Used for dashboard
    	parkhome = "";
    	auxhome = "";
    	homeSubPark = "";
    	editingPark = "";
    	editingBike = "";
    	viewingAll = "";
        //window.document.location = "./";
        $scope.showHome();
        sharedDataService.setInGlobalLogPage(false);
    };
    
    $scope.setHomeParkActive = function(){
    	home = "";
    	dashboard = "";	// Used for dashboard
    	parkhome = "active";
    	auxhome = "";
    	homeSubPark = "";
    	editingPark = "active";
    	editingBike = "";
    	viewingAll = "";
    	sharedDataService.setInGlobalLogPage(false);
    };
    
    $scope.isHomeParkActive = function(){
        return parkhome;
    };
    
    $scope.setHomeAuxActive = function(){
    	home = "";
    	dashboard = "";	// Used for dashboard
    	parkhome = "";
    	auxhome = "active";
    	viewingAll = "";
    };
    
    $scope.isHomeAuxActive = function(){
        return auxhome;
    };
    
    $scope.setHomeSubParkActive = function(){
    	homeSubPark = "active";
        editingPark = "";
        editingBike = "";
        viewingAll = "";
    };
    
    $scope.isHomeSubParkActive = function(){
        return homeSubPark;
    };
    
    $scope.setEditingParkActive = function(){
    	homeSubPark = "";
        editingPark = "active";
        editingBike = "";
        viewingAll = "";
    };
    
    $scope.isEditingParkActive = function(){
        return editingPark;
    };
    
    $scope.setEditingBikeActive = function(){
    	homeSubPark = "";
        editingPark = "";
        editingBike = "active";
        viewingAll = "";
    };
    
    $scope.isEditingBikeActive = function(){
        return editingBike;
    };
    
    $scope.setViewAllActive = function(){
    	parkhome = "";
    	homeSubPark = "";
        editingPark = "";
        editingBike = "";
        viewingAll = "active";
        auxhome = "";
        dashboard = "";
        sharedDataService.setInGlobalLogPage(false);
    };
    
    $scope.isViewAllActive = function(){
        return viewingAll;
    };
    
    $scope.logout = function() {
    	// Clear some session variables
    	sharedDataService.setName(null);
        sharedDataService.setSurname(null);
        sharedDataService.setBase64(null);
        $scope.user_token = null;
        sharedDataService.setInGlobalLogPage(false);
        
    	window.location.href = "logout";
    };
                  		    
    $scope.getToken = function() {
        return 'Bearer ' + $scope.user_token;
    };
                  		    
    $scope.authHeaders = {
         'Authorization': $scope.getToken(),
         'Accept': 'application/json;charset=UTF-8'
    };
                  		    
    // ------------------- User section ------------------
    //$scope.retrieveUserData = function() {
    	//$scope.getUser();				// retrieve user data
    	//$scope.getUserUeNationality();	// retrieve the user ue/extraue Nationality
    //};
    
//    $scope.user;
//    $scope.getUser = function() {
//    	console.log("user id " + $scope.citizenId );
//    	$http({
//        	method : 'GET',
//        	url : 'rest/citizen/user/' + $scope.citizenId,
//        	params : {},
//            headers : $scope.authHeaders
//        }).success(function(data) {
//        	$scope.user = data;
//        }).error(function(data) {
//        });
//    };
    
    // For user shared data
    if(user_name != null && user_surname != null){
    	sharedDataService.setName(user_name);
    	sharedDataService.setSurname(user_surname);
    }
    
    $scope.initPsManagers = function(vals){
    	var tmp = [];
    	tmp = vals.split(",");
    	return tmp;
    };
    
    $scope.initMunicipalities = function(vals){
    	var tmp = [];
    	tmp = vals.split(",");
    	return tmp;
    };
    
    var correctStringToList = function(stringList){
    	// appId=tn, description=posti per automobile, name=Car, language_key=car_vehicle, userName=trento1
    	var list = [];
    	var subList = stringList.substring(1, stringList.length - 1);
    	var elements = subList.split("},");
    	for(var i = 0; i < elements.length; i++){
    		var allAttribute = elements[i].split(", ");
    		var appId = allAttribute[0].split("=")[1];
    		var description = allAttribute[1].split("=")[1];
    		var name = allAttribute[2].split("=")[1];
    		var language = allAttribute[3].split("=")[1];
    		var userName = allAttribute[4].split("=")[1];
    		var corrVehicleType = {
    			appId: appId,
    			description: description,
    			name: name,
    			language_key: language,
    			userName: userName,
    			visible: false
    		};
    		list.push(corrVehicleType);
    	}
    	return list;
    }
    
    sharedDataService.setConfAppId(conf_app_id);
    sharedDataService.setConfMapCenter(conf_map_center);
    if(conf_map_recenter && conf_map_recenter != "null"){
    	sharedDataService.setConfMapRecenter(conf_map_recenter);
    }
    sharedDataService.setConfMapZoom(conf_map_zoom);
    //var zone_types = [];
    //zone_types.push(conf_macrozone_type);
    //zone_types.push(conf_microzone_type);
    //sharedDataService.setZoneTypeList(zone_types);
    //sharedDataService.setMicroZoneType(conf_microzone_type);
    //sharedDataService.setMacroZoneType(conf_macrozone_type);
    var ps_manager_vals = $scope.initPsManagers(conf_ps_managers);
    sharedDataService.setPsManagerVals(ps_manager_vals);
    $scope.widget_filters = initializeService.setWidgetFilters(conf_filters);
    $scope.widget_show_elements = initializeService.setWidgetElements(conf_elements);
    $scope.slots_vehicle_types = initializeService.setSlotsTypes(correctStringToList(conf_vehicle_type_list));
    initializeService.setConfAppId(conf_app_id);
    initializeService.setConfWidgetUrl(conf_widget_url);
    $scope.show_vt_footer = (conf_app_id == 'tn')?true:false;
 
    $scope.correctStringToJsonString = function(data){
    	var tmpData = data.replace(new RegExp('=','g'), '\":\"');
    	tmpData = tmpData.replace(new RegExp(',\u0020','g'), '\",\u0020\"'); // era new RegExp(', ','g'), '\", \"'
    	tmpData = tmpData.replace(new RegExp('{','g'), '{\"');
    	tmpData = tmpData.replace(new RegExp('}','g'), '\"}');
    	tmpData = tmpData.replace(/:\"\[\{/g, ':[{');
    	tmpData = tmpData.replace(/\}\]",/g, '}],');
    	tmpData = tmpData.replace(/\}",/g, '},');
    	tmpData = tmpData.replace(/,\u0020\"\{/g, ',\u0020{');		// era /, \"\{/g, ', {'
    	tmpData = tmpData.replace(/\]\"\}/g, ']}');
    	tmpData = tmpData.replace(/\"true\"/g, 'true');
    	tmpData = tmpData.replace(/\"false\"/g, 'false');
    	return tmpData;
    };
    
    $scope.loadConfObject = function(data){
    	var zoneTypes = [];
    	var cleanedData = $scope.correctStringToJsonString("{list\":" + data + "}");
    	var objList = JSON.parse(cleanedData);
    	var visibleObjList = objList.list;
    	for(var i = 0; i < visibleObjList.length; i++){
    		if(visibleObjList[i].id.indexOf("Zone") > -1){
    			var zone_type = {
    				value: visibleObjList[i].type,
    				label: visibleObjList[i].title
    			};
    			zoneTypes.push(zone_type);
    		}
    	}
    	//var allObjs = data.split("}]}");
    	//for(var i = 0; i < allObjs.length - 1; i++){
    	//	var objFields = allObjs[i].split(", attributes=[{");
    	//	var ids = objFields[0].split("=");
    	//	var showedObj = {
    	//			id : ids[1],
    	//			attributes: $scope.correctAtributes(objFields[1])
    	//	};
    	//	visibleObjList.push(showedObj);
    	//}
    	sharedDataService.setZoneTypeList(zoneTypes);
    	sharedDataService.setVisibleObjList(visibleObjList);
    	initializeService.setVisibleObjList(visibleObjList);
    	initializeService.initComponents();						// new method
    	initializeService.correctWidgetFiltersAndElements();	// new method
    };
    
    $scope.correctAtributes = function(data){
    	var corrAttribList = [];
    	var attribList = data.split("}, {");
    	for(var i = 0; i < attribList.length; i++){
    		var attribObj = attribList[i].split(", ");
    		var code = "";
    		var editable = "";
    		var visible = "";
    		var required = "";
    		for(var j = 0; j < attribObj.length; j++){
    			var rec = attribObj[j].split("=");
    			if(rec[0].indexOf("code") > -1){
    				code = rec[1];
    			}
    			if(rec[0].indexOf("editable") > -1){
    				editable = (rec[1] === 'true');
    			}
    			if(rec[0].indexOf("visible") > -1){
    				visible = (rec[1] === 'true');
    			}
    			if(rec[0].indexOf("required") > -1){
    				required = (rec[1] === 'true');
    			}
    		}
    		var attrib = {
    			code: code,
    			visible: visible,
    			required: required,
    			editable: editable
    		};
    		corrAttribList.push(attrib);
    	}
    	return corrAttribList;
    };
    
    $scope.loadConfObject(object_to_show);
	
	$scope.initComponents = function(){
	    $scope.showedObjects = sharedDataService.getVisibleObjList();
	    $scope.showBikeMenuLink = false;
	    $scope.showDashboardMenuLink = false;
	    $scope.showAuxMenuLink = false;
	    for(var i = 0; i < $scope.showedObjects.length; i++){
	    	if($scope.showedObjects[i].id == 'Bp'){
	    		$scope.showBikeMenuLink = true;
	    	}
	    	if($scope.showedObjects[i].id == 'Dashboard'){
	    		if($scope.showedObjects[i].attributes[0].visible){
	    			$scope.showDashboardMenuLink = true;
	    			$scope.setHomeDashboardActive();
	    		} else {
	    			$scope.showDashboardMenuLink = false;
	    		}
	    	}
	    	if($scope.showedObjects[i].id == 'Flux'){
	    		if($scope.showedObjects[i].attributes[0].visible){
	    			$scope.showAuxMenuLink = true;
	    		} else {
	    			$scope.showAuxMenuLink = false;
	    		}
	    	}
	    }
	    if($scope.showDashboardMenuLink == false){
	    	$scope.setHomeParkActive();
			//window.location.href = "home";
	    }
    };
    
    $scope.initComponents();
    
    $scope.getUserName = function(){
  	  return sharedDataService.getName();
    };
    
    $scope.getUserSurname = function(){
  	  return sharedDataService.getSurname();
    };
    
    $scope.getMail = function(){
      return sharedDataService.getMail();
    };
    
    $scope.setMail = function(value){
    	sharedDataService.setMail(value);
    };
    
    $scope.translateUserGender = function(value){
    	if(sharedDataService.getUsedLanguage() == 'eng'){
    		if(value == 'maschio'){
    			return 'male';
    		} else {
    			return 'female';
    		}
    	} else {
    		return value;
    	}
    };
    
    
                  			
}]);