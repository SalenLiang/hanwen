var domainModule = angular.module("BlurAdmin.domain.domainService",[]);

domainModule.factory('domainService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var domainData = new Object();
    //保存领域
    domainData.save = function (domain) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/domain/save',
            timeout:Constant.timeout,
            params:{
                domainCode:domain.domainCode,
                domainName:domain.domainName,
                status:domain.status,
                description:domain.description
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
			commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    domainData.domainPageList=function (params) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/domain/findDomainByPage',
            timeout:Constant.timeout,
            params:{
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                status:params.status,
                domainCode:params.domainCode,
                domainName:params.domainName
            }
        }).success(function (data, status, headers, config) {
            if(data != null&&data!=''){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
			commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    domainData.deleteDomain=function (domain) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/domain/delete',
            timeout:Constant.timeout,
            params:{
                domainId:domain.domainId,
                domainCode:domain.domainCode,
                domainName:domain.domainName,
                status:domain.status,
                description:domain.description
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
			commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    domainData.saveEditDomain=function (domain) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/domain/update',
            timeout:Constant.timeout,
            params:{
                domainId:domain.domainId,
                domainCode:domain.domainCode,
                domainName:domain.domainName,
                status:domain.status,
                description:domain.description
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
			commonService.goLoginPage(status);
        });
        return deferred.promise;
    };
    domainData.checkDomainCode=function (domainCode) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/domain/checkDomainCode/'+domainCode,
            timeout:Constant.timeout
        }).success(function (data, status, headers, config) {

            if (data.errorCode == '0') {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
			commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    domainData.findAll=function(){
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/domain/findAll',
            timeout:Constant.timeout
        }).success(function (data, status, headers, config) {
            if (data.errorCode == '0') {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
			commonService.goLoginPage(status);
        });
        return deferred.promise;
    };


    return domainData;

});