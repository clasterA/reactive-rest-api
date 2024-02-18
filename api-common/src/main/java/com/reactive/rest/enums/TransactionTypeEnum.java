/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.enums;

public enum TransactionTypeEnum {
  DEBIT("Debit"),
  CREDIT("Credit");
  private final String val;

  TransactionTypeEnum(String val) {
    this.val = val;
  }

  public String getVal() {
    return val;
  }

  public static TransactionTypeEnum fromString(String text) {
    for (TransactionTypeEnum status : TransactionTypeEnum.values()) {
      if (status.val.equalsIgnoreCase(text)) {
        return status;
      }
    }
    return null;
  }
}
