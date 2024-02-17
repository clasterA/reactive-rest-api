/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface AccountRepository extends ReactiveCrudRepository<AccountEntity, Long> {

  @Query("select * from " + Tables.ACCOUNT + " where client_guid = :clientName")
  Flux<AccountEntity> getClientAccountList(@Param("clientGuid") UUID clientGuid);
}
