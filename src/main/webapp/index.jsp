<!DOCTYPE html>
<html lang="en">


<head>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular.min.js"></script>
</head>

<body ng-app="myapp">

<!-- TODO: We should componentize and split these controllers out onto their own pages. -->
<div ng-controller="OrderController">

    <div style="width: 100%; display: table;" ng-show="myData.mainView">
        <div style="display: table-row">
            <div style="width: 300px; display: table-cell;">

                <div ng-repeat="theItem in myData.items">
                    <a ng-click="addItem(theItem.id)">
                        {{theItem.name}}, {{theItem.price}}
                    </a>
                </div>

            </div>
            <div style="display: table-cell;">
                <div ng-init="voidDisabled = true">
                    <div>
                        <div ng-repeat="lineItem in orderData.lineItems"
                             ng-click="selectItem(lineItem.type.id, lineItem.type.name)">
                            <div>{{lineItem.type.name}}, {{lineItem.qty}} x {{lineItem.price}}</div>
                            <div>----- {{lineItem.extendedPrice}}</div>
                        </div>
                    </div>
                    <div>
                        <div>Sub Total: {{orderData.subTotal}}</div>
                        <div>Tax: {{orderData.totalTax}}</div>
                        <div>Grand Total: {{orderData.grandTotal}}</div>
                        <button ng-model="button"
                                ng-disabled="orderData.selection == undefined"
                                ng-click="removeItem(orderData.selection.id)">Void
                        </button>
                        <button ng-model="button" ng-click="tenderPayment()">Pay Now</button>
                    </div>
                    <div style="padding-top: 20px;" ng-if="orderData.selection">
                        Selected: {{orderData.selection.name}}
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div style="width: 100%;" ng-show="myData.paymentView">
        <h2 style="padding-bottom: 50px;">Tender Payment</h2>

        <div style="display: table-row">
            <div style="display: table-cell;">
                Amount Due:
            </div>
            <div style="display: table-cell;">
                {{orderData.grandTotal}}
            </div>
        </div>
        <div style="display: table-row;">
            <div style="display: table-cell;">
                Amount Tendered:
            </div>
            <div style="display: table-cell;">
                <input type="text" ng-model="myData.tender" ng-change="updateTender()">
            </div>
        </div>
        <div style="display: table-row;">
            <div style="display: table-cell;">
                Change Due:
            </div>
            <dvi style="display: table-cell;">
                {{orderData.tenderRecord.changeGiven}}
            </dvi>
        </div>
        <div style="display: table-row;">
            <div style="display: table-cell;">
                <button ng-click="cancelTenderPayment()">Cancel</button>
            </div>
            <div style="display: table-cell;">
                <button ng-click="">Tender</button>
            </div>
        </div>
    </div>
</div>

<script>

    angular.module("myapp", [])
            .controller("OrderController", function ($scope, $http) {
                $scope.myData = {mainView: true};
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

                $http.get('/controller/order/create').then(
                        function (response) {
                            console.log("Found new order");
                            $scope.orderData = response.data;
                        }
                );

                $scope.addItem = function (itemSelected) {
                    $http.get('/controller/order/item/add', {
                        params: {
                            order: $scope.orderData.orderId,
                            item: itemSelected
                        }
                    }).then(
                            function (response) {
                                $scope.orderData = response.data;
                            }
                    );
                };

                $scope.selectItem = function (itemSelected, itemName) {
                    $scope.orderData.selection = {};
                    $scope.orderData.selection.id = itemSelected;
                    $scope.orderData.selection.name = itemName;
                };

                $scope.removeItem = function (itemToRemove) {
                    $http.get('/controller/order/item/remove', {
                        params: {
                            order: $scope.orderData.orderId,
                            item: itemToRemove
                        }
                    }).then(
                            function (response) {
                                // TODO: This might be kind of annoying to the user, since their selection will go away and to remove more they have to reselect
                                $scope.orderData = response.data;
                            }
                    )
                };

                $scope.tenderPayment = function () {
                    $scope.myData.mainView = false;
                    $scope.myData.paymentView = true;
                };

                $scope.cancelTenderPayment = function () {
                    $scope.myData.paymentView = false;
                    $scope.myData.mainView = true;
                };

                // Another approach could be to do the calculation of the change here, but I thought one place in the code should own that
                // and this isn't very expensive to do, at the moment.
                $scope.updateTender = function () {
                    $http.get('/controller/order/tender/change', {
                        params: {
                            order: $scope.orderData.orderId,
                            tender: $scope.myData.tender
                        }
                    }).then(
                            function (response) {
                                $scope.orderData = response.data;
                            }
                    )
                };
            });
</script>

</body>

</html>