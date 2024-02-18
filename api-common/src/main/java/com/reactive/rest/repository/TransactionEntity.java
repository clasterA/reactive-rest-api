/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(Tables.TRANSACTION)
public class TransactionEntity {

  @Id private Long id;

  @Column("guid")
  private UUID guid;

  @Column("acc_guid")
  private UUID accGuid;

  @Column("acc_currency")
  private String accCurrency;

  @Column("begin_amount")
  private BigDecimal beginAmount;

  @Column("trx_amount")
  private BigDecimal trxAmount;

  @Column("trx_currency")
  private String trxCurrency;

  @Column("end_amount")
  private BigDecimal endAmount;

  @Column("trx_type")
  private String trxType;

  @Column("created_at")
  @CreatedDate
  private LocalDateTime createdAt;
}
