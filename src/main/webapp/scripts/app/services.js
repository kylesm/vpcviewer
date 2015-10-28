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

angular.module('vpcviewerServices', ['LocalStorageModule'])
    .config(function (localStorageServiceProvider) {
        localStorageServiceProvider
            .setPrefix('vpcviewer')
            .setStorageType('sessionStorage')
            .setNotify(true, true);
    })
    .factory('cacheService', function ($rootScope, localStorageService) {
        var _bypassCache = localStorageService.get('bypassCache') || false;

        return {
            bypassCache: function () {
                return _bypassCache;
            },

            update: function (newState) {
                _bypassCache = newState;
                localStorageService.set('bypassCache', newState);
                $rootScope.$broadcast('bypassCache.update');
            }
        };
    })
    .factory('regionService', function ($rootScope, localStorageService) {
        var _region = localStorageService.get('region') || 'us-east-1';

        return {
            getRegion: function () {
                return _region;
            },

            changeRegion: function (newRegion) {
                _region = newRegion;
                $rootScope.$broadcast('region.update');
            }
        }
    });
