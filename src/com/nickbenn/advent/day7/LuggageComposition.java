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

import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuggageComposition {

  private static final Pattern CONTAINER_PATTERN = Pattern.compile("^(\\S+\\s+\\S+)(?=\\s+bags?)");
  private static final Pattern COMPONENT_PATTERN = Pattern
      .compile("(\\d+)\\s+(\\S+\\s+\\S+)(?=\\s+bags?)");
  private static final String SUBJECT_BAG_NAME = "shiny gold";

  private final Map<String, Map<String, Integer>> compositions;
  private final Map<String, Set<String>> containers;

  public LuggageComposition() throws IOException, URISyntaxException {
    compositions = new HashMap<>();
    containers = new HashMap<>();
    new Parser.Builder(getClass())
        .build()
        .lineStream()
        .forEach((line) -> {
          Matcher containerMatcher = CONTAINER_PATTERN.matcher(line);
          if (containerMatcher.find()) {
            ColoredBag container = ColoredBag.getInstance(containerMatcher.group(1));
            Matcher componentMatcher = COMPONENT_PATTERN.matcher(line);
            while (componentMatcher.find()) {
              ColoredBag component = ColoredBag.getInstance(componentMatcher.group(2));
              container.addComponent(component, Integer.parseInt(componentMatcher.group(1)));
            }
          }
        });
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    LuggageComposition composition = new LuggageComposition();
    ColoredBag bag = ColoredBag.getInstance(SUBJECT_BAG_NAME);
    System.out.println(bag.getAllContainers().size());
    System.out.println(
        bag.getAllComponents()
            .values()
            .stream()
            .reduce(Integer::sum)
            .orElse(0)
    );
  }

}
