(function() {
    'use strict';

    angular
        .module('mygatewayApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$uibModal'];

    function LoginService ($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open () {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/components/login/login.html',
                controller: 'LoginController',
                controllerAs: 'vm',
                backdrop  : 'static',
                keyboard  : false
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
