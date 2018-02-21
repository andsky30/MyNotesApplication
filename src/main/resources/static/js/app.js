'use strict';

var app=angular
    .module('app', ['ngRoute', 'ngResource', 'notesModule','usersModule', 'birthdaysModule'])
    .config(function ($routeProvider, $httpProvider) {
        $routeProvider
            .when('/register', {
                templateUrl: '/partials/register.html',
                controller: 'RegisterController',
                controllerAs: 'registerCtrl'
            })
            .when('/login', {
                templateUrl: '/partials/login.html',
                controller: 'AuthenticationController',
                controllerAs: 'authController'
            })
            .when('/profile', {
                templateUrl: '/partials_user/profile.html'
            })
            .when('/profile/show_users', {
                templateUrl: '/partials_admin/users.html',
                controller: 'ShowUsersController',
                controllerAs: 'showUsersCtrl'
            })
            .when('/profile/show_users/details/:id', {
                templateUrl: '/partials_admin/user_edit.html',
                controller: 'ShowUsersController',
                controllerAs: 'showUsersCtrl'
            })
            .when('/profile/show_users/delete_user/:id', {
                templateUrl: '/partials_admin/user_delete.html',
                controller: 'ShowUsersController',
                controllerAs: 'showUsersCtrl'
            })
            .when('/profile/my_account', {
                templateUrl: '/partials_user/account/user_account.html',
                controller: 'AccountController',
                controllerAs: 'accountCtrl'
            })
            .when('/profile/my_account/account/delete', {
                templateUrl: '/partials_user/delete_account.html',
                controller: 'AccountController',
                controllerAs: 'accountCtrl'
            })
            .when('/profile/notes', {
                templateUrl: '/partials_user/notes/notes.html',
                controller: 'NotesController',
                controllerAs: 'notesCtrl'
            })
            .when('/profile/notes/note/:id', {
                templateUrl: '/partials_user/notes/single_note.html',
                controller: 'SingleNoteController',
                controllerAs: 'singleNoteCtrl'
            })
            .when('/profile/notes/add_note', {
                templateUrl: '/partials_user/notes/add_note.html',
                controller: 'AddDeleteNoteController',
                controllerAs: 'addDelNoteCtrl'
            })
            .when('/profile/notes/note/:id/delete', {
                templateUrl: '/partials_user/notes/delete_note.html',
                controller: 'AddDeleteNoteController',
                controllerAs: 'addDelNoteCtrl'
            })
            .when('/profile/birthdays', {
                templateUrl: '/partials_user/birthdays/birthdays.html',
                controller: 'BirthdaysController',
                controllerAs: 'birthCtrl'
            })
            .when('/profile/birthdays/add_birthday', {
                templateUrl: '/partials_user/birthdays/add_birthday.html',
                controller: 'AddDeleteBirthdayController',
                controllerAs: 'addDelBirthCtrl'
            })
            .when('/profile/birthdays/birthday/:id/delete', {
                templateUrl: '/partials_user/birthdays/delete_birthday.html',
                controller: 'AddDeleteBirthdayController',
                controllerAs: 'addDelBirthCtrl'
            })
            .otherwise({
                redirectTo: '/'
            });

        $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
    })
    .constant('LOGIN_ENDPOINT', '/login')
    .service('AuthenticationService', function($http, LOGIN_ENDPOINT) {
        this.authenticate = function(credentials, successCallback) {
            var authHeader = {Authorization: 'Basic ' + btoa(credentials.username+':'+credentials.password)};
            var config = {headers: authHeader};
            $http
                .post(LOGIN_ENDPOINT, {}, config)
                .then(function success(value) {
                    $http.defaults.headers.common.Authorization = authHeader.Authorization;
                    successCallback();
                }, function error(reason) {
                    console.log('Login error');
                    console.log(reason);
                });
        }
        this.logout = function(successCallback) {
            delete $http.defaults.headers.common.Authorization;
            successCallback();
        }
    })
    .controller('AuthenticationController', function($rootScope, $location, AuthenticationService) {
        var vm = this;
        vm.credentials = {};
        var loginSuccess = function() {
            $rootScope.authenticated = true;
            $location.path('/profile');
        }
        vm.login = function() {
            AuthenticationService.authenticate(vm.credentials, loginSuccess);
        }
        var logoutSuccess = function() {
            $rootScope.authenticated = false;
            $location.path('/');
        }
        vm.logout = function() {
            AuthenticationService.logout(logoutSuccess);
        }
    })
;



