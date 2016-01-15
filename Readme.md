$scope.processSale = function(username,password,orderid,amount){
    if(username && password && orderid && parseFloat(amount)){
      console.log('Calling plugin now');
      cordova.plugins.MiuraMosambee.sale(amount,orderid,username,password, function(result){
          console.log(result);
          $scope.paymentResult.result = result.reason;
      }, function(err){
          $scope.paymentResult.result = 'ERROR';
      })
    }else{
      alert('Enter all details')
    }
  }
})