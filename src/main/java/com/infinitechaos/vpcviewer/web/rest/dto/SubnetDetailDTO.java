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

package com.infinitechaos.vpcviewer.web.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.ec2.model.RouteTable;
import com.amazonaws.services.ec2.model.Subnet;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SubnetDetailDTO extends SubnetDTO {
    private String routeTableId;
    private List<RouteDTO> routes = new ArrayList<>();

    public SubnetDetailDTO(final Subnet subnet) {
        super(subnet);
    }

    public SubnetDetailDTO(final Subnet subnet, final RouteTable routeTable) {
        super(subnet);

        routeTableId = routeTable.getRouteTableId();
        routes.addAll(
            routeTable.getRoutes()
                .stream()
                .map(RouteDTO::new)
                .collect(Collectors.toList()));
    }

    @JsonCreator
    public SubnetDetailDTO() {
    }
}
