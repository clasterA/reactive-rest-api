/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<ClientEntity, Long> {

  Mono<ClientEntity> findByGuid(UUID guid);

  @Query("select * from " + Tables.CLIENT + " order by created_at desc")
  Flux<ClientEntity> getClientList();

  @Query("update " + Tables.CLIENT + " set status = :status " + " where guid = :guid RETURNING *")
  Mono<ClientEntity> removeClient(@Param("guid") UUID guid, @Param("status") String status);
}
