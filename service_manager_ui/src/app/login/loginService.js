var loginServiceModule = angular.module('BlurAdmin.login.loginService',[]);

loginServiceModule.factory('loginService', function($http,$rootScope, $compile, $q){
	var loginServiceData = new Object();



	loginServiceData.login = function(loginName, password,token,checkAddress,sid) {
		var deferred = $q.defer();
		$http({
			method : "post",
			//url: 'http://sr.fahaicc.com/serviceManager/login',//'http://sr.fahaicc.com/serviceManager/login',
			url: 'http://localhost:8001/serviceManager/login',

			timeout : 10000,
			params : {
				userName : loginName,
				password : password,
				token : token,
				checkAddress : checkAddress,
				sid : sid
			}
		}).success(function(data, status, headers, config) {
			if (data.errorCode == 0) {
				deferred.resolve(data);
			} else {
				deferred.reject(data);
			}
		}).error(function(data, status, headers, config) {
			errInfo = {
					"data" : data,
					"status" : status,
					"headers" : headers,
					"config" : config
				};
				errInfo.message = "这是一个404错误";
		});
		return deferred.promise;
	};
	loginServiceData.resetPassword = function(params) {
		var deferred = $q.defer();
		$http({
			method : "post",
			url: 'serviceManager/login/resetPassword',
			timeout : 10000,
			params : {
				userName : params.checkUserName,
				userEmail : params.checkUserEmail
			}
		}).success(function(data, status, headers, config) {
			console.log(data);
			if (data.errorCode == 0) {
				deferred.resolve(data);
			} else {
				deferred.reject(data);
			}
		}).error(function(data, status, headers, config) {
			errInfo = {
					"data" : data,
					"status" : status,
					"headers" : headers,
					"config" : config
				};
				errInfo.message = "这是一个404错误";
		});
		return deferred.promise;
	};


	return loginServiceData;
});
