
var interfaceModelModule = angular.module("BlurAdmin.interfacemodel.interfaceModelService",[]);

interfaceModelModule.factory('interfaceModelService',function ($http,$q,$rootScope,$compile,Constant,commonService) {
    var interfaceModelData = new Object();

    //校验某个维度是否被模型使用
    interfaceModelData.dimensionYN = function(dimensionId){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/interfaceModel/dimensionYN',
            params:{
                dimensionId : dimensionId
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }

    //校验某个领域是否被模型使用
    interfaceModelData.domainYN = function(domainId){
        var deferred = $q.defer();
        $http({
            method:"post",
            url:Constant.prefixUrl+'/interfaceModel/domainYN',
            params:{
                domainId : domainId
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }
    //获取列表
    interfaceModelData.getInterfaceModelList = function (params){
    	var deferred = $q.defer();
    	$http({
    		method:"post",
    		url:Constant.prefixUrl+'/interfaceModel/find',
    		params:{
                currentPage:params.currentPage,
                pageSize:params.pageSize,
                status:params.status,
                modelName:params.modelName
            }
        }).success(function (data, status, headers, config) {
            if(data != null&&data!=''){
                deferred.resolve(data);
            }else{
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
    }
	//获取领域维度信息
	interfaceModelData.getAllDomainDimension = function (){
		var deferred = $q.defer();
		$http({
			method:"post",
    		url:Constant.prefixUrl+'/domain/findAllDomainDimension'
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
	}
	//保存新增的模型
	interfaceModelData.saveInterfaceModel = function(interfaceModel,domainDimension){
		var deferred = $q.defer();
		$http({
			method:"post",
    		url:Constant.prefixUrl+'/interfaceModel/save',
    		params:{
                interfaceModel:JSON.stringify(interfaceModel),
                addDomainDimension:JSON.stringify(domainDimension)
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
	}
	
	//删除模型
	interfaceModelData.deleteInterfaceModel =function (interfaceModel){
		var deferred = $q.defer();
		$http({
			method:"post",
    		url:Constant.prefixUrl+'/interfaceModel/delete',
    		params:{
    			modelId:interfaceModel.modelId,
    			modelName:interfaceModel.modelName,
    			description:interfaceModel.description
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
	}
	//查询 模型的领域纬度
	interfaceModelData.getModelDomainDimension =function (modelId){
		var deferred = $q.defer();
		$http({
			method:"post",
    		url:Constant.prefixUrl+'/interfaceModel/getModelDomainDimension',
    		params:{
    			modelId:modelId
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
	}
	
	//保存修改后的模型
	interfaceModelData.saveEditInterfaceModel = function(interfaceModel,domainDimension){
		var deferred = $q.defer();
		$http({
			method:"post",
    		url:Constant.prefixUrl+'/interfaceModel/update',
    		params:{
                interfaceModel:JSON.stringify(interfaceModel),
                editDomainDimension:JSON.stringify(domainDimension)
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
	}
	//校验模型参数的唯一性
	interfaceModelData.checkModel = function(interfaceModel,checkType){
		var deferred = $q.defer();
		$http({
			method:"post",
    		url:Constant.prefixUrl+'/interfaceModel/checkModel',
    		params:{
                modelName:interfaceModel.modelName,
                modelId:interfaceModel.modelId,
                description:interfaceModel.description,
                checkType:checkType
            }
        }).success(function (data, status, headers, config) {
            if (data.errorCode == 0) {
                deferred.resolve(data);
            } else {
                deferred.reject(data);
            }
        }).error(function (data, status, headers, config) {
            commonService.goLoginPage(status);
        });
        return deferred.promise;
	}
	
	
    return interfaceModelData;
});