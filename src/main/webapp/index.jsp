<!DOCTYPE html>
<html lang="en">


<head>
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.2.5/angular.min.js"></script>
</head>

<body ng-app="myapp">

<div ng-controller="OrderController">

    <div ng-repeat="theItem in myData.items">
        <a onClick="selectItem(); return false;"
           data-name="{{theItem.name}}"
           data-price="{{theItem.price}}">
            {{theItem.name}}, {{theItem.price}}
        </a>
    </div>

</div>

<script>

    angular.module("myapp", [])
            .controller("OrderController", function ($scope, $http) {
                console.log("Fetching items");
                $scope.myData = {};
                $http.get('/controller/item/list').then(
                        function (response) {
                            $scope.myData.items = response.data;
                        },

                        function (error) {
                            console.error(error);
                        }
                );
            });
</script>

</body>

</html>