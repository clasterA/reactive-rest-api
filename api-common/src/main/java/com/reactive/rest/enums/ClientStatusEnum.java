/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.enums;

public enum ClientStatusEnum {
  ACTIVE("Active"),
  CLOSED("Closed");
  private final String val;

  ClientStatusEnum(String val) {
    this.val = val;
  }

  public String getVal() {
    return val;
  }

  public static ClientStatusEnum fromString(String text) {
    for (ClientStatusEnum status : ClientStatusEnum.values()) {
      if (status.val.equalsIgnoreCase(text)) {
        return status;
      }
    }
    return null;
  }
}
