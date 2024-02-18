/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.error;

import lombok.Getter;

@Getter
public class CommonException extends RuntimeException {

  private final ErrorMessage error;

  public CommonException(ErrorMessage error) {
    super(error.getDescription());
    this.error = error;
  }

  public CommonException(ErrorMessage error, Throwable e) {
    super(error.getDescription(), e);
    this.error = error;
  }
}
