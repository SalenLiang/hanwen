var appCommonDirectiveModule = angular.module('app.commonDirective', []);


//定义全局变量
appCommonDirectiveModule.constant("Constant",{
	//prefixUrl:"http://localhost:8001/serviceManager",
    prefixUrl:"http://pingtai.fahaicc.com/serviceManager",   //定义了URL请求的前缀

    timeout:10000                                       //url请求超时时间
});

//加载loading动画
function loading($rootScope,$timeout){
    $rootScope.loading=true;    //加载动画
    $timeout(function () {
        closeLoading($rootScope);
    }, 300);
}
//关闭加载loading动画
function closeLoading($rootScope){
    $rootScope.loading=false;
}

//消息提示
function showSuccMsg(toastr,msg) {
    //TODO 消息提示位置
    toastr.clear();
    toastr.success(msg,'',{"timeOut": "1500"});
}

function showErrorMsg(toastr,msg) {
    //TODO 消息提示位置
    toastr.clear();
    toastr.error(msg, '错误',{"timeOut": "1500"});
}

function showWarnMsg(toastr,msg) {
    toastr.clear();
    toastr.warning(msg, '警告',{"timeOut": "1500"});
}

function showInfoMsg(toastr,msg) {
    //TODO 消息提示位置
    toastr.info(msg, 'info');
}

//求和
appCommonDirectiveModule.filter('parseFieldName', function() {
    return function(input) {
        var value = [];
        for (var i=0;i<input.length;i++){
            value.push(input[i].fieldName);
        }
        var result = value.join(",");
        if(result.length>10){
            result = result.substring(0,10)+"...";
        }
        return result;
    }
});

//手机号脱敏,中间4位数进行替换为****
appCommonDirectiveModule.filter('phoneDesensitization', function() {
    return function(phoneNum) {
        phoneNum=phoneNum+"";//phoneNum可能为number类型,需要将number转换成String类型,否则substring报错
        return phoneNum.substring(0,3)+'****'+phoneNum.substring(7);
    }
});

//权限控制的自定义指令
appCommonDirectiveModule.directive('hasPermission', function(permissions) {
    return {
        link : function(scope, element, attrs) {
            //获取到侧边栏的title
            var menuTitle = attrs.hasPermission.trim();
            //登录用户有这个菜单栏就显示，没有就隐藏
            if (permissions.hasPermission(menuTitle)){
                element.show();
            } else {
                element.hide();
            }
        },
        controller: ["$scope", function($scope) {
            /**处理相关逻辑*/
        }]
    };
});

//分页
appCommonDirectiveModule.directive('tmPagination',['$timeout',function($timeout,$scope,toastr){
    return {
        restrict: 'EA',
        template: '<div class="page-list">' +
            '<ul class="pagination" ng-show="conf.totalItems > 0">' +
            '<li ng-class="{disabled: conf.currentPage == 1}" ng-click="prevIndex()"><span>首页</span></li>' +
            '<li ng-class="{disabled: conf.currentPage == 1}" ng-click="prevPage()"><span>上一页</span></li>' +
            '<li ng-repeat="item in pageList track by $index" ng-class="{active: item == conf.currentPage, separate: item == \'...\'}" ' +
            'ng-click="changeCurrentPage(item)">' +
            '<span>{{ item }}</span>' +
            '</li>' +
            '<li ng-class="{disabled: conf.currentPage == conf.numberOfPages}" ng-click="nextPage()"><span>下一页</span></li>' +
            '<li ng-class="{disabled: conf.currentPage == conf.numberOfPages}" ng-click="nextBack()"><span>尾页</span></li>' +
            '</ul>' +
            '<div class="page-total" ng-show="conf.totalItems > 0">' +
/*
            '每页<select ng-model="conf.itemsPerPage" ng-options="option for option in conf.perPageOptions " ng-change="changeItemsPerPage()"></select>' +
*/
            '共<strong>{{ conf.totalItems }}</strong>条 ' +
            '跳转至<input type="text"  ng-model="jumpPageNum" ng-keyup="jumpPageKeyUp($event)"/>页' +
            '</div>' +
            '<div class="no-items" ng-show="conf.totalItems <= 0">暂无数据</div>' +
            '</div>',
        replace: true,
        scope: {
            conf: '='
        },
        link: function(scope, element, attrs) {

            var conf = scope.conf;
            scope.$watch('conf.totalItems', function(value, oldValue) {
                conf = scope.conf;
                if(conf == undefined){
                    return
                }
                // 在无值或值相等的时候，去执行onChange事件
                /*if(!value || value == oldValue) {
                    if(conf.onChange) {
                        conf.onChange();
                    }
                }*/
                getPagination();

                // 获取分页长度
                if(conf.pagesLength) {
                    // 判断一下分页长度
                    conf.pagesLength = parseInt(conf.pagesLength, 10);

                    if(!conf.pagesLength) {
                        conf.pagesLength = defaultPagesLength;
                    }
                    // 分页长度必须为奇数，如果传偶数时，自动处理
                    if(conf.pagesLength % 2 === 0) {
                        conf.pagesLength += 1;
                    }
                } else {
                    conf.pagesLength = defaultPagesLength
                }

                // 分页选项可调整每页显示的条数
                if(!conf.perPageOptions){
                    conf.perPageOptions = defaultPagesLength;
                }
            });


            //当timeout被定义时，它返回一个promise对象
            // var timer = $timeout(
            //     function() {
            //         conf = scope.conf;
            //         if(conf != undefined){
            //             $timeout.cancel( timer );
            //         }
            //     },
            //     100
            // );
            // 默认分页长度
            var defaultPagesLength = 10;

            // 默认分页选项可调整每页显示的条数
            var defaultPerPageOptions = [10, 15, 20, 30, 50];

            // 默认每页的个数
            var defaultPerPage = 15;

            // pageList数组
            function getPagination(newValue, oldValue) {
                 if(conf.currentPage) {
                     conf.currentPage = parseInt(scope.conf.currentPage, 10);
                 }
                if(!conf.currentPage) {
                    conf.currentPage = 1;
                }
                // conf.totalItems
                if(conf.totalItems) {
                    conf.totalItems = parseInt(conf.totalItems, 10);
                }

                // conf.totalItems
                if(!conf.totalItems) {
                    conf.totalItems = 0;
                    return;
                }

                // conf.itemsPerPage
                if(conf.itemsPerPage) {
                    conf.itemsPerPage = parseInt(conf.itemsPerPage, 10);
                }
                if(!conf.itemsPerPage) {
                    conf.itemsPerPage = defaultPerPage;
                }
                // numberOfPages
                conf.numberOfPages = Math.ceil(conf.totalItems/conf.itemsPerPage);

                // 如果分页总数>0，并且当前页大于分页总数
                if(scope.conf.numberOfPages > 0 && scope.conf.currentPage > scope.conf.numberOfPages){
                    scope.conf.currentPage = scope.conf.numberOfPages;
                }

                // 如果itemsPerPage在不在perPageOptions数组中，就把itemsPerPage加入这个数组中
                var perPageOptionsLength = scope.conf.perPageOptions.length;

                // 定义状态
                var perPageOptionsStatus;
                for(var i = 0; i < perPageOptionsLength; i++){
                    if(conf.perPageOptions[i] == conf.itemsPerPage){
                        perPageOptionsStatus = true;
                    }
                }
                // 如果itemsPerPage在不在perPageOptions数组中，就把itemsPerPage加入这个数组中
                if(!perPageOptionsStatus){
                    conf.perPageOptions.push(conf.itemsPerPage);
                }

                // 对选项进行sort
                conf.perPageOptions.sort(function(a, b) {return a - b});

                // 页码相关
                scope.pageList = [];
                if(conf.numberOfPages <= conf.pagesLength){
                    // 判断总页数如果小于等于分页的长度，若小于则直接显示
                    for(i =1; i <= conf.numberOfPages; i++){
                        scope.pageList.push(i);
                    }
                }else{
                    // 总页数大于分页长度（此时分为三种情况：1.左边没有...2.右边没有...3.左右都有...）
                    // 计算中心偏移量
                    var offset = (conf.pagesLength - 1) / 2;
                    if(conf.currentPage <= offset){
                        // 左边没有...
                        for(i = 1; i <= offset + 1; i++){
                            scope.pageList.push(i);
                        }
                        scope.pageList.push('...');
                        scope.pageList.push(conf.numberOfPages);
                    }else if(conf.currentPage > conf.numberOfPages - offset){
                        scope.pageList.push(1);
                        scope.pageList.push('...');
                        for(i = offset + 1; i >= 1; i--){
                            scope.pageList.push(conf.numberOfPages - i);
                        }
                        scope.pageList.push(conf.numberOfPages);
                    }else{
                        // 最后一种情况，两边都有...
                        scope.pageList.push(1);
                        scope.pageList.push('...');

                        for(i = Math.ceil(offset / 2) ; i >= 1; i--){
                            scope.pageList.push(conf.currentPage - i);
                        }
                        scope.pageList.push(conf.currentPage);
                        for(i = 1; i <= offset / 2; i++){
                            scope.pageList.push(conf.currentPage + i);
                        }

                        scope.pageList.push('...');
                        scope.pageList.push(conf.numberOfPages);
                    }
                }
                scope.$parent.conf = conf;
            }

            // prevPage
            scope.prevPage = function() {
                if(conf.currentPage > 1){
                    conf.currentPage -= 1;
                }
                scope.conf.currentPage = conf.currentPage;
                getPagination();
                if(conf.onChange) {
                    conf.onChange();
                }
            };
            //首页
            scope.prevIndex = function () {
                if (scope.conf.currentPage > 1) {
                    scope.conf.currentPage = 1;
                    getPagination();
                    if (conf.onChange) {
                        conf.onChange();
                    }
                }

            };
            //尾页
            scope.nextBack = function () {
                if (scope.conf.currentPage < conf.numberOfPages) {
                    scope.conf.currentPage = conf.numberOfPages ;
                    getPagination();
                    if (conf.onChange) {
                        conf.onChange();
                    }
                }

            };
            // nextPage
            scope.nextPage = function() {
                if(conf.currentPage < conf.numberOfPages){
                    conf.currentPage += 1;
                }
                scope.conf.currentPage = conf.currentPage;
                getPagination();
                if(conf.onChange) {
                    conf.onChange();
                }
            };

            // 变更当前页
            scope.changeCurrentPage = function(item) {

                if(item == '...'){
                    return;
                }else{
                    conf.currentPage = item;
                    scope.conf.currentPage = item;
                    getPagination();
                    // conf.onChange()函数
                    if(conf.onChange) {
                        conf.onChange();
                    }
                }
            };

            // 修改每页展示的条数
            scope.changeItemsPerPage = function() {
                // 一发展示条数变更，当前页将重置为1
                conf.currentPage = 1;
                getPagination();
                // conf.onChange()函数
                if(conf.onChange) {
                    conf.onChange();
                }
            };

            // 跳转页
            scope.jumpToPage = function() {
                var num = scope.jumpPageNum;
                if(num.match(/\d+/)) {
                    num = parseInt(num, 10);

                    if(num && num != conf.currentPage) {
                        if(num > conf.numberOfPages) {
                            num = conf.numberOfPages;
                        }
                        // 跳转
                        conf.currentPage = num;
                        scope.conf.currentPage = num;
                        getPagination();
                        // conf.onChange()函数
                        if(conf.onChange) {
                            conf.onChange();
                        }
                        scope.jumpPageNum = '';
                    }
                }else{
                    showWarnMsg(toastr,"请输入正确的页码");
                }

            };

            scope.jumpPageKeyUp = function(e) {
                var keycode = window.event ? e.keyCode :e.which;

                if(keycode == 13) {
                    scope.jumpToPage();
                }
            };
        }
    };
}]);

function formatJson(json, options) {
    var reg = null,
        formatted = '',
        pad = 0,
        PADDING = '    '; // one can also use '\t' or a different number of spaces

    // optional settings
    options = options || {};
    // remove newline where '{' or '[' follows ':'
    options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
    // use a space after a colon
    options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

    // begin formatting...
    if (typeof json !== 'string') {
        // make sure we start with the JSON as a string
        json = JSON.stringify(json);
    } else {
        // is already a string, so parse and re-stringify in order to remove extra whitespace
        json = JSON.parse(json);
        json = JSON.stringify(json);
    }

    // add newline before and after curly braces
    reg = /([\{\}])/g;
    json = json.replace(reg, '\r\n$1\r\n');

    // add newline before and after square brackets
    reg = /([\[\]])/g;
    json = json.replace(reg, '\r\n$1\r\n');

    // add newline after comma
    reg = /(\,)/g;
    json = json.replace(reg, '$1\r\n');

    // remove multiple newlines
    reg = /(\r\n\r\n)/g;
    json = json.replace(reg, '\r\n');

    // remove newlines before commas
    reg = /\r\n\,/g;
    json = json.replace(reg, ',');

    // optional formatting...
    if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
        reg = /\:\r\n\{/g;
        json = json.replace(reg, ':{');
        reg = /\:\r\n\[/g;
        json = json.replace(reg, ':[');
    }
    if (options.spaceAfterColon) {
        reg = /\:/g;
        json = json.replace(reg, ':');
    }

    $.each(json.split('\r\n'), function(index, node) {
        var i = 0,
            indent = 0,
            padding = '';

        if (node.match(/\{$/) || node.match(/\[$/)) {
            indent = 1;
        } else if (node.match(/\}/) || node.match(/\]/)) {
            if (pad !== 0) {
                pad -= 1;
            }
        } else {
            indent = 0;
        }

        for (i = 0; i < pad; i++) {
            padding += PADDING;
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
};



// 校验表单指令样式
appCommonDirectiveModule.directive('ngFocus', [ function(borderColor) {
    return {
        restrict : 'A',
        require : 'ngModel',
        link : function(scope, element, attrs, ctrl) {
            ctrl.$focused = true;
            element.bind('focus', function(evt) {
                scope.$apply(function() {
                    ctrl.$focused = true;
                });
            }).bind('blur', function(evt) {
                if(ctrl.$invalid){
                    // 验证不通过
                    element.css("border-color","#ed7878");
                    element.parent().addClass("has-error");
                    element.parent().removeClass("has-success");
                    element.next().removeClass("ion-checkmark-circled");
                    element.next().addClass("ion-android-cancel");
                }else{
                    if(Boolean(borderColor)){
                        // 自定义颜色
                        element.css("border-color",borderColor);
                    }else{
                        // 验证通过
                        element.css("border-color","#a6c733");
                    }
                    element.parent().addClass("has-success");
                    element.parent().removeClass("has-error");
                    element.next().addClass("ion-checkmark-circled");
                    element.next().removeClass("ion-android-cancel");
                }
                scope.$apply(function() {
                    ctrl.$focused = false;
                });
            });
        }
    }
} ]);

//只设置输入框颜色，不添加的图标的检验指令
appCommonDirectiveModule.directive('ngFocuss', [ function(borderColor) {
    return {
        restrict : 'A',
        require : 'ngModel',
        link : function(scope, element, attrs, ctrl) {
            ctrl.$focused = true;
            element.bind('focus', function(evt) {
                scope.$apply(function() {
                    ctrl.$focused = true;
                });
            }).bind('blur', function(evt) {
                if(ctrl.$invalid){
                    // 验证不通过
                    element.css("border-color","#ed7878");
                    element.parent().addClass("has-error");
                    element.parent().removeClass("has-success");
                }else{
                    if(Boolean(borderColor)){
                        // 自定义颜色
                        element.css("border-color",borderColor);
                    }else{
                        // 验证通过
                        element.css("border-color","#DCDCDC");
                    }
                    element.parent().addClass("has-success");
                    element.parent().removeClass("has-error");
                }
                scope.$apply(function() {
                    ctrl.$focused = false;
                });
            });
        }
    }
} ]);