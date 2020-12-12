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
package com.nickbenn.advent.day7;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HandyHaversacks {

  public static final String SUBJECT_BAG_NAME = "shiny gold";

  private static final Pattern CONTAINER_PATTERN = Pattern.compile("^(\\S+\\s+\\S+)(?=\\s+bags?)");
  private static final Pattern COMPONENT_PATTERN = Pattern
      .compile("(\\d+)\\s+(\\S+\\s+\\S+)(?=\\s+bags?)");

  public HandyHaversacks(String filename) throws IOException, URISyntaxException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
    ) {
      stream
          .forEach((line) -> {
            Matcher containerMatcher = CONTAINER_PATTERN.matcher(line);
            if (containerMatcher.find()) {
              LuggageRule container = LuggageRule.getInstance(containerMatcher.group(1));
              Matcher componentMatcher = COMPONENT_PATTERN.matcher(line);
              while (componentMatcher.find()) {
                LuggageRule component = LuggageRule.getInstance(componentMatcher.group(2));
                container.addComponent(component, Integer.parseInt(componentMatcher.group(1)));
              }
            }
          });
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    HandyHaversacks haversacks = new HandyHaversacks(Defaults.FILENAME);
    System.out.println(haversacks.countContainers(SUBJECT_BAG_NAME));
    System.out.println(haversacks.countComponents(SUBJECT_BAG_NAME));
  }

  public int countContainers(String bagName) {
    return LuggageRule.getInstance(bagName).getAllContainers().size();
  }

  public int countComponents(String bagName) {
    return LuggageRule.getInstance(bagName).getAllComponents()
        .values()
        .stream()
        .reduce(Integer::sum)
        .orElse(0);
  }

}
