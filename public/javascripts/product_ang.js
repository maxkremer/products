//Angular Module definitions
//---------------------------------------- productsModule --------------------------------------------//

var productsModule = angular.module('productsModule',['ngResource']);

productsModule.config(function($routeProvider){
	$routeProvider
		.when('/',
				{
					controller:'SimpleController',
					templateUrl: 'assets/partials/view1.html'
				})
		.when('/view2',
				{
					controller:'SimpleController',
					templateUrl: 'assets/partials/view2.html'
				})
		.otherwise({ redirectTo: '/'});
});

productsModule.factory('simpleFactory',function($resource){
	
	var factory = {};
	
	factory.getProducts = function(){
		
		var products=[];
        var r = $resource('/productdetails');
        products = r.query();
	    return products;
	};

    factory.getExperiences = function(){
        var experiences;
        experiences = {
            project: "Autodesk Inventor Online",
            days: [
                {dayid: 1,
                    daywidgets: [
                        {widgetid: 1, widgetname: "Email Widget", widgetconfig: "This is how the diget is configured"},
                        {widgetid: 2, widgetname: "Facebook Widget", widgetconfig: "This is how the diget is configured"},
                        {widgetid: 3, widgetname: "Salesforce Widget", widgetconfig: "This is how the diget is configured"},
                        {widgetid: 4, widgetname: "Another Widget", widgetconfig: "This is how the diget is configured"}
                    ]},
                {dayid: 2,
                    daywidgets: [
                        {widgetid: 1, widgetname: "Email Widget", widgetconfig: "This is how the diget is configured"},
                        {widgetid: 2, widgetname: "Facebook Widget", widgetconfig: "This is how the diget is configured"},
                        {widgetid: 3, widgetname: "Salesforce Widget", widgetconfig: "This is how the diget is configured"},
                        {widgetid: 4, widgetname: "Another Widget", widgetconfig: "This is how the diget is configured"}
                    ]

                }
            ]
        };
        return experiences;
    };
	
	return factory;
});

//Angular Controllers defined here
var controllers1 = {}; 
controllers1.SimpleController = function($scope, $http, simpleFactory){
							$scope.products = simpleFactory.getProducts();

                            $scope.experiences = simpleFactory.getExperiences();

                            $scope.restTest = function(){
                                //TODO make this use $resource
                                $http.get('/productdetails').success(function(data){
                                    $scope.restTestData  = data;
                                    $scope.products = data;
                                });
                            };
                            $scope.addProduct = function(){
                                //TODO make this use $resource
                                $http({
                                    url: "/products/new",
                                    method: "POST",
                                    data: $scope.newProduct
                                }).success(function(data, status, headers, config){

                                        $scope.products = simpleFactory.getProducts();
                                        $scope.message = "Database Updated";
                                    });
                            };
                            $scope.deleteProduct = function(id){
                                var url = "/products/"+id;
                                //TODO make this use $resource
                                $http.delete(url).success(function(data, status, headers, config){
                                    $scope.products = simpleFactory.getProducts();
                                    $scope.message = "Database Updated";
                                });

                            };


};

controllers1.MoreDataController = function($scope ){
							//dummy controller for later
						

						};

//Add controllers to module
productsModule.controller(controllers1);

