'use strict';

/*
 * Copyright (c) 2015, Kyle Smith
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

var vpcviewerApp = angular.module('vpcviewerApp', ['ui.router', 'vpcviewerControllers', 'vpcviewerDirectives']);

vpcviewerApp.config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/default');

    $stateProvider
        .state('switchRegion', {
            controller: 'DefaultCtrl'
        })
        .state('vpcDetail', {
            url: '/vpc/:vpcId',
            templateUrl: 'partials/vpc-detail.html',
            controller: 'VpcDetailCtrl'
        })
        .state('/default', {
            url: '/default',
            templateUrl: 'partials/default.html',
            controller: 'DefaultCtrl'
        });
});
