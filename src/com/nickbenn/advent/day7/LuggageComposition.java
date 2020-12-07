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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LuggageComposition {

  private static final Pattern CONTAINER_PATTERN = Pattern.compile("^(\\S+\\s+\\S+)(?=\\s+bags?)");
  private static final Pattern COMPOSITION_PATTERN =
      Pattern.compile("(\\d+)\\s+(\\S+\\s+\\S+)(?=\\s+bags?)");
  private static final String BAG_TO_CONTAIN = "shiny gold";
  private static final String BAG_TO_COMPOSE = "shiny gold";

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
            String key = containerMatcher.group(1);
            Map<String, Integer> composition = new HashMap<>();
            Matcher containedMatcher = COMPOSITION_PATTERN.matcher(line);
            while (containedMatcher.find()) {
              String color = containedMatcher.group(2);
              composition.put(color, Integer.parseInt(containedMatcher.group(1)));
              Set<String> containingColors = containers.getOrDefault(color, new HashSet<>());
              containingColors.add(key);
              containers.putIfAbsent(color, containingColors);
            }
            compositions.put(key, composition);
          }
        });
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    LuggageComposition composition = new LuggageComposition();
    System.out.println(composition.totalContaining(BAG_TO_CONTAIN));
    System.out.println(composition.totalComposition(BAG_TO_COMPOSE));
  }

  public int totalContaining(String color) {
    Set<String> colors = new HashSet<>();
    findAllContaining(color, colors);
    return colors.size();
  }

  public int totalComposition(String color) {
    return compositions.getOrDefault(color, new HashMap<>())
        .entrySet()
        .stream()
        .mapToInt((entry) -> entry.getValue() * (1 + totalComposition(entry.getKey())))
        .sum();
  }

  private void findAllContaining(String color, Set<String> composed) {
    if (containers.containsKey(color)) {
      Set<String> containingColors = containers.get(color);
      for (String container : containingColors) {
        if (!composed.contains(container)) {
          composed.add(container);
          findAllContaining(container, composed);
        }
      }
    }
  }

}
