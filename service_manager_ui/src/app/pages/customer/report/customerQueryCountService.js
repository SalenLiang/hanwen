angular.module("BlurAdmin.pages.customer.customerQueryCountService",[])
    .factory('customerQueryCountService',function ($http,$rootScope,$compile,$q,Constant,commonService) {

        var customerQueryCountServiceData = {};

        //获取客户日别报表
        customerQueryCountServiceData.dailyCountList = function(params){
            var deferred = $q.defer();
            $http({
                method : "post",
                url : Constant.prefixUrl+"/customerQueryCount/dailyCount",
                timeout : Constant.timeout,
                params : {
                    "currentPage" : params.currentPage,
                    "pageSize" : params.pageSize,
                    "companyName" : params.searchParam,
                    "beginDate": params.beginDate,
                    "endDate" : params.endDate
                }
            }).success(function(data, status, headers, config){
                deferred.resolve(data);
            }).error(function(data, status, headers, config){
                commonService.goLoginPage(status);
            });
            return deferred.promise;
        };
        //导出客户日别报表
        customerQueryCountServiceData.getDailyReport = function(params){
            var deferred = $q.defer();
            $http({
                method : "post",
                url : Constant.prefixUrl+"/customerQueryCount/exportDailyReport",
                timeout : Constant.timeout,
                responseType: "blob",
                params : {
                    "companyName" : params.searchParam,
                    "beginDate": params.beginDate,
                    "endDate" : params.endDate
                }
            }).success(function(data, status, headers, config){
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                if(blob.size>0){
                    var fileName = $rootScope.currentAdminUserId +""+ (new Date()).getTime()+".xlsx";
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = fileName;
                    a.href = URL.createObjectURL(blob);
                    a.click();
                }else{
                    deferred.resolve("下载报表失败");
                }
            }).error(function(data, status, headers, config){
                commonService.goLoginPage(status);
            });
            return deferred.promise;
        };
        //导出客户月别报表
        customerQueryCountServiceData.getMonthReport = function(params){
            var deferred = $q.defer();
            $http({
                method : "post",
                url : Constant.prefixUrl+"/customerQueryCount/exportMonthReport",
                timeout : Constant.timeout,
                responseType: "blob",
                params : {
                    "companyName" : params.searchParam,
                    "beginDate": params.beginDate,
                    "endDate" : params.endDate
                }
            }).success(function(data, status, headers, config){
                var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
                if(blob.size>0){
                    var fileName = $rootScope.currentAdminUserId +""+ (new Date()).getTime()+".xlsx";
                    var a = document.createElement("a");
                    document.body.appendChild(a);
                    a.download = fileName;
                    a.href = URL.createObjectURL(blob);
                    a.click();
                }else{
                    deferred.resolve("下载报表失败");
                }
            }).error(function(data, status, headers, config){
                commonService.goLoginPage(status);
            });
            return deferred.promise;
        };
        //获取客户月别报表
        customerQueryCountServiceData.monthCountList = function(params){
            var deferred = $q.defer();
            $http({
                method : "post",
                url : Constant.prefixUrl+"/customerQueryCount/monthCount",
                timeout : Constant.timeout,
                params : {
                    "currentPage" : params.currentPage,
                    "pageSize" : params.pageSize,
                    "companyName" : params.searchParam,
                    "beginDate": params.beginDate,
                    "endDate" : params.endDate
                }
            }).success(function(data, status, headers, config){
                deferred.resolve(data);
            }).error(function(data, status, headers, config){
                commonService.goLoginPage(status);
            });
            return deferred.promise;
        };
        return customerQueryCountServiceData;
    });