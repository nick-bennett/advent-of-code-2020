package com.nickbenn.advent.day2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Entry {

  private static final Pattern PARSE_PATTERN =
      Pattern.compile("^\\s*(\\d+)-(\\d+)\\s+([a-zA-Z]):\\s+(\\S+)\\s*$");

  private final int minimum;
  private final int maximum;
  private final String required;
  private final String password;

  public Entry(String input) {
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
