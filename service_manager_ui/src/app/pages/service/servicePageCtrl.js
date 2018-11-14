(function () {
    'use strict';

    angular.module("BlurAdmin.pages.service")
        .controller("mainCtrl",mainCtrl)
        .controller("interfaceManagerCtrl",interfaceManagerCtrl)
        .controller("fieldManagerCtrl",fieldManagerCtrl)
        .controller("domainManagerCtrl",domainManagerCtrl)
        .controller("dimensionManagerCtrl",dimensionManagerCtrl)
        .controller("interfaceModelCtrl",interfaceModelCtrl)
        .controller("limitwordCtrl",limitwordCtrl)
        .controller("editDimensionController",editDimensionController)
        .controller("addDimensionController",addDimensionController)
        .controller("editDomainController",editDomainController)
        .controller("editInterfaceController",editInterfaceController)
        .controller("editInterfaceModelController",editInterfaceModelController)
        .controller("addLimitwordCtrl",addLimitwordCtrl)
        .controller("settingTempletCtrl",settingTempletCtrl)
        .controller("templetDomainDimensionCtrl",templetDomainDimensionCtrl)
        .controller("templetFieldListCtrl",templetFieldListCtrl)
        .controller("editSettingTempletCtrl",editSettingTempletCtrl)
        .controller("editTempletFieldListCtrl",editTempletFieldListCtrl)
        .controller("modelSettingCtrl",modelSettingCtrl)
        .controller("reportInfoCtrl",reportInfoCtrl)
        .controller("addReportCtrl",addReportCtrl)
        .controller("updateReportCtrl",updateReportCtrl);
    ;

    /*-------------配置模板 begin-------------*/
    function settingTempletCtrl($scope,$rootScope,$state,$uibModal,toastr,settingTempletService,commonService){
        //跳转到新增页面
        $scope.addNewSettingTemplet = function(){
            $scope.settingTemplet = null;
            $state.go('service.settingTempletAddPage');
        }

        $scope.typeSettingTempletList = [
            {
                code:0,
                value:"启用"
            },{
                code:1,
                value:"停用"
            }
        ]
        //获取配置模板列表
        var  params = new Object();
        params.currentPage = 1;
        params.pageSize = 15;
        params.templetName =  $scope.templetName;
        params.status = 0;
        $rootScope.$emit("settingTempletListss",params);

        //搜索配置模板
        $scope.searchSettingTemplet = function(){
            var params = new Object();
            params.currentPage = 1;
            params.pageSize = 15;
            params.templetName = $scope.templetName;
            params.status = $scope.status;
            $rootScope.$emit("settingTempletListss",params);
        }

        //跳转到配置模板的编辑页面
        $scope.updateSettingTemplet = function(settingTemplet){
            window.localStorage.setItem("editSettingTemplet",JSON.stringify(settingTemplet));
            $state.go('service.settingTempletEditPage');
        }
        //删除配置模板
        $scope.removeSettingTemplet = function(index,settingTemplet){
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
                    var promise = settingTempletService.deleteSettingTempletById(settingTemplet);
                    promise.then(function (data) {
                        if(data.errorCode == '0'){
                            showSuccMsg(toastr,"删除数据成功!!");
                            var  params = new Object();
                            params.currentPage = 1;
                            params.pageSize = 15;
                            params.templetName =  $scope.templetName;
                            params.status = 0;
                            $rootScope.$emit("settingTempletListss",params);
                        }else{
                            showWarnMsg(toastr,"删除数据失败，请重新操作!!")
                        }
                        $rootScope.loading=false;
                    },function (data) {
                        commonService.goLoginPage(data.errorCode);
                    });
                }
            });
        }


    }
    function templetDomainDimensionCtrl($scope,$rootScope,$state){
        $rootScope.$emit("domainDimensionList","");

    }
    function templetFieldListCtrl($rootScope,$scope){
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
    function editSettingTempletCtrl ($scope,$rootScope,$uibModal,settingTempletService){
        $rootScope.editSettingTemplet = JSON.parse(window.localStorage.getItem("editSettingTemplet"));
        settingTempletService.getSettingTempletDetail($scope.editSettingTemplet.templetId).then(function(data){
            $rootScope.editTempletDomainDimensionLists = data.templetDomain;
        },function(data){

        })
        //打开编辑选择字段页面
        $scope.editSelectField = function($event,fieldList,domainDimension,dimensionS){
            $event.target.style.visibility="hidden";
            var resultView = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/service/settingTemplet/editSelectField.html',
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
        }


    }

    function editTempletFieldListCtrl($rootScope,$scope){
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

    /*-------------配置模板 end-------------*/
    function editInterfaceModelController($scope,$rootScope,$state){
       if( $scope.editInterfaceModel==null){
           $state.go('service.interfaceModel');
       }
    };
    function editInterfaceController($scope,$rootScope,$state){
       if($scope.interface==null){
           $state.go('service.interface');
       }
    };
    function editDomainController($scope,$rootScope,$state){
       if($scope.domain==null){
           $state.go('service.domain');
       }
    };
    function editDimensionController($scope,$rootScope,$state){
       if($scope.dimension==null){
           $state.go('service.dimension');
       }
    };
    function addDimensionController($scope,$rootScope,$state,toastr,domainService,commonService,dimensionService){
        var promise = domainService.findAll();
        promise.then(function (data) {
            if(data.errorCode == '0'){
                $scope.domainList=data.dataList;
            }
        },function (data) {
            commonService.goLoginPage(data.errorCode);
        });

        //保存方法
        $scope.saveDimension = function (form) {
            $rootScope.loading=true;
            var promise = dimensionService.saveDimensionData($scope.dimension,$scope.fieldList);
            promise.then(function (data) {
                if(data.errorCode == '0'){
                    toastr.success("新增维度成功");
                    $state.go('service.dimension');
                }else{
                    showErrorMsg(toastr,"新增维度失败")
                }
                $rootScope.loading=false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };
    };
    /*------------------------------------ 限定词 begin --------------------------------------*/
    function addLimitwordCtrl($scope,$rootScope,$state,$uibModal,toastr,limitwordService,commonService){
        $scope.saveLimitword = function () {
            if($scope.limitword.status == null){
                $scope.limitword.status = 0;
            }
            limitwordService.saveLimitword($scope.limitword).then(function(data){
                $scope.$dismiss();

                var paramData = new Object();
                paramData.currentPage=1;//待跳转的页面
                paramData.pageSize=15;
                showSuccMsg(toastr,"添加限定词成功")
                $rootScope.$emit("limitwordListss",paramData);
            },function(data){
                commonService.goLoginPage(data.errorCode);
            })
        };
        //检验限定词是否存在
        $scope.checkLimitword = function(event){
            if($scope.limitword.word != ""){
                limitwordService.checkLimitword($scope.limitword.word).then(function(data){
                    if(data.result == '1'){
                        $scope.checkLimitwordResult = true;
                        $(event.target).css("border-color","#ed7878");
                        $(event.target).parent().addClass("has-error");
                        $(event.target).parent().removeClass("has-success");
                        $(event.target).next().removeClass("ion-checkmark-circled");
                        $(event.target).next().addClass("ion-android-cancel");
                    }else{
                        $scope.checkLimitwordResult = false;
                    }
                },function(data){

                })

            }
        }

    }
    function limitwordCtrl($scope,$rootScope,$state,$uibModal,$filter,toastr){
        //限定词类型
        $rootScope.limitwordTypes =[
            {
                code: 'general',
                value: "通用"
            },{
                code:'q',
                value:"q参数"
            },{
                code:'pname',
                value:"pname参数"
            }
        ];
        //限定词状态
        $scope.status = [
            {
                code:0,
                value:"启用"
            },{
                code:1,
                value:"停用"
            }
        ]
        //跳转到新增限定词页面
        /*$scope.addNewLimitword = function(){
            $state.go('service.limitwordAddPage');
        }*/

        //打开新增限定词的模态窗口
        $scope.addNewLimitword = function() {
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/service/limitword/addLimitword.html',
                size: 'md',
                controller:function(){
                    $rootScope.limitword=null;
                }
            });
        };

        //获取限定词列表
        var params = new Object();
        params.currentPage = 1;
        params.pageSize = 15;
        params.word = $scope.word;
        params.wordType = $scope.wordType;
        $rootScope.$emit("limitwordListss",params);

        $scope.showStatus = function(status) {
            var selected = [];
            selected = $filter('filter')($scope.status, {code: status});
            return selected.length ? selected[0].value : 'Not set';
        };
        $scope.showWordType = function(wordType) {
            var selected = [];
            selected = $filter('filter')($scope.limitwordTypes, {code: wordType});
            return selected.length ? selected[0].value : 'Not set';
        };

        //保存修改后的限制词
        $scope.saveEditLimitword = function(limitword,form){
            var params = new Object();
            params.limitwordId = limitword.limitwordId;
            params.word = limitword.word;
            params.description = limitword.description;
            params.form = form;

            $rootScope.$emit("saveELimitwords",params);
        }
        //删除限定词
        $scope.delLimitword = function(limitword,index){
            var params = new Object();
            params.limitword = limitword;
            params.index = index;
            $rootScope.$emit("deleteLimitword",params);
        }

        //搜索
        $scope.searchLimitword = function(){
            var params = new Object();
            params.currentPage = 1;
            params.pageSize = 15;
            params.word = $scope.word;
            params.wordType = $scope.wordType;
            $rootScope.$emit("limitwordListss",params);
        }
    }
    /*------------------------------------ 限定词 end --------------------------------------*/
    /*------------------------------------ 配置模型 begin --------------------------------------*/
    function modelSettingCtrl($scope,$rootScope,$compile,toastr){
        $rootScope.$emit("domainListsss",null);

        //当领域选中之后，将其包含的维度放到维度的下拉菜单中
        $scope.sureDimensionList = function(){
            for(var i=0;i<$scope.domainLists.length;i++){
                if($scope.modelSetting.domainId == $scope.domainLists[i].domainId ){
                    $rootScope.dimensionList = $scope.domainLists[i].dimensionList;
                    break;
                }
            }
            $rootScope.$emit("dimensionOnly",-1);
        }
        //向modelSettingList中添加配置
        $scope.addDomainDimension = function(){
            for(var i=0;i<$scope.domainLists.length;i++){
                if($scope.modelSetting.domainId == $scope.domainLists[i].domainId ){
                    $scope.modelSetting.domainName = $scope.domainLists[i].domainName;
                    for(var j=0;j<$scope.dimensionList.length;j++){
                        if($scope.modelSetting.dimensionId == $scope.dimensionList[j].dimensionId){
                            $scope.modelSetting.dimensionName = $scope.dimensionList[j].dimensionName;
                            $scope.modelSetting.dimensionCode = $scope.dimensionList[j].dimensionCode;
                        }
                    }
                }
            }
            var resultCode = '';
            for(var i=0;i<$scope.targetCodeArr.length;i++){
                if($scope.targetCodeArr[i] != ''&& $scope.targetCodeArr[i] != null){
                    resultCode += ','+$scope.targetCodeArr[i];
                }
            }
            $scope.modelSetting.targetDimensionCode = resultCode.substring(1);
            if($scope.createOrEdit == -1){
                $rootScope.modelSettingList.push($scope.modelSetting);
            }else{
                $rootScope.modelSettingList[$scope.createOrEdit] = $scope.modelSetting;
            }

            $scope.$dismiss();
        }

        //封装目标code的值
        $scope.targetCodeArr = [''];

        //添加目标code的输入框
        $scope.addTargetCode = function(value){
            $scope.targetCodeArr.push(value);
            var index = $scope.targetCodeArr.length;
            var htmlStr = '<div class="form-group row">';
                htmlStr += '<label class="col-sm-3 align-right"></label>';
                htmlStr += '<div class="col-sm-6">';
                htmlStr += '<input type="text" class="form-control" placeholder="请输入目标Code" ng-model="targetCodeArr['
                htmlStr += index-1;
                htmlStr += ']" >';
                htmlStr += '</div><div class="col-sm-3">';
                htmlStr += '<button type="button" class="btn btn-danger btn-icon ion-minus-round" ng-click="delTargetCode($event,';
                htmlStr += index-1;
                htmlStr += ')"></button>';
                htmlStr += '</div></div>';
            $('#targetCodeEL').append($compile(htmlStr)($scope))
        }

        //删除目标code的输入框
        $scope.delTargetCode = function(event,index){
            $scope.targetCodeArr[index] = '';
            $(event.target).parent().parent().remove();
        }
        //编辑
        if($scope.createOrEdit != -1){
            var codeArr = $scope.modelSetting.targetDimensionCode.split(',')
            for(var i=0;i<codeArr.length;i++){
                if(i==0){
                    $scope.targetCodeArr[i] = codeArr[i];
                }else{
                    $scope.addTargetCode(codeArr[i]);
                }
            }
        }

    }
    /*------------------------------------ 配置模型 end --------------------------------------*/

    function mainCtrl($scope,$rootScope,$timeout,$uibModal,$compile,$state,toastr,$interval,
                      domainService,dimensionService,fieldService,interfaceService,interfaceModelService,commonService,customerService,limitwordService,
                      settingTempletService) {
        /*------------------------------------ 配置模板区域 begin --------------------------------------*/
        $rootScope.$on("domainDimensionList",function(event,data){
            $scope.templetDomainDimensionList(data);
        })

        $rootScope.templetDomainDimensionList = function (params){
            var promise = customerService.domainDimensionLists(params);

            promise.then(function (data) {
                $scope.templetDomainDimensionLists = data.domainList;
                $scope.restoreDomainDimensionLists = JSON.parse(JSON.stringify($scope.templetDomainDimensionLists))
                $rootScope.loading = false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })
        }

        //选择字段
        $scope.selectField=function ($event,fieldList,domainDimension,dimensionS) {
            $event.target.style.visibility="hidden";
            var resultView = $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/service/settingTemplet/selectField.html',
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
                    $rootScope.$broadcast("closeFieldList",null);
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

        $scope.screenTempletSelectYN = function(domainLists){
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
        //新增配置模板
        $scope.saveSettingTemplet = function(){
            if($scope.settingTemplet.status == null){
                $scope.settingTemplet.status = 0;//默认启用
            }
            var saveYN = $scope.screenTempletSelectYN($scope.templetDomainDimensionLists);
            if(!saveYN){
                showWarnMsg(toastr,'不能同时选择shixin和sxgg维度');
                return;
            }
            $rootScope.loading = true;
            settingTempletService.saveTemplet($scope.settingTemplet,$scope.addDomainDimensionLists).then(function(data){
                if(data.errorCode== '0'){
                    //保存之后，将对象置为空
                    $scope.settingTemplet = {};
                    $scope.domainDimensionLists = $scope.restoreDomainDimensionLists;
                    var params = new Object();
                    params.currentPage=1;
                    params.pageSize=15;
                    params.searchCompanyName=null;
                    toastr.success('新增模板成功！');
                    $state.go('service.settingTemplet');
                }else{
                    showErrorMsg(toastr,"新增模板失败！检查填写是否合法");
                }
            },function(data){
                commonService.goLoginPage(data.errorCode);
            })
            $rootScope.loading = false;
        }
        //检验是否存在相同名称的配置模板
        $scope.checkTemplet = function(checkType,contant,event){
            if(contant != null){
                settingTempletService.checkTemplet($scope.settingTemplet,checkType).then(function(data){
                    if(data.errorCode== '0') {
                        if (data.result == '0') {
                            if ("templetName" == checkType) {
                                $scope.checkTempletName = false;
                            }
                        } else {
                            if ("templetName" == checkType) {
                                $scope.checkTempletName = true;
                            }
                            $(event.target).css("border-color", "#ed7878");
                            $(event.target).parent().addClass("has-error");
                            $(event.target).parent().removeClass("has-success");
                            $(event.target).next().removeClass("ion-checkmark-circled");
                            $(event.target).next().addClass("ion-android-cancel");
                        }
                    }
                },function(data){
                    commonService.goLoginPage(data.errorCode);
                })
            }
        }

        //获取列表
        var settingTempletListss =  $rootScope.$on("settingTempletListss",function(event,data){
            $scope.settingTempletList(data);
        })
        $scope.$on('$destroy',function(){
            settingTempletListss();
        })

        $scope.settingTempletList = function(params){
            $rootScope.loading = true;
            $scope.status = params.status;
            $scope.templetName = params.templetName;
            settingTempletService.getSettingTempletList(params).then(function(data){
                $scope.settingTemplets = data.list;
                $scope.settingTempletPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        var paramData = new Object();
                        paramData.currentPage=$scope.settingTempletPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.templetName=$scope.templetName;
                        paramData.status=$scope.status;
                        $rootScope.$emit("settingTempletListss",paramData);
                    }
                };
                $rootScope.loading = false;
            },function(data){
                commonService.goLoginPage(data.errorCode);
            })
        }
        //检验是否存在相同名称的配置模板
        $scope.editCheckTemplet = function(checkType,contant,event){
            if(contant != null){
                settingTempletService.editCheckTemplet($rootScope.editSettingTemplet,checkType).then(function(data){
                    if(data.errorCode== '0') {
                        if (data.result == '0') {
                            if ("templetName" == checkType) {
                                $scope.editCheckTempletName = false;
                            }
                        } else {
                            if ("templetName" == checkType) {
                                $scope.editCheckTempletName = true;
                            }
                            $(event.target).css("border-color", "#ed7878");
                            $(event.target).parent().addClass("has-error");
                            $(event.target).parent().removeClass("has-success");
                            $(event.target).next().removeClass("ion-checkmark-circled");
                            $(event.target).next().addClass("ion-android-cancel");
                        }
                    }
                },function(data){
                    commonService.goLoginPage(data.errorCode);
                })
            }
        }

        //保存编辑后的配置
        $scope.editSaveSettingTemplet = function(){
            if($rootScope.editSettingTemplet.status == null){
                $rootScope.editSettingTemplet.status = 0;//默认启用
            }
            var saveYN = $scope.screenTempletSelectYN($rootScope.editTempletDomainDimensionLists);
            if(!saveYN){
                showWarnMsg(toastr,'不能同时选择shixin和sxgg维度');
                return;
            }
            $rootScope.loading = true;
            settingTempletService.saveEditTemplet($rootScope.editSettingTemplet,$scope.addDomainDimensionLists).then(function(data){
                if(data.errorCode== '0'){
                    var params = new Object();
                    params.currentPage=1;
                    params.pageSize=15;
                    params.searchCompanyName=null;
                    toastr.success('修改模板成功！');
                    $state.go('service.settingTemplet');
                }else{
                    showErrorMsg(toastr,"新增模板失败！检查填写是否合法");
                }
            },function(data){
                commonService.goLoginPage(data.errorCode);
            })
            $rootScope.loading = false;
        }
        /*------------------------------------ 配置模板区域 end --------------------------------------*/

        /*------------------------------------ 维度区域 begin --------------------------------------*/
        var dimensionPageListss = $rootScope.$on("dimensionPageListss",function (event,data) {
            $scope.dimensionPageList(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            dimensionPageListss();//回收广播
        });

        $scope.dimensionPageList=function (params) {
            $rootScope.loading = true;
            $scope.dimensionCode = params.dimensionCode;
            $scope.dimensionName = params.dimensionName;
            $scope.status = params.status;
            var promise = dimensionService.getDimensionList(params);
            promise.then(function (data) {
                $scope.dimensions = data.list;
                //分页区域
                $scope.dimensionPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        var paramData = new Object();
                        paramData.currentPage=$scope.dimensionPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.dimensionCode=$scope.dimensionCode;
                        paramData.dimensionName=$scope.dimensionName;
                        paramData.status=$scope.status;
                        $rootScope.$emit("dimensionPageListss",paramData);
                    }
                };
                $rootScope.loading = false;
            },function (data) {
                showErrorMsg(toastr,"获取维度列表异常，请联系管理员");
                $rootScope.loading = false;
            })
        };

        //搜索方法
        $scope.searchDimension = function () {
            var params = new Object();
            params.currentPage=1;
            params.pageSize=15;
            params.dimensionCode=$scope.dimensionCode;
            params.dimensionName=$scope.dimensionName;
            params.status=$scope.status;
            $rootScope.$emit("dimensionPageListss",params);
        };

        //编辑维度
        $scope.updateDimension = function(dimensionId){
            $rootScope.loading=true;
            $scope.dimension=null;
            $scope.domainList=null;
            $scope.fieldList=null;
            var promise = dimensionService.findDimensionById(dimensionId);
            promise.then(function (data) {
                $scope.dimension=data.dimension;
                $scope.domainList=data.domainList;
                if(data.fieldList == null){
                    $scope.fieldList= new Array();
                }else{
                    $scope.fieldList=data.fieldList;
                }
                $rootScope.loading=false;
                $state.go('service.dimensionEdit');
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });

        };

      /*  //保存方法
        $scope.saveDimension = function (form) {
            $rootScope.loading=true;
            var promise = dimensionService.saveDimensionData($scope.dimension,$scope.fieldList);
            promise.then(function (data) {
                if(data.errorCode == '0'){
                    toastr.success("新增维度成功");
                    $state.go('service.dimension');
                }else{
                    showErrorMsg(toastr,"新增维度失败")
                }
                $rootScope.loading=false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };*/

        //校验维度代码的唯一性
        $scope.checkDimensionCode=function (event) {
            var dimensionCode = $("#dimensionCode").val();
            if(dimensionCode != ''){

                var promise = dimensionService.checkDimensionId(dimensionCode);
                promise.then(function(data){

                    if(data.result == '1'){
                        //校验未通过
                        $scope.checkDimensionCodeResult=true;
                        $(event.target).css("border-color","#ed7878");
                        $(event.target).parent().addClass("has-error");
                        $(event.target).parent().removeClass("has-success");
                        $(event.target).next().removeClass("ion-checkmark-circled");
                        $(event.target).next().addClass("ion-android-cancel");
                    }else{
                        $scope.checkDimensionCodeResult=false;
                    }
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                })
            }
        };
        //校验接口代码的唯一性
        $scope.checkInterfaceCode=function (event) {
            var interfaceCode = $("#interfaceCode").val();
            if(interfaceCode != ''){
                var promise = interfaceService.checkInterfaceCode(interfaceCode);
                promise.then(function(data){
                    if(data.errorCode == '0'){
                        if(data.result == '1'){
                            //校验未通过
                            $scope.checkInterfaceCodeResult=true;
                            $(event.target).css("border-color","#ed7878");
                            $(event.target).parent().addClass("has-error");
                            $(event.target).parent().removeClass("has-success");
                            $(event.target).next().removeClass("ion-checkmark-circled");
                            $(event.target).next().addClass("ion-android-cancel");
                        }else{
                            $scope.checkInterfaceCodeResult=false;
                        }
                    }else{
                        showErrorMsg(toastr,"校验接口代码错误，请联系管理员");
                    }
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                })
            }
        };

        //删除一项领域数据
        $scope.removeDimension=function (index,dimension) {
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
                    //接收到删除的指令
                    loading($rootScope,$timeout);
                    $rootScope.loading=true;
                    customerService.dimensionYN(dimension.dimensionId).then(function(data){
                        if(data.result == 1){
                            showWarnMsg(toastr,"该维度被客户使用不能删除");
                            $rootScope.loading=true;
                        }else{
                            interfaceModelService.dimensionYN(dimension.dimensionId).then(function(data){
                                if(data.result == 1) {
                                    showWarnMsg(toastr, "该维度被模型使用不能删除");
                                    $rootScope.loading = true;
                                }else{
                                    var promise = dimensionService.deleteDimension(dimension);
                                    promise.then(function (data) {
                                        if(data.errorCode == '0'){
                                            showSuccMsg(toastr,"删除领域成功!!");
                                            var params = new Object();
                                            params.currentPage=$scope.dimensionPaginationConf.currentPage;
                                            params.pageSize=15;
                                            params.dimensionCode=$scope.dimensionCode;
                                            params.dimensionName=$scope.dimensionName;
                                            params.status=$scope.status;
                                            $rootScope.$emit("dimensionPageListss",params);
                                        }else{
                                            showWarnMsg(toastr,"删除维度失败");
                                        }
                                        $rootScope.loading=false;
                                    },function (data) {
                                        commonService.goLoginPage(data.errorCode);
                                    });
                                }
                            },function(data){
                                commonService.goLoginPage(data.errorCode);
                            })
                        }
                    },function(data){
                        commonService.goLoginPage(data.errorCode);
                    })
                }
                console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });
        };

        $scope.typeDimensionList=[
            {
                code:0,
                status:'启用'
            },{
                code:1,
                status:'停用'
            }
        ];

        //跳转至新增页面
        $scope.addNewDimension = function () {
            loading($rootScope,$timeout);
            //清空输入框的数据
            $scope.dimension=null;
            $scope.fieldList=null;
            $scope.domainList=null;
           /* var promise = domainService.findAll();
            promise.then(function (data) {
                if(data.errorCode == '0'){
                    $scope.domainList=data.dataList;
                }
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });*/
            $state.go('service.dimensionAddPage');
        };


        /*------------------------------------ 维度区域 end --------------------------------------*/

        /*------------------------------------ 字段区域 begin --------------------------------------*/
        //接收添加的字段，待与维度一起提交后台保存
        $rootScope.$on("addFieldList",function (event,data) {
            $scope.addFieldList(data);
        });
        $rootScope.$on("allFieldListByDimensionId",function (event,data) {
            $scope.allFieldListByDimensionId(data.dimensionId);
        });


        $scope.addFieldList=function (field) {
            var fieldList = $scope.fieldList;
            if(fieldList == null){
                fieldList = [];
            }
            fieldList.push(field);
            $scope.fieldList=fieldList;
        };

        $scope.allFieldListByDimensionId=function (dimensionId) {
            $rootScope.loading = true;
            var promise = fieldService.findFieldListByDimensionId(dimensionId);
            promise.then(function (data) {
                if(data.errorCode == '0'){
                    $scope.fieldList = data.dataList;
                }else{
                    showErrorMsg(toastr,"查询字段列表失败");
                }
                $rootScope.loading = false;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };

        //启用停用
        $scope.editStatus=function (index,fieldCode,status,isPost) {
            var url;
            if(status == 1){
                url = 'app/pages/common/confimStart.html';
            }else{
                url = 'app/pages/common/confimStop.html';
            }
            var modalInstance = $uibModal.open({
                animation: true,
                size:'sm',
                templateUrl: url,
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
                    if(isPost == 1){
                        //不与后台交互
                        var fieldList = $scope.fieldList;
                        for(var i=0 ;i < fieldList.length;i++){
                            var field = fieldList[i];
                            if(fieldCode == field.fieldCode){
                                field.status = status;
                                break;
                            }
                        }
                        $scope.fieldList = fieldList;
                    }else{
                        //与后台交互
                    }
                }
                console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });
        };

        $scope.saveEditField=function (fieldId,status,form) {
            $rootScope.loading = true;
            var field = form.$data;
            field.fieldId=fieldId;
            if(fieldId == null){
                var fieldList = $scope.fieldList;
                for(var i=0 ;i < fieldList.length;i++){
                    if(field.fieldCode == fieldList[i].fieldCode){
                        fieldList[i] = field;
                        break;
                    }
                }
                $scope.fieldList = fieldList;
                $rootScope.loading = false;
                showSuccMsg(toastr,"修改字段成功")
            }else{
                if(status==1){
                    customerService.fieldYN(fieldId).then(function(data){
                        if(data.result==1){
                            showWarnMsg(toastr,"该字段被客户使用不能停用");
                            $rootScope.loading = false;
                        }else{
                            var promise = fieldService.saveEditField(field);
                            promise.then(function (data) {
                                if(data.errorCode == '0'){
                                    $rootScope.loading = false;
                                    showSuccMsg(toastr,"修改字段成功")
                                }else{
                                    showErrorMsg(toastr,"修改字段失败,请重试")
                                }
                            },function (data) {
                                commonService.goLoginPage(data.errorCode);
                            });
                        }
                    },function(data){
                        commonService.goLoginPage(data.errorCode);
                    })
                }else{
                    var promise = fieldService.saveEditField(field);
                    promise.then(function (data) {
                        if(data.errorCode == '0'){
                            $rootScope.loading = false;
                            showSuccMsg(toastr,"修改字段成功")
                        }else{
                            showErrorMsg(toastr,"修改字段失败,请重试")
                        }
                    },function (data) {
                        commonService.goLoginPage(data.errorCode);
                    });
                }
            }
        };

        $scope.deleteField = function (index,field,isPost) {
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
            $rootScope.loading=true;
            modalInstance.result.then(function (result) {
                console.log(result); //result关闭是回传的值 TODO
            }, function (reason) {
                if('yes' == reason){
                    $rootScope.loading=true;
                    if(isPost == 1){
                        //新增维度删除字段
                        var fieldList = $scope.fieldList;
                        for(var i=0 ;i < fieldList.length;i++){
                            if(field.fieldCode == fieldList[i].fieldCode){
                                fieldList.splice(index, 1);
                                break;
                            }
                        }
                        $scope.fieldList = fieldList;
                    }else{
                        //编辑维度删除字段
                        if(field.fieldId == null){
                            //删除新增加的字段
                            var fieldList = $scope.fieldList;
                            for(var i=0 ;i < fieldList.length;i++){
                                if(field.fieldCode == fieldList[i].fieldCode){
                                    fieldList.splice(index, 1);
                                    break;
                                }
                            }
                            $scope.fieldList = fieldList;
                        }else{
                            //删除已经存在数据库中的字段,将状态改为停用
                            customerService.fieldYN(field.fieldId).then(function(data){
                                if(data.result==1){
                                    showWarnMsg(toastr,"该字段被客户使用不能删除");
                                    $rootScope.loading=false;
                                }else{
                                    field.status = 1;//停用
/*

                                    var dimensionId = $scope.dimension.dimensionId;
                                    var promise = fieldService.deleteField(fieldId,fieldCode);
                                    promise.then(function (data) {
                                        if(data.errorCode == '0'){
                                            showSuccMsg(toastr,"删除字段成功");
                                            $rootScope.$emit("allFieldListByDimensionId",{dimensionId:dimensionId});
                                        }else{
                                            showSuccMsg(toastr,"删除字段失败")
                                        }
                                        $rootScope.loading=false;
                                    },function (data) {
                                        commonService.goLoginPage(data.errorCode);
                                    });*/
                                }
                            },function(data){
                                commonService.goLoginPage(data.errorCode);
                            })
                        }
                    }
                }
                $rootScope.loading=false;
                console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });

            var dataList = $rootScope.fieldList;
            if(dataList != undefined){
                $rootScope.fieldList = [];
                for(var i=0 ; i<dataList.length ;i++){
                    var data = dataList[i];
                    if(id != data.id){
                        $rootScope.fieldList.push(data);
                    }
                }
            }
        };
        /*------------------------------------ 字段区域 end --------------------------------------*/

        /*------------------------------------ 领域区域 begin --------------------------------------*/
        var domainPageListss = $rootScope.$on("domainPageListss",function (event,data) {
            $scope.domainPageList(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            domainPageListss();//回收广播
        });

        $scope.domainPageList = function (params) {
            $rootScope.loading = true;
            $scope.domainCode = params.domainCode;
            $scope.domainName = params.domainName;
            $scope.status = params.status;

            var promise = domainService.domainPageList(params);
            promise.then(function (data) {
                $scope.domains = data.list;
                //分页区域
                $scope.domainPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        var paramData = new Object();
                        paramData.currentPage=$scope.domainPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.domainCode=$scope.domainCode;
                        paramData.domainName=$scope.domainName;
                        paramData.status=$scope.status;
                        $rootScope.$emit("domainPageListss",paramData);
                    }
                };
                $rootScope.loading = false;
            },function (data) {
                showErrorMsg(toastr,"获取领域列表异常，请联系管理员");
                $rootScope.loading = false;
            });
        };

        
        function domainInsertCtrl($rootScope, $http, $scope, $state, typeService ,commonService){
        	//新增领域
        	$scope.insertType = function(isValid) {
        		if (isValid) {
        			$rootScope.wait = true;
        			var promise = typeService.insertType($scope.typeName,$scope.typeCode);
        			promise.then(function(resultData) {
        				$scope.$dismiss();
        				commonService.showSuccessMsg("添加成功");
        				$rootScope.$emit("getTypeList", {});
        				$rootScope.wait = false;
        			}, function(data) {
        				
        				commonService.goLoginPage(data.errCode);
        			});
        		}
        	}
        }
        /*------------------------------------ 领域区域 end --------------------------------------*/

        /*------------------------------------ 接口区域 begin --------------------------------------*/
        //保存接口
        $scope.saveInterface=function () {
            $rootScope.loading=true;
            var bean = $scope.interface;
            var status = $("input:radio[name='status']:checked").val();
            bean.status=status;
            interfaceService.save(bean).then(function (data) {
                $rootScope.loading=false;
                if(data.errorCode == '0'){
                    showSuccMsg(toastr,"保存接口成功");
                    $state.go('service.interface');
                }else{
                    showErrorMsg(toastr,"保存接口到列表失败,请重试");
                }
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };
        var interfaceL = $rootScope.$on("interfaceListss",function (event,data) {
            $scope.interfaceList(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            interfaceL();//回收广播
        });

        $scope.interfaceList=function (params) {
            $rootScope.loading = true;
            $scope.interfaceName = params.interfaceName;
            $scope.interfaceCode = params.interfaceCode;
            $scope.status = params.status;
            interfaceService.getInterfaceList(params).then(function (data) {
                $scope.interfaces = data.list;
                //分页区域
                $scope.interfacePaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        var paramData = new Object();
                        paramData.currentPage=$scope.interfacePaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.interfaceCode=$scope.interfaceCode;
                        paramData.interfaceName=$scope.interfaceName;
                        paramData.status=$scope.status;
                        $rootScope.$emit("interfaceListss",paramData);
                    }
                };
                $rootScope.loading = false;
            },function (data) {
                showErrorMsg(toastr,"获取接口列表异常，请联系管理员");
                $rootScope.loading = false;
            })
        };

        //跳转编辑页面
        $scope.updateInterface = function (data) {
            $rootScope.loading=true;
            $state.go('service.interfaceUpdate');
            $scope.interface= data;
            $scope.debugResult=null;
            $rootScope.loading=false;
        };

        //跳转新增接口页面
        $scope.interfaceAddPage = function () {
            $rootScope.loading=true;
            $scope.interface= null;
            $scope.debugResult=null;
            $scope.checkAdminUserNameResult=false;
            $state.go('service.interfaceAddPage');
            $rootScope.loading=false;
        };

        //模糊搜索
        $scope.searchInterface=function () {
            var params = new Object();
            params.currentPage=1;
            params.pageSize=15;
            params.status=$scope.status;
            params.interfaceName=$scope.interfaceName;
            params.interfaceCode=$scope.interfaceCode;
            $rootScope.$emit("interfaceListss",params);
        };

        $scope.typeInterfaceList=[
            {
                code:'0',
                status:'开放'
            },{
                code:'1',
                status:'内测'
            },{
                code:'2',
                status:'删除'
            }
        ];

        //保存编辑后的接口
        $scope.saveEditInterface=function () {
            $rootScope.loading=true;
            var bean = $scope.interface;
            //判断用户选择的是2删除的话，去判断是不是有客户正在使用该接口，如果正在使用则不能删除
            if($scope.interface.status == 2){
                customerService.interfaceYN($scope.interface.interfaceId).then(function (data) {
                    if(data.result=='1'){//该接口正在被客户使用
                        showWarnMsg(toastr,"该接口被客户使用不能删除");
                        $rootScope.loading=false;
                        return;
                    }else{
                        interfaceService.saveEditInterface(bean).then(function (data) {
                            if(data.errorCode == '0'){
                                showSuccMsg(toastr,"编辑接口成功");
                                $state.go('service.interface');
                            }else{
                                showErrorMsg(toastr,"编辑接口失败");
                            }
                            $rootScope.loading=false;
                        },function (data) {
                            commonService.goLoginPage(data.errorCode);
                        });
                    }
                });

            }else{
                interfaceService.saveEditInterface(bean).then(function (data) {
                    if(data.errorCode == '0'){
                        showSuccMsg(toastr,"编辑接口成功");
                        $state.go('service.interface');
                    }else{
                        showErrorMsg(toastr,"编辑接口失败");
                    }
                    $rootScope.loading=false;
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                });
            }


        };

        //接口测试
        $scope.debugInterface = function(testUrl,event){
            var pattern = new RegExp("http://(.*).fahaicc.com/fhfk/(.*)\?authCode=(.*)");
            if(!pattern.test(testUrl)){//测试url的校验
                if(testUrl==null){
                    testUrl = '';
                }
                var resultStr = "测试url："+testUrl+"   无效\n";
                    resultStr += "请按照规则填写测试url，例如：http://api.fahaicc.com/fhfk/xxxxx?authCode=xxxxx";
                $scope.debugResult = resultStr;
            }else{//向服务器发起请求，测试url
                $(event.target).attr('disabled',true);
                var testStr = "\n- - - - - - - - - - - Debug Interface Begin - - - - - - - - - - - - - \n";
                    testStr += "\n测试连接："+testUrl+"\n";
                $scope.debugResult = testStr;
                setTimeout(function(){
                    interfaceService.debugInterfaceUrl(testUrl).then(function(data){
                        testStr += "\n测试结果：\n";
                        if(data.data=='1'){
                            testStr += "仔细检查测试url或接口异常，请联系管理员\n";
                        }else{
                            testStr += formatJson(data.data);
                        }
                        testStr += "\n- - - - - - - - - - - Debug Interface End - - - - - - - - - - - - - \n";
                        $scope.debugResult = testStr;
                        $(event.target).attr('disabled',false);
                    },function(data){
                        commonService.goLoginPage(data.errorCode);
                    })
                },500);


            }
        }

        //接口测试
        var timerHandler;
        $scope.interfaceDebug = function(){
            var debugUrl = $scope.interface.testURL;
            var interfaceURL = $scope.interface.interfaceURL;

            var url="";
            var str="" ;
            if(debugUrl ){
                str = "<span style='color: black'>待测试url:"+debugUrl+"</span><br/>";
                url = debugUrl;
            }else if(interfaceURL){
                url = interfaceURL;
                str = "<span style='color: black'>待测试url:"+interfaceURL+"</span><br/>";
            }else{
                str="<span style='color: red'>未填写待测试url或接口url，接口测试失败</span>";
                $scope.debugResult=str;
                return ;
            }
            $scope.debugResult=str;
            str+="<span style='color: green'>开始进行接口测试";
            $scope.debugResult=str;

            var second=20;
            timerHandler=$interval(function(){
                if(second = 20){
                    //接口测试
                    interfaceService.debugUrl(url).then(function (data) {
                        if(data.errorCode == '0'){
                            $interval.cancel(timerHandler);
                            var resultJosn = data.data;
                            str+="</span><br/>";
                            str+="<span style='color: #9428ff'>接口返回结果："+formatJson(resultJosn)+"</span>";
                            $scope.debugResult=str;
                        }else{
                            $interval.cancel(timerHandler);
                            str+="</span><br/>";
                            str+="<span style='color: red'>接口测试失败!</span>";
                            $scope.debugResult=str;
                        }
                    },function (data) {
                        commonService.goLoginPage(data.errorCode);
                    });
                }else{
                    if(second<=0){
                         $interval.cancel(timerHandler);
                         str+="</span><br/>";
                         str+="<span style='color: red'>接口测试超时,接口测试失败!</span>";
                          $scope.debugResult=str;
                    }
                    str+=".";
                    $scope.debugResult=str;
                }
                second--;
            },500);

        };


        /**------------------------------------------我是分割线------------------------------------------*/
        /***********接口区域开始************/
        // $scope.interfaces=initData();

        //查看接口情况
        $scope.details = function (page,size) {
            loading($rootScope,$timeout);
            $uibModal.open({
                animation: true,
                templateUrl: page,
                size: size
            });
        };
        /***********接口区域结束************/

        /***********领域区域开始************/
        //删除一项领域数据
        $scope.removeDomain=function (index,domain) {
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
                    //接收到删除的指令
                    $rootScope.loading=true;
                    customerService.domainYN(domain.domainId).then(function(data){
                        if(data.result == 1){
                            showWarnMsg(toastr,"该领域被客户使用不能删除");
                            $rootScope.loading=false;
                        }else{
                            interfaceModelService.domainYN(domain.domainId).then(function(data) {
                                if (data.result == 1) {
                                    showWarnMsg(toastr, "该领域被模型使用不能删除");
                                    $rootScope.loading = false;
                                } else {
                                    var promise = domainService.deleteDomain(domain);
                                    promise.then(function (data) {
                                        if(data.errorCode == '0'){
                                            showSuccMsg(toastr,"删除领域成功!!");

                                            var params = new Object();
                                            params.currentPage=$scope.domainPaginationConf.currentPage;
                                            params.pageSize=15;
                                            params.domainCode=$scope.domainCode;
                                            params.domainName=$scope.domainName;
                                            params.status=$scope.status;
                                            $rootScope.$emit("domainPageListss",params);
                                        }else{
                                            showWarnMsg(toastr,"删除领域失败");
                                        }
                                        $rootScope.loading=false;
                                    },function (data) {
                                        commonService.goLoginPage(data.errorCode);
                                    });
                                }
                            },function(data){
                                commonService.goLoginPage(data.errorCode);
                            })
                        }
                    },function(data){
                        commonService.goLoginPage(data.errorCode);
                    })
                    /*loading($rootScope,$timeout);
                    $scope.domains.splice(index, 1);
                    toastr.warning("删除数据成功!!");*/
                }
                console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });
        };

        $scope.typeDomainList=[
            {
                code:0,
                status:'启用'
            },{
                code:1,
                status:'停用'
            }
        ];

        //模糊搜索
        $scope.searchDomain=function () {
            var params = new Object();
            params.currentPage=1;
            params.pageSize=15;
            params.domainCode=$scope.domainCode;
            params.domainName=$scope.domainName;
            params.status=$scope.status;
            $rootScope.$emit("domainPageListss",params);
        };

        //跳转至新增领域界面
        $scope.addNewDomain = function () {
            loading($rootScope,$timeout);
            $scope.domain=null;
            $state.go('service.domainAddPage');
        };

        //跳转至修改界面
        $scope.updateDomain = function (domain) {
            $scope.domain=domain;
            loading($rootScope,$timeout);
            $state.go('service.domainEdit');
        };
        
        //保存领域
        $scope.saveDomain=function () {
            $rootScope.loading=true;
            if($scope.domain.status == null){
                $scope.domain.status = 0;
            }
            var promise = domainService.save($scope.domain);
            promise.then(function (data) {
                $rootScope.loading=false;
                if(data.errorCode == '0'){
                    showSuccMsg(toastr,"添加领域成功");
                }
                $state.go('service.domain');
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            });
        };

        //保存编辑后的领域
        $scope.saveEditDomain=function (form) {
            $rootScope.loading=true;
            if($scope.domain.status == 1){
                customerService.domainYN($scope.domain.domainId).then(function(data){
                    if(data.result == 1){
                        showWarnMsg(toastr,"该领域被客户使用不能停用");
                        $rootScope.loading=false;
                    }else{
                        interfaceModelService.domainYN($scope.domain.domainId).then(function(data){
                            if(data.result == 1) {
                                showWarnMsg(toastr, "该领域被模型使用不能停用");
                                $rootScope.loading = false;
                            }else{
                                var promise = domainService.saveEditDomain($scope.domain);
                                promise.then(function (data) {
                                    $rootScope.loading=false;
                                    if(data.errorCode == '0'){
                                        showSuccMsg(toastr,"修改领域成功");
                                    }
                                    $state.go('service.domain');
                                },function (data) {
                                    commonService.goLoginPage(data.errorCode);
                                });
                            }
                        },function(data){
                            commonService.goLoginPage(data.errorCode);
                        })
                    }
                },function(data){
                    commonService.goLoginPage(data.errorCode);
                })
            }else{
                var promise = domainService.saveEditDomain($scope.domain);
                promise.then(function (data) {
                    $rootScope.loading=false;
                    if(data.errorCode == '0'){
                        showSuccMsg(toastr,"修改领域成功");
                    }
                    $state.go('service.domain');
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                });
            }

         };

        //校验领域代码的唯一性
        $scope.checkDomainCode=function (event) {
            var domainCode = $("#domainCode").val();
            if(domainCode != ''){
                var promise = domainService.checkDomainCode(domainCode);
                promise.then(function(data){

                    if(data.result == '1'){
                        //console.log(222)
                        //校验未通过
                        $scope.checkDomainCodeResult=true;
                        $(event.target).css("border-color","#ed7878");
                        $(event.target).parent().addClass("has-error");
                        $(event.target).parent().removeClass("has-success");
                        $(event.target).next().removeClass("ion-checkmark-circled");
                        $(event.target).next().addClass("ion-android-cancel");
                    }else{
                        //console.log(111)
                        $scope.checkDomainCodeResult=false;
                    }
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                })
            }
        };

        /***********领域区域结束************/

        /***********维度区域开始************/

        $scope.confirmDelete = function (id) {
            $uibModal.open({
                animation: true,
                size:'sm',
                templateUrl: 'app/pages/common/confimDelete.html',
                resolve: {
                    /* index: function() {
                     return index;
                     }*/
                }
            });

            var dataList = $scope.dimensions;
            $scope.dimensions = [];
            for(var i=0 ; i<dataList.length ;i++){
                var data = dataList[i];
                if(id != data.id){
                    $scope.dimensions.push(data);
                }
            }
        };

        //保存修改后的维度
        $scope.saveEditDimension=function (form) {
            $rootScope.loading=true;
            if($scope.dimension.status == 1){
                customerService.dimensionYN($scope.dimension.dimensionId).then(function(data){
                    if(data.result==1){
                        showWarnMsg(toastr,"该维度被客户使用不能停用");
                        $rootScope.loading=false;
                    }else{
                        interfaceModelService.dimensionYN($scope.dimension.dimensionId).then(function(data){
                            if(data.result==1){
                                showWarnMsg(toastr,"该维度被模型使用不能停用");
                                $rootScope.loading=false;
                            }else{
                                var promise = dimensionService.saveEditDimensionData($scope.dimension,$scope.fieldList);
                                promise.then(function (data) {
                                    if(data.errorCode == '0'){
                                        toastr.success("修改维度成功");
                                        $state.go('service.dimension');
                                    }else{
                                        showErrorMsg(toastr,"修改维度失败")
                                    }
                                    $rootScope.loading=false;
                                },function (data) {
                                    commonService.goLoginPage(data.errorCode);
                                });
                            }
                        },function(data){
                            commonService.goLoginPage(data.errorCode);
                        })
                    }
                },function(data){
                    commonService.goLoginPage(data.errorCode);
                })
            }else{
                var promise = dimensionService.saveEditDimensionData($scope.dimension,$scope.fieldList);
                promise.then(function (data) {
                    if(data.errorCode == '0'){
                        toastr.success("修改维度成功");
                        $state.go('service.dimension');
                    }else{
                        showErrorMsg(toastr,"修改维度失败")
                    }
                    $rootScope.loading=false;
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                });
            }


            /*loading($rootScope,$timeout);
            var dimensionStatus = $("input[name='dimensionStatus']:checked").val();
            var fieldArr = [];
            $("input[name='checkField']:checked").each(function () {
                fieldArr.push( $(this).val());
            });
            var dimension= {
                id:$scope.dimension.id,
                dimensionCode:$("#dimensionCode").val(),//$scope.dimension.dimensionCode,
                dimensionName:$("#dimensionName").val(),//$scope.dimension.dimensionName,
                status:dimensionStatus,
                domainId:$("#domainId").val(),
                description:$("#description").val(),//$scope.dimension.description,
                fields:fieldArr.join(',')
            };

            for(var i=0;i<$scope.dimensions.length ;i++){
                if($scope.dimensions[i].id == dimension.id){
                    $scope.dimensions[i]=dimension;
                    break;
                }
            }
            console.log($scope.dimensions);
            toastr.success("编辑维度成功");
            $state.go('service.dimension');*/
        };

        /***********维度区域结束************/


        /***********字段区域开始************/

        $rootScope.newFieldList = [];

        //弹出框
        $scope.open = function(page, size) {
            $uibModal.open({
                animation: true,
                templateUrl: page,
                size: size
            });
        };



        /***********字段区域结束************/
       
       /***************接口模型******************/
        var interfaceModelPageListss = $rootScope.$on("interfaceModelPageListss",function (event,data) {
            $scope.interfaceModelPageList(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            interfaceModelPageListss();//回收广播
        });
        $scope.interfaceModelPageList=function(params){
        	$rootScope.loading = true;
        	$scope.modelName = params.modelName;
        	$scope.status = params.status;
        	interfaceModelService.getInterfaceModelList(params).then(function (data){
        		$scope.interfaceModels = data.list;
        		$scope.interfaceModelPaginationConf = {
        			currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        var paramData = new Object();
                        paramData.currentPage=$scope.interfaceModelPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.modelName=$scope.modelName;
                        paramData.status=$scope.status;
                        $rootScope.$emit("interfaceModelPageListss",paramData);
                    }
                };
                $rootScope.loading = false;
            },function () {
                showErrorMsg(toastr,"获取模型列表异常，请联系管理员");
                $rootScope.loading = false;
            })
        }
        
         $scope.typeInterfaceModelList=[
            {
                code:0,
                status:'启用'
            },{
                code:1,
                status:'停用'
            }
        ];
        //搜索方法
        $scope.searchInterfaceModel = function () {
            var params = new Object();
            params.currentPage=1;
            params.pageSize=15;
            params.modelName=$scope.modelName;
            params.status=$scope.status;
            $rootScope.$emit("interfaceModelPageListss",params);
        };
        //跳转至新增模型页面
        $scope.addNewInterfaceModel = function () {
            $rootScope.loading = true;
            //清空输入框
            $scope.interfaceModel=null;
            //初始化参数
            $rootScope.modelSettingList = new Array();
            $rootScope.loading = false;
            $state.go('service.interfaceModelAddPage');
        };


        /*------------------------------------ 配置模型 begin --------------------------------------*/
        //添加modelSetting
        $scope.addModelSetting = function () {
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/service/interfaceModel/modelSetting.html',
                size: "md",
                controller:function(){
                    $rootScope.modelSetting = $scope.newObject();
                    $rootScope.createOrEdit = -1;
                    $rootScope.dimensionList = null;
                }
            });
        };
        //封装模型参数
        $scope.newObject = function(){
            var modelObj = {
                domainId : '',
                domainName:'',
                dimensionId : '',
                dimensionName:'',
                dimensionCode : '',
                targetDimensionCode : '',
                targetCluster : ''
            };
            return modelObj;
        }

        //封装模型对象
        $rootScope.modelSettingList = new Array();

        var domainListsss = $rootScope.$on("domainListsss",function(event,data){
            $scope.domainListsss(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            domainListsss();//回收广播
        });
        //获取领域纬度集合
        $scope.domainListsss = function(params) {
            if ($rootScope.domainLists == null || $rootScope.domainLists.length < 1) {
                $rootScope.loading = true;
                interfaceModelService.getAllDomainDimension().then(function (data) {
                    $rootScope.domainLists = data.domainList;
                    $rootScope.loading = false;
                }, function (data) {
                    commonService.goLoginPage(data.errorCode);
                })
            }
        }

        //编辑modelSetting
        $scope.editModelSetting = function (index){
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/service/interfaceModel/modelSetting.html',
                size: "md",
                controller:function(){
                    $rootScope.modelSetting = $rootScope.dimensionList = JSON.parse(JSON.stringify($rootScope.modelSettingList[index]));
                    $rootScope.createOrEdit = index;
                    for(var i=0;i<$scope.domainLists.length;i++){
                        if($rootScope.modelSettingList[index].domainId == $scope.domainLists[i].domainId){
                            $rootScope.dimensionList = $scope.domainLists[i].dimensionList;
                        }
                    }
                    $scope.dimensionOnly(index);
                }
            });
        }
        //删除modelSetting
        $scope.removeModelSetting = function (index){
            $rootScope.modelSettingList.splice(index,1);
        }

        $rootScope.$on("dimensionOnly",function(event,data){
            $scope.dimensionOnly(data);
        })

        //保证一个模型中维度的唯一性
        $scope.dimensionOnly = function(index){
            if($rootScope.modelSettingList != null && $rootScope.modelSettingList.length >0){
                $rootScope.dimensionList = JSON.parse(JSON.stringify($rootScope.dimensionList));
                outer:
                for(var i=0;i<$rootScope.modelSettingList.length;i++){
                    if(i==index){
                        continue;
                    }
                    //去掉同一个领域中目前已经添加的维度
                    if($rootScope.modelSettingList[i].domainId == $rootScope.modelSetting.domainId ){
                        if($rootScope.dimensionList != null && $rootScope.dimensionList.length > 0){
                            inner:
                            for(var j=0;j<$rootScope.dimensionList.length;j++){
                                if($rootScope.modelSettingList[i].dimensionId == $rootScope.dimensionList[j].dimensionId ){
                                    $rootScope.dimensionList.splice(j,1);
                                    continue outer;
                                }
                            }
                        }
                    }
                }
            }

        }


        /*------------------------------------ 配置模型 end --------------------------------------*/
        /*------------------------------------ 限定词 begin --------------------------------------*/
        //获取限定词列表
        var limitwordListss = $rootScope.$on("limitwordListss",function(event,data){
            $scope.limitwordList(data);
        });
        $scope.$on('$destroy',function(){//controller回收资源时执行
            limitwordListss();//回收广播
        });
        $scope.limitwordList = function(data){
            $rootScope.loading = true;
            $rootScope.word = data.word;
            $rootScope.wordType = data.wordType;
            limitwordService.limitwordList(data).then(function(data){
                $rootScope.limitwords = data.list;
                $rootScope.loading = false;
                //分页区域
                $scope.limitwordPaginationConf = {
                    currentPage: data.currentPage,              //当前所在的页（…默认第1页）
                    totalItems: data.totalCount,                //总共有多少条记录
                    itemsPerPage: data.pageSize,                //每页展示的数据条数（…默认15条）
                    pagesLength: 5,                             //分页条目的长度（如果设置建议设置为奇数）
                    perPageOptions: [10, 20, 30, 40, 50],        //可选择显示条数的数组
                    onChange: function(){
                        //分页
                        var paramData = new Object();
                        paramData.currentPage=$scope.limitwordPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.word=$scope.word;
                        paramData.wordType=$scope.wordType;
                        $rootScope.$emit("limitwordListss",paramData);
                    }
                };
            },function(data){
                showErrorMsg(toastr,"获取限定词列表异常，请联系管理员");
                $rootScope.loading = false;
            });
        }

        //保存修改后的限定词
        $rootScope.$on("saveELimitwords",function(event,params){
            $scope.saveEditLimitwords(params);
        });
        $scope.saveEditLimitwords = function(params){
            $rootScope.loading = true;
            limitwordService.saveEditLimitword(params).then(function(data){
                var paramData = new Object();
                paramData.currentPage=$scope.limitwordPaginationConf.currentPage;//待跳转的页面
                paramData.pageSize=15;
                paramData.word=$scope.word;
                paramData.wordType=$scope.wordType;
                $rootScope.loading = false;
                $rootScope.$emit("limitwordListss",paramData);
                showSuccMsg(toastr,"修改限定词成功");
            },function(data){
                commonService.goLoginPage(data.errorCode);
            });
        }
        //删除限定词
        $rootScope.$on("deleteLimitword",function(event,data){
            $scope.deleteLimitword(data);
        })
        $scope.deleteLimitword = function (params){

            var modalInstance = $uibModal.open({
                animation: true,
                size:'sm',
                templateUrl: 'app/pages/common/confimDelete.html',
                resolve: {
                    index: function() {
                        return params.index;
                    }
                }
            });
            modalInstance.result.then(function (result) {
                console.log(result); //result关闭是回传的值 TODO
            }, function (reason) {
                if('yes' == reason){
                    $rootScope.loading = true;
                    limitwordService.deleteLimitword(params.limitword).then(function(data){
                        var paramData = new Object();
                        paramData.currentPage=$scope.limitwordPaginationConf.currentPage;//待跳转的页面
                        paramData.pageSize=15;
                        paramData.word=$scope.word;
                        paramData.wordType=$scope.wordType;
                        $rootScope.loading = false;
                        $rootScope.$emit("limitwordListss",paramData);
                        showSuccMsg(toastr,'删除限定词成功');
                    },function(data){
                        commonService.goLoginPage(data.errorCode);
                    });
                }
                    console.log(reason);//点击空白区域，总会输出backdrop click，点击取消，则会输出cancel
            });
        }

        /*------------------------------------ 限定词 end --------------------------------------*/

        //添加选项卡
        $scope.addTab = function(){
        	$scope.interfaceModelParams.push($scope.newObject());
        	var htmlStr = '<div><hr/><div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">领域：</label>';
        		htmlStr += '<div class="col-sm-5">'; 
        		htmlStr += '<select class="form-control" style="overflow:scroll;" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].domainId" name="customerArea"';
        		htmlStr += 'ng-options="x.domainId as x.domainName for x in domainDimensionLists"  id="customerArea" ng-change="findDomainDimensionList(interfaceModelParams['
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].domainId,';
        		htmlStr += $scope.domainParams.length+1
        		htmlStr += ')" >';
        		htmlStr += '</select></div></div>';
        		htmlStr += '<div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">维度：</label>';
        		htmlStr += '<div class="col-sm-5">';
        		htmlStr += '<select class="form-control" style="overflow:scroll;" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].dimensionId" name="customerArea" ';
        		htmlStr += 'ng-options="x.dimensionId as x.dimensionName for x in domainParams[';
        		htmlStr += $scope.domainParams.length
        		htmlStr += '].dimensionList"  id="customerArea" ng-change="dealDimensionCode(';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += ',';
        		htmlStr += $scope.domainParams.length+1
				htmlStr += ')">';
        		htmlStr += '</select></div></div>';
        		htmlStr += '<div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">目标Code：</label>';
        		htmlStr += '<div class="col-sm-5">';
        		htmlStr += '<input type="text" class="form-control" placeholder="请输入目标Code" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].targetDimensionCode">';
        		htmlStr += '</div></div>';
        		htmlStr += '<div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">目标集群：</label>';
        		htmlStr += '<div class="col-sm-5">';
        		htmlStr += '<input type="text" class="form-control" placeholder="请输入目标集群" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].targetCluster">';
        		htmlStr += '</div><div class="col-sm-3">';
        		htmlStr += '<button type="button" class="btn btn-danger btn-icon ion-minus-round" ng-click="delTab($event,';
        		htmlStr += $scope.interfaceModelParams.length-1
				htmlStr += ');"></button>';
				htmlStr += '</div></div>';        	 
        	$("#mybut").parent().parent().after($compile(htmlStr)($scope));
//      	$scope.htmlData.push({});
			
			
			/*var 'interfaceModelObj'+$scope.interfaceModelParams.length = $scope.newObject();
			$scope.interfaceModelParams.push('interfaceModelObj'+$scope.interfaceModelParams.length);*/

        }
        //维度集合参数
        $scope.domainParams = new Array();
        //选择的领域纬度参数
        $scope.interfaceModelParams = new Array();
        
        $scope.htmlData = new Array();
        //删除选项卡
        $scope.delTab = function(event,index){
        	$(event.target).parent().parent().parent().remove();
//			$scope.htmlData.splice(index,1);
			$scope.interfaceModelParams[index] = null;
        }
         //选取领域维度的联动 和模型的目标维度和目标集群
        $scope.checkField=function ($event,domainDimension,dimensionS) {
            var isChecked = $event.target.checked;//checkbox是否被选中
            if(isChecked){
            	domainDimension.selectYN = isChecked; 
            	var resultView = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/pages/service/interfaceModel/addModelParams.html',
                    size: "md",
                    controller:function(){
                		$rootScope.dimension = JSON.parse(JSON.stringify(dimensionS));
                		$rootScope.domainDimension = domainDimension;
                		$rootScope.selectDimension = dimensionS;
                    }
                });
                
            }else{
                for(var i=0;i<(domainDimension.dimensionList).length;i++){
                	if((domainDimension.dimensionList)[i].selectYN){
                		isChecked = true;
                	}
                }
                domainDimension.selectYN = isChecked; 
            }
        };
        //保存
        $scope.saveInterfaceModel=function(){
        	$rootScope.loading=true;
        	if($scope.interfaceModel.status==null){
        		$scope.interfaceModel.status = 0;
        	}
        	interfaceModelService.saveInterfaceModel($scope.interfaceModel,$rootScope.modelSettingList).then(function (data){
                $rootScope.modelSettingList = new Array();
                $rootScope.loading = false;
                $state.go('service.interfaceModel');
                showSuccMsg(toastr,"保存模型成功");
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })

        }
        //新增检测是否重复
        $scope.checkModel = function(checkType,contant,event){
        	if(contant != null){
	        	interfaceModelService.checkModel($scope.interfaceModel,checkType).then(function(data){
	        		if(data.errorCode== '0'){
		        		if(data.result== '0'){
		        			if("modelName" == checkType){
		        				$scope.checkModelNameResult = false;
		        			}
		        		}else{
		        			if("modelName" == checkType){
		        				$scope.checkModelNameResult = true;
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
        //编辑检测是否重复
        $scope.editCheckModel = function(checkType,contant,event){
        	if(contant != null){
	        	interfaceModelService.checkModel($scope.editInterfaceModel,checkType).then(function(data){
	        		if(data.errorCode== '0'){
		        		if(data.result== '0'){
		        			if("modelName" == checkType){
		        				$scope.editCheckModelNameResult = false;
		        			}
		        		}else{
		        			if("modelName" == checkType){
		        				$scope.editCheckModelNameResult = true;
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
        //跳转到修改页面
        $scope.updateinterfaceModel=function(interfaceModel){
            $rootScope.loading = true;
            $scope.editInterfaceModel = interfaceModel;
            $scope.interfaceModel=null;
            //初始化参数
            $rootScope.modelSettingList = new Array();
            //获取该模型的领域维度信息
            interfaceModelService.getModelDomainDimension(interfaceModel.modelId).then(function (data){
                $rootScope.modelSettingList = data.domainList;
            },function (data) {
                commonService.goLoginPage(data.errorCode);
            })

            interfaceModelService.getAllDomainDimension().then(function (data){
                $scope.domainLists = data.domainList;
            },function () {
                showErrorMsg(toastr,"查询模型失败，请稍候再试");
            })
            $rootScope.loading = false;
            $state.go('service.interfaceModelEditPage');
        }


        /*$scope.updateinterfaceModel=function(interfaceModel){
            $scope.editInterfaceModel = interfaceModel;
            $scope.interfaceModel=null;
            //初始化参数
            //维度集合参数
            $scope.domainParams = new Array();
            //选择的领域纬度参数
            $scope.interfaceModelParams = new Array();
            //获取该模型的领域维度信息
            interfaceModelService.getModelDomainDimension(interfaceModel.modelId).then(function (data){
                $scope.domainDimensionLists = data.domainList;
                var stat = true;
                for(var i=0;i<data.domainList.length;i++){
                    if(data.domainList[i].selectYN){
                        for(var j=0;j<data.domainList[i].dimensionList.length;j++){
                            if(data.domainList[i].dimensionList[j].selectYN){
                                var modelObj = {
                                    domainId : data.domainList[i].domainId,
                                    dimensionId : data.domainList[i].dimensionList[j].dimensionId,
                                    dimensionCode : data.domainList[i].dimensionList[j].dimensionCode,
                                    targetDimensionCode : data.domainList[i].dimensionList[j].targetDimensionCode,
                                    targetCluster : data.domainList[i].dimensionList[j].targetCluster
                                };
                                $scope.interfaceModelParams.push(modelObj);
                                if(stat){
                                    $scope.domains=data.domainList[i];
                                    stat = false;
                                }else{
                                    $scope.domainParams.push(data.domainList[i]);
                                    $scope.toEditAddTab();
                                }

                            }
                        }
                    }
                }
                $rootScope.loading = false;
            },function () {
                showErrorMsg(toastr,"查询模型失败，请稍候再试");
            })
            $state.go('service.interfaceModelEditPage');
        }    */

            /*//获取该模型的领域维度信息
             interfaceModelService.getModelDomainDimension(interfaceModel.modelId).then(function (data){
             $scope.editDomainDimensionLists = data.domainList;
             $rootScope.loading = false;
             },function () {
             showErrorMsg(toastr,"查询模型失败，请稍候再试");
             })
             $state.go('service.interfaceModelEditPage');*/


        $scope.screenSelectYN = function(domainLists){
        	//过滤出选中的领域 维度 字段
        	if(domainLists != null){
	            var aList = JSON.parse(JSON.stringify(domainLists));
	            for(var i=0;i<aList.length;i++){
	                var domain =  aList[i];
	                if(domain.selectYN){
	                    var dimensionList = domain.dimensionList;
	                    for(var j=0;j<dimensionList.length;j++){
	                        var dimension=dimensionList[j];
	                        if(!dimension.selectYN){
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
	        }
        }

        //删除模型
        $scope.removeinterfaceModel=function(index,interfaceModel){
        	//打开模态
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

            }, function (reason) {
                if('yes' == reason){
                    //接收到删除的指令
                    $rootScope.loading=true;
                    customerService.modelYN(interfaceModel.modelId).then(function(data){
                        if(data.result == 1){
                            showWarnMsg(toastr,"该模型被客户使用不能删除");
                            $rootScope.loading=false;
                        }else{
                            var promise = interfaceModelService.deleteInterfaceModel(interfaceModel);
                            promise.then(function (data) {
                                if(data.errorCode == '0'){
                                    showSuccMsg(toastr,"删除模型成功!!");
                                    var params = new Object();
                                    params.currentPage=$scope.interfaceModelPaginationConf.currentPage;
                                    params.pageSize=15;
                                    params.modelName=$scope.modelName;
                                    params.status=$scope.status;
                                    $rootScope.$emit("interfaceModelPageListss",params);
                                }else{
                                    showErrorMsg(toastr,"删除模型失败");
                                }
                                $rootScope.loading=false;
                            },function (data) {
                                commonService.goLoginPage(data.errorCode);
                            });
                        }
                    },function(data){
                        commonService.goLoginPage(data.errorCode);
                    })
                }
            });
        };
         //跳到编辑页面添加选项卡
        $scope.toEditAddTab = function(){
        	var htmlStr = '<div><hr/><div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">领域：</label>';
        		htmlStr += '<div class="col-sm-5">'; 
        		htmlStr += '<select class="form-control" style="overflow:scroll;" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].domainId" name="customerArea"';
        		htmlStr += 'ng-options="x.domainId as x.domainName for x in domainDimensionLists"  id="customerArea" ng-change="findDomainDimensionList(interfaceModelParams['
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].domainId,';
        		htmlStr += $scope.domainParams.length
        		htmlStr += ')" >';
        		htmlStr += '</select></div></div>';
        		htmlStr += '<div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">维度：</label>';
        		htmlStr += '<div class="col-sm-5">';
        		htmlStr += '<select class="form-control" style="overflow:scroll;" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].dimensionId" name="customerArea" ';
        		htmlStr += 'ng-options="x.dimensionId as x.dimensionName for x in domainParams[';
        		htmlStr += $scope.domainParams.length-1
        		htmlStr += '].dimensionList"  id="customerArea" ng-change="dealDimensionCode(';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += ',';
        		htmlStr += $scope.domainParams.length
				htmlStr += ')">';
        		htmlStr += '</select></div></div>';
        		htmlStr += '<div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">目标Code：</label>';
        		htmlStr += '<div class="col-sm-5">';
        		htmlStr += '<input type="text" class="form-control" placeholder="请输入目标Code" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].targetDimensionCode">';
        		htmlStr += '</div></div>';
        		htmlStr += '<div class="form-group row">';
        		htmlStr += '<label class="col-sm-2 align-right">目标集群：</label>';
        		htmlStr += '<div class="col-sm-5">';
        		htmlStr += '<input type="text" class="form-control" placeholder="请输入目标集群" ng-model="interfaceModelParams[';
        		htmlStr += $scope.interfaceModelParams.length-1
        		htmlStr += '].targetCluster">';
        		htmlStr += '</div><div class="col-sm-3">';
        		htmlStr += '<button type="button" class="btn btn-danger btn-icon ion-minus-round" ng-click="delTab($event,';
        		htmlStr += $scope.interfaceModelParams.length-1
				htmlStr += ');"></button>';
				htmlStr += '</div></div>';        	 
        	$("#mybut").parent().parent().after($compile(htmlStr)($scope));
        }
        
        //保存修改后的模型
        $scope.saveEditInterfaceModel=function(){
        	$rootScope.loading=true;
        	if($scope.editInterfaceModel.status==null){
        		$scope.editInterfaceModel.status = 0;
        	}
        	$scope.editInterfaceModel.lastModifyDate=null;
            if($scope.editInterfaceModel.status == 1){
                customerService.modelYN($scope.editInterfaceModel.modelId).then(function(data){
                    if(data.result == 1){
                        showWarnMsg(toastr,"该模型被客户使用不能停用");
                        $rootScope.loading=false;
                    }else{
                        interfaceModelService.saveEditInterfaceModel($scope.editInterfaceModel,$rootScope.modelSettingList).then(function (data){
                            $rootScope.modelSettingList = new Array();
                            $rootScope.loading=false;
                            $state.go('service.interfaceModel');
                            showSuccMsg(toastr,"保存模型成功");
                        },function (data) {
                            commonService.goLoginPage(data.errorCode);
                        })
                    }
                },function(data){
                    commonService.goLoginPage(data.errorCode);
                })
            }else{
                interfaceModelService.saveEditInterfaceModel($scope.editInterfaceModel,$rootScope.modelSettingList).then(function (data){
                    $rootScope.modelSettingList = new Array();
                    $rootScope.loading=false;
                    $state.go('service.interfaceModel');
                    showSuccMsg(toastr,"保存模型成功");
                },function (data) {
                    commonService.goLoginPage(data.errorCode);
                })
            }
        	


        }
        
        
    }

    /*function initData() {
        var initDataArray = [
            {
                id:1,
                interfaceNumber:'jk_001',
                interfaceName:'普通接口1',
                interfaceURL:'http://query1.jsp?authCode=xxxx',
                status:'开放',
                manualURL:'http://interface.info.html',
                description:'普通接口1，供普通用户使用'

            },{
                id:2,
                interfaceNumber:'jk_002',
                interfaceName:'普通接口2',
                interfaceURL:'http://query2.jsp?authCode=xxxx',
                status:'开放',
                manualURL:'http://interface.info.html',
                description:'普通接口1，供普通用户使用'
            },{
                id:3,
                interfaceNumber:'jk_003',
                interfaceName:'VIP接口1',
                interfaceURL:'http://vip.query1.jsp?authCode=xxxx',
                status:'内测',
                manualURL:'http://interface.info.html',
                description:'普通接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:4,
                interfaceNumber:'jk_004',
                interfaceName:'VIP接口2',
                interfaceURL:'http://vip.query2.jsp?authCode=xxxx',
                status:'内测',
                manualURL:'http://interface.info.html',
                description:'普通接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:5,
                interfaceNumber:'jk_005',
                interfaceName:'阿里接口',
                interfaceURL:'http://ali.query1.jsp?authCode=xxxx',
                status:'删除',
                manualURL:'http://interface.info.html',
                description:'阿里专用接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:6,
                interfaceNumber:'jk_006',
                interfaceName:'阿里接口',
                interfaceURL:'http://ali.query1.jsp?authCode=xxxx',
                status:'删除',
                manualURL:'http://interface.info.html',
                description:'阿里专用接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:7,
                interfaceNumber:'jk_007',
                interfaceName:'阿里接口',
                interfaceURL:'http://ali.query1.jsp?authCode=xxxx',
                status:'删除',
                manualURL:'http://interface.info.html',
                description:'阿里专用接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:8,
                interfaceNumber:'jk_005',
                interfaceName:'阿里接口',
                interfaceURL:'http://ali.query1.jsp?authCode=xxxx',
                status:'删除',
                manualURL:'http://interface.info.html',
                description:'阿里专用接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:9,
                interfaceNumber:'jk_005',
                interfaceName:'阿里接口',
                interfaceURL:'http://ali.query1.jsp?authCode=xxxx',
                status:'删除',
                manualURL:'http://interface.info.html',
                description:'阿里专用接口1，供VIP用户使用，现在处于内测时期'
            },{
                id:10,
                interfaceNumber:'jk_005',
                interfaceName:'阿里接口',
                interfaceURL:'http://ali.query1.jsp?authCode=xxxx',
                status:'删除',
                manualURL:'http://interface.info.html',
                description:'阿里专用接口1，供VIP用户使用，现在处于内测时期'
            }
        ];
        return initDataArray;
    }*/
	
	
	//模型参数配置页面
	function modelParamsCtrl($scope,$rootScope){
		$scope.addModelParams = function(){
    		$rootScope.selectDimension.targetCluster = $rootScope.dimension.targetCluster;
    		$rootScope.selectDimension.targetDimensionCode = $rootScope.dimension.targetDimensionCode;
    		$scope.$dismiss();
		}
	}
    //接口界面的操作
    function interfaceManagerCtrl($scope,$state,$http,$uibModal,$rootScope,$timeout,toastr,$interval) {
        var params = new Object();
        params.currentPage=1;
        params.pageSize=15;
        params.status='0';
        $rootScope.$emit("interfaceListss",params);
    }

    //领域页面的操作
    function domainManagerCtrl($scope,$rootScope) {
        //初始化的一些参数
        var params = new Object();
        params.currentPage=1;
        params.pageSize=15;
        params.status=0;
        $rootScope.$emit("domainPageListss",params);
    }


    //维度页面的操作
    function dimensionManagerCtrl($rootScope) {
        //初始化参数
        var params = new Object();
        params.currentPage=1;
        params.pageSize=15;
        params.name=null;
        params.status=0;//默认正常的

        $rootScope.$emit("dimensionPageListss",params);

    }
    //接口模型页面的操作
    function interfaceModelCtrl($rootScope){
    	//初始化参数
    	var params = new Object();
    	params.currentPage = 1;
    	params.pageSize = 15;
    	params.name = null;
    	params.status = 0;
    	
    	
    	$rootScope.$emit("interfaceModelPageListss",params);
    }
    
    //字段管理的方法
    function fieldManagerCtrl($scope,$rootScope,$filter,$uibModal) {

        $scope.fieldStatus=[
            {
                status:0,
                value:'启用'
            },
            {
                status:1,
                value:'停用'
            }
        ];
        $scope.fieldDefaultYN=[
            {
                defaultYN:1,
                value:'是'
            },
            {
                defaultYN:0,
                value:'否'
            }
        ];
        $scope.showFieldDefaultYN=function (defaultYN) {
            var selected = [];
            if(defaultYN ==1 || defaultYN==0) {
                selected = $filter('filter')($scope.fieldDefaultYN, {defaultYN: defaultYN});
            }
            return selected.length ? selected[0].value : '未选择状态';
        };
        $scope.showFieldStatusValue=function (status) {
            var selected = [];
            if(status ==1 || status==0) {
                selected = $filter('filter')($scope.fieldStatus, {status: status});
            }
            return selected.length ? selected[0].value : '未选择状态';
        };

        $scope.saveField = function () {
            var field = $scope.field;
            field.status = '0';
            field.listShowYN=0;
            field.defaultListShowYN = field.defaultListShowYN ? 1 : 0;
        	field.defaultDetailShowYN = field.defaultDetailShowYN ? 1 : 0;
        	field.searchYN = field.searchYN ? 1 : 0;
            $rootScope.$emit("addFieldList",field);
            $scope.$dismiss();
        };
        
        $scope.booleanToInteger = function(obj){
        	obj.defaultListShowYN = obj.defaultListShowYN ? 1 : 0;
        	obj.defaultDetailShowYN = obj.defaultDetailShowYN ? 1 : 0;
        	obj.searchYN = obj.searchYN ? 1 : 0;
        }

        //打开配置字段的模态窗口
        $scope.openFieldSetting = function (data){
            $uibModal.open({
                animation: true,
                templateUrl: 'app/pages/service/field/addField.html',
                size: "md",
                controller:function(){
                    $rootScope.currentFieldList = data;
                }
            });
        }
        //校验字段的唯一性
        $scope.checkFielddOnly = function(type,event){
            if("fieldName"==type){
                if($rootScope.currentFieldList !=null && $rootScope.currentFieldList.length>0){
                    for(var i=0;i<$rootScope.currentFieldList.length;i++){
                        if($scope.field.fieldName == $rootScope.currentFieldList[i].fieldName ){
                            $scope.checkFieldNameResult =true;//已经存在该字段名称
                            $(event.target).css("border-color","#ed7878");
                            $(event.target).parent().addClass("has-error");
                            $(event.target).parent().removeClass("has-success");
                            $(event.target).next().removeClass("ion-checkmark-circled");
                            $(event.target).next().addClass("ion-android-cancel");
                            break;
                        }else{
                            $scope.checkFieldNameResult =false;
                        }
                    }
                }else{
                    $scope.checkFieldNameResult =false;
                }
            }else if("fieldCode"==type){
                if($rootScope.currentFieldList !=null && $rootScope.currentFieldList.length>0){
                    for(var i=0;i<$rootScope.currentFieldList.length;i++){
                        if($scope.field.fieldCode == $rootScope.currentFieldList[i].fieldCode ){
                            $scope.checkFieldCodeResult =true;//已经存在该字段代码
                            $(event.target).css("border-color","#ed7878");
                            $(event.target).parent().addClass("has-error");
                            $(event.target).parent().removeClass("has-success");
                            $(event.target).next().removeClass("ion-checkmark-circled");
                            $(event.target).next().addClass("ion-android-cancel");
                        }else{
                            $scope.checkFieldCodeResult =false;
                        }
                    }
                }else{
                    $scope.checkFieldCodeResult =false;
                }
            }

        }

    }

    //报告
    function reportInfoCtrl($scope,$state,$rootScope,$timeout,$uibModal) {
        $scope.status = 0;
        $scope.typeList=[{code:0, status:'启用'},{code:1, status:'停用'}];
        $scope.$on("searchReport",function (event,data) {
            $scope.searchReport(data);
        });
        //报告查询
        $scope.searchReport = function () {
            $scope.reportlList = [
                 {reportId:1,reportName:"法海涉诉舆情报告",reportDesc:"测试模板1",reportStatus:0}
                ,{reportId:2,reportName:"法海风控涉诉一眼清报告",reportDesc:"测试模板2",reportStatus:0}
                ,{reportId:3,reportName:"涉诉一眼清及企业关联关系报告",reportDesc:"测试模板3",reportStatus:0}
            ];
        }

        var paramData = new Object();
        $scope.$emit("searchReport",paramData);

        //报告新增
        $scope.addReport = function () {
            loading($rootScope,$timeout);
            $state.go('service.addReport');
        }

        //报告修改
        $scope.updateReport = function (report) {
            var params = new Object();
            params.report = report;
            $state.go('service.updateReport',{items:params});
        }

        //报告删除
        $scope.removeReport = function (index,report) {
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
    }
    //新增报告Controller
    function addReportCtrl($scope,toastr,$state) {
        $scope.report = {};
        $scope.saveReport = function (params) {
            showSuccMsg(toastr,"新增成功");
            $state.go('service.report');
        }
    }
    //修改报告Controller
    function updateReportCtrl($scope,toastr,$state,$stateParams,locals) {
        var report = $stateParams.items.report;
        $scope.report = report;
        $scope.saveReport = function (params) {
            showSuccMsg(toastr,"修改成功");
            $state.go('service.report');
        }
    }
})();