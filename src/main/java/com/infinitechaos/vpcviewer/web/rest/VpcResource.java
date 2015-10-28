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

package com.infinitechaos.vpcviewer.web.rest;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.ec2.model.Vpc;
import com.infinitechaos.vpcviewer.service.VpcService;
import com.infinitechaos.vpcviewer.web.rest.dto.RouteTableDTO;
import com.infinitechaos.vpcviewer.web.rest.dto.SubnetDTO;
import com.infinitechaos.vpcviewer.web.rest.dto.VpcDTO;
import com.infinitechaos.vpcviewer.web.rest.dto.VpcDetailDTO;

@RestController
@RequestMapping(value = "/api", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
public class VpcResource {
    @Autowired
    private VpcService vpcService;

    @RequestMapping(value = "/vpcs")
    public List<VpcDTO> getVpcs(@RequestParam("region") final String region,
                                @RequestParam(value = "bypassCache", required = false) boolean bypassCache) {
        return vpcService.getVpcsInRegion(region, bypassCache)
            .stream()
            .map(VpcDTO::new)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/vpcs/{vpcId}/detail")
    public VpcDTO getVpcDetail(@RequestParam("region") final String region,
                               @PathVariable("vpcId") final String vpcId,
                               @RequestParam(value = "bypassCache", required = false) boolean bypassCache) {
        Vpc vpc = vpcService.getVpcInRegion(vpcId, region, bypassCache);

        return new VpcDetailDTO(vpc,
            vpcService.getSubnetsForVpcInRegion(vpcId, region, bypassCache),
            vpcService.getRouteTablesForVpcInRegion(vpcId, region, bypassCache));
    }

    @RequestMapping(value = "/vpcs/{vpcId}")
    public VpcDTO getVpc(@RequestParam("region") final String region,
                         @PathVariable("vpcId") final String vpcId,
                         @RequestParam(value = "bypassCache", required = false) boolean bypassCache) {
        return new VpcDTO(vpcService.getVpcInRegion(vpcId, region, bypassCache));
    }

    @RequestMapping(value = "/routeTables")
    public List<RouteTableDTO> getRoutesForVpc(@RequestParam("vpcId") final String vpcId,
                                               @RequestParam("region") final String region,
                                               @RequestParam(value = "bypassCache", required = false) boolean bypassCache) {
        return vpcService.getRouteTablesForVpcInRegion(vpcId, region, bypassCache)
            .stream()
            .map(RouteTableDTO::new)
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/subnets")
    public List<SubnetDTO> getSubnetsForVpc(@RequestParam("vpcId") final String vpcId,
                                            @RequestParam("region") final String region,
                                            @RequestParam(value = "bypassCache", required = false) boolean bypassCache) {
        return vpcService.getSubnetsForVpcInRegion(vpcId, region, bypassCache)
            .stream()
            .map(SubnetDTO::new)
            .collect(Collectors.toList());
    }
}
