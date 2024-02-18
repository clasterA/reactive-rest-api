/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessage {

  private int httpCode;

  private String name;

  private String description;
}
