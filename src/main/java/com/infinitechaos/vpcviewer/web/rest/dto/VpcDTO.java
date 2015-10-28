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

import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.Vpc;
import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class VpcDTO {
    private String name;
    private String vpcId;
    private String state;
    private String cidrBlock;
    private List<TagDTO> tags = new ArrayList<>();

    public VpcDTO(final Vpc vpc) {
        this.vpcId = vpc.getVpcId();
        this.cidrBlock = vpc.getCidrBlock();
        this.state = vpc.getState();
        this.tags.addAll(
            vpc.getTags()
                .stream()
                .map(TagDTO::new)
                .collect(Collectors.toList()));

        this.name = vpc.getTags()
            .stream()
            .filter(t -> t.getKey().equals("Name"))
            .findFirst()
            .map(Tag::getValue)
            .orElse("n/a");
    }

    @JsonCreator
    public VpcDTO() {

    }
}
