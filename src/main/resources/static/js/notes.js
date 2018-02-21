'use strict';

angular
    .module('notesModule',[])
    .constant('NOTES_ENDPOINT', '/api/notes/:id')
    .factory('Note', function($resource, NOTES_ENDPOINT) {
        return $resource(NOTES_ENDPOINT);
    })
    .service('Notes', function(Note) {

        this.getAll = function() {
            return Note.query();
        };

        this.get = function (index) {
            return Note.get({id: index});
        };

        this.add = function(note) {
            note.$save();
        };

        this.delete = function (index) {
            return Note.delete({id: index})
        }
    })
    .controller('NotesController', function(Notes) {
        var vm = this;
        vm.notes = Notes.getAll();
    })
    .controller('SingleNoteController', function(Notes, Note, $routeParams) {
        var vm = this;

        var noteId = $routeParams.id;
        vm.note = Notes.get(noteId);
    })
    .controller('AddDeleteNoteController', function(Notes, Note, $routeParams, $location) {
        var vm = this;

        var noteId = $routeParams.id;
        vm.note = new Note();

        vm.saveNote = function() {

            Notes.add(vm.note);
            vm.note = new Note();

            $location.path('/profile/notes');
        };

        vm.deleteNote = function () {
            Notes.delete(noteId);

            $location.path('/profile/notes/');
        }
    });