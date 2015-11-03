console.log("Load MainController")
angular.module("myapp").controller("MainController", ['$rootScope', '$scope', '$http', '$location',
    function ($rootScope, $scope, $http, $location) {

        $http.get('/controller/item/list').then(
                // TODO: Error handling
                function (response) {
                    console.log("Found items");
                    $rootScope.myData.items = response.data;
                }
        );

        $scope.addItem = function (itemSelected) {
            $http.get('/controller/order/item/add', {
                params: {
                    order: $rootScope.orderData.orderId,
                    item: itemSelected
                }
            }).then(
                    function (response) {
                        $rootScope.orderData = response.data;
                    }
            );
        };

        $scope.selectItem = function (itemSelected, itemName) {
            $rootScope.orderData.selection = {};
            $rootScope.orderData.selection.id = itemSelected;
            $rootScope.orderData.selection.name = itemName;
        };

        $scope.removeItem = function (itemToRemove) {
            $http.get('/controller/order/item/remove', {
                params: {
                    order: $rootScope.orderData.orderId,
                    item: itemToRemove
                }
            }).then(
                    function (response) {
                        // TODO: This might be kind of annoying to the user, since their selection will go away and to remove more they have to reselect
                        $rootScope.orderData = response.data;
                    }
            )
        };

        $scope.tenderPayment = function () {
            $location.url('/payment');
        };

        $scope.showOrdersView = function () {
            $location.url('/orders');

            $http.get('/controller/order/list').then(
                    function (response) {
                        console.log("Found Orders");
                        $rootScope.myData.orders = response.data;
                    }
            );
        };
    }]);
