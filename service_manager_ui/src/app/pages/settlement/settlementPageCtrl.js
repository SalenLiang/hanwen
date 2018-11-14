(function () {
    'use strict';

    angular.module("BlurAdmin.pages.settlement")
        .controller("settlementCtrl",settlementCtrl)
        .controller("interfaceLogCtrl",interfaceLogCtrl);


    function settlementCtrl($scope,$rootScope,$timeout,$uibModal,$compile,$state,toastr,$interval,interfaceLogService,commonService){
        /*----------------------------日志 begin ------------------------------*/
        //获取接口列表
        $rootScope.$on("interfaceTypeListsss",function(event,data){
            $scope.interfaceTypeList(data);
        })
        $scope.interfaceTypeList =  function(data){
            $rootScope.loading = true;
            interfaceLogService.interfaceList(data).then(function(data){
                $rootScope.interfaceType = data.interfaceList;
                $rootScope.loading = false;
            },function(data){
                commonService.goLoginPage(data.errorCode);
            })
        }

        //获取日志列表
        $rootScope.$on("interfaceLogList",function(event,data){
            $scope.interfaceLogList(data);
        })
        $scope.interfaceLogList = function(data){
            $rootScope.loading = true;

            $rootScope.companyName = data.companyName;
            $rootScope.ettType = data.ettType;
            $rootScope.interfaceCode = data.interfaceCode;
            interfaceLogService.interfaceLogList(data).then(function(data){
                $rootScope.interfaceLogs = data.list;
                $rootScope.loading = false;

                //分页区域
                $scope.interfaceLogPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        //分页
                        var paramData = new Object();
                        paramData.currentPage=$scope.interfaceLogPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.companyName=$scope.companyName;
                        paramData.interfaceCode=$scope.interfaceCode;
                        params.beginDate = $scope.beginDate;
                        params.endDate = $scope.endDate;
                        paramData.ettType=$scope.ettType;
                        $rootScope.$emit("interfaceLogList",paramData);
                    }
                };
            },function(data){
                $rootScope.loading = false;

            });
        }

        /*----------------------------日志 end   ------------------------------*/
    }

    function interfaceLogCtrl($scope,$rootScope){
        $scope.logType = [
            {
                code:'0000',
                value:'成功次数'
            },{
                code:'9999',
                value:'失败次数'
            },{
                code:'1000',
                value:'查的次数'
            },{
                code:'2001',
                value:'日别去重'
            },{
                code:'2002',
                value:'月别去重'
            }
        ];
        //获取接口列表
        $rootScope.$emit("interfaceTypeListsss",null);
        //获取日志列表
        var params = new Object();
        params.companyName = null;
        params.interfaceCode = null;
        params.ettType = null;
        params.currentPage = 1;
        params.pageSize = 15;
        $rootScope.$emit("interfaceLogList",params);

        //搜索
        $scope.searchInterfaceLog = function(){
            var params = new Object();
            params.companyName = $scope.companyName;
            params.ettType = $scope.ettType;
            params.interfaceCode = $scope.interfaceCode;
            params.beginDate = $scope.beginDate;
            params.endDate = $scope.endDate;
            params.currentPage = 1;
            params.pageSize = 15;

            $rootScope.$emit("interfaceLogList",params);
        }
    }
})();