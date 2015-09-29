'use strict';

// Declare app level module which depends on filters, and services
var app = angular.module('myApp', ['myApp.filters', 'myApp.services', 'myApp.directives','ngResource', 'ngSanitize']).
    config(['$routeProvider', '$locationProvider', function($routeProvider, $locationProvider) {


        $routeProvider.
            when('/', {templateUrl: 'partials/article_list.html', controller: ArticleEntriesListCtrl }).
            when('/articles/', {templateUrl: 'partials/article_list.html', controller: ArticleEntriesListCtrl }).
            when('/articles/edit/:id', {templateUrl: 'partials/article_form.html', controller: ArticleCtrl }).
            when('/articles/:id', {templateUrl: 'partials/article_view.html', controller: ArticleCtrl }).
            when('/users/', {templateUrl: 'partials/user_list.html', controller: UserEntriesListCtrl }).
            when('/users/edit/:id', {templateUrl: 'partials/user_form.html', controller: UserCtrl }).
            when('/users/:id', {templateUrl: 'partials/user_view.html', controller: UserCtrl }).
            otherwise({redirectTo: '/'});

    }]);

