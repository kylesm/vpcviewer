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

package com.infinitechaos.vpcviewer.config;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;

@Configuration
@EnableCaching
public class CachingConfiguration {
    public static final String VPC_LISTS_CACHE = "vpcLists";
    public static final String VPC_CACHE = "vpcs";
    public static final String SUBNET_CACHE = "subnets";
    public static final String ROUTE_TABLE_CACHE = "routeTables";

    @Bean
    public CacheManager cacheManager() {
        GuavaCacheManager cm = new GuavaCacheManager(VPC_LISTS_CACHE, VPC_CACHE, ROUTE_TABLE_CACHE, SUBNET_CACHE);
        cm.setAllowNullValues(false);
        cm.setCacheBuilder(CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES));

        return cm;
    }
}
