/**
 * @author v.lugovksy
 * created on 16.12.2015
 */
(function () {
  'use strict';

  angular.module('BlurAdmin.pages.dashboard')
      .controller('TrafficChartCtrl', TrafficChartCtrl);

  /** @ngInject */
  function TrafficChartCtrl($scope,$rootScope, baConfig, colorHelper,toastr,dashboardService) {

      $scope.transparent = baConfig.theme.blur;
      var dashboardColors = baConfig.colors.dashboard;
      $scope.doughnutData = {
          labels: [] ,
          datasets: [
              {
                  data: [],
                  backgroundColor: [
                      dashboardColors.white,
                      dashboardColors.blueStone,
                      dashboardColors.surfieGreen,
                      dashboardColors.silverTree,
                      dashboardColors.gossip

                  ],
                  hoverBackgroundColor: [
                      colorHelper.shade(dashboardColors.white, 15),
                      colorHelper.shade(dashboardColors.blueStone, 15),
                      colorHelper.shade(dashboardColors.surfieGreen, 15),
                      colorHelper.shade(dashboardColors.silverTree, 15),
                      colorHelper.shade(dashboardColors.gossip, 15)
                  ],
                  percentage: []
              }]
      };

      setTimeout(function(){
          $scope.chartInitTop5();
      },1000)
      //获取Top5数据
      $scope.labels = new Array();
      $scope.percentage = new Array();
      $scope.data = new Array();
      $scope.chartInitTop5 = function(){
          dashboardService.getTop5Data().then(function(data){
              setMessageInnerHTML(data)
          },function(data){
              showWarnMsg(toastr,"获取Top5数据失败");
          })
      }

      function setMessageInnerHTML(data) {
              $('#qsTotal').html('<span>昨日使用总量</span>' + data.qsTotal);
              if (data.qsTotal != 0) {
                  for (var i = 0; i < data.top5List.length; i++) {
                      $scope.labels[i] =data.top5List[i].companyName;
                      $scope.data[i] = data.top5List[i].dayAmount;
                      //$scope.percentage[i] = parseFloat(data.top5List[i].scoreValue*100);
                      var value = parseFloat(data.top5List[i].scoreValue*100).toString();
                      if(value.indexOf(".")!=-1){
                          $scope.percentage[i] = value.substr(0,value.indexOf(".")+3);
                      }else{
                          $scope.percentage[i] = value;
                      }
                  }
                  $scope.doughnutData.labels=  $scope.labels;
                  $scope.doughnutData.datasets[0].data=  $scope.data;
                  $scope.doughnutData.datasets[0].percentage=  $scope.percentage;
              }
              var ctx = document.getElementById('chart-area').getContext('2d');
              window.myDoughnut = new Chart(ctx, {
                  type: 'doughnut',
                  data: $scope.doughnutData,
                  options: {
                      cutoutPercentage: 64,
                      responsive: true,
                      elements: {
                          arc: {
                              borderWidth: 0
                          }
                      }
                  }
              });
      }


  }
})();