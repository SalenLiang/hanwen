var roleModule = angular.module("BlurAdmin.role.roleService",[]);
roleModule.factory('roleService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var roleData = {};
    //获取角色列表
    roleData.getRoleList = function () {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/role/find',
            timeout:Constant.timeout
        }).success(function (data, status, headers, config) {
            if(data.errorCode == '0'){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            };
        }).error(function(data, status, headers, config){
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };
    return roleData;
});