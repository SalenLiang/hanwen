(function () {
    'use strict';
    angular.module("BlurAdmin.pages.customer")
        .controller("customerManagerCtrl",customerManagerCtrl)
        .controller("customerCtrl",customerCtrl)
//      .controller("interfaceListCtrl",interfaceListCtrl)
        .controller("domainDimensionCtrl",domainDimensionCtrl)
        .controller("fieldListCtrl",fieldListCtrl)
        .controller("editFieldListCtrl",editFieldListCtrl)
        .controller("editInterfaceListCtrl",editInterfaceListCtrl)
        .controller("modelCtrl",modelCtrl)
        .controller("dailyCountCtrl",dailyCountCtrl)
        .controller("monthCountCtrl",monthCountCtrl)
        .controller("editCustomerController",editCustomerController)
        .controller("addCustomerController",addCustomerController)
		.controller("interfaceSettingCtrl",interfaceSettingCtrl)
		.controller("customerYearCountCtrl",customerYearCountCtrl)
		.controller("customerMonthCountCtrl",customerMonthCountCtrl)
		.controller("useSettingTempletCtrl",useSettingTempletCtrl)
		.controller("customerDailyCountCtrl",customerDailyCountCtrl)
        .controller("bankManagerCtrl",bankManagerCtrl)
		.controller("riskReportCtrl",riskReportCtrl)
		.controller("userSonInfoCtrl",userSonInfoCtrl)
        .controller("userSonUpdateCtrl",userSonUpdateCtrl)
        .filter('customerPhoneDesensitization', function() {
	        return function(contactPhone) {
	        	if(angular.isString(contactPhone)){
	            	return contactPhone.substring(0,3)+'****'+contactPhone.substring(7);
	            }
	        	return contactPhone;
	        }
    	});
	function useSettingTempletCtrl($rootScope,$scope,$state,$filter,settingTempletService,customerService,commonService){
		$scope.useSettingTemplet = function(who){
			settingTempletService.getSettingTempletDetail($scope.selectTempletId).then(function(data){
				var resultData = new Object();
				resultData.templetDomain = data.templetDomain;
				resultData.who = customerService.whoOpenSettingTemplet;
				$rootScope.$emit('switchSettingPar',resultData);
			},function(data){
				commonService.goLoginPage(status);
			})
			$scope.$dismiss();
		}

		//获取模板列表
		$scope.settingTempletList = function (params){
			var promise = customerService.settingTempletLists(params);
			promise.then(function (data) {
				$scope.availableTempletList = data.availableTempletList;
				$rootScope.loading = false;
			},function (data) {
				commonService.goLoginPage(data.errorCode);
			})
		}
		$scope.settingTempletList();
	}

	/*function yearCountReportCtrl($rootScope,$scope,$state,$filter,toastr,customerService,commonService){


			$scope.loadYearPieCharts = function(legendData,seriesData1,seriesData2,status){
				if(status=='edit'){
					var option = $scope.myChart.getOption();
					option.legend.data = legendData;
					option.series[0].data = seriesData1;
					option.series[1].data = seriesData2;
					$scope.myChart.setOption(option);
				}else {
					$scope.myChart = echarts.init(document.getElementById('xxmain'));
					var option = {
						tooltip: {
							trigger: 'item',
							formatter: "{b}: {c} ({d}%)"
						},
						legend: {
							orient: 'vertical',
							x: 'left',
							data: legendData
						},
						series: [
							{
								type: 'pie',
								selectedMode: 'single',
								radius: [0, '30%'],

								label: {
									normal: {
										position: 'inner'
									}
								},
								labelLine: {
									normal: {
										show: false
									}
								},
								data: seriesData1
							},
							{
								type: 'pie',
								radius: ['40%', '55%'],
								label: {
									normal: {
										formatter: '{b|{b}：}{c}  {per|{d}%}  ',
										backgroundColor: '#eee',
										borderColor: '#aaa',
										borderWidth: 1,
										borderRadius: 4,
										shadowBlur: 3,
										shadowOffsetX: 2,
										shadowOffsetY: 2,
										shadowColor: '#999',
										padding: [0, 7],
										rich: {
											a: {
												color: '#999',
												lineHeight: 22,
												align: 'center'
											},
											hr: {
												borderColor: '#aaa',
												width: '100%',
												borderWidth: 0.5,
												height: 0
											},
											b: {
												fontSize: 16,
												lineHeight: 33
											},
											per: {
												color: '#eee',
												backgroundColor: '#334455',
												padding: [2, 4],
												borderRadius: 2
											}
										}
									}
								},
								data: seriesData2
							}
						]
					};
					$scope.myChart.setOption(option);
				}
			}
		var params = new Object();
		if($rootScope.reportCustomer==null){
			$rootScope.reportCustomer = JSON.parse(window.localStorage.getItem("reportCustomer"));
		}
		params.status = 'add';
		params.customerId = $rootScope.reportCustomer.customerId;
		params.yearTime = $filter('date')(new Date(),'yyyy');
		$scope.getYearPieData = function(params){
			$scope.yearCountPieDate = params.yearTime;
			customerService.getYearPieData(params).then(function(data){
				if(data.legendData==null||!data.legendData.length>0){
					$scope.loadYearPieCharts([], [], [], params.status);
				}else{
					$scope.loadYearPieCharts(data.legendData, data.seriesData1, data.seriesData2, params.status);
				}
			},function(data){
				commonService.goLoginPage(data.status);
			})
		}
		$scope.getYearPieData(params);

		$scope.resetPitChars = function(){
			if($scope.yearCountPieDate==null||$scope.yearCountPieDate==''){
				$scope.yearCountPieDate = $filter('date')(new Date(),'yyyy');
			}
			var params = new Object();
			params.status = 'edit';
			params.customerId = $scope.reportCustomer.customerId;
			params.yearTime = $scope.yearCountPieDate;
			$scope.getYearPieData(params);
		}
	}*/
	function customerDailyCountCtrl($rootScope,$scope,$state,$filter,$timeout,toastr,customerService,commonService){
		if($rootScope.reportCustomer==null){
			$rootScope.reportCustomer = JSON.parse(window.localStorage.getItem("reportCustomer"));
		}
		var params = new Object();
		params.currentPage = 1;
		params.pageSize = 15;
		params.customerId = $rootScope.reportCustomer.customerId;
		params.beginDate =  $filter('date')(new Date(),'yyyy-MM-dd');
		params.endDate =  $filter('date')(new Date(),'yyyy-MM-dd');
		$rootScope.$emit("customerDailyCountListss",params);

		$scope.searchCustomerDailyCount = function(){
			var params = new Object();
			params.currentPage = 1;
			params.pageSize = 15;
			params.customerId = $rootScope.reportCustomer.customerId;
			params.beginDate = $scope.customerDailyBeginDate;
			params.endDate = $scope.customerDailyEndDate;
			$rootScope.$emit("customerDailyCountListss",params);
		}

		$scope.exportCustomerDailyReport = function () {
			$scope.exportCustomerDailyReportStatus = true;
			var params = new Object();
			params.customerId = $rootScope.reportCustomer.customerId;
			params.companyName = $rootScope.reportCustomer.companyName;
			params.beginDate = $scope.customerDailyBeginDate;
			params.endDate = $scope.customerDailyEndDate;
			$timeout(function(){
				$scope.exportCustomerDailyReportStatus = false;
			},10000)
			customerService.getCustomerDailyReport(params).then(function(data){
				showWarnMsg(toastr,data);
			},function(){
				commonService.goLoginPage(status);
			})
		};

	}
	function customerMonthCountCtrl($rootScope,$scope,$state,$timeout,$filter,toastr,customerService,commonService){
		if($rootScope.reportCustomer==null){
			$rootScope.reportCustomer = JSON.parse(window.localStorage.getItem("reportCustomer"));
		}
		var params = new Object();
		params.currentPage = 1;
		params.pageSize = 15;
		params.customerId = $rootScope.reportCustomer.customerId;
		params.beginDate =  $filter('date')(new Date(),'yyyy-MM');
		params.endDate =  $filter('date')(new Date(),'yyyy-MM');
		$rootScope.$emit("customerMonthCountListss",params);

		$scope.searchCustomerMonthCount = function(){
			var params = new Object();
			params.currentPage = 1;
			params.pageSize = 15;
			params.customerId = $rootScope.reportCustomer.customerId;
			params.beginDate = $scope.customerMonthBeginDate;
			params.endDate = $scope.customerMonthEndDate;
			$rootScope.$emit("customerMonthCountListss",params);
		}

		$scope.exportCustomerMonthReport = function () {
			$scope.exportCustomerMonthReportStatus = true;
			var params = new Object();
			params.customerId = $rootScope.reportCustomer.customerId;
			params.companyName = $rootScope.reportCustomer.companyName;
			params.beginDate = $scope.customerMonthBeginDate;
			params.endDate = $scope.customerMonthEndDate;
			$timeout(function(){
				$scope.exportCustomerMonthReportStatus = false;
			},10000)
			customerService.getCustomerMonthReport(params).then(function(data){
				showWarnMsg(toastr,data);
			},function(){
				commonService.goLoginPage(status);
			})
		};

	}
	function customerYearCountCtrl($rootScope,$scope,$state,$filter,$timeout,toastr,customerService,commonService){
		if($rootScope.reportCustomer==null){
			$rootScope.reportCustomer = JSON.parse(window.localStorage.getItem("reportCustomer"));
		}
		var params = new Object();
		params.currentPage = 1;
		params.pageSize = 15;
		params.customerId = $rootScope.reportCustomer.customerId;
		params.beginDate =  $filter('date')(new Date(),'yyyy');
		params.endDate =  $filter('date')(new Date(),'yyyy');
		$rootScope.$emit("customerYearCountListss",params);

		$scope.searchCustomerYearCount = function(){
			var params = new Object();
			params.currentPage = 1;
			params.pageSize = 15;
			params.customerId = $rootScope.reportCustomer.customerId;
			params.beginDate = $scope.customerYearBeginDate;
			params.endDate = $scope.customerYearEndDate;
			$rootScope.$emit("customerYearCountListss",params);
		}

		$scope.exportCustomerYearReport = function () {
			$scope.exportCustomerYearReportStatus = true;
			var params = new Object();
			params.customerId = $rootScope.reportCustomer.customerId;
			params.companyName = $rootScope.reportCustomer.companyName;
			params.beginDate = $scope.customerYearBeginDate;
			params.endDate = $scope.customerYearEndDate;
			$timeout(function(){
				$scope.exportCustomerYearReportStatus = false;
			},10000)
			customerService.getCustomerYearReport(params).then(function(data){
				showWarnMsg(toastr,data);
			},function(){
				commonService.goLoginPage(status);
			})
		};
	}
	function addCustomerController($rootScope,$scope,$state,$filter,toastr,customerService){
        $scope.bankPanel = false;
        $scope.reportPanel = false;
        $scope.monitorPanel = false;
        $scope.panelIndex = 0;

        $scope.clickBankCheck = function (params) {
            $scope.bankPanel = params.bankCheck;
        }
        $scope.clickReportCheck = function (params) {
            $scope.reportPanel = params.reportCheck;
        }
        $scope.clickMonitorCheck = function (params) {
            $scope.monitorPanel = params.monitorCheck;
        }


		if($scope.customer==null){
			$state.go('customer.manager');
		}
		$rootScope.$on('switchSettingSon',function(event,data){
			$scope.domainDimensionLists = data;
			customerService.transformDomainDimensionLists = $scope.domainDimensionLists;
		})
	}
	function editCustomerController($rootScope,$scope,$state,$filter,toastr,customerService){
		if($scope.customer==null){
			$state.go('customer.manager');
		}
		$rootScope.$on('editSwitchSettingSon',function(event,data){
			$scope.editDomainDimensionLists = data;
			customerService.transformDomainDimensionLists = data;
		})
	}
	/*-----------------------------------------------客户接口报表 begin-----------------------------------------*/
	//日别统计
	function dailyCountCtrl($rootScope,$scope,$filter,$timeout,toastr,customerQueryCountService,Constant,commonService){
		var params = new Object();
		params.currentPage = 1;
		params.pageSize = 15;
		params.searchParam = null;
		params.beginDate =  $filter('date')(new Date(),'yyyy-MM-dd');
		params.endDate =  $filter('date')(new Date(),'yyyy-MM-dd');
		$rootScope.$emit("dailyCountListss",params);

		$scope.searchDailyCount = function(){
			var params = new Object();
			params.currentPage = 1;
			params.pageSize = 15;
			params.searchParam = $scope.searchParam;
			params.beginDate = $scope.beginDate;
			params.endDate = $scope.endDate;
			$rootScope.$emit("dailyCountListss",params);
		}

		$scope.exportDailyReport = function () {
			$scope.exportDailyReportStatus = true;
			var params = new Object();
			params.searchParam = $scope.searchParam;
			params.beginDate = $scope.beginDate;
			params.endDate = $scope.endDate;
			$timeout(function(){
				$scope.exportDailyReportStatus = false;
			},10000)
			customerQueryCountService.getDailyReport(params).then(function(data){
				showWarnMsg(toastr,data);
			},function(){
				commonService.goLoginPage(status);
			})
		};
	}
	//月别统计
	function monthCountCtrl($rootScope,$scope,$filter,$timeout,$http,toastr,customerQueryCountService,Constant,commonService){
		var params = new Object();
		params.currentPage = 1;
		params.pageSize = 15;
		params.searchParam = null;
		params.beginDate = $filter('date')(new Date(),'yyyy-MM');
		params.endDate =  $filter('date')(new Date(),'yyyy-MM');
		$rootScope.$emit("monthCountListss",params);

		$scope.searchMonthCount = function(){
			var params = new Object();
			params.currentPage = 1;
			params.pageSize = 15;
			params.searchParam = $scope.monthSearchParam;
			params.beginDate = $scope.monthBeginDate;
			params.endDate = $scope.monthEndDate;
			$rootScope.$emit("monthCountListss",params);
		}


		$scope.exportMonthReport = function () {
			$scope.exportMonthReportStatus = true;
			var params = new Object();
			params.searchParam = $scope.monthSearchParam;
			params.beginDate = $scope.monthBeginDate;
			params.endDate = $scope.monthEndDate;
			$timeout(function(){
				$scope.exportMonthReportStatus = false;
			},10000)
			customerQueryCountService.getMonthReport(params).then(function(data){
				showWarnMsg(toastr,data);
			 },function(){
				commonService.goLoginPage(status);
			 })

		};
	}

	/*-----------------------------------------------客户接口报表 end-----------------------------------------*/
	/*-----------------------------------------------接口选择 begin-----------------------------------------*/
	function interfaceSettingCtrl($rootScope,$scope,$compile,toastr){
		$rootScope.$emit("interfaceModelList",null);
		//检验填写的自定义每页条数是否合格
		$scope.checkCustomRange = function(){
			if($scope.interfaceSetting.customRange==null||$scope.interfaceSetting.customRange ==""||($scope.interfaceSetting.customRange<1 || $scope.interfaceSetting.customRange>100)){
				$scope.checkResult= true;
			}else{
				$scope.checkResult= false;
			}
		}

		//添加配置的接口信息到selIntrface
		$scope.addCustomerInterface = function(){
			var flag = true;
			for(var i=0;i<$rootScope.interfaceSettingList.length;i++){
				if(i == $rootScope.createOrEdit ){
					continue;
				}
				if($rootScope.interfaceSettingList[i].modelId == $scope.interfaceSetting.modelId && $rootScope.interfaceSettingList[i].modelId && $rootScope.interfaceSettingList[i].interfaceId == $scope.interfaceSetting.interfaceId ){
					flag = false;
				}
			}
			if(flag) {
				for (var i = 0; i < $scope.interfaces.length; i++) {
					if ($scope.interfaceSetting.interfaceId == $scope.interfaces[i].interfaceId) {
						$scope.interfaceSetting.interfaceName = $scope.interfaces[i].interfaceName;
					}
				}
				for (var i = 0; i < $scope.modelLists.length; i++) {
					if ($scope.interfaceSetting.modelId == $scope.modelLists[i].modelId) {
						$scope.interfaceSetting.modelName = $scope.modelLists[i].modelName;
					}
				}
				if ($rootScope.createOrEdit == -1) { //新增
					$rootScope.interfaceSettingList.push($scope.interfaceSetting);
				} else { //修改
					$rootScope.interfaceSettingList[$rootScope.createOrEdit] = $scope.interfaceSetting;
				}
			}else{
				showWarnMsg(toastr,"该接口配置已存在");
			}
			$scope.$dismiss()
		}

		//自定义查询类型
		$scope.queryTypes = [
			{type: "common",code : "common"},{type:"query",code:"query"}
		]

	}
	/*-----------------------------------------------接口选择 end-----------------------------------------*/

	function modelCtrl($rootScope,$scope){
		$rootScope.$emit("modelList",null);
		
		$scope.addModel = function(){
			$rootScope.serviceInterface.modelId = $rootScope.selectInterface.modelId;
			$rootScope.serviceInterface.customRange = $rootScope.selectInterface.customRange;
			$scope.$dismiss()
		}

		$scope.checkCustomRange = function(){
			if($scope.selectInterface.customRange!=null&&($scope.selectInterface.customRange<1 || $scope.selectInterface.customRange>21)){
				$scope.checkResult= true;
			}else{
				$scope.checkResult= false;
			}
		}
	}
	function customerCtrl($rootScope,$scope) {
        //初始化参数
        var params = new Object();
		params.currentPage=1;
        params.pageSize=15;
        params.searchCompanyName=null;
		params.searchAuthCode = null;
		params.status = 0;
        $rootScope.$emit("customerLists",params);

    }
	/*function interfaceListCtrl($rootScope,$scope,customerService,$uibModal){
		//查询开放接口
		if($rootScope.interfaces == null){
			var promise = customerService.selInterfaceLists("");
			promise.then(function (data) {
            	$rootScope.interfaces = data.interfaceList;
            	$rootScope.restoreInterfaces = JSON.parse(JSON.stringify($scope.interfaces));
            	$rootScope.loading = false;
        	},function () {
            	showErrorMsg(toastr,"查询接口失败，请稍候再试");
        	})
		}
		//选择接口模型
		$rootScope.selectInterfaceModel = function(serviceInterface){
			var resultView = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/customer/manager/selModel.html',
                size: "md",
                controller:function(){
            		$rootScope.selectInterface = JSON.parse(JSON.stringify(serviceInterface));
            		$rootScope.serviceInterface = serviceInterface;
                }
            });
		}*/
		/*//客户选择接口
		$rootScope.addInterface = function(){
			$rootScope.$emit("customerInterfaceLists",$rootScope.interfaces);
			$scope.$dismiss();
		}*/
//	}

	function editInterfaceListCtrl($rootScope,$scope,customerService){
		//客户选择接口
		$rootScope.editAddInterface = function(){
			$rootScope.$emit("editCustomerInterfaceLists",$rootScope.editInterfaces);
			$scope.$dismiss();
		}
	}
	function domainDimensionCtrl($rootScope,$scope,$uibModal){

		$rootScope.$emit("domainDimensionList","");

	}
	
	function editFieldListCtrl($rootScope,$scope){
		$scope.editListNum = 0;
		$scope.editDetailNum = 0;
		//进入选择字段页面，判断是否全选
		$scope.editAllSelectYN = function(){
			$scope.editAllListSelect = true;
			$scope.editAllDetailSelect = true;
			$scope.editListNum = 0;
			$scope.editDetailNum = 0;
			for(var i=0;i<$scope.fields.length;i++){
				if($scope.fields[i].defaultListShowYN==null||$scope.fields[i].defaultListShowYN==0){//未选中
					$scope.editAllListSelect = false;
				}else{
					$scope.editListNum++;
				}
				if($scope.fields[i].defaultDetailShowYN==null||$scope.fields[i].defaultDetailShowYN==0){//未选中
					$scope.editAllDetailSelect = false;
				}else{
					$scope.editDetailNum++;
				}
			}
		}
		$scope.editAllSelectYN();

		$scope.addField = function(){
			$scope.editEle.style.visibility="visible";
			//添加字段，如果字段都未选中，将领域和维度置为为选中
			var stat1 = false;
			for(var i=0;i<$scope.fields.length;i++){
				if($scope.fields[i].defaultListShowYN==1){
					$scope.selectFields[i].defaultListShowYN = 1;
					stat1 = true;
				}else{
					$scope.selectFields[i].defaultListShowYN = 0;
				}
				if($scope.fields[i].defaultDetailShowYN==1){
					$scope.selectFields[i].defaultDetailShowYN = 1;
					stat1 = true;
				}else{
					$scope.selectFields[i].defaultDetailShowYN = 0;
				}
			}
			$scope.editDimensionS.selectYN = stat1;
			
            var stat2 = false;
            for(var i=0;i<$scope.editDomainDimensionS.dimensionList.length;i++){
            	if($scope.editDomainDimensionS.dimensionList[i].selectYN){
            		stat2 = true;
            	}
            }
			$scope.editDomainDimensionS.selectYN = stat2;
            
			$scope.$dismiss();
			
		}
		$rootScope.$on("editCloseFieldList",function (event,data) {
            $scope.editCloseFieldList();
        });
		
		 //关闭字段列表页
        $scope.editCloseFieldList =  function(){
        	$scope.editEle.style.visibility="visible";
        	//如果字段都未选中，将领域和维度置为为选中
			/*var stat1 = false;
			for(var i=0;i<$scope.selectFields.length;i++){
				if($scope.selectFields[i].defaultListShowYN==1){
					stat1 = true;
				}
				if($scope.selectFields[i].defaultDetailShowYN==1){
					stat1 = true;
				}
			}
			
			$scope.editDimensionS.selectYN = stat1;
            
            var stat2 = false;
            for(var i=0;i<$scope.editDomainDimensionS.dimensionList.length;i++){
            	if($scope.editDomainDimensionS.dimensionList[i].selectYN){
            		stat2 = true;
            	}
            }
			$scope.editDomainDimensionS.selectYN = stat2; */
			$scope.$dismiss();
        }
        
         $scope.editChangeStatu = function(who,field){
        	if(who=="list"){
        		field.defaultListShowYN = field.defaultListShowYN==1 ? 0 : 1;
				$scope.editAllSelectYN();
        	}
        	if(who=="detail"){
        		field.defaultDetailShowYN = field.defaultDetailShowYN==1 ? 0 : 1;
				$scope.editAllSelectYN();
        	}
        }


		//切换全选中的状态
		$scope.editChangeAllStatu = function(who,even){
			var isChecked = even.target.checked;
			if(who == "detail"){
				$scope.editAllDetailSelect = isChecked;
				for(var i=0;i<$scope.fields.length;i++){
					if(isChecked){
						$scope.fields[i].defaultDetailShowYN = 1;//将所有的字段详情选中置为选中
					}else{
						$scope.fields[i].defaultDetailShowYN = 0;//将所有的字段详情选中置为不选中
					}
				}
				if(isChecked){
					$scope.editDetailNum = $scope.fields.length;//将所有的字段详情选中置为选中
				}else{
					$scope.editDetailNum = 0;//将所有的字段详情选中置为不选中
				}
			}
			if(who == "list"){
				$scope.editAllListSelect = isChecked;
				for(var i=0;i<$scope.fields.length;i++){
					if(isChecked){
						$scope.fields[i].defaultListShowYN = 1;//将所有的字段列表选中置为选中
					}else{
						$scope.fields[i].defaultListShowYN = 0;//将所有的字段列表选中置为不选中
					}
				}
				if(isChecked){
					$scope.editListNum= $scope.fields.length;//将所有的字段列表选中置为选中
				}else{
					$scope.editListNum = 0;//将所有的字段列表选中置为不选中
				}
			}
		}
	}
	
	
	function fieldListCtrl($rootScope,$scope){
		$scope.listNum = 0;
		$scope.detailNum = 0;
		//进入选择字段页面，判断是否全选
		$scope.allSelectYN = function(){
			$scope.allListSelect = true;
			$scope.allDetailSelect = true;
			$scope.listNum = 0;
			$scope.detailNum = 0;
			for(var i=0;i<$scope.fields.length;i++){
				if($scope.fields[i].defaultListShowYN==0){//未选中
					$scope.allListSelect = false;
				}
				if($scope.fields[i].defaultListShowYN==1){//选中，统计选中的个数
					$scope.listNum++;
				}
				if($scope.fields[i].defaultDetailShowYN==0){//未选中
					$scope.allDetailSelect = false;
				}
				if($scope.fields[i].defaultDetailShowYN==1){//选中，统计选中的个数
					$scope.detailNum++;
				}
			}

		}
		$scope.allSelectYN();

		$scope.addField = function(){
			$scope.ele.style.visibility="visible";
			//添加字段，如果字段都未选中，将领域和维度置为为选中
			var stat1 = false;
			for(var i=0;i<$scope.fields.length;i++){
				if($scope.fields[i].defaultListShowYN==1){
					stat1 = true;
				}
				if($scope.fields[i].defaultDetailShowYN==1){
					stat1 = true;
				}
			}
			$scope.dimensionS.selectYN = stat1;
			
            var stat2 = false;
            for(var i=0;i<$scope.domainDimensionS.dimensionList.length;i++){
            	if($scope.domainDimensionS.dimensionList[i].selectYN){
            		stat2 = true;
            	}
            }
			$scope.domainDimensionS.selectYN = stat2;  
			
			$scope.$dismiss();
		}
		$rootScope.$on("closeFieldList",function (event,data) {
            $scope.closeFieldList();
        });
		
		 //关闭字段列表页
        $scope.closeFieldList =  function(){
        	$scope.ele.style.visibility="visible";
        	//如果字段都未选中，将领域和维度置为为选中
			for(var i=0;i<$scope.recoverField.length;i++){
				if($scope.recoverField[i].defaultListShowYN==1){
					$scope.fields[i].defaultListShowYN=1;
				}else{
					$scope.fields[i].defaultListShowYN=0;
				}
				if($scope.recoverField[i].defaultDetailShowYN==1){
					$scope.fields[i].defaultDetailShowYN=1;
				}else{
					$scope.fields[i].defaultDetailShowYN=0;
				}
			}
			$scope.$dismiss();
        }
        //切换状态
        $scope.changeStatu = function(who,field){
        	if(who=="list"){
        		field.defaultListShowYN = field.defaultListShowYN==1 ? 0 : 1;
				$scope.allSelectYN();
        	}
        	if(who=="detail"){
        		field.defaultDetailShowYN = field.defaultDetailShowYN==1 ? 0 : 1;
				$scope.allSelectYN();
        	}
        }
		//切换所有的状态
		$scope.changeAllStatu = function(who,even){
			var isChecked = even.target.checked;
			if(who == "detail"){
				$scope.allDetailSelect = isChecked;

				for(var i=0;i<$scope.fields.length;i++){
					if(isChecked){
						$scope.fields[i].defaultDetailShowYN = 1;//将所有的字段详情选中置为选中
					}else{
						$scope.fields[i].defaultDetailShowYN = 0;//将所有的字段详情选中置为不选中
					}
				}
				//统计个数
				if(isChecked){
					//将所有的字段详情选中置为选中
					$scope.detailNum = $scope.fields.length;
				}else{
					$scope.detailNum = 0;//将所有的字段详情选中置为不选中
				}
			}
			if(who == "list"){
				$scope.allListSelect = isChecked;
				for(var i=0;i<$scope.fields.length;i++){
					if(isChecked){
						$scope.fields[i].defaultListShowYN = 1;//将所有的字段列表选中置为选中
					}else{
						$scope.fields[i].defaultListShowYN = 0;//将所有的字段列表选中置为不选中
					}
				}
				//统计个数
				if(isChecked){
					//将所有的字段详情选中置为选中
					$scope.listNum = $scope.fields.length;
				}else{
					$scope.listNum = 0;//将所有的字段详情选中置为不选中
				}
			}
		}
	}

    function customerManagerCtrl( $scope, $state ,$uibModal,toastr,$filter,$rootScope,$timeout,$interval,customerService,commonService,customerQueryCountService,adminUserService,Constant) {

		$rootScope.$on('switchSettingPar',function(event,data){
			if(data.who=='new'){
				$rootScope.$broadcast("switchSettingSon",data.templetDomain);
			}
			if(data.who=='edit'){
				$rootScope.$broadcast("editSwitchSettingSon",data.templetDomain);
			}
		})

		$scope.selSettingTemplet = function(who){
			customerService.whoOpenSettingTemplet = who;
			$uibModal.open({
				animation: true,
				templateUrl: 'app/pages/customer/manager/settingTemplet.html',
				size: "md"
			});
		}

		$scope.userSon = function (customer) {
			$state.go("customer.userSon",{params:customer});
        }


		$scope.typeCustomer = [
			{
				code:0,
				value:'启用'
			},
			{
				code:1,
				value:'停用'
			}
		];

        //日别统计
       /* $scope.dailyStatistics = dailyStatistics();
        $scope.monthStatistics = monthStatistics();*/
        //初始数据
		$scope.customerLists = function (paramss) {
            $rootScope.loading = true;
            $scope.searchCompanyName = paramss.searchCompanyName;
            $scope.searchAuthCode = paramss.searchAuthCode;
			$scope.status = paramss.status;
            var promise = customerService.getCustomerList(paramss);

            promise.then(function (data) {
                $scope.customers = data.list;
                //分页区域
                $scope.customerPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        //分页
                        var paramData = new Object();
                        paramData.currentPage=$scope.customerPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.searchCompanyName=$scope.searchCompanyName;
                        paramData.searchAuthCode=$scope.searchAuthCode;
						paramData.status=$scope.status;
						$rootScope.$emit("customerLists",paramData);
                    }
                };
				$rootScope.loading = false;
            },function (data) {
				showErrorMsg(toastr,"获取客户列表异常，请联系管理员");
				$rootScope.loading = false;
            })
        };
        
		var customerLi = $rootScope.$on("customerLists",function (event,data) {
			
            $scope.customerLists(data);
        });
		$scope.$on('$destroy',function(){//controller回收资源时执行
			customerLi();//回收广播
		});
		
        //实现搜索功能searchCustomer
         $scope.searchCustomer = function () {
         	 var paramData = new Object();
             paramData.currentPage=1;//待跳转的页面
             paramData.pageSize=15;
             paramData.searchCompanyName=$scope.searchCompanyName;
             paramData.searchAuthCode=$scope.searchAuthCode;
			 paramData.status = $scope.status;
	        $rootScope.$emit("customerLists",paramData);
         };
		//创建CustomerConfig
		$scope.creaCustomerConfig = function(){
			$scope.customerConfig = {
				trialQuantity:'',
				qpmLimit:'',
				ipWhiteList:'',
				totalSearchQuantity:'',
				useCache:1,
				useLimitWord:1,
				cacheStatus:false,
				limitWordStatus:false
			}
		}

        //跳转至新增客户页面
        $scope.addNewCustomer = function () {
            loading($rootScope, $timeout);
			$scope.findRegionAll();
			$scope.findAdminUserAll();
            //清空输入框的数据
            $scope.customer = null;
			$scope.customer = {customerArea : "0001",ownerId : $rootScope.currentAdminUserId};
			$rootScope.interfaces = null;
			$rootScope.modelLists = null;
			$scope.authCodeCustomer = null;
			$rootScope.interfaceSettingList = new Array();
			$scope.creaCustomerConfig();
            $state.go('customer.managerAddPage');
        };
        
        
        //选择接口模型
		$rootScope.selectInterfaceModel = function($event,serviceInterface){
			var isChecked = $event.target.checked;//checkbox是否被选中
            if(isChecked){
                var resultView = $uibModal.open({
	                animation: true,
	                templateUrl: 'app/pages/customer/manager/selModel.html',
	                size: "md",
	                controller:function(){
	            		$rootScope.selectInterface = JSON.parse(JSON.stringify(serviceInterface));
	            		$rootScope.serviceInterface = serviceInterface;
	                }
	            });
	        }
		}
        //获取区域信息
		$scope.findRegionAll = function() {
			var promise = customerService.findRegionAll();
			promise.then(function (data) {
				$scope.areas = data;
			}, function (data) {
			});
		}
		//获取所有的用户
		$scope.findAdminUserAll = function(){
			var promise = adminUserService.getAdminUserList();
			promise.then(function (data) {
				$scope.adminUsers = data;
			}, function (data) {
			});
		}

        //校验  客户的 trialQuantity  qpmLimit  ipWhiteList  totalSearchQuantity 字段
		$scope.checkoutFileds = function(param){
			if(param.trialQuantity!=''){
				 var trialQuantityPattern = new RegExp("^([1-9][0-9]{0,8}|-1)$");
				 if(!trialQuantityPattern.test(param.trialQuantity)){
					 showErrorMsg(toastr,"账户试用期查询条数，请填写10位以下非0数字，-1代表不限制");
					 return false;
				 }
			}
			if(param.qpmLimit!=''){
				var qpmLimitPattern = new RegExp("^([1-9][0-9]{0,8}|-1)$");
				if(!qpmLimitPattern.test(param.qpmLimit)){
					showErrorMsg(toastr,"每分钟可查询次数，请填写10位以下非0数字，-1代表不限制");
					return false;
				}
			}
			if(param.totalSearchQuantity!=''){

				var totalSearchQuantityPattern = new RegExp("^([1-9][0-9]{0,8}|-1)$");
				if(!totalSearchQuantityPattern.test(param.totalSearchQuantity)){
					showErrorMsg(toastr,"账户总查询条数，请填写10位以下非0数字，-1代表不限制");
					return false;
				}
			}
			if(param.ipWhiteList!=''){
				var ipWhiteListPattern = new RegExp("^(((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)),)*((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))$");
				if(!ipWhiteListPattern.test(param.ipWhiteList)){
					showErrorMsg(toastr,"ip白名单填写有误");
					return false;
				}
			}
			return true;
		}
		$scope.deliverValue = function(source,target){
			if(source.trialQuantity==''){
			 	target.trialQuantity = null;
			 }else{
				target.trialQuantity = source.trialQuantity;
			}
			 if(source.totalSearchQuantity==''){
			 	 target.totalSearchQuantity = null;
			 }else{
				 target.totalSearchQuantity = source.totalSearchQuantity;
			 }
			 if(source.qpmLimit==''){
			 	 target.qpmLimit = 99;
			 }else{
				 target.qpmLimit = source.qpmLimit;
			 }
			 if(source.ipWhiteList==''){
			 	 target.ipWhiteList = null;
			 }else{
				 target.ipWhiteList = source.ipWhiteList;
			 }
			 if(source.cacheStatus){
				 target.useCache = 0;
			 }else{
				 target.useCache = 1;
			 }
			 if(source.limitWordStatus){
				 target.useLimitWord = 0;
			 }else{
				 target.useLimitWord = 1;
			 }
		}
		$scope.yesOrNoArr = [
			{
				code:0,
				value:'是'},{
				code:1,
				value:'否'
		}]



		//保存客户信息 customer.companyName
        $scope.saveCustomer = function (form) {
			//校验
			var flat = $scope.checkoutFileds($scope.customerConfig);
			if(!flat){
				return;
			}
			//赋值
			$scope.deliverValue($scope.customerConfig,$scope.customer);
			var saveYN = $scope.screenSelectYN(customerService.transformDomainDimensionLists);
			if(!saveYN){
				showWarnMsg(toastr,'不能同时选择shixin和sxgg维度');
				return;
			}
			if(form.$valid){
				$rootScope.loading = true;
				var promise = customerService.saveCustomer($scope.customer,$scope.addDomainDimensionLists,$rootScope.interfaceSettingList);

            	promise.then(function(data){
            		if(data.errorCode== '0'){
            			
	            		//保存之后，将对象置为空
	            		$scope.customer = {};
	            		$scope.domainDimensionLists = $scope.restoreDomainDimensionLists;
	            		var params = new Object();
				        params.currentPage=1;
				        params.pageSize=15;
				        params.searchCompanyName=null;
	            		$scope.customerLists(params);
	            		toastr.success('新增客户成功！');
	            		$state.go('customer.manager');
	            	}else{
	            		showErrorMsg(toastr,"新增客户失败！检查填写是否合法");
	            	}
            	},function(data){
					commonService.goLoginPage(data.errorCode);
				})
			};
			$rootScope.loading = false;
			$rootScope.interfaceSettingList = new Array();
		};
        
        $scope.screenSelectYN = function(domainLists){
        	//过滤出选中的领域 维度 字段
        	if(domainLists != null){
				var saveYN = 0;
	            var aList = JSON.parse(JSON.stringify(domainLists));
	            for(var i=0;i<aList.length;i++){
	                var domain =  aList[i];
	                if(domain.selectYN){
	                    var dimensionList = domain.dimensionList;
	                    for(var j=0;j<dimensionList.length;j++){
	                        var dimension=dimensionList[j];
	                        if(dimension.selectYN){
								if(dimension.dimensionCode=='sxgg'||dimension.dimensionCode=='shixin'){
									saveYN++;
								}
	                            var fieldList = dimension.fieldList;
	                            for(var z=0;z<fieldList.length;z++){
	                                var field = fieldList[z];
	                                if(!field.defaultListShowYN&&!field.defaultDetailShowYN){
	                                    aList[i].dimensionList[j].fieldList.splice(z, 1);
	                                    z--;
	                                }
	                            }
	                        }else{
	                            aList[i].dimensionList.splice(j, 1);
	                            j--;
	                        }
	                    }
	                }else{
	                    aList.splice(i, 1);
	                    i--;
	                }
	            }
	            $scope.addDomainDimensionLists = aList;
				if(saveYN>1){
					return false; //同时选中sxgg和shixin 返回false
				}
				return true;
	        }
        }
        
        //激活客户
		$scope.activationCustomer = function(customer){
			customerService.activationCustomerStatus(customer).then(function(data){
				showSuccMsg(toastr,"激活客户成功!!");
				$state.go('customer.manager');
			},function(data){
				commonService.goLoginPage(data.errorCode);
			});
		}
		$scope.lookCustomerAuthcode = function(customer){
			$uibModal.open({
				animation: true,
				templateUrl: 'app/pages/customer/manager/authCodeDetil.html',
				controller:function(){
					$rootScope.authCodeCustomer = customer;
				}

			});
		}
        
        
        //后退到客户列表页面
        $scope.backCustomer = function () {
			$rootScope.loading=true;
            $state.go('customer.manager');
        };
		//跳转到客户报表
		$scope.reportCount = function(customer){
			$rootScope.reportCustomer = customer;
			window.localStorage.setItem("reportCustomer",JSON.stringify(customer));
			$state.go('customer.managerReport');
		}

         //跳转至修改页面
        $scope.updateCustomer = function (customer) {
			$rootScope.loading=true;
            //清空之前的数据
			$rootScope.interfaces = null;
			$rootScope.modelLists = null;
			$rootScope.interfaceSettingList = new Array();
			$scope.findRegionAll();
			$scope.findAdminUserAll();
			$scope.authCodeCustomer = customer;
			$scope.creaCustomerConfig();
			if(customer.trialQuantity != null){
				$scope.customerConfig.trialQuantity = customer.trialQuantity;
			}
			if(customer.totalSearchQuantity!=null){
				$scope.customerConfig.totalSearchQuantity = customer.totalSearchQuantity;
			}
			if(customer.ipWhiteList!=null){
				$scope.customerConfig.ipWhiteList = customer.ipWhiteList;
			}
			if(customer.qpmLimit!=null){
				$scope.customerConfig.qpmLimit = customer.qpmLimit;
			}
			if(customer.useCache==0){
				$scope.customerConfig.cacheStatus =true;
			}else{
				$scope.customerConfig.cacheStatus =false;
			}
			if(customer.useLimitWord==0){
				$scope.customerConfig.limitWordStatus =true;
			}else{
				$scope.customerConfig.limitWordStatus =false;
			}

            customer.trialBeginDate = $filter("date")(customer.trialBeginDate, "yyyy-MM-dd");
            customer.trialEndDate = $filter("date")(customer.trialEndDate, "yyyy-MM-dd");
            customer.contractBeginDate = $filter("date")(customer.contractBeginDate, "yyyy-MM-dd");
            customer.contractEndDate = $filter("date")(customer.contractEndDate, "yyyy-MM-dd");
			customer.createDate = $filter("date")(customer.createDate, "yyyy-MM-dd ");
			customer.lastModifyDate = $filter("date")(customer.lastModifyDate, "yyyy-MM-dd");
            customer.contactPhone = parseInt(customer.contactPhone);

            var promise = customerService.findCustomerDetail(customer.customerId);
			promise.then(function (data){
            	$scope.editDomainDimensionLists = data.editDomains;
				customerService.transformDomainDimensionLists = $scope.editDomainDimensionLists;
				$rootScope.interfaceSettingList = data.editInterfaces;
				$rootScope.loading=false;
			},function(data){
				commonService.goLoginPage(data.errorCode);
			})
			$state.go('customer.managerEdit');
            $scope.customer = customer;
        };
        //修改接口
        $scope.editInterface = function (interfaces){
        	$uibModal.open({
				animation: true,
				templateUrl: 'app/pages/customer/manager/editInterfaceList.html',
				size: "md",
				controller:function(){
					$rootScope.editInterfaces = interfaces;
				}
        	});
        }
		//文档预览
		/*$scope.previewDocument = function(){

			$('#customerAPIDocument').media({width:890, height:550,autoplay:true,bgColor: '#e7e7e7'});
		}*/
       
        //保存修改客户
        $scope.saveEditCustomer = function(){
			//校验customerConfig
			var flag = $scope.checkoutFileds($scope.customerConfig);
			if(!flag){
				return;
			}
			//将CustomerConfig赋值到Customer
			$scope.deliverValue($scope.customerConfig,$scope.customer);
        	$rootScope.loading=true;
			var saveYN = $scope.screenSelectYN(customerService.transformDomainDimensionLists);
			if(!saveYN){
				showWarnMsg(toastr,'不能同时选择shixin和sxgg维度');
				$rootScope.loading=false;
				return;
			}
        	customerService.saveEditCustomer($scope.customer,$scope.addDomainDimensionLists,$rootScope.interfaceSettingList).then(function (data) {
        	    if(data.errorCode== '0'){
                    showSuccMsg(toastr,"修改客户成功！");
                    var params = new Object();
                    params.currentPage=$scope.customerPaginationConf.currentPage;
                    params.pageSize=15;
                    params.name=$scope.searchCompanyName;
                    $rootScope.$emit("customerLists",params);
                    $state.go('customer.manager');
                }else{
                    showErrorMsg(toastr,"修改客户失败，请重新尝试！");
                }
                $rootScope.loading=false;
            },function (data) {
               commonService.goLoginPage(data.errorCode);
            });
        };
        /*-----------------------------------------------接口选择 begin-----------------------------------------*/
		$rootScope.$on("interfaceModelList",function(event,data){
			$scope.interfaceModelList(data);
		});
		//获取所有的接口和模型
		$scope.interfaceModelList = function(params){
			$rootScope.loading = true;
			if($rootScope.modelLists == null){
				customerService.modelList(params).then(function (data) {
					$rootScope.modelLists = data.modelList;
					$rootScope.pureModelLists = data.modelList;

				},function (data) {
					commonService.goLoginPage(data.errorCode);
				});
			}
			customerService.selInterfaceLists("").then(function (data) {
				$rootScope.interfaces = data.interfaceList;
				$rootScope.pureInterfaces = data.interfaceList;
			}, function (data) {
				commonService.goLoginPage(data.errorCode);
			});
			$rootScope.loading = false;
		}

		//创建封装客户配置的接口信息
		$scope.createObject = function(){
			var interfaceSetting = {
				interfaceName:'',
				interfaceId:'',
				modelId:'',
				modelName:'',
				customQuery:'common',
				customRange:''
			}
			return interfaceSetting;
		}

		//存放配置的接口
		$rootScope.interfaceSettingList = new Array();

		//添加interfaceSetting
		$scope.addInterfaceSetting = function(){
			$uibModal.open({
				animation: true,
				templateUrl: 'app/pages/customer/manager/interfaceSetting.html',
				size: "md",
				controller:function(){
					$rootScope.interfaceSetting = $scope.createObject();
					$rootScope.interfaceSetting.customRange = 20;
					$rootScope.createOrEdit = -1;
				}
			});
		}

		//删除selInterfacel页的列表中配置的接口信息
		$scope.removeInterfaceModel = function(index){
			$rootScope.interfaceSettingList.splice(index,1);
		}
		//修改selInterfacel页的列表中配置的接口信息
		$scope.editInterfaceModel = function(index){
			$uibModal.open({
				animation: true,
				templateUrl: 'app/pages/customer/manager/interfaceSetting.html',
				size: "md",
				controller:function(){
					$rootScope.interfaceSetting = JSON.parse(JSON.stringify( $rootScope.interfaceSettingList[index]));
					$rootScope.createOrEdit = index;
				}
			});
		}

		/*-----------------------------------------------接口选择 end-----------------------------------------*/
		$rootScope.$on("modelList",function(event,data){
			$scope.modelList(data);
		})
		$rootScope.modelList = function (params){
			$rootScope.loading = true;
			var promise = customerService.modelList(params);
			promise.then(function (data) {
				$rootScope.interfaceModelList = data.modelList;
				$rootScope.loading = false;
			},function (data) {
				commonService.goLoginPage(data.errorCode);
			})
		}
        //删除用户
        $scope.removeCustomer = function (index,customer) {
            // //为了安全,删除用户需要进行弹框确认,删除任务提示
            var modalInstance = $uibModal.open({
                animation: true,
                size: 'sm',
                templateUrl: 'app/pages/common/confimDelete.html',
                resolve: {
                    index: function () {
                        return index;
                    }
                }
            });
                 
           modalInstance.result.then(function (result) {
           	
            }, function (reason) {
                if('yes' == reason){
                    //接收到删除的指令
                    $rootScope.loading=true;
					//格式化时间
					customer.trialBeginDate = $filter("date")(customer.trialBeginDate, "yyyy-MM-dd");
					customer.trialEndDate = $filter("date")(customer.trialEndDate, "yyyy-MM-dd");
					customer.contractBeginDate = $filter("date")(customer.contractBeginDate, "yyyy-MM-dd");
					customer.contractEndDate = $filter("date")(customer.contractEndDate, "yyyy-MM-dd");
					customer.createDate = $filter("date")(customer.createDate, "yyyy-MM-dd HH:mm:ss");
					customer.lastModifyDate = $filter("date")(customer.lastModifyDate, "yyyy-MM-dd HH:mm:ss");
					customer.contactPhone = parseInt(customer.contactPhone);
                    var promise = customerService.deleteCustomerById(customer);
                    promise.then(function (data) {
                        if(data.errorCode == '0'){
                            showSuccMsg(toastr,"删除数据成功!!");
                            var params = new Object();
                            params.currentPage=$scope.customerPaginationConf.currentPage;
                            params.pageSize=15;
							params.status = $scope.status;
                            params.name=$scope.searchCompanyName;
                            $rootScope.$emit("customerLists",params);
                        }else{
                            showWarnMsg(toastr,"删除数据失败，请重新操作!!")
                        }
                        $rootScope.loading=false;
                    },function (data) {
						commonService.goLoginPage(data.errorCode);
                    });
                    // loading($rootScope,$timeout);
                }
                //console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });
        }
		
		//获取接口模型列表
		$rootScope.$on("modelList",function(event,data){
			$scope.modelList(data);
		})
		$rootScope.modelList = function (params){
			$rootScope.loading = true;
			var promise = customerService.modelList(params);
			promise.then(function (data) {
                $rootScope.interfaceModelList = data.modelList;
                $rootScope.loading = false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })
		}
		$rootScope.$on("domainDimensionList",function(event,data){
			$scope.domainDimensionList(data);
		})
		
		$rootScope.domainDimensionList = function (params){
			var promise = customerService.domainDimensionLists(params);
			
			promise.then(function (data) {
				/*$scope.useSettingTemplet = false;
				$scope.customerTempletId = 0;*/
				$scope.domainDimensionLists = data.domainList;
				customerService.transformDomainDimensionLists = $scope.domainDimensionLists;
				$scope.restoreDomainDimensionLists = JSON.parse(JSON.stringify($scope.domainDimensionLists))
                $rootScope.loading = false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })
		}

		//删除选中的接口
		$scope.removeserviceInterface = function (index,interfaceId){
			$scope.customerInterfaces.splice(index, 1);
			for(var i=0;i<$scope.interfaces.length;i++){
				if($scope.interfaces[i].interfaceId == interfaceId){
					$scope.interfaces[i].selectYN = false;
				}
			}
		}
		//编辑删除选中的接口
		$scope.editRemoveserviceInterface = function (index,interfaceId){
			$scope.editCustomerInterfaces.splice(index, 1);
			for(var i=0;i<$scope.editInterfaces.length;i++){
				if($scope.editInterfaces[i].interfaceId == interfaceId){
					$scope.editInterfaces[i].selectYN = false;
				}
			}
		}
		//添加选中的接口信息到selInterface
		$scope.customerInterfaceLists = function (data){
			var checkedInterfaces = new Array();
			for(var i=0;i<data.length;i++){
				if(data[i].selectYN){
					checkedInterfaces.push(data[i])
				}
			}
			$scope.customerInterfaces = checkedInterfaces;
		}
		$rootScope.$on("customerInterfaceLists",function(event,data){
			$scope.customerInterfaceLists(data);
		})
		
		
		//编辑添加选中的接口信息到editSelInterface
		$scope.editCustomerInterfaceLists = function (data){
			var checkedInterfaces = new Array();
			for(var i=0;i<data.length;i++){
				if(data[i].selectYN){
					checkedInterfaces.push(data[i])
				}
			}
			$scope.editCustomerInterfaces = checkedInterfaces;
		}
		$rootScope.$on("editCustomerInterfaceLists",function(event,data){
			$scope.editCustomerInterfaceLists(data);
		})

		//选择字段
        $scope.checkField=function ($event,fieldList,domainDimension,dimensionS) {
//          var isChecked = $event.target.checked;//checkbox是否被选中
            $event.target.style.visibility="hidden";
//          if(isChecked){
//          	domainDimension.selectYN = isChecked; 
                var resultView = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/pages/customer/manager/selectField.html',
                    size: "md",
                    controller:function(){
                		$rootScope.fields = fieldList;
                		$rootScope.dimensionS = dimensionS;
                		$rootScope.domainDimensionS = domainDimension;
                		$rootScope.ele = $event.target;
                		$rootScope.recoverField = JSON.parse(JSON.stringify(fieldList));//用于关闭字段选择列表时初始化字段
                    }
                });
                
	            resultView.result.then(function (result) {
	           	
	            }, function (reason) {
	            	//当点击空白位置的时候即为关闭模态
	            	if('backdrop click'==reason){
	            		$rootScope.$emit("closeFieldList",null);
	            	}
	            	
	            });
        };
        //领域维度的联动效果
        $scope.linkage=function(domainDimension,dimensionS){
        	var isChecked = false;
    		for(var i=0;i<(domainDimension.dimensionList).length;i++){
            	if((domainDimension.dimensionList)[i].selectYN){
            		isChecked = true;
            	}
            }
    		domainDimension.selectYN = isChecked;
        }


        $scope.editCheckField=function ($event,fieldList,domainDimension,dimensionS) {
//          var isChecked = $event.target.checked;//checkbox是否被选中
			$event.target.style.visibility="hidden";
                var resultView = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/pages/customer/manager/editSelectField.html',
                    size: "md",
                    controller:function(){
                		$rootScope.fields = JSON.parse(JSON.stringify(fieldList));
                		$rootScope.selectFields = fieldList;
                		$rootScope.editDimensionS = dimensionS;
                		$rootScope.editDomainDimensionS = domainDimension;
                		$rootScope.editEle = $event.target;
                    }
                });
	            resultView.result.then(function (result) {
	           	
	            }, function (reason) {
	            	//当点击空白位置的时候即为关闭模态
	            	if('backdrop click'==reason){
	            		$rootScope.$emit("editCloseFieldList",null);
	            	}
	            	
	            });
        };

        //新增校验客户信息
        $scope.checkCustomer = function(checkType,contant,event){
        	if(contant != null){
	        	customerService.checkCustomer($scope.customer,checkType).then(function(data){
	        		if(data.errorCode== '0'){
		        		if(data.result== '0'){
		        			if("companyName" == checkType){
		        				$scope.checkCustomerCompanyNameResult = false;
		        			}else if("contactTel" == checkType){
		        				$scope.checkCustomerContactTelResult = false;
		        			}else if("contactEmail" == checkType){
		        				$scope.checkCustomerContactEmailResult = false;
		        			}else if("userName" == checkType){
		        				$scope.checkCustomerUserNameResult = false;
		        			}else if("contactPhone" == checkType){
		        				$scope.checkCustomerContactPhoneResult = false;
		        			}
		        		}else{
		        			if("companyName" == checkType){
		        				$scope.checkCustomerCompanyNameResult = true;
		        			}else if("contactTel" == checkType){
		        				$scope.checkCustomerContactTelResult = true;
		        			}else if("contactEmail" == checkType){
		        				$scope.checkCustomerContactEmailResult = true;
		        			}else if("userName" == checkType){
		        				$scope.checkCustomerUserNameResult = true;
		        			}else if("contactPhone" == checkType){
		        				$scope.checkCustomerContactPhoneResult = true;
		        			}

							$(event.target).css("border-color","#ed7878");
							$(event.target).parent().addClass("has-error");
							$(event.target).parent().removeClass("has-success");
							$(event.target).next().removeClass("ion-checkmark-circled");
							$(event.target).next().addClass("ion-android-cancel");
		        		}
		        	}
	        	},function(data){
					commonService.goLoginPage(data.errorCode);
	        	});
	        }
        }
        //修改校验客户信息
         $scope.editCheckCustomer = function(checkType,contant,event){
        	if(contant != null){
	        	customerService.checkCustomer($scope.customer,checkType).then(function(data){
	        		if(data.errorCode== '0'){
		        		if(data.result== '0'){
		        			if("companyName" == checkType){
		        				$scope.editCheckCustomerCompanyNameResult = false;
		        			}else if("contactTel" == checkType){
		        				$scope.editCheckCustomerContactTelResult = false;
		        			}else if("contactEmail" == checkType){
		        				$scope.editCheckCustomerContactEmailResult = false;
		        			}else if("userName" == checkType){
		        				$scope.editCheckCustomerUserNameResult = false;
		        			}else if("contactPhone" == checkType){
		        				$scope.editCheckCustomerContactPhoneResult = false;
		        			}
		        		}else{
		        			if("companyName" == checkType){
		        				$scope.editCheckCustomerCompanyNameResult = true;
		        			}else if("contactTel" == checkType){
		        				$scope.editCheckCustomerContactTelResult = true;
		        			}else if("contactEmail" == checkType){
		        				$scope.editCheckCustomerContactEmailResult = true;
		        			}else if("userName" == checkType){
		        				$scope.editCheckCustomerUserNameResult = true;
		        			}else if("contactPhone" == checkType){
		        				$scope.editCheckCustomerContactPhoneResult = true;
		        			}
							$(event.target).css("border-color","#ed7878");
							$(event.target).parent().addClass("has-error");
							$(event.target).parent().removeClass("has-success");
							$(event.target).next().removeClass("ion-checkmark-circled");
							$(event.target).next().addClass("ion-android-cancel");
		        		}
		        	}
	        	},function(data){
					commonService.goLoginPage(data.errorCode);
	        	});
	        }
        }

		//校验日期的合理性
		$scope.clearOtherYN = function(who){
			if(who=='trialBeginDate'){
				if($scope.customer.trialBeginDate!=null&&$scope.customer.trialBeginDate!=''&&$scope.customer.trialEndDate!=null&&$scope.customer.trialEndDate!=''){
					if($scope.customer.trialBeginDate>$scope.customer.trialEndDate){
						$scope.customer.trialEndDate = '';
					}
				}
			}
			if(who=='trialEndDate'){
				if($scope.customer.trialBeginDate!=null&&$scope.customer.trialBeginDate!=''&&$scope.customer.trialEndDate!=null&&$scope.customer.trialEndDate!='') {
					if ($scope.customer.trialBeginDate > $scope.customer.trialEndDate) {
						$scope.customer.trialBeginDate = '';
					}
				}
			}
			if(who=='contractBeginDate'){
				if($scope.customer.contractBeginDate!=null&&$scope.customer.contractBeginDate!=''&&$scope.customer.contractEndDate!=null&&$scope.customer.contractEndDate!='') {
					if ($scope.customer.contractBeginDate > $scope.customer.contractEndDate) {
						$scope.customer.contractEndDate = '';
					}
				}
			}
			if(who=='contractEndDate'){
				if($scope.customer.contractBeginDate!=null&&$scope.customer.contractBeginDate!=''&&$scope.customer.contractEndDate!=null&&$scope.customer.contractEndDate!='') {
					if ($scope.customer.contractBeginDate > $scope.customer.contractEndDate) {
						$scope.customer.contractBeginDate = '';
					}
				}
			}
		}
		
        $scope.canClick = false;
        $scope.description = "获取验证码";
        var second = 59;
        var timerHandler;
        $scope.sendIdentifyingCode = function () {
            var phoneNum = $("#phoneNum").val();
            toastr.success("向手机号[" + phoneNum + "]发送短信验证码成功");
            console.log("输入手机号为:" + phoneNum);
            $scope.description = second + "s后重发";
            $scope.canClick = true;
            timerHandler = $interval(function () {
                if (second <= 0) {
                    $interval.cancel(timerHandler);
                    second = 59;
                    $scope.description = "获取验证码";
                    $scope.canClick = false;
                } else {
                    $scope.description = second + "s后重发";
                    second--;
                    $scope.canClick = true;
                }
            }, 1000)
        };


        //客户列表
        var id=100;
        $scope.addCustomerData = function (customer) {
            //日期转成字符串 $filter('date')(time, 'HH:mm');
            var expirationEndDateStr = $filter('date')(customer.expirationEndDate, "yyyy-MM-dd");
            var expirationStartDateStr = $filter('date')(customer.expirationStartDate, "yyyy-MM-dd");
            var trialEndDateStr = $filter('date')(customer.trialEndDate, "yyyy-MM-dd");
            var trialStartDateStr = $filter('date')(customer.trialStartDate, "yyyy-MM-dd");
            var newCustomer = {
                id:id++,
                companyName: customer.companyName,
                email: customer.email,
                contactPerson: customer.contactPerson,
                contactNumber: customer.contactNumber,
                totalSearchNum: customer.totalSearchNum,
                ipAddress: customer.ipAddress,
                phoneNum: customer.phoneNum,
                trialStartDate: trialStartDateStr,
                trialEndDate: trialEndDateStr,
                expirationStartDate: expirationStartDateStr,
                expirationEndDate: expirationEndDateStr,
                loginDate: ""
            };
            return newCustomer;
        };


		var customerDailyCountListss = $rootScope.$on("customerDailyCountListss",function(event,data){
			$scope.customerDailyCountList(data);
		})
		$scope.$on('$destroy',function(){
			customerDailyCountListss();
		})
		$scope.customerDailyCountList = function(params){
			$rootScope.loading = true;
			$rootScope.customerDailyBeginDate = params.beginDate;
			$rootScope.customerDailyEndDate = params.endDate;
			customerService.dailyCountList(params).then(function(data){
				$rootScope.dailyCountListData = data.list;
				//分页区域
				$scope.customerDailyCountPaginationConf = {
					currentPage: data.currentPage,              //当前所在的页（…默认第1页）
					totalItems: data.totalCount,                //总共有多少条记录
					itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
					pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
					perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
					onChange: function(){
						//分页
						var paramData = new Object();
						paramData.currentPage=$scope.customerDailyCountPaginationConf.currentPage;//待跳转的页面
						paramData.pageSize=15;
						paramData.customerId=$rootScope.reportCustomer.customerId;
						paramData.beginDate = $scope.customerDailyBeginDate;
						paramData.endDate = $scope.customerDailyEndDate;
						$rootScope.$emit("customerDailyCountListss",paramData);
					}
				};
				$rootScope.loading = false;
			},function(data){
				commonService.goLoginPage(data.errorCode)
			})
		}

		var customerMonthCountListss = $rootScope.$on("customerMonthCountListss",function(event,data){
			$scope.customerMonthCountList(data);
		})
		$scope.$on('$destroy',function(){
			customerMonthCountListss();
		})
		$scope.customerMonthCountList = function(params){
			$rootScope.loading = true;
			$rootScope.customerMonthBeginDate = params.beginDate;
			$rootScope.customerMonthEndDate = params.endDate;
			customerService.monthCountList(params).then(function(data){
				$rootScope.monthCountListData = data.list;
				//分页区域
				$scope.customerMonthCountPaginationConf = {
					currentPage: data.currentPage,              //当前所在的页（…默认第1页）
					totalItems: data.totalCount,                //总共有多少条记录
					itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
					pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
					perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
					onChange: function(){
						//分页
						var paramData = new Object();
						paramData.currentPage=$scope.customerMonthCountPaginationConf.currentPage;//待跳转的页面
						paramData.pageSize=15;
						paramData.customerId=$rootScope.reportCustomer.customerId;
						paramData.beginDate = $scope.customerMonthBeginDate;
						paramData.endDate = $scope.customerMonthEndDate;
						$rootScope.$emit("customerMonthCountListss",paramData);
					}
				};
				$rootScope.loading = false;
			},function(data){
				commonService.goLoginPage(data.errorCode)
			})
		}
		var customerYearCountListss = $rootScope.$on("customerYearCountListss",function(event,data){
			$scope.customerYearCountList(data);
		})
		$scope.$on('$destroy',function(){
			customerYearCountListss();
		})
		$scope.customerYearCountList = function(params){
			$rootScope.loading = true;
			$rootScope.customerYearBeginDate = params.beginDate;
			$rootScope.customerYearEndDate = params.endDate;
			customerService.yearCountList(params).then(function(data){
				$rootScope.yearCountListData = data.list;
				//分页区域
				$scope.customerYearCountPaginationConf = {
					currentPage: data.currentPage,              //当前所在的页（…默认第1页）
					totalItems: data.totalCount,                //总共有多少条记录
					itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
					pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
					perPageOptions: [10, 20, 30, 40, 50],
					onChange: function(){
						//分页
						var paramData = new Object();
						paramData.currentPage=$scope.customerYearCountPaginationConf.currentPage;//待跳转的页面
						paramData.pageSize=15;
						paramData.customerId=$rootScope.reportCustomer.customerId;
						paramData.beginDate = $scope.customerYearBeginDate;
						paramData.endDate = $scope.customerYearEndDate;
						$rootScope.$emit("customerYearCountListss",paramData);
					}
				};
				$rootScope.loading = false;
			},function(data){
				commonService.goLoginPage(data.errorCode)
			})
		}
		/*---------------------------------------报表 begin ----------------------------------------------*/
		//日别报表
		var dailyCountL = $rootScope.$on("dailyCountListss" ,function(event,data){
			$scope.dailyCountList(data);
		})
		$scope.$on('$destroy',function(){//controller回收资源时执行
			dailyCountL();//回收广播
		});
		$scope.dailyCountList = function(params){
			$rootScope.loading = true;
			$rootScope.searchParam = params.searchParam;
			$rootScope.beginDate = params.beginDate;
			$rootScope.endDate = params.endDate;
			customerQueryCountService.dailyCountList(params).then(function(data){
				$rootScope.customerQueryCounts = data.list;
				//分页区域
				$scope.dailyCountPaginationConf = {
					currentPage: data.currentPage,              //当前所在的页（…默认第1页）
					totalItems: data.totalCount,                //总共有多少条记录
					itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
					pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
					perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
					onChange: function(){
						//分页
						var paramData = new Object();
						paramData.currentPage=$scope.dailyCountPaginationConf.currentPage;//待跳转的页面
						paramData.pageSize=15;
						paramData.searchParam=$scope.searchParam;
						paramData.beginDate = $scope.beginDate;
						paramData.endDate = $scope.endDate;
						$rootScope.$emit("dailyCountListss",paramData);
					}
				};
				$rootScope.loading = false;
			},function(data){
				commonService.goLoginPage(data.errorCode)
			})
		}
		//月别报表
		var monthCountL = $rootScope.$on("monthCountListss" ,function(event,data){
			$scope.monthCountList(data);
		})
		$scope.$on('$destroy',function(){//controller回收资源时执行l
			monthCountL();//回收广播
		});
		$scope.monthCountList = function(params){
			$rootScope.loading = true;
			$rootScope.monthSearchParam = params.searchParam;
			$rootScope.monthBeginDate = params.beginDate;
			$rootScope.monthEndDate = params.endDate;
			customerQueryCountService.monthCountList(params).then(function(data){
				$rootScope.customerQueryMonthCounts = data.list;
				//分页区域
				$scope.monthCountPaginationConf = {
					currentPage: data.currentPage,              //当前所在的页（…默认第1页）
					totalItems: data.totalCount,                //总共有多少条记录
					itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
					pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
					perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
					onChange: function(){
						//分页
						var paramData = new Object();
						paramData.currentPage=$scope.monthCountPaginationConf.currentPage;//待跳转的页面
						paramData.pageSize=15;
						paramData.searchParam=$scope.monthSearchParam;
						paramData.beginDate = $scope.monthBeginDate;
						paramData.endDate = $scope.monthEndDate;
						$rootScope.$emit("monthCountListss",paramData);
					}
				};
				$rootScope.loading = false;
			},function(data){
				commonService.goLoginPage(data.errorCode)
			})
		}
		/*---------------------------------------报表 end ----------------------------------------------*/

    }

    function bankManagerCtrl($scope) {
        $scope.bank = {};
        $scope.bank.riskQeury = false;
        $scope.bank.riskReport = false;
        $scope.bank.monitor = false;
        $scope.bank.self = false;

        $scope.reportLimitList = [{reportName:"涉诉一眼清报告",reportType:"ssyyq"},{reportName:"关联关系报告",reportType:"glgx"},{reportName:"企业风险报告",reportType:"qyfx"}];


        $scope.uploadFile = function () {
            document.getElementById('uploadFile').click();
        }
        $scope.getFile = function () {
            var file = document.getElementById("uploadFile").files[0];
            if(file != 'undefined'){
                // console.log(file);
                document.getElementById('logoName').value = file.name;
            }
        };

    }
    function riskReportCtrl($scope,$uibModal) {

        $scope.userReportList = [{reportId: 1,reportName:"法海涉诉舆情报告",reportLimit:"测试条件1",reportStyle:"测试风格1"},
            {reportId: 2,reportName:"法海风控涉诉一眼清报告",reportLimit:"测试条件2",reportStyle:"测试风格2"},
            {reportId: 3,reportName:"涉诉一眼清及企业关联关系报告",reportLimit:"测试条件3",reportStyle:"测试风格3"}
        ]


        //新增报告
		$scope.chooseReport = function(){
			var params = new Object();
            $scope.openReport(params);
		}
        //修改报告
		$scope.editUserReport = function (userReport) {
            var params = new Object();
            params.userReport = userReport;
            $scope.openReport(params);
        }

        //模态窗口，新增报告，修改报告用一个
        $scope.openReport = function (params) {
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/customer/manager/riskReport/openReport.html',
                size: "md",
                controller:function($scope,params,toastr){
                    $scope.report = {};
                    $scope.report.reportId = 1;
                    var type = 1;
                    if(params != null && params != ""){
                        type = 2;
                        $scope.report = params.userReport;
                    }
                    $scope.report.typeList = [
                        {reportName: '法海涉诉舆情报告', reportId: 1},
                        {reportName: '法海风控涉诉一眼清报告', reportId: 2},
                        {reportName: '涉诉一眼清及企业关联关系报告', reportId: 3}
                    ];

					$scope.saveUserReport = function () {
						if(type == 1){
                            showSuccMsg(toastr,"新增报告成功");
                        }else if(type ==2){
                            showSuccMsg(toastr,"修改报告成功");
						}
                        $scope.$dismiss();
                    }

                },
                resolve: {
                    params: function() {
                        return params;
                    }
                }
            });
        }

        //删除报告
		$scope.removeUserReport = function (index, userReport) {

        }
    }

    function userSonInfoCtrl($scope,$state,$uibModal,$timeout) {
        $scope.typeList=[{code:0, status:'启用'},{code:1, status:'停用'}];
        $scope.status = 0;
        $scope.userSonList = [{userId:"001",userName:"测试用户1",companyName:"北京鼎泰智源科技有限公司",userStatus:"0"},
            {userId:"002",userName:"测试用户2",companyName:"北京鼎泰智源科技有限公司",userStatus:"0"},
            {userId:"003",userName:"测试用户3",companyName:"北京鼎泰智源科技有限公司",userStatus:"1"},
            {userId:"004",userName:"测试用户4",companyName:"北京鼎泰智源科技有限公司",userStatus:"1"}];

        //查询
        $scope.searchUserSon = function () {

        }
        //修改
		$scope.updateUser = function (user) {
			$state.go("customer.updateUser",{params:user});
        }
        //删除
        $scope.delUserSon = function (index,user) {
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
        }

        //tree

        $scope.ignoreChanges = false;
        var newId = 0;
        $scope.ignoreChanges = false;
        $scope.newNode = {};

        $scope.basicConfig = {
            core: {
                multiple: false,
                check_callback: true,
                worker: true
            },
            'types': {
                'folder': {
                    'icon': 'ion-ios-folder'
                },
                'default': {
                    'icon': 'ion-document-text'
                }
            },
            'plugins': ['types'],
            'version': 1
        };

        $scope.dragConfig = {
            'core': {
                'check_callback': true,
                'themes': {
                    'responsive': false
                }
            },
            'types': {
                'folder': {
                    'icon': 'ion-ios-folder'
                },
                'default': {
                    'icon': 'ion-document-text'
                }
            },
            "plugins": ["dnd", 'types']
        };

        $scope.addNewNode = function () {
            $scope.ignoreChanges = true;
            var selected = this.basicTree.jstree(true).get_selected()[0];
            if (selected)
                $scope.treeData.push({
                    id: (newId++).toString(),
                    parent: selected,
                    text: "New node " + newId,
                    state: {opened: true}
                });
            $scope.basicConfig.version++;
        };


        $scope.refresh = function () {
            $scope.ignoreChanges = true;
            newId = 0;
            $scope.treeData = getDefaultData();
            $scope.basicConfig.version++;
        };

        $scope.expand = function () {
            $scope.ignoreChanges = true;
            $scope.treeData.forEach(function (n) {
                n.state.opened = true;
            });
            $scope.basicConfig.version++;
        };

        $scope.collapse = function () {
            $scope.ignoreChanges = true;
            $scope.treeData.forEach(function (n) {
                n.state.opened = false;
            });
            $scope.basicConfig.version++;
        };

        $scope.readyCB = function() {
            $timeout(function() {
                $scope.ignoreChanges = false;
            });
        };


        $scope.applyModelChanges = function() {
            return !$scope.ignoreChanges;
        };

        $scope.treeData = getDefaultData();
        $scope.dragData = [
            {
                "id": "nd1",
                "parent": "#",
                "type": "folder",
                "text": "Node 1",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd2",
                "parent": "#",
                "type": "folder",
                "text": "Node 2",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd3",
                "parent": "#",
                "type": "folder",
                "text": "Node 3",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd4",
                "parent": "#",
                "type": "folder",
                "text": "Node 4",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd5",
                "parent": "nd1",
                "text": "Node 1.1",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd6",
                "parent": "nd1",
                "text": "Node 1.2",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd7",
                "parent": "nd1",
                "text": "Node 1.3",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd8",
                "parent": "nd2",
                "text": "Node 2.1",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd9",
                "parent": "nd2",
                "text": "Node 2.2",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd10",
                "parent": "nd2",
                "text": "Node 2.3",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd11",
                "parent": "nd3",
                "text": "Node 3.1",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd12",
                "parent": "nd3",
                "text": "Node 3.2",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd13",
                "parent": "nd3",
                "text": "Node 3.3",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd14",
                "parent": "nd4",
                "text": "Node 4.1",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd15",
                "parent": "nd4",
                "text": "Node 4.2",
                "state": {
                    "opened": true
                }
            },
            {
                "id": "nd16",
                "parent": "nd4",
                "text": "Node 4.3",
                "state": {
                    "opened": true
                }
            }
        ];

        function getDefaultData() {
            return [
                {
                    "id": "n1",
                    "parent": "#",
                    "type": "folder",
                    "text": "Node 1",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n2",
                    "parent": "#",
                    "type": "folder",
                    "text": "Node 2",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n3",
                    "parent": "#",
                    "type": "folder",
                    "text": "Node 3",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n5",
                    "parent": "n1",
                    "text": "Node 1.1",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n6",
                    "parent": "n1",
                    "text": "Node 1.2",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n7",
                    "parent": "n1",
                    "text": "Node 1.3",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n8",
                    "parent": "n1",
                    "text": "Node 1.4",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n9",
                    "parent": "n2",
                    "text": "Node 2.1",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n10",
                    "parent": "n2",
                    "text": "Node 2.2 (Custom icon)",
                    "icon": "ion-help-buoy",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n12",
                    "parent": "n3",
                    "text": "Node 3.1",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n13",
                    "parent": "n3",
                    "type": "folder",
                    "text": "Node 3.2",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n14",
                    "parent": "n13",
                    "text": "Node 3.2.1",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n15",
                    "parent": "n13",
                    "text": "Node 3.2.2",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n16",
                    "parent": "n3",
                    "text": "Node 3.3",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n17",
                    "parent": "n3",
                    "text": "Node 3.4",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n18",
                    "parent": "n3",
                    "text": "Node 3.5",
                    "state": {
                        "opened": true
                    }
                },
                {
                    "id": "n19",
                    "parent": "n3",
                    "text": "Node 3.6",
                    "state": {
                        "opened": true
                    }
                }
            ]
        }



    }

    function userSonUpdateCtrl($scope,$stateParams) {
		$scope.userSon = $stateParams.params;
		$scope.saveUserSon = function (form1) {
			alert(24);
        }
    }

})();