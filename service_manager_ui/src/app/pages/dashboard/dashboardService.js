/**
 * Created by z on 2017/8/2.
 */
angular.module("BlurAdmin.pages.dashboard.dashboardService",[])
    .factory('dashboardService',function ($http,$rootScope,$compile,$q,Constant,commonService) {

        var dashboardServiceData = {};

        //获取dashboard top5数据
        dashboardServiceData.getTop5Data = function(){
            var deffered = $q.defer();
            $http({
                method : "post",
                url : Constant.prefixUrl+"/dashboard/getTop5Data",
                timeout : Constant.timeout
            }).success(function(data,status,headers,config){
                if(data.errorCode == 0){
                    deffered.resolve(data);
                }else{
                    deffered.reject(data);
                }
            }).error(function(data,status,headers,config){
                commonService.goLoginPage(status);
            });
            return deffered.promise;
        }
        //获取dashboard interface预警数据
        dashboardServiceData.getInterfaceCountData = function(){
            var deffered = $q.defer();
            $http({
                method : "post",
                url : Constant.prefixUrl+"/dashboard/getInterfaceCountData",
                timeout : Constant.timeout
            }).success(function(data,status,headers,config){
                if(data.errorCode == 0){
                    deffered.resolve(data);
                }else{
                    deffered.reject(data);
                }
            }).error(function(data,status,headers,config){
                commonService.goLoginPage(status);
            });
            return deffered.promise;
        }



        return dashboardServiceData;
    });