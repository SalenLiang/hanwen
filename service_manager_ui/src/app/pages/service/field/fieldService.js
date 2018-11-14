var fiedlModule = angular.module("BlurAdmin.field.fieldService",[]);

fiedlModule.factory('fieldService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var fieldData = new Object();

    fieldData.saveEditField=function (field) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+"/field/update",
            timeout:Constant.timeout,
            params:{
                fieldId:field.fieldId,
                fieldCode:field.fieldCode,
                fieldName:field.fieldName,
                defaultListShowYN:field.defaultListShowYN,
                defaultDetailShowYN:field.defaultDetailShowYN,
                searchYN:field.searchYN,
                status:field.status,
                description:field.description
            }
        }).success(function (data, status, headers, config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    fieldData.deleteField=function (fieldId,fieldCode) {
        var deferred = $q.defer();

        $http({
            method:'post',
            url:Constant.prefixUrl+"/field/delete/"+fieldId,
            timeout:Constant.timeout
        }).success(function (data, status, headers, config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    fieldData.findFieldListByDimensionId=function (dimensionId) {
        var deferred = $q.defer();

        $http({
            method:'post',
            url:Constant.prefixUrl+"/field/findFieldListByDimensionId/"+dimensionId,
            timeout:Constant.timeout
        }).success(function (data, status, headers, config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    return fieldData;

});