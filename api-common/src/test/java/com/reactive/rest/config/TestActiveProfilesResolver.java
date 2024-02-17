/* Copyright (C) MZ Project - All Rights Reserved
 *
 */
package com.reactive.rest.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.test.context.ActiveProfilesResolver;
import org.testcontainers.shaded.org.apache.commons.lang3.StringUtils;

@Slf4j
public class TestActiveProfilesResolver implements ActiveProfilesResolver {

  @Override
  public String[] resolve(Class<?> testClass) {
    String activeProfiles = System.getProperty("spring.profiles.active");
    if (StringUtils.isNotEmpty(activeProfiles)) {
      log.info("Profiles selected: " + activeProfiles);
      return activeProfiles.split(",");
    }
    log.info("Profiles selected: itest");
    return new String[] {"itest"};
  }
}
