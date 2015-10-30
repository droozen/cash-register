<!DOCTYPE html>
<html lang="en">


<head>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular.min.js"></script>
</head>

<body ng-app="myapp">

<div ng-controller="OrderController">

    <div style="width: 100%; display: table;">
        <div style="display: table-row">
            <div style="width: 300px; display: table-cell;">

                <div ng-repeat="theItem in myData.items">
                    <a ng-click="selectItem({{theItem.name}})">
                        {{theItem.name}}, {{theItem.price}}
                    </a>
                </div>

            </div>
            <div style="display: table-cell;">

                <div>
                    <div style="height: 500px;">
                        <div ng-repeat="lineItem in orderData.lineItems">
                            <a ng-click="selectLineItem({{lineItem.name}})">
                                {{lineItem.name}}, {{lineItem.extendedPrice}}
                            </a>
                        </div>
                    </div>
                    <div style="height: 100px;">
                        <div>{{orderData.subTotal}}</div>
                        <div>{{orderData.totalTax}}</div>
                        <div>{{orderData.grandTotal}}</div>
                    </div>
                </div>

            </div>
        </div>
    </div>

</div>

<script>

    angular.module("myapp", [])
            .controller("OrderController", function ($scope, $http) {
                console.log("Fetching items");
                $scope.myData = {};
                $scope.orderData = {};

                $http.get('/controller/item/list').then(
                        function (response) {
                            $scope.myData.items = response.data;
                        },

                        function (error) {
                            console.error(error);
                        }
                );

                $http.get('/controller/order/new').then(
                        function (response) {
                            $scope.orderData = response.data;
                        }
                )

                $scope.selectItem = function (itemSelected) {
                    $http.get('/controller/order/select', {order: order, item: itemSelected}).then(
                            function (response) {
                                $scope.myData.order.items = response.data;
                            }
                    );
                };
            });

    function selectItem() {

    }
</script>

</body>

</html>