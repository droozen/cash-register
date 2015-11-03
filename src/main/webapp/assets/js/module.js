console.log("Load Module");
angular.module("myapp", ['ngRoute'], function ($routeProvider) {
    console.log("Configuration");
    $routeProvider
            .when('/main',
            {
                controller: 'MainController',
                templateUrl: '/assets/partial/main.html'
            })
            .when('/payment',
            {
                controller: 'PaymentController',
                templateUrl: '/assets/partial/payment.html'
            })
            .when('/orders',
            {
                controller: 'OrderController',
                templateUrl: '/assets/partial/orders.html'
            })
            .otherwise({
                controller: 'MainController',
                templateUrl: '/assets/partial/main.html'
            });
    console.log("End Configuration");
});

angular.module("myapp").run(function ($rootScope, $http, $location) {
    $rootScope.myData = {
        statusFilter: {
            all: true,
            inprogress: true,
            unpaid: true,
            paid: true
        }
    };

    $rootScope.orderData = {};

    createNewOrder($rootScope, $http, $location);
});

function createNewOrder($rootScope, $http, $location) {
    $http.get('/controller/order/create').then(
            function (response) {
                console.log("Found new order");
                $rootScope.orderData = response.data;
                $location.url('/main');
            }
    );
}