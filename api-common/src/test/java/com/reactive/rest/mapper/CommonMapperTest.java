/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.reactive.rest.enums.ClientStatusEnum;
import com.reactive.rest.repository.ClientEntity;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class CommonMapperTest {

  private final CommonMapper mapper = Mappers.getMapper(CommonMapper.class);

  @Test
  @DisplayName("Client entity to dto layer mapping test and dto to entity layer mapping test.")
  void clientEntityToDtoAndDtoToEntityTest() {

    var guid = UUID.randomUUID();

    var clientEntity =
        ClientEntity.builder()
            .id(1L)
            .guid(guid)
            .name("Test Name")
            .status(ClientStatusEnum.ACTIVE.getVal())
            .build();

    assertThat(clientEntity).isNotNull();

    var clientDto = mapper.map(clientEntity);

    assertThat(clientDto).isNotNull();
    assertThat(clientDto.getGuid()).isEqualTo(guid);
    assertThat(clientDto.getName()).isEqualTo("Test Name");
    assertThat(clientDto.getStatus()).isEqualTo(ClientStatusEnum.ACTIVE);

    clientDto.setName("Abc");
    clientDto.setStatus(ClientStatusEnum.CLOSED);

    clientEntity = mapper.map(clientDto);
    assertThat(clientEntity).isNotNull();
    assertThat(clientEntity.getGuid()).isEqualTo(guid);
    assertThat(clientEntity.getName()).isEqualTo("Abc");
    assertThat(clientEntity.getStatus()).isEqualTo(ClientStatusEnum.CLOSED.getVal());
  }
}
