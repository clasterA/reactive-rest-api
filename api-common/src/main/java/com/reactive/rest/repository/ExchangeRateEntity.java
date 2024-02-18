/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
@Table(Tables.EXCHANGE_RATE)
public class ExchangeRateEntity {

  @Id private Long id;

  @Column("base_currency")
  private String baseCurrency;

  @Column("currency")
  private String currency;

  @Column("rate")
  private BigDecimal rate;

  @Column("valid_from")
  private LocalDate validFrom;

  @Column("created_at")
  @CreatedDate
  private LocalDateTime createdAt;
}
