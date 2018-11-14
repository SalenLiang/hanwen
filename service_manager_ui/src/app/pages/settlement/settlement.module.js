(function () {
    'user strict';

    angular.module("BlurAdmin.pages.settlement",
        [
            'BlurAdmin.interfaceLog.interfaceLogService'
        ])
        .config(routeConfig);
    function routeConfig($stateProvider) {
        $stateProvider.state("settlement",{
            url:"/settlement",
            template:'<ui-view  autoscroll="true" autoscroll-body-top></ui-view>',
            abstract:true,
            controller: 'settlementCtrl',
            title:'结算管理',
            sidebarMeta:{
                icon:'ion-calculator',
                order:20
            }
        })

        /*.state("settlement.report",{
         url:'/settlementReport',
         templateUrl:'/app/pages/settlement/report/settlementReport.html',
         title:'报表',
         sidebarMeta:{
         order:0
         }
         })*/
    }
})();