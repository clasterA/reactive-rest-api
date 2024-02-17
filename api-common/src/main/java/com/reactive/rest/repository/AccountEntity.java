/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

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
@Table(Tables.ACCOUNT)
public class AccountEntity {

  @Id private Long id;

  @Column("guid")
  private UUID guid;

  @Column("client_guid")
  private UUID clientGuid;

  @Column("client_name")
  private String clientName;

  @Column("name")
  private String name;

  @Column("currency")
  private String currency;

  @Column("created_at")
  @CreatedDate
  private LocalDateTime createdAt;
}
