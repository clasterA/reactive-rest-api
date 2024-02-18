/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import java.util.UUID;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface TransactionRepository extends ReactiveCrudRepository<TransactionEntity, Long> {

  @Query(
      "select * from "
          + Tables.TRANSACTION
          + " where acc_guid = :accGuid order by created_at desc limit 1")
  Mono<TransactionEntity> getLastTransactionForAccount(@Param("accGuid") UUID accGuid);
}
