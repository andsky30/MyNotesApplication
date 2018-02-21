'use strict';

angular
    .module('birthdaysModule',[])
    .constant('BIRTHDAYS_ENDPOINT', '/api/birthdays/:id')
    .factory('Birthday', function($resource, BIRTHDAYS_ENDPOINT) {
        return $resource(BIRTHDAYS_ENDPOINT);
    })
    .service('Birthdays', function(Birthday) {

        this.getAll = function() {
            return Birthday.query();
        };

        this.add = function(birthday) {
            birthday.$save();
        };

        this.delete = function (index) {
            return Birthday.delete({id: index})
        };

    })
    .controller('BirthdaysController', function(Birthdays) {
        var vm = this;
        vm.birthdays = Birthdays.getAll();
    })
    .controller('AddDeleteBirthdayController', function(Birthdays, Birthday, $routeParams, $location) {
        var vm = this;

        var birthdayId = $routeParams.id;

        vm.birthday = new Birthday();

        vm.saveBirthday = function() {

            Birthdays.add(vm.birthday);
            vm.birthday = new Birthday();

            $location.path('/profile/birthdays');
        };

        vm.deleteBirthday = function () {
            Birthdays.delete(birthdayId);

            $location.path('/profile/birthdays');
        }

    });