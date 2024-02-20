/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommonUtilsTest {

  @Test
  @DisplayName("Should return error when resource not found")
  void commonUtilsResourceNotFoundTest() {

    Exception exception =
        assertThrows(Exception.class, () -> CommonUtils.getResourceFileAsString("error_resource"));

    assertThat(exception).isNotNull();
    assertThat(exception.getMessage()).isEqualTo("resource not found");
  }
}
