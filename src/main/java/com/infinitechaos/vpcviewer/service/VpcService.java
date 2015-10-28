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

package com.infinitechaos.vpcviewer.service;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.amazonaws.services.ec2.model.RouteTable;
import com.amazonaws.services.ec2.model.Subnet;
import com.amazonaws.services.ec2.model.Vpc;

/**
 * Methods for interacting with VPCs and their resources types.
 */
public interface VpcService {
    /**
     * Gets a single specific VPC.
     *
     * @param vpcId       the identifier of the VPC to retrieve
     * @param region      the AWS region the VPC exists in
     * @param bypassCache whether or not the cache should be ignored
     * @return
     */
    Vpc getVpcInRegion(@NotBlank String vpcId, @NotBlank String region, boolean bypassCache);

    /**
     * Gets the VPCs that exist in the given AWS region.
     *
     * @param region      the AWS region to retrieve VPCs for
     * @param bypassCache whether or not the cache should be ignored
     * @return the non-null list of VPCs in the region
     */
    List<Vpc> getVpcsInRegion(@NotBlank String region, boolean bypassCache);

    /**
     * Gets the subnets that exist in the given VPC.
     *
     * @param vpcId       the identifier of the VPC to retrieve subnets for
     * @param region      the AWS region the VPC exists in
     * @param bypassCache whether or not the cache should be ignored
     * @return the non-null list of subnets in the VPC
     */
    List<Subnet> getSubnetsForVpcInRegion(@NotBlank String vpcId, @NotBlank String region, boolean bypassCache);

    /**
     * Gets the route tables that exist in the given VPC.
     *
     * @param vpcId       the identifier of the VPC to retrieve route tables for
     * @param region      the AWS region the VPC exists in
     * @param bypassCache whether or not the cache should be ignored
     * @return the non-null list of route tables in the VPC
     */
    List<RouteTable> getRouteTablesForVpcInRegion(@NotBlank String vpcId, @NotBlank String region, boolean bypassCache);
}
