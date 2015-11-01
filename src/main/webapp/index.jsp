<!DOCTYPE html>
<html lang="en">


<head>
    <link rel="stylesheet" type="text/css" href="assets/css/style.css">
    <!-- TODO: In production we should actually download a specific version of angular and serve it ourselves for more control, or at least specify a version number -->
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular.min.js"></script>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"
          integrity="sha512-dTfge/zgoMYpP7QbHy4gWMEGsbsdZeCXz7irItjcC3sPUFtf0kuFbDz/ixG7ArTxmDjLXDmezHubeNikyKGVyQ=="
          crossorigin="anonymous">

    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>

<body ng-app="myapp">

<!-- TODO: We should componentize and split these controllers out onto their own pages. -->
<div ng-controller="OrderController">

    <div style="width: 100%; display: table;" ng-show="myData.mainView">
        <div style="display: table-row">
            <div style="display: table-cell" class="col-md-4">
            </div>
            <div style="display: table-cell" class="col-md-4">
                <button style="padding-right: 100px;" class="btn" ng-click="showOrdersView()">Orders</button>
            </div>
        </div>
        <div style="display: table-row">
            <div style="display: table-cell;" class="col-md-4">

                <div ng-repeat="theItem in myData.items">
                    <a ng-click="addItem(theItem.id)">
                        {{theItem.name}}, {{theItem.price | currency}}
                    </a>
                </div>

            </div>
            <div style="display: table-cell;" class="col-md-4">
                <div ng-init="voidDisabled = true">
                    <div ng-if="orderData.orderNumber">
                        Order # {{orderData.orderNumber}}
                    </div>
                    <div>
                        <div ng-repeat="lineItem in orderData.lineItems"
                             ng-click="selectItem(lineItem.type.id, lineItem.type.name)">
                            <div>{{lineItem.type.name}}, {{lineItem.qty}} x {{lineItem.price | currency}}</div>
                            <div>----- {{lineItem.extendedPrice | currency}}</div>
                        </div>
                    </div>
                    <div>
                        <div>Sub Total: {{orderData.subTotal | currency}}</div>
                        <div>Tax: {{orderData.totalTax | currency}}</div>
                        <div>Grand Total: {{orderData.grandTotal | currency}}</div>
                        <div ng-if="orderData.tenderRecord">Amount Tendered: {{orderData.tenderRecord.amountTendered |
                            currency}}
                        </div>
                        <div ng-if="orderData.tenderRecord">Change Given: {{orderData.tenderRecord.changeGiven |
                            currency}}
                        </div>
                        <button class="btn"
                                ng-disabled="orderData.selection == undefined"
                                ng-click="removeItem(orderData.selection.id)">Void
                        </button>
                        <button class="btn" ng-click="tenderPayment()">Pay Now</button>
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
                {{orderData.grandTotal | currency}}
            </div>
        </div>
        <div style="display: table-row;">
            <div style="display: table-cell;">
                Amount Tendered:
            </div>
            <div style="display: table-cell;">
                $<input type="text" ng-model="myData.tender" ng-change="updateTender()">
            </div>
        </div>
        <div style="display: table-row;">
            <div style="display: table-cell;">
                Change Due:
            </div>
            <dvi style="display: table-cell;">
                {{orderData.tenderRecord.changeGiven | currency}}
            </dvi>
        </div>
        <div style="display: table-row;">
            <div style="display: table-cell;">
                <button ng-click="cancelTenderPayment()" class="btn">Cancel</button>
            </div>
            <div style="display: table-cell;">
                <button ng-click="completeOrder()" class="btn"
                        ng-disabled="!myData.tender || myData.tender < orderData.grandTotal">Tender
                </button>
            </div>
        </div>
    </div>

    <div style="width: 100%;" ng-show="myData.ordersView">
        <div class="col-md-12" style="padding: 10px;">
            <button ng-click="toggleAll()" class="btn"
                    ng-class="{true: 'btn-primary', false: 'btn-danger'}[myData.statusFilter.all]">ALL
            </button>
            <button ng-click="toggleInProgress()" class="btn"
                    ng-class="{true: 'btn-primary', false: 'btn-danger'}[myData.statusFilter.inprogress]">INPROGRESS
            </button>
            <button ng-click="toggleUnpaid()" class="btn"
                    ng-class="{true: 'btn-primary', false: 'btn-danger'}[myData.statusFilter.unpaid]">UNPAID
            </button>
            <button ng-click="togglePaid()" class="btn"
                    ng-class="{true: 'btn-primary', false: 'btn-danger'}[myData.statusFilter.paid]">PAID
            </button>
            <button style="float: right;" ng-click="createNewOrder()">NEW ORDER</button>
        </div>

        <table class="col-md-12 table">
            <tr>
                <th>Status</th>
                <th>Timestamp</th>
                <th>Order Number</th>
                <th>Total</th>
            </tr>
            <tr ng-repeat="theOrder in myData.orders | filter:statusFilterFn" ng-click="selectOrder(theOrder.orderId)">
                <td>{{theOrder.statusCode}}</td>
                <td>{{theOrder.timestamp}}</td>
                <td>{{theOrder.orderNumber}}</td>
                <td>{{theOrder.grandTotal | currency}}</td>
            </tr>
        </table>
    </div>

</div>

<script>

    angular.module("myapp", [])
            .controller("OrderController", function ($scope, $http) {
                $scope.myData = {
                    mainView: true,
                    statusFilter: {
                        all: true,
                        inprogress: true,
                        unpaid: true,
                        paid: true
                    }
                };
                $scope.orderData = {};

                $http.get('/controller/item/list').then(
                        function (response) {
                            console.log("Found items");
                            $scope.myData.items = response.data;
                        }
                );


                $scope.createNewOrder = function () {
                    $http.get('/controller/order/create').then(
                            function (response) {
                                console.log("Found new order");
                                $scope.orderData = response.data;
                                $scope.myData.ordersView = false;
                                $scope.myData.mainView = true;
                            }
                    );
                };

                $scope.createNewOrder();

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
                    $http.get('/controller/tender/change', {
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

                $scope.completeOrder = function () {
                    $http.get('/controller/order/assign', {
                        params: {
                            order: $scope.orderData.orderId
                        }
                    }).then(
                            function (response) {
                                $http.get('/controller/tender/complete', {
                                    params: {
                                        order: $scope.orderData.orderId,
                                        tender: $scope.myData.tender
                                    }
                                }).then(
                                        function (response) {
                                            $scope.orderData = response.data;
                                            $scope.myData.paymentView = false;
                                            $scope.myData.mainView = true;
                                        }
                                );
                            }
                    );
                };

                $scope.showOrdersView = function () {
                    $scope.myData.mainView = false;
                    $scope.myData.ordersView = true;

                    $http.get('/controller/order/list').then(
                            function (response) {
                                console.log("Found Orders");
                                $scope.myData.orders = response.data;
                            }
                    );
                };

                $scope.selectOrder = function (orderSelected) {
                    $http.get('/controller/order/find', {
                        params: {
                            order: orderSelected
                        }
                    }).then(
                            function (response) {
                                $scope.orderData = response.data;
                                // TODO: We need a more frameworky way to changing screens.
                                $scope.myData.ordersView = false;
                                $scope.myData.mainView = true;
                            }
                    );
                };

                $scope.statusFilterFn = function (order) {
                    var statusCode = order.statusCode;
                    if (statusCode == 'INPROGRESS') {
                        return $scope.myData.statusFilter.inprogress;
                    } else if (statusCode == 'UNPAID') {
                        return $scope.myData.statusFilter.unpaid;
                    } else if (statusCode == 'PAID') {
                        return $scope.myData.statusFilter.paid;
                    }
                    return false;
                };

                $scope.toggleInProgress = function () {
                    $scope.myData.statusFilter.inprogress = !$scope.myData.statusFilter.inprogress;
                    updateAllStatus($scope);
                };

                $scope.toggleUnpaid = function () {
                    $scope.myData.statusFilter.unpaid = !$scope.myData.statusFilter.unpaid;
                    updateAllStatus($scope);
                };

                $scope.togglePaid = function () {
                    $scope.myData.statusFilter.paid = !$scope.myData.statusFilter.paid;
                    updateAllStatus($scope);
                };

                $scope.toggleAll = function () {
                    $scope.myData.statusFilter.all = !$scope.myData.statusFilter.all;
                    $scope.myData.statusFilter.inprogress = $scope.myData.statusFilter.all;
                    $scope.myData.statusFilter.unpaid = $scope.myData.statusFilter.all;
                    $scope.myData.statusFilter.paid = $scope.myData.statusFilter.all;
                }
            });

    function updateAllStatus(scope) {
        if (scope.myData.statusFilter.unpaid &&
                scope.myData.statusFilter.paid &&
                scope.myData.statusFilter.inprogress) {
            scope.myData.statusFilter.all = true;
        } else {
            scope.myData.statusFilter.all = false;
        }
    }

</script>

</body>

</html>