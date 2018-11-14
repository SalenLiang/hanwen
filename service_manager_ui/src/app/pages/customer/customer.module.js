(function () {
    'use strict';
    //声明AngularJS模块, 并把BlurAdmin.pages.customer.cumstomerService
    // 传入AngularJS主模块，所有的结合起来我们就得到了Angular模块。
    //告诉HTML页面这是一个AngularJS作用的页面，它的内容由AngularJS引擎来解释。
    angular.module("BlurAdmin.pages.customer",[
        'BlurAdmin.pages.customer.customerService',
        'BlurAdmin.pages.customer.customerQueryCountService',
        'ui.select'
    ]).config(routeConfig);

    // 声明了把 $stateProvider 和 $urlRouteProvider 路由引擎作为函数参数传入，这样我们就可以为这个应用程序配置路由了.
    //config(routeConfig)
    /** @ngInject */
    function routeConfig($stateProvider) {
        $stateProvider.state('customer',{
                url:"/customer",
                template:'<ui-view  autoscroll="true" autoscroll-body-top></ui-view>',
                abstract:true,
                controller: 'customerManagerCtrl',
                title:'客户管理',
                sidebarMeta:{
                    icon:'ion-person',
                    order:10
                }
            }).state('customer.manager',{
                url:'/customerManager',
                templateUrl:'app/pages/customer/manager/customerManager.html',
                title:'客户',
                sidebarMeta:{
                    order:0
                }

    }).state('customer.report',{
                url:'/customerReport',
                templateUrl:'app/pages/customer/report/customerReportForm.html',
                title:"接口报表",
                sidebarMeta:{
                    order:10
                }
            }).state('customer.managerAddPage',{
                url:'/customerManagerAddPage',
                title:'新增客户',
                templateUrl:'app/pages/customer/manager/addCustomer.html'
            }).state('customer.managerEdit',{
                url:'/managerEdit',
                title:'编辑客户',
                templateUrl:'app/pages/customer/manager/editCustomer.html'
            }).state('customer.managerReport',{
                url:'/managerReport',
                title:'客户报表',
                templateUrl:'app/pages/customer/manager/customerReport.html'
            }).state('customer.userSon',{
                url:'/userSon',
                title:'子用户管理',
                params:{params:null},
                templateUrl:'app/pages/customer/manager/userSon/userSonInfo.html'
             }).state('customer.updateUser',{
                url:'/updateUser',
                title:'子用户修改',
                controller: 'userSonUpdateCtrl',
                params:{params:null},
                templateUrl:'app/pages/customer/manager/userSon/updateUser.html'
             });
    }
})();