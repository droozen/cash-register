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
                    <a ng-click="selectItem('{{theItem.name}}')">
                        {{theItem.name}}, {{theItem.price}}
                    </a>
                </div>

            </div>
            <div style="display: table-cell;">
                <div>
                    <div>
                        <div ng-repeat="lineItem in orderData.lineItems">
                            <div>{{lineItem.type.name}}, {{lineItem.qty}} x {{lineItem.price}}</div>
                            <div>----- {{lineItem.extendedPrice}}</div>
                        </div>
                    </div>
                    <div>
                        <div>Sub Total: {{orderData.subTotal}}</div>
                        <div>Tax: {{orderData.totalTax}}</div>
                        <div>Grand Total: {{orderData.grandTotal}}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</div>

<script>

    angular.module("myapp", [])
            .controller("OrderController", function ($scope, $http) {
                $scope.myData = {};
                $scope.orderData = {};

                $http.get('/controller/item/list').then(
                        function (response) {
                            console.log("Found items");
                            $scope.myData.items = response.data;
                        },

                        function (error) {
                            console.error(error);
                        }
                );

                $http.get('/controller/order/new').then(
                        function (response) {
                            console.log("Found new order");
                            $scope.orderData = response.data;
                        }
                )

                $scope.selectItem = function (itemSelected) {
                    $http.get('/controller/order/select', {order: $scope.orderData.orderId, item: itemSelected}).then(
                            function (response) {
                                $scope.myData.order.items = response.data;
                            }
                    );
                };
            });
</script>

</body>

</html>