angular.module('BlurAdmin.login.loginCtrl',['BlurAdmin.login.loginService'])
						.controller('loginCtrl', loginCtrl)
						.controller('resetPasswordCtrl', resetPasswordCtrl)
						.config(routeConfig);
  function routeConfig($stateProvider, $urlRouterProvider) {
    $stateProvider.state('dashboard', {
          url: '/dashboard',
          templateUrl: 'index.html',
          title: 'Basic Tables',
          sidebarMeta: {
            order: 0
          }
       });
  }						

/*controller*/
function loginCtrl($scope,$uibModal,$rootScope, loginService ,toastr) {
    //登陆
	$scope.login = function(){
        var userName = $scope.userName;
        var password = $scope.password;
        if(userName == "" || password == ""){
            showWarnMsg(toastr,"请输入账号名或密码");
            return;
        }
        //获取验证码的参数
        var token = $('#token').val();
        var checkAddress = $('#checkAddress').val();
        var sid = $('#sid').val();
        var promise = loginService.login(userName, password,token,checkAddress,sid);
        promise.then(function (data) {
            showSuccMsg(toastr,"登陆系统成功");
            setTimeout(function(){
                window.location.href = "/dashboard";
            },500)
        },function (data) {
            showErrorMsg(toastr,data.errorMsg);
        });
	};

    //重置密码
    $scope.resetPwd = function(){
        $uibModal.open({
            animation: true,
            size: 'md',
            templateUrl: 'resetPassword.html',
            controller:function(){
                $rootScope.checkUserName = null;
                $rootScope.checkUserEmail = null;
            }
        });
    }
}

function resetPasswordCtrl($scope,$uibModal, loginService ,toastr){
    $scope.resetPassword = function(){
        //点击确定按钮之后，不可用按钮10s
        $('#rpBtn').attr("disabled",true);
        window.setTimeout(function(){
            $('#rpBtn').removeAttr("disabled");
        },10000)
        var params = new Object();
        params.checkUserName = $scope.checkUserName;
        params.checkUserEmail = $scope.checkUserEmail;
        var promise = loginService.resetPassword(params);
        promise.then(function (data) {
            if(data.result == '0'){
                showWarnMsg(toastr,data.errorMsg);
            }else{
                showSuccMsg(toastr,data.errorMsg);
            }
            $scope.$dismiss();
        },function (data) {
            showErrorMsg(toastr,data.errorMsg);
        });
    }
}
