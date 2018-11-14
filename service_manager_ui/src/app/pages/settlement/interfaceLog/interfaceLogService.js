/**
 * Created by z on 2017/7/17.
 */
var dimensionModule = angular.module("BlurAdmin.interfaceLog.interfaceLogService",[]);

dimensionModule.factory('interfaceLogService',function ($http,$rootScope,$compile,$q,Constant,commonService) {

    var interfaceLogServiceData = {};

    interfaceLogServiceData.interfaceList = function (params) {
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/interface/findAllValid",
            timeout: Constant.timeout
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

    interfaceLogServiceData.interfaceLogList =  function(params){
        var deferred = $q.defer();
        $http({
            method: "post",
            url: Constant.prefixUrl + "/interfaceLog/find",
            timeout: Constant.timeout,
            params:{
                companyName:params.companyName,
                ettType:params.ettType,
                interfaceCode:params.interfaceCode,
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                beginDate:params.beginDate,
                endDate:params.endDate
            }
        }).success(function (data, status, headers, config) {
                deferred.resolve(data);
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }
    return interfaceLogServiceData;
});