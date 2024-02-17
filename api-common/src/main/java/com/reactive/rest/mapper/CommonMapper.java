/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.mapper;

import com.reactive.rest.dto.Client;
import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.repository.ClientEntity;
import org.mapstruct.*;

@Mapper(
    collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = "spring")
public interface CommonMapper {

  @Mapping(target = "status", expression = "java(getClientStatusEnumFromString(source))")
  Client map(ClientEntity source);

  @Mapping(target = "status", expression = "java(getClientStatusEnumFromValue(source))")
  ClientEntity map(Client source);

  default ClientStatusEnum getClientStatusEnumFromString(ClientEntity source) {
    return ClientStatusEnum.fromString(source.getStatus());
  }

  default String getClientStatusEnumFromValue(Client source) {
    return source.getStatus().getVal();
  }
}
