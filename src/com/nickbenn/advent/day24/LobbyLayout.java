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
package com.nickbenn.advent.day24;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LobbyLayout {

  public static final int NUM_GENERATIONS = 100;

  private static final Pattern NEXT_DIRECTION = Pattern.compile("E|SE|SW|W|NW|NE");
  private static final String POPULATION_REPORT_FORMAT = "Generation %d: Population = %d.%n";

  private final List<List<HexagonalDirection>> tileChanges;

  public LobbyLayout(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream();
    ) {
      tileChanges = stream
          .map(String::toUpperCase)
          .map((line) -> {
            List<HexagonalDirection> directions = new LinkedList<>();
            Matcher matcher = NEXT_DIRECTION.matcher(line);
            while (matcher.find()) {
              directions.add(HexagonalDirection.valueOf(matcher.group()));
            }
            return directions;
          })
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    LobbyLayout layout = new LobbyLayout(Defaults.FILENAME);
    Set<HexagonalCell> population = layout.getInitialConfiguration();
    System.out.printf(POPULATION_REPORT_FORMAT, 0, population.size());
    for (int i = 0; i < NUM_GENERATIONS; i++) {
      population = layout.iterate(population);
    }
    System.out.printf(POPULATION_REPORT_FORMAT, NUM_GENERATIONS, population.size());
  }

  public Set<HexagonalCell> getInitialConfiguration() {
    Set<HexagonalCell> flips = new HashSet<>();
    for (List<HexagonalDirection> directions : tileChanges) {
      int x = 0;
      int y = 0;
      for (HexagonalDirection direction : directions) {
        x += direction.getX();
        y += direction.getY();
      }
      HexagonalCell cell = new HexagonalCell(x, y);
      if (flips.contains(cell)) {
        flips.remove(cell);
      } else {
        flips.add(cell);
      }
    }
    return flips;
  }

  public Set<HexagonalCell> iterate(Set<HexagonalCell> previousGeneration) {
    Set<HexagonalCell> nextGeneration = new HashSet<>();
    Map<HexagonalCell, Integer> boundary = new HashMap<>();
    for (HexagonalCell cell : previousGeneration) {
      Set<HexagonalCell> neighbors = cell.getNeighbors();
      for (HexagonalCell neighbor : neighbors) {
        if (!previousGeneration.contains(neighbor)) {
          boundary.put(neighbor, boundary.getOrDefault(neighbor, 0) + 1);
        }
      }
      neighbors.retainAll(previousGeneration);
      int count = neighbors.size();
      if (count > 0 && count < 3) {
        nextGeneration.add(cell);
      }
    }
    boundary.entrySet().stream()
        .filter((entry) -> entry.getValue() == 2)
        .map(Map.Entry::getKey)
        .forEach(nextGeneration::add);
    return nextGeneration;
  }

}
