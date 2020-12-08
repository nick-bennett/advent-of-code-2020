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
package com.nickbenn.advent.day4;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PassportProcessing {

  private static final Pattern ENTRY_DELIMITER = Pattern.compile("\\s+");
  private static final Pattern PAIR_DELIMITER = Pattern.compile(":");
  private static final Pattern HEIGHT_PARSER = Pattern.compile("^(?:(\\d+)cm)|(?:(\\d+)in)$");
  private static final Pattern HAIR_COLOR_VALIDATOR = Pattern.compile("^#[0-9a-f]{6}$");
  private static final Pattern EYE_COLOR_VALIDATOR = Pattern
      .compile("^amb|blu|brn|gry|grn|hzl|oth$");
  private static final Pattern PASSPORT_ID_VALIDATOR = Pattern.compile("^\\d{9}$");
  private static final Map<String, Predicate<String>> RULES = Map.of(
      "byr", (v) -> isParsedValueInRange(v, 1920, 2002),
      "iyr", (v) -> isParsedValueInRange(v, 2010, 2020),
      "eyr", (v) -> isParsedValueInRange(v, 2020, 2030),
      "hgt", (v) -> isHeightValid(v, 59, 76, 150, 193),
      "hcl", (v) -> HAIR_COLOR_VALIDATOR.matcher(v).matches(),
      "ecl", (v) -> EYE_COLOR_VALIDATOR.matcher(v).matches(),
      "pid", (v) -> PASSPORT_ID_VALIDATOR.matcher(v).matches()
  );
  private static final Set<String> REQUIRED_FIELDS = RULES.keySet();

  private final List<Map<String, String>> passports;

  public PassportProcessing(String filename) throws IOException, URISyntaxException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineGroupStream()
    ) {
      passports = stream
          .map((passport) -> ENTRY_DELIMITER.splitAsStream(passport)
              .map(PAIR_DELIMITER::split)
              .collect(Collectors.toMap((String[] pair) -> pair[0], (String[] pair) -> pair[1]))
          )
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    PassportProcessing check = new PassportProcessing(Defaults.FILENAME);
    System.out.println(check.basicValidation());
    System.out.println(check.advancedValidation());
  }

  public long basicValidation() {
    return passports.stream()
        .filter((passport) -> passport.keySet().containsAll(REQUIRED_FIELDS))
        .count();
  }

  public long advancedValidation() {
    return passports.stream()
        .filter((passport) -> passport.keySet().containsAll(REQUIRED_FIELDS))
        .filter((passport) -> passport.keySet().stream()
            .allMatch((key) -> RULES.getOrDefault(key, (v) -> true).test(passport.get(key)))
        )
        .count();
  }

  private static boolean isParsedValueInRange(String s, int min, int max) {
    try {
      int year = Integer.parseInt(s);
      return year >= min && year <= max;
    } catch (Exception e) {
      return false;
    }
  }

  private static boolean isHeightValid(String value,
      int minInches, int maxInches, int minCentimeters, int maxCentimeters) {
    boolean valid = false;
    Matcher matcher = HEIGHT_PARSER.matcher(value);
    if (matcher.matches()) {
      int height;
      if (matcher.group(1) == null || matcher.group(1).isEmpty()) {
        height = Integer.parseInt(matcher.group(2));
        valid = height >= minInches && height <= maxInches;
      } else {
        height = Integer.parseInt(matcher.group(1));
        valid = height >= minCentimeters && height <= maxCentimeters;
      }
    }
    return valid;
  }

}
