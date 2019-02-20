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

package com.infinitechaos.vpcviewer.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.ec2.AmazonEC2ClientBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeRouteTablesRequest;
import com.amazonaws.services.ec2.model.DescribeRouteTablesResult;
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsResult;
import com.amazonaws.services.ec2.model.DescribeVpcsRequest;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.RouteTable;
import com.amazonaws.services.ec2.model.Subnet;
import com.amazonaws.services.ec2.model.Vpc;
import com.google.common.base.Preconditions;
import com.infinitechaos.vpcviewer.config.CachingConfiguration;
import com.infinitechaos.vpcviewer.service.VpcService;

@Service
public class VpcServiceImpl implements VpcService {
    private static final Logger LOG = LoggerFactory.getLogger(VpcServiceImpl.class);

    private final Map<Regions, AmazonEC2> clients = new HashMap<>();

    @Override
    @Cacheable(value = CachingConfiguration.VPC_CACHE, key = "#vpcId", condition = "#bypassCache == false")
    public Vpc getVpcInRegion(final String vpcId, final String region, boolean bypassCache) {
        Preconditions.checkArgument(StringUtils.isNotBlank(vpcId), "vpcId may not be null or blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(region), "region may not be null or blank");

        LOG.info("Retrieving VPC {} in region {} ({})", vpcId, region, bypassCache);
        DescribeVpcsRequest request = new DescribeVpcsRequest()
            .withVpcIds(vpcId);
        DescribeVpcsResult result = getClientForRegion(region).describeVpcs(request);

        List<Vpc> results = result.getVpcs();

        if (results.size() != 1) {
            throw new IllegalArgumentException("Did not get expected result");
        }

        return results.get(0);
    }

    @Override
    @Cacheable(value = CachingConfiguration.VPC_LISTS_CACHE, key = "#region", condition = "#bypassCache == false")
    public List<Vpc> getVpcsInRegion(final String region, boolean bypassCache) {
        Preconditions.checkArgument(StringUtils.isNotBlank(region), "region may not be null or blank");

        LOG.info("Retrieving all VPCs in region {} ({})", region, bypassCache);
        DescribeVpcsResult result = getClientForRegion(region).describeVpcs();

        return result.getVpcs();
    }

    @Override
    @Cacheable(value = CachingConfiguration.SUBNET_CACHE, key = "#vpcId", condition = "#bypassCache == false")
    public List<Subnet> getSubnetsForVpcInRegion(String vpcId, final String region, boolean bypassCache) {
        Preconditions.checkArgument(StringUtils.isNotBlank(vpcId), "vpcId may not be null or blank");
        Preconditions.checkArgument(StringUtils.isNotBlank(region), "region may not be null or blank");

        LOG.info("Retrieving subnets for VPC {} in region {} ({})", vpcId, region, bypassCache);
        DescribeSubnetsRequest request = new DescribeSubnetsRequest()
            .withFilters(new Filter()
                .withName("vpc-id")
                .withValues(vpcId));
        DescribeSubnetsResult result = getClientForRegion(region).describeSubnets(request);

        return result.getSubnets();
    }

    @Override
    @Cacheable(value = CachingConfiguration.ROUTE_TABLE_CACHE, key = "#vpcId", condition = "#bypassCache == false")
    public List<RouteTable> getRouteTablesForVpcInRegion(final String vpcId, final String region, boolean bypassCache) {
        LOG.info("Retrieving route tables for VPC {} in region {} ({})", vpcId, region, bypassCache);
        DescribeRouteTablesRequest request = new DescribeRouteTablesRequest()
            .withFilters(new Filter()
                .withName("vpc-id")
                .withValues(vpcId));
        DescribeRouteTablesResult result = getClientForRegion(region).describeRouteTables(request);
        return result.getRouteTables();
    }

    private synchronized AmazonEC2 getClientForRegion(final String regionName) {
        return clients.computeIfAbsent(Regions.fromName(regionName), region -> {
            LOG.info("Creating client for region {}", region);
            return AmazonEC2ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
        });
    }
}
