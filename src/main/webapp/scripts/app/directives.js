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

angular.module('vpcviewerDirectives', ['vpcviewerServices'])
    .directive('awsLink', ['$compile', 'regionService', function ($compile, regionService) {
        function makeLink(input) {
            var link = 'https://console.aws.amazon.com/vpc/home?region=' + regionService.getRegion() + "#";

            if (/^i-[a-f0-9]+/.test(input)) {
                link += "Instances:instanceId=";
            } else if (/^igw-[a-f0-9]+/.test(input)) {
                link += "igws:filter=";
            } else if (/^pcx-[a-f0-9]+/.test(input)) {
                link += "peer:filter=";
            } else if (/^vgw-[a-f0-9]+/.test(input)) {
                link += "vgws:filter=";
            } else if (/^local$/.test(input)) {
                return "#";
            }

            link += input;

            return link;
        }

        return {
            restrict: 'E',
            template: '<a></a>',
            replace: true,
            link: function (scope, elem, attr) {
                attr.$observe('href', function (value) {
                    elem.attr('href', makeLink(value));
                    elem.text(value);
                });
            }
        };
    }]);
