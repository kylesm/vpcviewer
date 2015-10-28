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


var vpcviewerControllers = angular.module('vpcviewerControllers', ['vpcviewerServices', 'nya.bootstrap.select']);

vpcviewerControllers.controller('RegionListCtrl', ['$scope', 'regionService', function ($scope, regionService) {
    $scope.regions = [
        'us-east-1',
        'us-west-1',
        'us-west-2',
        'eu-west-1',
        'ap-southeast-1',
        'ap-southeast-2'
    ];

    $scope.currentRegion = regionService.getRegion();

    $scope.$watch(function () {
        return $scope.currentRegion;
    }, function (newValue) {
        regionService.changeRegion($scope.currentRegion);
    });
}]);

vpcviewerControllers.controller('VpcListCtrl', ['$scope', '$http', 'cacheService', 'regionService', function ($scope, $http, cacheService, regionService) {
    $scope.vpcs = [];
    $scope.bypassCache = cacheService.bypassCache();
    $scope.region = regionService.getRegion();

    $scope.$on('bypassCache.update', function () {
        $scope.bypassCache = cacheService.bypassCache();
    });

    $scope.$on('region.update', function () {
        var newRegion = regionService.getRegion();

        if (newRegion !== $scope.region) {
            $scope.region = newRegion;
            $scope.getVpcs();
        }
    });

    $scope.update = function () {
        cacheService.update($scope.bypassCache);
    };

    $scope.getVpcs = function () {
        $http.get('api/vpcs', {
            params: {
                region: $scope.region,
                bypassCache: $scope.bypassCache
            }
        }).success(function (data) {
            $scope.vpcs = [];

            for (var i = 0; i < data.length; i++) {
                $scope.vpcs.push({id: data[i].vpcId, name: data[i].name});
            }
        });
    };

    $scope.getVpcs();
}]);

vpcviewerControllers.controller('VpcDetailCtrl', ['$scope', '$http', '$stateParams', 'cacheService', 'regionService', function ($scope, $http, $stateParams, cacheService, regionService) {
    $scope.vpc = {
        name: "Unknown",
        vpcId: "Unknown"
    };
    $scope.subnets = {};
    $scope.routeTables = [];
    $scope.bypassCache = cacheService.bypassCache();
    $scope.region = regionService.getRegion();

    $scope.$on('bypassCache.update', function () {
        $scope.bypassCache = cacheService.bypassCache();
    });

    $scope.$on('region.update', function () {
        $scope.region = regionService.getRegion();
    });

    $http.get('api/vpcs/' + $stateParams.vpcId + "/detail", {
        params: {
            region: $scope.region,
            bypassCache: $scope.bypassCache
        }
    }).success(function (data) {
        $scope.vpc = {
            name: data.name,
            vpcId: $stateParams.vpcId,
            subnets: data.subnets,
            routeTables: data.routeTables
        };
    });

    $scope.makeLink = function (input) {
        var link = '<a href="https://console.aws.amazon.com/vpc/home?region=' + $scope.region + "#";

        if (/^i-[a-f0-9]+/.test(input)) {
            link += "Instances:instanceId=";
        } else if (/^igw-[a-f0-9]+/.test(input)) {
            link += "igws:filter=";
        } else if (/^pcx-[a-f0-9]+/.test(input)) {
            link += "peer:filter=";
        } else if (/^vgw-[a-f0-9]+/.test(input)) {
            link += "vgws:filter=";
        }

        link += input + '">' + input + '</a>';

        return link;
    }

}]);

vpcviewerControllers.controller('DefaultCtrl', function ($scope) {
});

