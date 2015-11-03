console.log("Load OrderController");
angular.module("myapp").controller("OrderController", ['$rootScope', '$scope', '$http', '$location',
    function ($rootScope, $scope, $http, $location) {
        $scope.createNewOrder = function () {
            createNewOrder($rootScope, $http, $location);
        }

        $scope.selectOrder = function (orderSelected) {
            $http.get('/controller/order/find', {
                params: {
                    order: orderSelected
                }
            }).then(
                    function (response) {
                        $rootScope.orderData = response.data;
                        $location.url('/main');
                    }
            );
        };

        $scope.statusFilterFn = function (order) {
            var statusCode = order.statusCode;
            if (statusCode == 'INPROGRESS') {
                return $rootScope.myData.statusFilter.inprogress;
            } else if (statusCode == 'UNPAID') {
                return $rootScope.myData.statusFilter.unpaid;
            } else if (statusCode == 'PAID') {
                return $rootScope.myData.statusFilter.paid;
            }
            return false;
        };

        $scope.toggleInProgress = function () {
            $rootScope.myData.statusFilter.inprogress = !$rootScope.myData.statusFilter.inprogress;
            updateAllStatus($scope);
        };

        $scope.toggleUnpaid = function () {
            $rootScope.myData.statusFilter.unpaid = !$rootScope.myData.statusFilter.unpaid;
            updateAllStatus($scope);
        };

        $scope.togglePaid = function () {
            $rootScope.myData.statusFilter.paid = !$rootScope.myData.statusFilter.paid;
            updateAllStatus($rootScope);
        };

        $scope.toggleAll = function () {
            $rootScope.myData.statusFilter.all = !$rootScope.myData.statusFilter.all;
            $rootScope.myData.statusFilter.inprogress = $rootScope.myData.statusFilter.all;
            $rootScope.myData.statusFilter.unpaid = $rootScope.myData.statusFilter.all;
            $rootScope.myData.statusFilter.paid = $rootScope.myData.statusFilter.all;
        }
    }]);

function updateAllStatus(scope) {
    if (scope.myData.statusFilter.unpaid &&
            scope.myData.statusFilter.paid &&
            scope.myData.statusFilter.inprogress) {
        scope.myData.statusFilter.all = true;
    } else {
        scope.myData.statusFilter.all = false;
    }
}
