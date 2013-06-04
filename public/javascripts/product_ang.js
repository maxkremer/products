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
					controller:'MoreDataController',
					templateUrl: 'assets/partials/view2.html'
				})
		.otherwise({ redirectTo: '/'});
});

productsModule.factory('simpleFactory',function($resource){
	
	var factory = {};
	
	factory.getProducts = function(){
		
		var r = $resource('/productdetails');
		var products = r.query();
		
//		var products = [{id:1, name:"bobs ur uncle",description:"You know"}
//		  ,{id:2, name:"brocoli",description:"green and globulous"}
//		  ,{id:3, name:"orange",description:"spherical citrus"}
//		  ,{id:4, name:"apple",description:"good with cheeses"}
//		  ,{id:5, name:"zippy doo da",description:"blah blah blag"}];
	    return products;
	};
	
	return factory;
});

//Angular Controllers defined here
var controllers1 = {}; 
controllers1.SimpleController = function($scope, simpleFactory){
							$scope.products = simpleFactory.getProducts();
						};

controllers1.MoreDataController = function($scope){
							$scope.products = [{id:1, name:"bobs ur uncle",description:"You know"}
	    				  ,{id:2, name:"brocoli",description:"green and globulous"}
	    				  ,{id:3, name:"orange",description:"spherical citrus"}
	    				  ,{id:4, name:"apple",description:"good with cheeses"}
	    				  ,{id:5, name:"zippy doo da",description:"blah blah blag"}];
						
							$scope.addProduct = function(){
								$scope.products.push( 
									{name: $scope.newProduct.name, description: $scope.newProduct.description});
								};
						};

//Add controllers to module
productsModule.controller(controllers1);

//---------------------------------------- products2Module --------------------------------------------//

//Angular Module definition - depends on ngResource
var products2Module = angular.module('products2Module',['ngResource']);
//Angular Controller
var controllers2 = {}; 

controllers2.SimpleController = function($scope){
					$scope.products = [{id:1, name:"bobs ur uncle",description:"You smell like shit"}
				    				  ,{id:2, name:"thisd",description:"and that"}];
				};
//Angular Controller
controllers2.MoreDataController =  function($scope){
				$scope.products = [{id:1, name:"bobs ur uncle",description:"You know"}
			    				  ,{id:2, name:"brocoli",description:"green and globulous"}
			    				  ,{id:3, name:"orange",description:"spherical citrus"}
			    				  ,{id:4, name:"apple",description:"good with cheeses"}
			    				  ,{id:5, name:"zippy doo da",description:"blah blah blag"}];
				};
//Add controllers to module
products2Module.controller(controllers2);