(function () {
    'use strict';
    angular.module("BlurAdmin.pages.customerService",[]).config(routeConfig);

    function routeConfig($stateProvider) {
        $stateProvider.state("keFu",{
                url:"/keFuService",
                template:'<ui-view  autoscroll="true" autoscroll-body-top></ui-view>',
                abstract:true,
                title:'客服管理',
                sidebarMeta:{
                    icon:'ion-social-whatsapp',
                    order:30
                }
        });
    }
})();
