console.log("Load PaymentController");
angular.module("myapp").controller("PaymentController", ['$rootScope', '$scope', '$http', '$location',
    function ($rootScope, $scope, $http, $location) {

        $scope.cancelTenderPayment = function () {
            $location.url('/main');
        };

        // Another approach could be to do the calculation of the change here, but I thought one place in the code should own that
        // and this isn't very expensive to do, at the moment.
        $scope.updateTender = function () {
            $http.get('/controller/tender/change', {
                params: {
                    order: $rootScope.orderData.orderId,
                    tender: $rootScope.myData.tender
                }
            }).then(
                    function (response) {
                        $rootScope.orderData = response.data;
                    }
            )
        };

        $scope.completeOrder = function () {
            $http.get('/controller/order/assign', {
                params: {
                    order: $rootScope.orderData.orderId
                }
            }).then(
                    function (response) {
                        $http.get('/controller/tender/complete', {
                            params: {
                                order: $rootScope.orderData.orderId,
                                tender: $rootScope.myData.tender
                            }
                        }).then(
                                function (response) {
                                    $rootScope.orderData = response.data;
                                    $location.url('/main');
                                }
                        );
                    }
            );
        };
    }]);
