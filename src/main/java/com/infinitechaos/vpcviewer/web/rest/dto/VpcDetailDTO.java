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

import static java.util.function.Function.identity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.ec2.model.RouteTable;
import com.amazonaws.services.ec2.model.Subnet;
import com.amazonaws.services.ec2.model.Vpc;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class VpcDetailDTO extends VpcDTO {
    private static final Logger LOG = LoggerFactory.getLogger(VpcDetailDTO.class);

    private List<SubnetDetailDTO> subnets = new ArrayList<>();

    public VpcDetailDTO(final Vpc vpc) {
        super(vpc);
    }

    public VpcDetailDTO(final Vpc vpc, final List<Subnet> subnets, final List<RouteTable> routeTables) {
        super(vpc);

        final Map<String, SubnetDetailDTO> subnetDetails = new HashMap<>();
        subnetDetails.putAll(
            subnets.stream()
                .map(SubnetDetailDTO::new)
                .collect(Collectors.toMap(s -> s.getSubnetId(), identity())));

        LOG.trace("Details map: {}", subnetDetails);

        routeTables.stream()
            .map(RouteTableDTO::new)
            .forEach(rt ->
                rt.getAssociations().forEach(assoc -> {
                    SubnetDetailDTO dto = subnetDetails.get(assoc.getSubnetId());

                    if (dto == null) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("RT: {}, Assoc.SubnetID: {}, Assocs: {}",
                                rt.getRouteTableId(),
                                assoc.getSubnetId(),
                                rt.getAssociations());
                        }

                        return;
                    }

                    dto.setRouteTableId(rt.getRouteTableId());
                    dto.getRoutes().addAll(rt.getRoutes());
                }));

        this.subnets.addAll(subnetDetails.values());
    }

    @JsonCreator
    public VpcDetailDTO() {

    }
}
