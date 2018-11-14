angular.module('BlurAdmin.pages.dashboard')
    .controller('dashboardCtrl',dashboardCtrl);

function dashboardCtrl($scope,$rootScope,$timeout,toastr,dashboardService) {
    $scope.testInterface=function () {
        loading($rootScope,$timeout);
    };

    //分页区
    $scope.paginationConf = {
        currentPage: 1,                                 //当前所在的页（…默认第1页）
        totalItems: 142,                                //总共有多少条记录
        itemsPerPage: 15,                               //每页展示的数据条数（…默认15条）
        pagesLength: 7,                                 //分页条目的长度（如果设置建议设置为奇数）
        perPageOptions: [10, 20, 30, 40, 50],           //可选择显示条数的数组
        onChange: function(){

        }
    };

    //获取interface 预警的数据
    dashboardService.getInterfaceCountData().then(function(data){
        $scope.interfaceCountData = data.dataList;
        //$scope.resultCode = data.result;
    },function(data){
        showWarnMsg(toastr,"获取接口预警数据失败")
    });

    //获取接口的存活状态
    // var sock = new SockJS("http://localhost:8080/sockjs/websocket");
     var sock = new SockJS("/serviceManager/sockjs/websocket");
     sock.onopen = function() {
        console.log("websocket连接成功")
     };
     sock.onmessage = function(evt) {
         $scope.$apply(function () {
             $scope.checkResult = JSON.parse(evt.data);
             if($scope.checkResult.errorCode==0){
                 for(var i=0;i<$scope.interfaceCountData.length;i++){
                     for(var j=0;j<$scope.checkResult.result.length;j++){
                         if($scope.interfaceCountData[i].interfaceId==$scope.checkResult.result[j].interfaceId){
                             $scope.interfaceCountData[i].heartStatus=$scope.checkResult.result[j].heartStatus;
                         }
                     }
                 }
             }
         })
     };
     sock.onclose = function() {
        console.log("websocket连接关闭")
     }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接,防止连接还没有断开就关闭窗口，server端会抛出异常
     window.onbeforeumload = function () {
         if(sock != null){
         sock.close();
     }
     }

}