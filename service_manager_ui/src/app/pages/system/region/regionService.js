var regionServiceModule = angular.module("BlurAdmin.region.regionService",[]);
regionServiceModule.factory("regionService",function ($http,$rootScope,$compile,$q,Constant,commonService) {
    var regionServiceData = new Object();
    
    // 查询区域列表
    regionServiceData.regionList = function () {
        var deferred = $q.defer();
        $http({
            method : "post",
            url: Constant.prefixUrl+'/region/find',
            timeout:Constant.timeout
        }).success(function(data, status, headers, config) {
            deferred.resolve(data);
        }).error(function(data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };
    
    //保存区域
    regionServiceData.saveRegion = function (region) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/region/save',
            timeout : Constant.timeout,
            params:{
                regionCode:region.regionCode,
                regionName:region.regionName,
                adminUserId:region.adminUserId
            }
        }).success(function (data,status,headers,config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    //保存编辑后的区域
    // 编辑领域
    regionServiceData.saveEditRegion = function(region) {
        var deferred = $q.defer();
        $http({
            method : "post",
            url: Constant.prefixUrl+'/region/update',
            timeout : Constant.timeout,
            params : {
                regionCode:region.regionCode,
                regionName:region.regionName,
                adminUserId:region.adminUserId,
                sortord:region.sortord,
                status:region.status
            }
        }).success(function(data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function(data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    //通过区域ID删除区域
    regionServiceData.deleteRegion = function (region) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/region/delete',
            timeout : Constant.timeout,
            params:{
                regionCode : region.regionCode,
                regionName : region.regionName,
                adminUserId : region.adminUserId,
                sortord : region.sortord,
                status : region.status
            }
        }).success(function (data,status,headers,config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };
    //查找管理用户，用户负责区域
    regionServiceData.getUnChargeAdminUserList = function () {
        var deferred = $q.defer();
        $http({
            method:'post',
            timeout:Constant.timeout,
            url:Constant.prefixUrl+'/adminUser/findUnChargeAdminUser'
        }).success(function (data,status,headers,config) {
            deferred.resolve(data);
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    regionServiceData.checkRegionCode=function (regionCode) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+"/region/checkRegionCode/"+regionCode,
            timeout:Constant.timeout
        }).success(function (data,status,headers,config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    
    return regionServiceData;
});