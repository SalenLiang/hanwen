(function () {
    'use strict';
    angular.module("BlurAdmin.pages.system")
        .controller("systemManagerCtrl",systemManagerCtrl)
        .controller("adminUserCtrl",adminUserCtrl)
        .controller("addSystemUserController",addSystemUserController)
        .controller("editSystemUserController",editSystemUserController)
        .controller("regionController",regionController)
        .controller("regionAddCtrl",regionAddCtrl)
        .controller("settingPasswordCtrl",settingPasswordCtrl)
        .controller("regionEditCtrl",regionEditCtrl)
        .controller("roleManagerCtrl",roleManagerCtrl);


    function settingPasswordCtrl($rootScope,$scope,$state,$timeout,$http,toastr,adminUserService,commonService,Constant) {
        $scope.oldPassword = '';
        $scope.newPassword = '';
        $scope.updatePassword = function(){
            $scope.$dismiss();
            var promise = adminUserService.updateAdminUserPassword($scope.oldPassword,$scope.newPassword);
            promise.then(function (data) {
                if(data.result == '0'){
                    showWarnMsg(toastr,data.errorMsg);
                }else{
                    showSuccMsg(toastr,data.errorMsg);//密码修改成功
                    //登出
                    $timeout(function(){
                        $http({
                            method:'post',
                            url:Constant.prefixUrl+'/login/logout'
                        }).success(function (data, status, headers, config) {
                            window.location.href = "login.html";
                        }).error(function (data, status, headers, config) {
                            window.location.href = "login.html";
                        })
                    },700);
                }
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        }
    }
    function editSystemUserController($rootScope,$scope,$state) {
        if($scope.adminUserId==null){
            $state.go('system.user');
        }
    }
    function addSystemUserController($rootScope,$scope,$state,regionService,roleService,commonService,adminUserService,toastr) {
        //获取区域列表
        regionService.regionList().then(function (data) {
            $scope.regions = data;
        },function (data) {
            commonService.goLoginPage(data.errorCode);
        });

        //获取角色列表
        roleService.getRoleList().then(function (data) {
            if(data.errorCode == '0'){
                $scope.roles = data.dataList;
            }
        },function () {

        });

        //校验用户信息的唯一性
        $scope.checkAdminUser=function (checkType,event) {
            var adminUser = $scope.adminUser;
            if(adminUser.name == ''||adminUser.email==''|| adminUser.mobile==''){
                return;
            }
            adminUserService.checkAdminUser(adminUser,checkType).then(function(data){
                if(data.errorCode == '0'){
                    if(data.result == '0'){
                        if("name" == checkType){
                            $scope.checkAdminUserNameResult=false;
                        }else if("email" == checkType){
                            $scope.checkAdminUserEmailResult=false;
                        }else if("mobile" == checkType){
                            $scope.checkAdminUserMobileResult=false;
                        }

                    }else{
                        if("name" == checkType){
                            $scope.checkAdminUserNameResult=true;
                        }else if("email" == checkType){
                            $scope.checkAdminUserEmailResult=true;
                        }else if("mobile" == checkType){
                            $scope.checkAdminUserMobileResult=true;
                        }

                        $(event.target).css("border-color","#ed7878");
                        $(event.target).parent().addClass("has-error");
                        $(event.target).parent().removeClass("has-success");
                        $(event.target).next().removeClass("ion-checkmark-circled");
                        $(event.target).next().addClass("ion-android-cancel");
                    }
                }else{
                    $rootScope.loading = false;
                }
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })
        };
        //保存用户
        $scope.saveUser = function (form) {
            $rootScope.loading = true;
            var user=$scope.adminUser;
            var regionCode=null;

            var roleIdArray = [];
            $("input[name='roleId']:checked").each(function () {
                roleIdArray.push($(this).val())
            });
            if(roleIdArray.length == 0){
                showWarnMsg(toastr,"请选择账户所对应的角色");
                $rootScope.loading = false;
                return;
            }
            if(user.regionYN == null){
                user.regionYN = 1;
            }else if(user.regionYN){
                user.regionYN = 0;
            }else{
                user.regionYN = 1;
            }
            if(user.regionYN == 0){
                var checked = [];
                $("input[name='areaCode']:checked").each(function () {
                    checked.push($(this).val())
                });
                if(checked.length == 0){
                    showWarnMsg(toastr,"请选择区域");
                    $rootScope.loading = false;
                    return;
                }
                regionCode = checked.join(",");
            }


            adminUserService.saveUser(user,regionCode,roleIdArray.join(",")).then(function (data) {
                showSuccMsg(toastr,"添加系统用户成功");
                $state.go('system.user');
            },function (data) {
                if(data.errorCode == '2'){
                    toastr.warning(data.errorMsg, 'Warning');
                }else{
                    //commonService.goLoginPage(data.errorCode);
                }
            });
        };
    }
    //系统管理用户列表
    function adminUserCtrl($rootScope,$scope) {
        //初始化参数
        var params = new Object();
        params.currentPage=1;
        params.pageSize=15;
        params.name=null;
        params.status=0;//默认正常的

        $rootScope.$emit("adminUserList",params);
    }
    //区域列表
    function regionController($rootScope) {
        $rootScope.$emit("regionList",{});
        $rootScope.$emit("getAdminUserList",{});
        $rootScope.$emit("getUnChargeAdminUserList",{});
    }
    function regionEditCtrl($rootScope,$scope,regionService,toastr,commonService){
        //保存编辑区域
        $scope.updateRegion = function(){
            $rootScope.loading = true;
            var promise = regionService.saveEditRegion($rootScope.editRegion);
            promise.then(function() {
                showSuccMsg(toastr,"修改区域成功");
                $scope.$dismiss();
                $rootScope.loading = false;
                $rootScope.$emit("regionList", {});
            }, function(data) {
                //commonService.goLoginPage(data.errorCode);
                $rootScope.loading = false;
            });
        }
    }
    //保存区域
    function regionAddCtrl($rootScope,$scope,regionService,toastr,commonService) {

        //校验regionCode唯一性
        $scope.checkRegionCode=function () {
            var regionCode = $("#regionCode").val();
            if(regionCode.length == 4){
                var promise = regionService.checkRegionCode(regionCode);
                promise.then(function (data) {
                    if(data.result == '1'){
                        //校验未通过
                        $scope.checkRegionCodeResult=false;
                    }else{
                        $scope.checkRegionCodeResult=true;
                    }
                },function (data) {
                    //commonService.goLoginPage(data.errorCode);
                })
            }
        };

        $scope.saveRegion = function (isValid) {
            if(isValid){
                $rootScope.loading = true;
                var promise = regionService.saveRegion($scope.region);
                promise.then(function (data) {
                    if(data.errorCode == '0'){
                        showSuccMsg(toastr,"添加区域成功");
                        $scope.$dismiss();
                        //更新一下区域列表
                        $rootScope.$emit("regionList", {});
                    }
                },function (data) {
                   //commonService.goLoginPage(data.errorCode);
                });
                $rootScope.loading = false;
            }else{
                showWarnMsg(toastr,"保存区域失败，请检查输入内容");
            }
        };

    }

    //操作用户
    function systemManagerCtrl($scope,$state,$rootScope,toastr,$uibModal,$filter,regionService,adminUserService,roleService,commonService) {


        $scope.getAdminUserList=function () {
            $rootScope.loading = true;
            var promise = adminUserService.getAdminUserList();
            promise.then(function (data) {
                $rootScope.chargePersonList = data;
                $rootScope.loading = false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };

        $scope.getUnChargeAdminUserList=function () {
            $rootScope.loading = true;
            var promise = regionService.getUnChargeAdminUserList();
            promise.then(function (data) {
                $rootScope.unChargePersonList = data;
                $rootScope.loading = false;
            },function (data) {
                //commonService.goLoginPage(data.errorCode);
            });
        };

        //获取系统用户列表
        $scope.adminUserList = function (params) {
            $rootScope.loading = true;
            $scope.searchSystemUserName = params.name;
            $scope.status = params.status;
            var promise = adminUserService.getUserList(params);
            promise.then(function (data) {
                $scope.users = data.list;
                //分页区域
                $scope.adminUserPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        // $location.search('currentPage', $scope.paginationConf.currentPage);
                        // alert($scope.adminUserPaginationConf.currentPage);
                        //分页
                        var paramData = new Object();
                        paramData.currentPage=$scope.adminUserPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.name=$scope.searchSystemUserName;
                        paramData.status=$scope.status;
                        $rootScope.$emit("adminUserList",paramData);
                    }
                };
                $rootScope.loading = false;
            },function (data) {
               commonService.goLoginPage(data.errorCode);
            })
        };

        //获取区域列表
        $scope.regionList = function () {
            $rootScope.loading = true;
            var promise = regionService.regionList();
            promise.then(function (data) {
                $scope.regions = data;
                $rootScope.loading = false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };


        //打开添加区域页面
        $scope.openAddRegionPage = function() {
            $rootScope.$emit("getUnChargeAdminUserList",{});
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/system/region/addRegion.html',
                size: 'md'
            });
        };

        //打开编辑区域页面
        $scope.editRegion = function(regiondata){
            $rootScope.$emit("getUnChargeAdminUserList",{});
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/system/region/editRegion.html',
                size: 'md',
                controller:function(){
                    $rootScope.editRegion= angular.copy(regiondata);
                }
            });
        }

        //保存编辑后的区域
        $scope.saveEditRegion = function (region,form) {
            $rootScope.loading = true;
            if(form.$data.regionName==null || form.$data.regionName==''){
                showWarnMsg(toastr,"区域名称不能为空");
                $rootScope.loading = false;
                $rootScope.$emit("regionList", {});

                return;
            }
            var promise = regionService.saveEditRegion(region, form);
            promise.then(function() {
                form.$submit();
                showSuccMsg(toastr,"修改区域成功");
                $rootScope.loading = false;
            }, function(data) {
               commonService.goLoginPage(data.errorCode);
            });

        };

      /*  //校验用户信息的唯一性
        $scope.checkAdminUser=function (checkType,event) {
            var adminUser = $rootScope.adminUser;
            if(adminUser.name == ''||adminUser.email==''|| adminUser.mobile==''){
                return;
            }
            adminUserService.checkAdminUser(adminUser,checkType).then(function(data){
                if(data.errorCode == '0'){
                    if(data.result == '0'){
                        if("name" == checkType){
                            $scope.checkAdminUserNameResult=false;
                        }else if("email" == checkType){
                            $scope.checkAdminUserEmailResult=false;
                        }else if("mobile" == checkType){
                            $scope.checkAdminUserMobileResult=false;
                        }

                    }else{
                        if("name" == checkType){
                            $scope.checkAdminUserNameResult=true;
                        }else if("email" == checkType){
                            $scope.checkAdminUserEmailResult=true;
                        }else if("mobile" == checkType){
                            $scope.checkAdminUserMobileResult=true;
                        }

                        $(event.target).css("border-color","#ed7878");
                        $(event.target).parent().addClass("has-error");
                        $(event.target).parent().removeClass("has-success");
                        $(event.target).next().removeClass("ion-checkmark-circled");
                        $(event.target).next().addClass("ion-android-cancel");
                    }
                }else{
                    $rootScope.loading = false;
                }
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })
        };*/
        /*//校验用户姓名的唯一性
        $scope.checkAdminUserName=function () {
            var name = $("#name").val();
            adminUserService.checkAdminUser(name).then(function(data){
                if(data.errorCode == '0'){
                    if(data.result == '0'){
                        $scope.checkAdminUserNameResult=false;
                    }else{
                        $scope.checkAdminUserNameResult=true;
                    }
                }else{

                }
            },function () {

            })
        };

        //校验邮箱的唯一性
        $scope.checkAdminUserEmail=function () {
            var adminUser = $scope.adminUser;
            adminUserService.checkAdminUser(email).then(function(data){
                if(data.errorCode == '0'){
                    if(data.result == '0'){
                        $scope.checkAdminUserEmailResult=false;
                    }else{
                        $scope.checkAdminUserEmailResult=true;
                    }
                }else{

                }
            },function () {

            })
        };*/

        /**
         * $emit只能向parent controller传递event与data
         * $broadcast只能向child controller传递event与data
         * $on用于接收event与data
         */
        var regionList = $rootScope.$on("regionList", function(event, data){
            $scope.regionList();
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            regionList();//回收广播
        });
        var getUnChargeAdminUserList = $rootScope.$on("getUnChargeAdminUserList",function (event,data) {
            $scope.getUnChargeAdminUserList();
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            getUnChargeAdminUserList();//回收广播
        });
        var adminUserListt = $rootScope.$on("adminUserList",function (event,data) {
            $scope.adminUserList(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            adminUserListt();//回收广播
        });

        var getAdminUserList = $rootScope.$on("getAdminUserList",function (event,data) {
            $scope.getAdminUserList();
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            getUnChargeAdminUserList();//回收广播
        });


        /*$scope.canClick=false;
        $scope.description="获取验证码";
        var second=59;
        var timerHandler;
        $scope.sendIdentifyingCode=function(){
            var phoneNum = $("#phoneNum").val();
            toastr.success("向手机号["+phoneNum+"]发送短信验证码成功");
            $scope.description=second+"s后重发";
            $scope.canClick=true;
            timerHandler=$interval(function(){
                if(second<=0){
                    $interval.cancel(timerHandler);
                    second=59;
                    $scope.description="获取验证码";
                    $scope.canClick=false;
                }else{
                    $scope.description=second+"s后重发";
                    second--;
                    $scope.canClick=true;
                }
            },1000)
        };*/

        //保存编辑后的用户
        $scope.saveEditUser=function () {
            $rootScope.loading = true;
            var roleIdArray = [];
            $("input[name='roleId']:checked").each(function () {
                roleIdArray.push($(this).val())
            });
            if(roleIdArray.length == 0){
                showWarnMsg(toastr,"请选择账户所对应的角色");
                $rootScope.loading = false;
                return;
            }
            var user=$scope.adminUser;
            var regionCode=null;
            if(user.regionYN){
                user.regionYN = 0;
            }else{
                user.regionYN = 1;
            }
            if(user.regionYN == 0){
                var checked = [];
                $("input[name='areaCode']:checked").each(function () {
                    checked.push($(this).val())
                });
                if(checked.length == 0){
                    showWarnMsg(toastr,"请选择区域");
                    $rootScope.loading = false;
                    return;
                }
                regionCode = checked.join(",");
            }
            adminUserService.saveEditUser(user,regionCode,roleIdArray.join(",")).then(function (data) {
                if(data.errorCode = '0'){
                    showSuccMsg(toastr,"修改系统用户成功");
                    $state.go('system.user');
                }else{
                    showErrorMsg(toastr,"修改系统用户失败，请重新操作");
                }
                $rootScope.loading = false;
            },function (data) {
                /*commonService.goLoginPage(data.errorCode);*/
                if(data.errorCode == '2'){
                    toastr.warning(data.errorMsg, 'Warning');
                }else{
                    //commonService.goLoginPage(data.errorCode);
                }
            });
        };

       /* //保存用户
        $scope.saveUser = function (form) {
            $rootScope.loading = true;
            var user=$rootScope.adminUser;
            var regionCode=null;

            var roleIdArray = [];
            $("input[name='roleId']:checked").each(function () {
                roleIdArray.push($(this).val())
            });
            if(roleIdArray.length == 0){
                showWarnMsg(toastr,"请选择账户所对应的角色");
                $rootScope.loading = false;
                return;
            }
            if(user.regionYN == null){
            	user.regionYN = 1;
            }else if(user.regionYN){
                user.regionYN = 0;
            }else{
                user.regionYN = 1;
            }
            if(user.regionYN == 0){
                var checked = [];
                $("input[name='areaCode']:checked").each(function () {
                    checked.push($(this).val())
                });
                if(checked.length == 0){
                    showWarnMsg(toastr,"请选择区域");
                    $rootScope.loading = false;
                    return;
                }
                regionCode = checked.join(",");
            }


            adminUserService.saveUser(user,regionCode,roleIdArray.join(",")).then(function (data) {
                showSuccMsg(toastr,"添加系统用户成功");
                $state.go('system.user');
            },function (data) {
                if(data.errorCode == '2'){
                    toastr.warning(data.errorMsg, 'Warning');
                }else{
                    //commonService.goLoginPage(data.errorCode);
                }
            });
        };*/

        //跳转至新增系统用户页面
        $scope.newSystemUser = function(){
            $rootScope.loading=true;
            $scope.adminUser=null;
            $scope.checkAdminUserNameResult=false;
            $scope.checkAdminUserEmailResult=false;
            $scope.checkAdminUserMobileResult=false;
            $rootScope.loading=false;
            $state.go('system.userAddPage');
        };

        $scope.isChecked=function (roleId) {
            var userRoleList = $scope.userRoleList;
            if(userRoleList == undefined){
                return;
            }
            for(var i=0 ;i<userRoleList.length;i++){
                if(roleId == userRoleList[i].roleId){
                    return true;
                }
            }
            return false;
        };

        //跳转至编辑系统用户页面
        $scope.editUser = function(adminUserId){
            $rootScope.loading=true;

            $scope.adminUser = null;
            $scope.region = null;
            $scope.userRoleList=null;
            $scope.adminUserId = adminUserId;
            //获取角色列表
            roleService.getRoleList().then(function (data) {
                if(data.errorCode == '0'){
                    $scope.roles = data.dataList;
                }
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });

            adminUserService.findByAdminId(adminUserId).then(function (data) {
                if(data.errorCode == '0'){
                    var adminRegion = data.data;
                    $scope.adminUser = adminRegion.adminUser;
                    $scope.adminUser.regionYN = $scope.adminUser.regionYN==0?true:false;
                    $scope.userRoleList=adminRegion.userRoleList;
                    // $scope.adminUserRegion = adminRegion.adminUserRegion;
                    var selectRegions = [];
                    for(var i=0;i<adminRegion.adminUserRegion.length ; i++){
                        selectRegions.push(adminRegion.adminUserRegion[i].regionCode);
                    }
                    $scope.selectRegions=selectRegions;
                }
            },function (data) {
                //commonService.goLoginPage(data.errorCode);
            });
            regionService.regionList().then(function (data) {
                $scope.regions = data;
            },function (data) {
                //commonService.goLoginPage(data.errorCode);
            });
            $state.go('system.userEditPage');
            $rootScope.loading=false;
        };

        /**
         * 返回是否被选中
         * @param regionCode
         * @returns {*}
         */
        $scope.isCheckedRegion=function (regionCode) {
            return $.inArray(regionCode, $scope.selectRegions) >= 0;
        };

        //删除用户
        $scope.removeUser = function (index,user) {
            var modalInstance = $uibModal.open({
                animation: true,
                size:'sm',
                templateUrl: 'app/pages/common/confimDelete.html',
                resolve: {
                    index: function() {
                        return index;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                console.log(result); //result关闭是回传的值 TODO
            }, function (reason) {
                if('yes' == reason){
                    // $scope.users.splice(index, 1);
                    //接收到删除的指令
                    $rootScope.loading=true;
                    var promise = adminUserService.deleteAdminUserById(user);
                    promise.then(function (data) {
                        if(data.errorCode == '0'){
                            showSuccMsg(toastr,"删除系统用户成功!!");
                            var params = new Object();
                            params.currentPage=$scope.adminUserPaginationConf.currentPage;
                            params.pageSize=15;
                            params.name=$scope.searchSystemUserName;
                            params.status=$scope.status;
                            $rootScope.$emit("adminUserList",params);
                        }else{
                            showWarnMsg(toastr,"删除数据失败，请重新操作!!")
                        }
                        $rootScope.loading=false;
                    },function (data) {
                        commonService.goLoginPage(data.errorCode);
                    });
                    // loading($rootScope,$timeout);
                }
                console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });
        };

        //激活用户
        $scope.activationUser=function (adminUser) {
            $rootScope.loading=true;
            if(adminUser.regionYN){
                adminUser.regionYN = 0;
            }else{
                adminUser.regionYN = 1;
            }
            adminUserService.activationUser(adminUser).then(function (data) {
                if(data.errorCode == '0'){
                    showSuccMsg(toastr,"激活用户成功");
                    $state.go('system.user');
                } else {
                    showErrorMsg(toastr,"激活用户失败");
                }
                $rootScope.loading=false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })
        };

        //搜索
        $scope.searchSystemUser = function () {
            var params = new Object();
            params.currentPage=1;
            params.pageSize=15;
            params.name=$scope.searchSystemUserName;
            params.status=$scope.status;
            $rootScope.$emit("adminUserList",params);
        };

        $scope.typeAllList=[
            {
                code:0,
                status:'正常'
            },
            {
                code:1,
                status:'删除'
            }
        ];

        //删除区域
        $scope.deleteRegion = function(region) {
            //为了安全,删除操作需要进行弹框确认
            var modalInstance = $uibModal.open({
                animation: true,
                size: 'sm',
                templateUrl: 'app/pages/common/confimDelete.html'
            });
            modalInstance.result.then(function (result) {
            }, function (reason) {
                if ('yes' == reason) {
                    //接收到删除的指令
                    $rootScope.loading = true;
                    var promise = regionService.deleteRegion(region);
                    promise.then(function (data) {
                        if(data.errorCode == '0'){
                            showSuccMsg(toastr,"删除区域成功");
                            $rootScope.loading=false;
                            $rootScope.$emit("regionList",{});
                        }
                    },function (data) {
                        commonService.goLoginPage(data.errorCode);
                    });
                }
            });

        };

        $scope.showManager=function (region) {
            var selected = [];
            if(region.adminUserId) {
                selected = $filter('filter')($scope.chargePersonList, {adminUserId: region.adminUserId});
            }
            if(selected == undefined){
                return;
            }
            return selected.length ? selected[0].realName : '未选择区域负责人';
        }
    }

    //操作角色
    function roleManagerCtrl($scope,$state) {

        $scope.roles = [
            {
                roleId : 1,
                roleName : '销售'
            },{
                roleId : 2,
                roleName : '财务'
            },{
                roleId : 3,
                roleName : '客服'
            },{
                roleId : 4,
                roleName : '管理员'
            }
        ];
        
        
        $scope.removeRole = function (index) {
            $scope.roles.splice(index, 1);
        };
        $scope.newRole = function () {
            $state.go('system.newRole');
        }
        
    }
})();