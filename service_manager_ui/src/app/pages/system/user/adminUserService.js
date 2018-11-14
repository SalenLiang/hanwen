var adminUserModule = angular.module("BlurAdmin.adminUser.adminUserService",[]);

adminUserModule.factory('adminUserService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var userData = new Object();

    //修改密码
    userData.updateAdminUserPassword = function (oldPassword,newPassword) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/updatePassword',
            timeout:Constant.timeout,
            params:{
                oldPassword:oldPassword,
                newPassword:newPassword
            }
        }).success(function (data, status, headers, config) {
            deferred.resolve(data);
        }).error(function(data, status, headers, config){
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };
    //分页获取系统管理用户列表
    userData.getUserList = function (params) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/find',
            timeout:Constant.timeout,
            params:{
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                name:params.name,
                status:params.status
            }
        }).success(function (data, status, headers, config) {
            deferred.resolve(data);
        }).error(function(data, status, headers, config){
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    //删除系统用户
    userData.deleteAdminUserById=function (user) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/delete',
            timeout:Constant.timeout,
            params:{
                adminUserStr:JSON.stringify(user)
            }
        }).success(function (data, status, headers, config) {
            if(data.errorCode == '0'){     //删除系统用户成功
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data,status,headers,config) {
           commonService.goLoginPage(status);
        });
        return deferred.promise;
    };

    userData.findByAdminId=function (adminUserId) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/findByAdminId/'+adminUserId,
            timeout:Constant.timeout
        }).success(function (data, status, headers, config) {
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

    userData.saveEditUser=function (adminUser,regionCode,roleIds) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/saveEditUser',
            timeout:Constant.timeout,
            params:{
                adminUser:JSON.stringify(adminUser),
                regionCode:regionCode,
                roleIds:roleIds
            }
        }).success(function (data, status, headers, config) {
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

    userData.saveUser=function (adminUser,regionCode,roleIds) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/saveUser',
            timeout:Constant.timeout,
            params:{
                adminUser:JSON.stringify(adminUser),
                regionCode:regionCode,
                roleIds:roleIds
            }
        }).success(function (data, status, headers, config) {
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
    //激活用户
    userData.activationUser=function (adminUser) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/activationUser',
            timeout:Constant.timeout,
            params:{
                adminUserStr:JSON.stringify(adminUser)
            }
        }).success(function (data, status, headers, config) {
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

    //校验姓名的唯一性
    userData.checkAdminUser=function (adminUser,checkType) {
        var deferred = $q.defer();
        $http({
            method:'post',
            url:Constant.prefixUrl+'/adminUser/checkAdminUserName',
            timeout:Constant.timeout,
            params:{
                name:adminUser.name,
                email:adminUser.email,
                mobile:adminUser.mobile,
                adminUserId:adminUser.adminUserId,
                checkType:checkType
            }
        }).success(function (data, status, headers, config) {
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
    //获取所有的用户
    userData.getAdminUserList = function () {
        var deferred = $q.defer();
        $http({
            method:'post',
            timeout:Constant.timeout,
            url:Constant.prefixUrl+'/adminUser/findAdminUser'
        }).success(function (data,status,headers,config) {
            deferred.resolve(data);
        }).error(function (data,status,headers,config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    };
    return userData;
});