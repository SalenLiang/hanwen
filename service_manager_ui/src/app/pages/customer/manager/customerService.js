angular.module("BlurAdmin.pages.customer.customerService",[])
    .factory('customerService',function ($http,$rootScope,$compile,$q,Constant,commonService) {

		this.transformDomainDimensionLists = {};
		this.whoOpenSettingTemplet = {};
        var customerSeServiceData = {};

		//日别统计列表
		customerSeServiceData.dailyCountList = function(params){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/dailyCountList",
				timeout : Constant.timeout,
				params :{
					beginDate : params.beginDate,
					endDate : params.endDate,
					customerId : params.customerId,
					"currentPage" : params.currentPage,
					"pageSize" : params.pageSize,
				}
			}).success(function(data,status,headers,config){
				deffered.resolve(data);
			}).error(function(data,status,headers,config){
				commonService.goLoginPage(status);
			});
			return deffered.promise;
		}
		customerSeServiceData.getCustomerDailyReport = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/exportCustomerDailyReport",
				timeout : Constant.timeout,
				responseType: "blob",
				params : {
					"customerId" : params.customerId,
					"companyName" : params.companyName,
					"beginDate": params.beginDate,
					"endDate" : params.endDate
				}
			}).success(function(data, status, headers, config){
				var blob = new Blob([data], {type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"});
				//var blob = new Blob([data], {type: "application/vnd.ms-excel"});
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
		//月度统计列表
		customerSeServiceData.monthCountList = function(params){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/monthCountList",
				timeout : Constant.timeout,
				params :{
					beginDate : params.beginDate,
					endDate : params.endDate,
					customerId : params.customerId,
					"currentPage" : params.currentPage,
					"pageSize" : params.pageSize,
				}
			}).success(function(data,status,headers,config){
				deffered.resolve(data);
			}).error(function(data,status,headers,config){
				commonService.goLoginPage(status);
			});
			return deffered.promise;
		}
		customerSeServiceData.getCustomerMonthReport = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/exportCustomerMonthReport",
				timeout : Constant.timeout,
				responseType: "blob",
				params : {
					"customerId" : params.customerId,
					"companyName" : params.companyName,
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
		//年度统计列表
		customerSeServiceData.yearCountList = function(params){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/yearCountList",
				timeout : Constant.timeout,
				params :{
					beginDate : params.beginDate,
					endDate : params.endDate,
					customerId : params.customerId,
					"currentPage" : params.currentPage,
					"pageSize" : params.pageSize,
				}
			}).success(function(data,status,headers,config){
				deffered.resolve(data);
			}).error(function(data,status,headers,config){
				commonService.goLoginPage(status);
			});
			return deffered.promise;
		}
		//年度报表饼图数据
		customerSeServiceData.getYearPieData = function(params){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/getYearPieData",
				timeout : Constant.timeout,
				params :{
					yearTime : params.yearTime,
					customerId : params.customerId
				}
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
		customerSeServiceData.getCustomerYearReport = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/exportCustomerYearReport",
				timeout : Constant.timeout,
				responseType: "blob",
				params : {
					"customerId" : params.customerId,
					"companyName" : params.companyName,
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
		//校验某个字段是否正在被客户使用
		customerSeServiceData.fieldYN = function(fieldId){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/fieldYN",
				timeout : Constant.timeout,
				params :{
					fieldId : fieldId
				}
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

		//校验某个维度是否正在被客户使用
		customerSeServiceData.dimensionYN = function(dimensionId){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/dimensionYN",
				timeout : Constant.timeout,
				params :{
					dimensionId : dimensionId
				}
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

		//校验某个领域是否正在被客户使用
		customerSeServiceData.domainYN = function(domainId){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/domainYN",
				timeout : Constant.timeout,
				params :{
					domainId : domainId
				}
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

		//校验某个模型是否正在被客户使用
		customerSeServiceData.modelYN = function(modelId){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/modelYN",
				timeout : Constant.timeout,
				params :{
					modelId : modelId
				}
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

		//校验某个接口是否正在被客户使用
		customerSeServiceData.interfaceYN =  function(interfaceId){
			var deffered = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/interfaceYN",
				timeout : Constant.timeout,
				params :{
					interfaceId : interfaceId
				}
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
        
        //校验唯一性
        customerSeServiceData.checkCustomer = function (customer,checkType){
        	var deferred = $q.defer();
        	$http({
        		method : "post",
        		url : Constant.prefixUrl+"/customer/checkCustomer",
        		timeout : Constant.timeout,
        		params : {
        			companyName : customer.companyName,
        			contactTel : customer.contactTel,
        			contactEmail : customer.contactEmail,
        			userName : customer.userName,
        			contactPhone : customer.contactPhone,
        			customerId : customer.customerId,
        			checkType : checkType
        		}
        	}).success(function(data,status,headers,config){
				if (data.errorCode == 0) {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
        	}).error(function(data,status,headers,config){
				commonService.goLoginPage(status);
        	});
        	
        	return deferred.promise;
        }
        
        //查询所有区域
        customerSeServiceData.findRegionAll = function(){
        	var deferred = $q.defer();
        	$http({
        		method : "post",
        		url : Constant.prefixUrl+"/region/find",
        		timeout : Constant.timeout
        	}).success(function(data,status,headers,config){
					deferred.resolve(data);
        	}).error(function(data,status,headers,config){
				commonService.goLoginPage(status);
        	});
        	
        	return deferred.promise;
        }
        
        //获取用户的详细信息
        customerSeServiceData.findCustomerDetail = function(customerId){
        	var deferred = $q.defer();
        	$http({
        		method : "post",
        		url : Constant.prefixUrl+"/customer/findCustomerDetail",
        		timeout : Constant.timeout,
        		params : {
					customerId : customerId
				}
        	}).success(function (data,status,headers,config) {
                if (data.errorCode == 0) {
                    deferred.resolve(data);
                }else{
                    deferred.reject(data);
                }
            }).error(function (data,status,headers,config) {
				commonService.goLoginPage(status);
            });
            return deferred.promise;
        }
        //删除客户信息
        customerSeServiceData.deleteCustomerById = function(customer){
        	var deferred =$q.defer();
        	$http({
        		method : "post",
				url : Constant.prefixUrl+"/customer/deleteCustomerById",
				timeout : Constant.timeout,
				params : {
					customerStr:JSON.stringify(customer)
				}
        	}).success(function (data,status,headers,config) {
                if (data.errorCode == 0) {
                    deferred.resolve(data);
                }else{
                    deferred.reject(data);
                }
            }).error(function (data,status,headers,config) {
				commonService.goLoginPage(status);
            });
            return deferred.promise;
        }
        //激活客户
		customerSeServiceData.activationCustomerStatus = function(customer){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/activationCustomer",
				timeout : Constant.timeout,
				params : {
					customerStr:JSON.stringify(customer)
				}
			}).success(function (data,status,headers,config) {
				if (data.errorCode == 0) {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
			}).error(function (data,status,headers,config) {
				commonService.goLoginPage(status);
			});
			return deferred.promise;
		}
		//保存修改后的客户
		customerSeServiceData.saveEditCustomer = function (customer,domainDimensionLists,customerInterfaces){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/update",
				timeout : Constant.timeout,
				headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                transformRequest: function(obj) { 
				    var str = []; 
				    for(var p in obj){ 
				      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p])); 
				    } 
				    return str.join("&"); 
				},
                data: {
                	customer : JSON.stringify(customer),
                    addCustomerServiceDomains : JSON.stringify(domainDimensionLists),
                    addCustomerServiceInterfaces: JSON.stringify(customerInterfaces)
                }
			}).success(function (data,status,headers,config) {
                if (data.errorCode == '0') {
                    deferred.resolve(data);
                }else{
                    deferred.reject(data);
                }
            }).error(function (data,status,headers,config) {
				commonService.goLoginPage(status);
            });
            return deferred.promise;
		}
        //保存客户信息
        customerSeServiceData.saveCustomer = function (customer,domainDimensionLists,customerInterfaces) {
            var deferred = $q.defer();/* $q需要在创建service时引用 */
           	/*var objValue =window.encodeURI(window.encodeURI(customer.companyName));
           	var objValue2 =window.encodeURI(customer.customerArea);*/
            $http({
                method:"post",
                url:Constant.prefixUrl+"/customer/save",
                timeout:Constant.timeout,
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                transformRequest: function(obj) { 
				    var str = []; 
				    for(var p in obj){ 
				      str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p])); 
				    } 
				    return str.join("&"); 
				},
                data: {
                	customer : JSON.stringify(customer),
                    addCustomerServiceDomains : JSON.stringify(domainDimensionLists),
                    addCustomerServiceInterfaces: JSON.stringify(customerInterfaces)
                }
            }).success(function (data,status,headers,config) {
				if (data.errorCode == '0') {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
            }).error(function (data,status,headers,config) {
				commonService.goLoginPage(status);
            });
            return deferred.promise;
        };
		//获取客户列表
		customerSeServiceData.getCustomerList = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/customer/find",
				timeout : Constant.timeout,
				params : {
					"currentPage" : params.currentPage,
					"pageSize" : params.pageSize,
					"companyName" : params.searchCompanyName,
					"authCode" : params.searchAuthCode,
					"status": params.status
				}
			}).success(function(data, status, headers, config){
				if(data != null&&data!=''){
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
			}).error(function(data, status, headers, config){
				commonService.goLoginPage(status);
			});
			return deferred.promise;
		};
		
		//获取接口列表
		customerSeServiceData.selInterfaceLists = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/interface/findAllValid",
				timeout : Constant.timeout,
				params : {}
			}).success(function(data, status, headers, config){
				if (data.errorCode == '0') {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
			}).error(function(data, status, headers, config){
				commonService.goLoginPage(status);
			});
				return deferred.promise;
		}
		
		//获取领域 维度列表domainDimensionLists
		customerSeServiceData.domainDimensionLists = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/domain/findAllDomain",
				timeout : Constant.timeout,
				params : {}
			}).success(function(data, status, headers, config){
				if (data.errorCode == '0') {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
			}).error(function(data, status, headers, config){
				commonService.goLoginPage(status);
			});
				return deferred.promise;
		}
		//获取配置模板列表
		customerSeServiceData.settingTempletLists = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/settingTemplet/findAllSettingTemplet",
				timeout : Constant.timeout,
				params : {}
			}).success(function(data, status, headers, config){
				if (data.errorCode == '0') {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
			}).error(function(data, status, headers, config){
				commonService.goLoginPage(status);
			});
				return deferred.promise;
		}
		//获取接口模型列表
		customerSeServiceData.modelList = function(params){
			var deferred = $q.defer();
			$http({
				method : "post",
				url : Constant.prefixUrl+"/interfaceModel/findAll",
				timeout : Constant.timeout
			}).success(function(data, status, headers, config){
				if (data.errorCode == '0') {
					deferred.resolve(data);
				}else{
					deferred.reject(data);
				}
			}).error(function(data, status, headers, config){
				commonService.goLoginPage(status);
			});
				return deferred.promise;
		}
		return customerSeServiceData;
    });