/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
/**
 * @author v.lugovsky
 * created on 16.12.2015
 */
(function () {
  'use strict';
  angular.module('BlurAdmin.theme.components', [])
      .controller("componentsCtrl",function ($scope,$rootScope,$timeout,toastr,$uibModal,$state,$http,Constant,commonService) {

        $scope.loginOut=function ($window) {

            $rootScope.loading = true;
            $http({
                method:'post',
                url:Constant.prefixUrl+'/login/logout'
            }).success(function (data, status, headers, config) {
                if(data.errorCode == '0'){
                    toastr.success("退出系统成功");
                    window.location.href = "login.html";
                }else{
                    toastr.warning("退出系统失败");
                }
                $rootScope.loading = false;
            }).error(function (data, status, headers, config) {
                $rootScope.loading = false;
                toastr.error("服务器出现异常，请联系管理员");
                window.location.href = "login.html";
            })
        };

          $scope.checkUser =  function(){
                  $http({
                      method : "post",
                      url:Constant.prefixUrl + '/login/checkLogin',
                      timeout : Constant.timeout
                  }).success(function(data, status, headers, config) {
                      if (data.errorCode == '0') {
                          $rootScope.currentUserName = data.currentUser.realName;
                          $rootScope.currentAdminUserId = data.currentUser.adminUserId;
                      } else {
                          window.location.href = '/';
                      }
                  }).error(function(data, status, headers, config) {
                      commonService.goLogin(status);
                  });
          };
          $scope.checkUser();

          //修改密码
          $scope.settingPSW = function(){
              $uibModal.open({
                  animation: true,
                  size: 'md',
                  templateUrl: 'app/pages/system/user/settingPassword.html',
                  controller:function(){
                      $rootScope.checkUserName = null;
                      $rootScope.checkUserEmail = null;
                      $rootScope.newPSW = null;
                  }
              });
          }
  });

})();
