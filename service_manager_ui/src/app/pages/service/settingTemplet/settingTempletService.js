var settingTempletModule = angular.module("BlurAdmin.settingTemplet.settingTempletService",[]);

settingTempletModule.factory('settingTempletService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var settingTempletData = new Object();

    settingTempletData.saveEditTemplet = function(settingTemplet,domainDimensionLists){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+"/settingTemplet/editSave",
            timeout:Constant.timeout,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function(obj) {
                var str = [];
                for(var p in obj){
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            },
            data: {
                settingTempletStr : JSON.stringify(settingTemplet),
                addDomainDimensionList : JSON.stringify(domainDimensionLists)
            }
        }).success(function (data,status,headers,config) {
            if (data.errorCode == '0') {
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }
    settingTempletData.saveTemplet = function(settingTemplet,domainDimensionLists){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+"/settingTemplet/save",
            timeout:Constant.timeout,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function(obj) {
                var str = [];
                for(var p in obj){
                    str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                }
                return str.join("&");
            },
            data: {
                settingTempletStr : JSON.stringify(settingTemplet),
                addDomainDimensionList : JSON.stringify(domainDimensionLists)
            }
        }).success(function (data,status,headers,config) {
            if (data.errorCode == '0') {
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }
    settingTempletData.getSettingTempletDetail = function(params){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/settingTemplet/getSettingTempletDetail',
            params:{
                templetId:params
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
    }
    settingTempletData.getSettingTempletList = function(params){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/settingTemplet/find',
            params:{
                status:params.status,
                templetName:params.templetName,
                currentPage:params.currentPage,
                pageSize:params.pageSize
            }
        }).success(function (data, status, headers, config) {
            deferred.resolve(data);
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }
    settingTempletData.editCheckTemplet = function(settingTemplet,checkType){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/settingTemplet/editCheckTemplet',
            params:{
                templetName:settingTemplet.templetName,
                templetId:settingTemplet.templetId,
                checkType:checkType
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
    }
    settingTempletData.checkTemplet = function(settingTemplet,checkType){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/settingTemplet/checkTemplet',
            params:{
                templetName:settingTemplet.templetName,
                checkType:checkType
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
    }
    settingTempletData.deleteSettingTempletById = function(settingTemplet){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/settingTemplet/deleteSettingTempletById',
            params:{
                templetId:settingTemplet.templetId
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
    }

    return settingTempletData;
});