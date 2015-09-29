'use strict';

/* Controllers */


function AppCtrl($scope, $http) {
    $scope.user = null;
}
AppCtrl.$inject = ['$scope', '$http'];


function NavBarController($scope) {
}
NavBarController.$inject = ['$scope'];


/* ==  == ARTICLE CONTROLLERS == ==*/

function ArticleEntriesListCtrl($scope, $http, $location) {
    $scope.entries = null;
    var query;
    var uri = '/api/articles';
    var tag = $location.search().tag;

    if (tag != null) {
        uri = uri + "/by_tag/"+ tag;
    }


    $http({method: 'GET', url: uri})
        .success(function (data, status, headers, config) {
            $scope.entries = new Array();
            $scope.entries = data;
        })
        .error(function (data, status, headers, config) {
            $scope.name = 'Error!'
        });

}
ArticleEntriesListCtrl.$inject = ['$scope', '$http','$location'];

function ArticleCtrl($rootScope, $scope, $routeParams, $http, $location) {
    $scope.entry = null;
    $scope.authorList = [{}];


    if ( $routeParams.id != "" ) {
        loadData();
    }



    $http.get('/api/users/').success(function (items) {
        items.forEach( function(auth) {
            var x = {"name" : auth.first_name +" "+auth.last_name, "id" : auth._id  };
            $scope.authorList.push(x);
        } );
    });



    $scope.save = function() {
        $http.put('/api/articles/', $scope.entry ).success(function (data) {
            $location.path("/articles/");
        });
    }

    $scope.likeIt = function() {
        $http.put('/api/articles/like/'+ $scope.entry._id , {} ).success(function (data) {
            // this could be done on the client
            // but the goal is to see that database is updated.
            loadData();
        });
    }


    function loadData() {
        $http({method: 'GET', url: '/api/articles/' + $routeParams.id})
            .success(function (data, status, headers, config) {
                $scope.entry = data;
            })
            .error(function (data, status, headers, config) {
                $scope.name = 'Error!'
            });
    }

}
ArticleCtrl.$inject = ['$rootScope', '$scope', '$routeParams', '$http', '$location'];





/* == == USER CONTROLLERS  == == */
function UserEntriesListCtrl($scope, $http) {
    $scope.entries = null;
    var query;
    var uri = '/api/users/';

    $http({method: 'GET', url: uri})
        .success(function (data, status, headers, config) {
            $scope.entries = new Array();
            $scope.entries = data;
        })
        .error(function (data, status, headers, config) {
            $scope.name = 'Error!'
        });

}
UserEntriesListCtrl.$inject = ['$scope', '$http'];


function UserCtrl($rootScope, $scope, $routeParams, $http, $location) {
    $scope.entry = null;
    $scope.newUser = ( $routeParams.id == "" );

    if (!$scope.newUser) {
        $http({method: 'GET', url: '/api/users/' + $routeParams.id})
            .success(function (data, status, headers, config) {
                $scope.entry = data;
            })
            .error(function (data, status, headers, config) {
                $scope.name = 'Error!'
            });
    }

    $scope.save = function() {
        $http.put('/api/users/' , $scope.entry ).success(function (data) {
            console.log($scope.entry)
            $location.path("/users/"+ $scope.entry._id);
        });
    }

    $scope.delete = function() {
        $http.delete('/api/users/'+ $scope.entry._id , {} ).success(function (data) {
            $location.path("/users/");
        });
    }

}
UserCtrl.$inject = ['$rootScope', '$scope', '$routeParams', '$http', '$location'];


