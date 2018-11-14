(function () {
    'user strict';

    angular.module("BlurAdmin.pages.system",
        [
            'BlurAdmin.region.regionService',
            'BlurAdmin.role.roleService',
            'BlurAdmin.adminUser.adminUserService'
        ])
        .config(routeConfig);

    function routeConfig($stateProvider) {
        $stateProvider.state("system",{
            url:'/system',
            template:"<ui-view autoscroll='true' autoscroll-body-top></ui-view>",
            abstract:true,
            title:'系统管理',
            controller:'systemManagerCtrl',
            sidebarMeta:{
                icon:"ion-settings",
                order:50
            }
        }).state("system.user",{
            url:'/systemUser',
            templateUrl:'app/pages/system/user/systemUser.html',
            title:"用户",
            sidebarMeta:{
                order:0
            }
        })/*.state("system.department",{
            url:'/systemDepartment',
            templateUrl:'app/pages/system/user/systemDepartment.html',
            title:"部门",
            sidebarMeta:{
                order:20
            }
        })*/.state("system.region",{
            url:'/systemRegion',
            templateUrl:'app/pages/system/region/systemRegion.html',
            title:"区域",
            sidebarMeta:{
                order:10
            }
        }).state("system.userAddPage",{
            url:'/systemNewUser',
            templateUrl:'app/pages/system/user/newSystemUser.html',
            title:"新增用户"
        }).state("system.userEditPage",{
            url:'/editSystemUser',
            templateUrl:'app/pages/system/user/editSystemUser.html',
            title:"编辑用户"
        });/*.state("system.role",{
            url:'/systemRole',
            templateUrl:'app/pages/system/role/systemRole.html',
            title:"角色",
            sidebarMeta:{
                order:10
            }
        }).state("system.newRole",{
            url:'/systemNewRole',
            templateUrl:'app/pages/system/role/newRole.html',
            title:"新增角色"
        });*/
        /*.state("system.authority",{
            url:'/systemAuthority',
            templateUrl:'/app/pages/system/authority/systemAuthority.html',
            title:"权限",
            sidebarMeta:{
                order:20
            }
        })*/
    }
})();