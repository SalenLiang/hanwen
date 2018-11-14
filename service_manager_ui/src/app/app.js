'use strict';

var app =angular.module('BlurAdmin', [
  'ngAnimate',
  'ui.bootstrap',
  'ui.sortable',
  'ui.router',
  'ngTouch',
  'toastr',
  'smart-table',
  "xeditable",
  'ui.slimscroll',
  'ngJsTree',
  'angular-progress-button-styles',
  'app.commonDirective', //公共的自定义指令
  'app.commonService', //公用方法
  'BlurAdmin.theme',
  'BlurAdmin.pages'
]);

var permissionList;

app.run(function(permissions) {

    permissions.setPermissions(permissionList);

});
angular.module('BlurAdmin').factory('permissions', function () {
    return {
        setPermissions: function(permissions) {
            permissionList = permissions;
        },
        hasPermission: function (permission) {
            var flag = false;
            //var permissionListObj = JSON.parse(permissionList);
            for(var i=0;i<permissionList.length;i++){
                if(permissionList[i] == permission){
                    flag = true;
                    break;
                }
            }
            return flag;

        }
    };
});
// 操作DOM一开始就加载
//angular.element(document).ready(function() {

   /* $.post('http://localhost:8001/serviceManager/login/checkLogin', function(data) {
        if(data.errorCode == "1"){
            window.location.href = '/';
        };
    });*/

    //查找左侧侧边栏
  /*  $.post('http://localhost:8001/serviceManager/menu/findMenuList', function(data) {
        if(data.errorCode == '0'){
            permissionList = data.dataList;
            angular.bootstrap(document,['BlurAdmin']);
        }else{
            console.log(data);
        }
    });

});*/
// 获取权限动态启动 ng-app
angular.element(document).ready(function() {
    $.post('http://localhost:3002/serviceManager/login/checkLogin', function(data) {
        if(data.errorCode == "1"){
            window.location.href = '/';
        };
    });
    $.post('http://localhost:3002/serviceManager/menu/findMenuList', function(data) {
        permissionList = data.dataList;
        angular.bootstrap(document.getElementById("adminHtml"),['BlurAdmin']);
    });
});

var appCommonModule = angular.module('app.appCommonDirective', []);
appCommonModule.directive('hasPermission', function(permissions) {
    return {
        link : function(scope, element, attrs) {
            var value = attrs.hasPermission.trim();
            function toggleVisibilityBasedOnPermission() {
                var hasPermission = permissions.hasPermission(value);
                if (hasPermission){
                    element.show();
                } else {
                    element.hide();
                }
            }
            toggleVisibilityBasedOnPermission();
        },
        controller: ["$scope", function($scope) {
            /**处理相关逻辑*/
        }]
    };
});



