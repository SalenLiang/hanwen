/**
 *公共常量方法
 */
var appCommonServiceModule = angular.module('app.commonService', []);

appCommonServiceModule.filter('numformt', function() {  
   return function(num) {  
   		if(!isNaN(num)){
   			return parseInt(num).toLocaleString();
   		}else{
   			return 0;
   		}
   };  
});

appCommonServiceModule.factory('locals',['$window',function($window){
    return{        //存储单个属性
        set :function(key,value){
            $window.localStorage[key]=value;
        },        //读取单个属性
        get:function(key,defaultValue){
            return  $window.localStorage[key] || defaultValue;
        },        //存储对象，以JSON格式存储
        setObject:function(key,value){
            $window.localStorage[key]=JSON.stringify(value);
        },        //读取对象
        getObject: function (key) {
            return JSON.parse($window.localStorage[key] || '{}');
        }

    }
}]);
appCommonServiceModule.factory('sessionLocals',['$window',function($window){
    return{        //存储单个属性
        set :function(key,value){
            $window.sessionStorage[key]=value;
        },        //读取单个属性
        get:function(key,defaultValue){
            return  $window.sessionStorage[key] || defaultValue;
        },        //存储对象，以JSON格式存储
        setObject:function(key,value){
            $window.sessionStorage[key]=JSON.stringify(value);
        },        //读取对象
        getObject: function (key) {
            return JSON.parse($window.sessionStorage[key] || '{}');
        }

    }
}]);
 
appCommonServiceModule.factory('commonService', function($http, $rootScope, $compile, $state, $q, $uibModal, toastr) {

	$rootScope.errMsg = {};
    var commonService = new Object();

    // 弹框
    commonService.open = function(page, size) {
        $uibModal.open({
            animation: true,
            templateUrl: page,
            size: size
        });
    };
    
    //跳转到登陆页
    commonService.goLoginPage = function(status){
    	console.log(status);
    	$rootScope.loading = false;
        if(status == 1){
            commonService.showWarningMsg("请求超时");
        }else if(status == 405 || status == 302){
    		commonService.showWarningMsg(status+"登陆超时，请重新登陆！");
    		setTimeout(function(){window.location.href = "/";},1000);
    	}else if(status == 200 || !Boolean(status)){
    		commonService.showWarningMsg("登陆超时，请重新登陆！");
    		setTimeout(function(){window.location.href = "/";},1000);
    	}
    }
    
    commonService.goLoginIndex = function(){
    	$rootScope.wait = false;
		commonService.showWarningMsg("登陆超时，请重新登陆！");
		setTimeout(function(){window.location.href = "/";},1000);
    }
    
	// 成功
    commonService.showSuccessMsg = function(msg) {
		toastr.success(msg);
    };
    
    // 错误
    commonService.showErrorMsg = function(msg) {
        toastr.error(msg, 'Error');
    };
    
    // 一般提醒
    commonService.showInfoMsg = function(msg) {
		toastr.info(msg, 'Information');
	};
	
	// 警告
	commonService.showWarningMsg = function(msg) {
		toastr.warning(msg, 'Warning');
	};
    
    //分页
    commonService.getPageNo = function(data) {
        data.pageCount = parseInt((data.totalsCount + data.pageSize - 1) / data.pageSize);
        var pageNoArray = new Array();
        var icount = 0;

        //没有可显示的项目
        if (data.totalsCount == 0) {
            data.pageShow = 0;
        } else {
            data.pageShow = 1;
            //页号越界处理
            if (data.currentPage > data.pageCount) {
                data.currentPage = data.pageCount;
            }
            if (data.currentPage < 1) {
                data.currentPage = 1;
            }

            //如果前面页数过多,显示"..."拿0代替
            var start = 1;
            if (data.currentPage > 4) {
                start = data.currentPage - 1;
                pageNoArray[icount++] = 1;
                pageNoArray[icount++] = 2;
                pageNoArray[icount++] = '-1';
            }
            //显示当前页附近的页
            var end = data.currentPage + 1;
            if (end > data.pageCount) {
                end = data.pageCount;
            }
            for (var i = start; i <= end; i++) {
                pageNoArray[icount++] = i;
            }
            //如果后面页数过多,显示"..."拿0代替
            if (end < data.pageCount - 2) {
                pageNoArray[icount++] = '-2'
            }
            if (end < data.pageCount - 1) {
                pageNoArray[icount++] = data.pageCount - 1;
            }
            if (end < data.pageCount) {
                pageNoArray[icount++] = data.pageCount;
            }
        }
        data.pageList = pageNoArray;
        return data;
    }

	//格式化json
	commonService.syntaxHighlight = function(json) {
	    if (typeof json != 'string') {
	        json = JSON.stringify(json, undefined, 2);
	    }
	    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
	    var style = '<style>pre {font-size: 16px; }.string { color: green; }.number { color: #209e91; }.boolean { color: blue; }.null { color: magenta; }.key { color: red; }</style>';
	    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function(match) {
	        var cls = 'number';
	        if (/^"/.test(match)) {
	            if (/:$/.test(match)) {
	                cls = 'key';
	            } else {
	                cls = 'string';
	            }
	        } else if (/true|false/.test(match)) {
	            cls = 'boolean';
	        } else if (/null/.test(match)) {
	            cls = 'null';
	        }
	        return '<span class="' + cls + '">' + match + '</span>';
	    }) + style;
	}

    return commonService;
});

	
