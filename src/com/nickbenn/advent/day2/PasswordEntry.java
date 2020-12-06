/*
 *  Copyright 2020 Nicholas Bennett.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.nickbenn.advent.day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordEntry {

  private static final Pattern PARSE_PATTERN =
      Pattern.compile("^\\s*(\\d+)-(\\d+)\\s+([a-zA-Z]):\\s+(\\S+)\\s*$");

  private final int minimum;
  private final int maximum;
  private final String required;
  private final String password;

  public PasswordEntry(String input) {
    Matcher matcher = PARSE_PATTERN.matcher(input);
    if (matcher.matches()) {
      minimum = Integer.parseInt(matcher.group(1));
      maximum = Integer.parseInt(matcher.group(2));
      required = matcher.group(3);
      password = matcher.group(4);
    } else {
      throw new IllegalArgumentException();
    }
  }

  public int getMinimum() {
    return minimum;
  }

  public int getMaximum() {
    return maximum;
  }

  public String getRequired() {
    return required;
  }

  public String getPassword() {
    return password;
  }

}
