var interfaceModule = angular.module("BlurAdmin.interface.interfaceService",[]);

interfaceModule.factory('interfaceService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var interfaceData = new Object();
    //保存接口
    interfaceData.save = function (interface) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/save',
            timeout:Constant.timeout,
            params:{
                interfaceCode:interface.interfaceCode,
                interfaceName:interface.interfaceName,
                interfaceURL:interface.interfaceURL,
                manualURL:interface.manualURL,
                testURL:interface.testURL,
                status:interface.status,
                description:interface.description
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

    interfaceData.getInterfaceList=function (params) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/findByPage',
            timeout:Constant.timeout,
            params:{
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                status:params.status,
                interfaceCode:params.interfaceCode,
                interfaceName:params.interfaceName
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

    interfaceData.deleteInterface=function (interfaceId) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/delete/'+interfaceId,
            timeout:Constant.timeout
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

    interfaceData.saveEditInterface=function (interface) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/update',
            timeout:Constant.timeout,
            params:{
                interfaceId:interface.interfaceId,
                interfaceCode:interface.interfaceCode,
                interfaceName:interface.interfaceName,
                interfaceURL:interface.interfaceURL,
                manualURL:interface.manualURL,
                status:interface.status,
                testURL:interface.testURL,
                description:interface.description
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

    interfaceData.debugUrl=function (debugUrl) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/debugUrl',
            timeout:Constant.timeout,
            params:{
                debugUrl:debugUrl
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
    interfaceData.debugInterfaceUrl=function (testUrl) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/debugInterfaceUrl',
            timeout:Constant.timeout,
            params:{
                testUrl:testUrl
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

    interfaceData.checkInterfaceCode=function (interfaceCode) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/interface/checkInterfaceCode/'+interfaceCode,
            timeout:Constant.timeout
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
    return interfaceData;

});