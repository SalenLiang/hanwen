var dimensionModule = angular.module("BlurAdmin.dimension.dimensionService",[]);

dimensionModule.factory('dimensionService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var dimensionData = new Object();


    dimensionData.getDimensionList=function (params) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/dimension/find',
            timeout:Constant.timeout,
            params:{
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                dimensionCode:params.dimensionCode,
                dimensionName:params.dimensionName,
                status:params.status
            }
        }).success(function (data, status, headers, config) {
            if(data != null&&data!=''){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function(data, status, headers, config){
            commonService.goLoginPage(status);
        });
        return deferred.promise;    };

    dimensionData.saveDimensionData=function (dimension,fieldList) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/dimension/save',
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
                dimension:JSON.stringify(dimension),
                fieldList: JSON.stringify(fieldList)
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

    dimensionData.saveEditDimensionData=function (dimension,fieldList) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/dimension/update',
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
                dimension:JSON.stringify(dimension),
                fieldList: JSON.stringify(fieldList)
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

    dimensionData.deleteDimension=function (dimension) {
        var deferred = $q.defer();
        $http({
            method:'post',
            timeout:Constant.timeout,
            url:Constant.prefixUrl+'/dimension/delete',
            params: {
                dimensionId: dimension.dimensionId,
                dimensionCode: dimension.dimensionCode,
                domainId : dimension.domainId,
                dimensionName : dimension.dimensionName,
                status : dimension.status,
                description : dimension.description
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

    dimensionData.findDimensionById=function (dimensionId) {
        var deferred = $q.defer();
        $http({
            method:'post',
            timeout:Constant.timeout,
            url:Constant.prefixUrl+'/dimension/findDimensionById/'+dimensionId
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

    dimensionData.checkDimensionId=function (dimensionId) {
        var deferred = $q.defer();
        $http({
            method:'post',
            timeout:Constant.timeout,
            url:Constant.prefixUrl+'/dimension/checkDimensionById/'+dimensionId
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
    return dimensionData;

});