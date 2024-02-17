/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.repository;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Table(Tables.CLIENT)
public class ClientEntity {

  @Id private Long id;

  @Column("guid")
  private UUID guid;

  @Column("name")
  private String name;

  @Column("status")
  private String status;

  @Column("created_at")
  @CreatedDate
  private LocalDateTime createdAt;
}
