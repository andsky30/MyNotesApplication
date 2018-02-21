'use strict';

angular
    .module('usersModule',[])
    .constant('USER_ENDPOINT', '/api/users/:id')
    .factory('User', function($resource, USER_ENDPOINT) {
        return $resource(USER_ENDPOINT);
    })
    .service('Users', function(User) {

        this.getAll = function () {
            return User.query();
        }

        this.get = function (index) {
            return User.get({id: index});
        }

        this.add = function (user) {
            user.$save()
        }

        this.delete = function (index) {
            return User.delete({id: index})
        }
        this.deleteAccount = function () {
            return User.delete()
        }
    })
    .constant('ACCOUNT_ENDPOINT', '/api/users/account')
    .factory('Account', function($resource, ACCOUNT_ENDPOINT) {
        return $resource(ACCOUNT_ENDPOINT);
    })
    .service('Accounts', function(Account) {

        this.get = function () {
            return Account.get();
        }
    })

    .controller('RegisterController', function(Users, User) {
        var vm = this;
        vm.user = new User();
        vm.saveUser = function() {
            Users.add(vm.user);
            vm.user = new User();
        }
    })
    .controller('ShowUsersController', function(Users, $routeParams, $location) {
        var vm = this;
        vm.users = Users.getAll();

        var userId = $routeParams.id;
        vm.user = Users.get(userId);

        vm.deleteUser = function () {
            Users.delete(userId);

            $location.path('/profile/show_users');
        }
    })
    .controller('AccountController', function(Users, Accounts, $routeParams, $location) {
        var vm = this;

        vm.user = Accounts.get();

        vm.delete = function () {
            Users.deleteAccount();

            $location.path('/');
        }
    })
;
