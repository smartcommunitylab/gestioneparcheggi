'use strict';

/* Controllers */
var pmControllers = angular.module('pmControllers');

pm.controller('AuxCtrl', ['$scope', '$http', '$routeParams', '$rootScope', '$route', '$location', '$dialogs', 'sharedDataService', '$filter', 'invokeWSService', 'invokeWSServiceProxy', 'invokePdfServiceProxy', 'invokeAuxWSService', 'getMyMessages', '$timeout',
                               function($scope, $http, $routeParams, $rootScope, $route, $location, $dialogs, sharedDataService, $filter, invokeWSService, invokeWSServiceProxy, invokePdfServiceProxy, invokeAuxWSService, getMyMessages, $timeout) { 
	this.$scope = $scope;
    $scope.params = $routeParams;
    
    $scope.showDetails = false;
    $scope.showFiltered = false;
    $scope.maxLogs = 15;
    
    $scope.logtabs = [ 
        { title:'Log generale', index: 1, content:"partials/aux/logs/global_logs.html" },
        { title:'Log vie', index: 2, content:"partials/aux/logs/street_logs.html" },
        { title:'Log parcheggi', index: 3, content:"partials/aux/logs/parking_logs.html" }
    ];
    
    $scope.setIndex = function($index){
    	switch($index){
	    	case 0:
	    		sharedDataService.setInGlobalLogPage(true);
	    		sharedDataService.setInStreetLogPage(false);
	    		sharedDataService.setInParkLogPage(false);
	    		$scope.initGlobalLogs();
	    		break;
	    	case 1:
	    		sharedDataService.setInGlobalLogPage(false);
	    		sharedDataService.setInStreetLogPage(true);
	    		sharedDataService.setInParkLogPage(false);
	    		$scope.initStreetLogs();
	    		break;
	    	case 2:
	    		sharedDataService.setInGlobalLogPage(false);
	    		sharedDataService.setInStreetLogPage(false);
	    		sharedDataService.setInParkLogPage(true);
	    		$scope.initParkLogs();
	    		break;
    	}
       	$scope.tabIndex = $index;
    };
    
    $scope.authHeaders = {
        'Accept': 'application/json;charset=UTF-8'
    };
    
    $scope.logCounts = 0;
    $scope.logParkCounts = 0;
    $scope.logStreetCounts = 0;
    $scope.globalLogs = [];
    $scope.parkLogs = [];
    $scope.streetLogs = [];
    
    // For default
    $scope.initGlobalLogs = function(){
    	$scope.showDetails = false;
    	$scope.countAllLogsInDb();
    };
    
    $scope.countAllLogsInDb = function(){
		var elements = 0;
		var method = 'GET';
		var appId = sharedDataService.getConfAppId();
		var myDataPromise = invokeAuxWSService.getProxy(method, appId + "/log/count/all", null, $scope.authHeaders, null);
		myDataPromise.then(function(result){
		    console.log("log counted in db: " + JSON.stringify(result));
		    elements = parseInt(result);
		    $scope.logCounts = elements;
		    $scope.logCountsPage = Math.ceil($scope.logCounts / $scope.maxLogs);
		    if($scope.globalLogs.length != $scope.logCounts){
		    	$scope.globalLogs = [];
		    }
		    $scope.getAllLogsFromDb(0);
		});
	};
	
	$scope.getAllLogsFromDb = function(skip){
		if($scope.globalLogs.length < $scope.logCounts){
			var method = 'GET';
			var appId = sharedDataService.getConfAppId();
			var myDataPromise = invokeAuxWSService.getProxy(method, appId + "/log/all/" + skip, null, $scope.authHeaders, null);
			myDataPromise.then(function(result){
				//console.log("log finded in db: " + JSON.stringify(result));
				$scope.globalLogs = $scope.globalLogs.concat(result);
			    if(skip < $scope.logCounts && sharedDataService.isInGlobalLogPage()){
			    	skip += 100;
			    	$scope.getAllLogsFromDb(skip);
			    } else {
			    	console.log("All log finded: last skip = " + skip);
			    }
			});
		} else {
			console.log("global log already loaded.");
		}
	};
	
	// For parking
	$scope.initParkLogs = function(){
		$scope.showDetails = false;
    	$scope.countAllParkLogsInDb();
    };
	
	$scope.countAllParkLogsInDb = function(){
		var elements = 0;
		var method = 'GET';
		var appId = sharedDataService.getConfAppId();
		var myDataPromise = invokeAuxWSService.getProxy(method, appId + "/log/count/parking", null, $scope.authHeaders, null);
		myDataPromise.then(function(result){
		    console.log("park log counted in db: " + JSON.stringify(result));
		    elements = parseInt(result);
		    $scope.logParkCounts = elements;
		    $scope.logParkCountsPage = Math.ceil($scope.logParkCounts / $scope.maxLogs);
		    if($scope.parkLogs.length != $scope.logParkCounts){
		    	$scope.parkLogs = [];
		    }
		    $scope.getAllParkLogsFromDb(0);
		});
	};
	
	$scope.getAllParkLogsFromDb = function(skip){
		if($scope.parkLogs.length < $scope.logParkCounts){
			var method = 'GET';
			var appId = sharedDataService.getConfAppId();
			var myDataPromise = invokeAuxWSService.getProxy(method, appId + "/log/parkings/" + skip, null, $scope.authHeaders, null);
			myDataPromise.then(function(result){
				console.log("park log finded in db: " + JSON.stringify(result));
				$scope.parkLogs = $scope.parkLogs.concat(result);
			    if(skip < $scope.logParkCounts && sharedDataService.isInParkLogPage()){
			    	skip += 100;
			    	$scope.getAllParkLogsFromDb(skip);
			    } else {
			    	console.log("Park log finded: last skip = " + skip + " counts = " + $scope.logParkCounts + " isInParkLogPage = " + sharedDataService.isInParkLogPage());
			    }
			});
		} else {
			console.log("park log already loaded.");
		}
	};
	
	// For street
	$scope.initStreetLogs = function(){
		$scope.showDetails = false;
    	$scope.countAllStreetLogsInDb();
    };
	
	$scope.countAllStreetLogsInDb = function(){
		var elements = 0;
		var method = 'GET';
		var appId = sharedDataService.getConfAppId();
		var myDataPromise = invokeAuxWSService.getProxy(method, appId + "/log/count/street", null, $scope.authHeaders, null);
		myDataPromise.then(function(result){
		    console.log("street log counted in db: " + JSON.stringify(result));
		    elements = parseInt(result);
		    $scope.logStreetCounts = elements;
		    $scope.logStreetCountsPage = Math.ceil($scope.logStreetCounts / $scope.maxLogs);
		    if($scope.streetLogs.length != $scope.logStreetCounts){
		    	$scope.streetLogs = [];
		    }
		    $scope.getAllStreetLogsFromDb(0);
		});
	};
	
	$scope.getAllStreetLogsFromDb = function(skip){
		if($scope.streetLogs.length < $scope.logStreetCounts){
			var method = 'GET';
			var appId = sharedDataService.getConfAppId();
			var myDataPromise = invokeAuxWSService.getProxy(method, appId + "/log/streets/" + skip, null, $scope.authHeaders, null);
			myDataPromise.then(function(result){
				//console.log("street log finded in db: " + JSON.stringify(result));
				$scope.streetLogs = $scope.streetLogs.concat(result);
			    if(skip < $scope.logStreetCounts && sharedDataService.isInStreetLogPage()){
			    	skip += 100;
			    	$scope.getAllStreetLogsFromDb(skip);
			    } else {
			    	console.log("Street log finded: last skip = " + skip + " counts = " + $scope.logStreetCounts + " isInStreetLogPage = " + sharedDataService.isInStreetLogPage());
			    }
			});
		} else {
			console.log("street log already loaded.");
		}
	};
	
	$scope.viewDetails = function(log){
		$scope.showDetails = true;
		$scope.logDetails = log;
		$scope.json_log_value = JSON.stringify(log, undefined, 4);
	};
	
	$scope.closeDetails = function(){
		$scope.showDetails = false;
		$scope.logDetails = {};
	};
	
	$scope.viewFiltered = function(log){
		$scope.filterForLog = log.objId;
		$scope.showFiltered = true;
	};
	
	$scope.closeFiltered = function(){
		$scope.filterForLog = null;
		$scope.showFiltered = false;
	};
	
    
    
}]);    