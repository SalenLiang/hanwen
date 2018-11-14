/**
 * Created by z on 2017/7/17.
 */
var limitwordModule = angular.module("BlurAdmin.limitword.limitwordService",[]);

limitwordModule.factory('limitwordService',function ($http,$rootScope,$compile,$q,Constant,commonService) {

    var limitwordServiceData = {};

    limitwordServiceData.saveLimitword = function (params) {
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/limitword/save",
            timeout: Constant.timeout,
            params:{
                word:params.word,
                wordType:params.wordType,
                status:params.status,
                description:params.description
            }
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
    }
    limitwordServiceData.limitwordList = function (params) {
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/limitword/find",
            timeout: Constant.timeout,
            params:{
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                wordType:params.wordType,
                word:params.word
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
    }
    limitwordServiceData.saveEditLimitword = function (params) {
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/limitword/update",
            timeout: Constant.timeout,
            params:{
                word:params.word,
                wordType:params.form.$data.wordType,
                status:params.form.$data.status,
                limitwordId:params.limitwordId,
                description:params.description
            }
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
    }
    limitwordServiceData.deleteLimitword = function (params) {
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/limitword/delete",
            timeout: Constant.timeout,
            params:{
                limitwordId:params.limitwordId,
                word:params.word,
                wordType:params.wordType,
                status:params.status,
                description:params.description
            }
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
    }
    limitwordServiceData.checkLimitword = function (params) {
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/limitword/checkLimitword",
            timeout: Constant.timeout,
            params:{
              word:params
            }
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
    }
    return limitwordServiceData;
});