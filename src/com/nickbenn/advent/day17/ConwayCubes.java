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
package com.nickbenn.advent.day17;

import com.nickbenn.advent.ca.Cell;
import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConwayCubes {

  private static final Comparator<Cell> REVERSE_DIMENSION_COMPARATOR = (cellA, cellB) -> {
    int upperBound = Math.min(cellA.getDimensions(), cellB.getDimensions());
    int comparison = 0;
    for (int i = upperBound - 1; i >= 0; i--) {
      if ((comparison = cellA.get(i) - cellB.get(i)) != 0) {
        break;
      }
    }
    return comparison;
  };

  private final Set<Cell> cells;

  public ConwayCubes(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
    ) {
      int[] yValue = {0};
      cells = stream
          .flatMap((line) -> {
            Set<Cell> row = new HashSet<>();
            for (int xValue = line.indexOf('#'); xValue >= 0;
                xValue = line.indexOf('#', xValue + 1)) {
              row.add(new Cell(xValue, yValue[0]));
            }
            yValue[0]++;
            return row.stream();
          })
          .collect(Collectors.toCollection(TreeSet::new));
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    ConwayCubes cubes = new ConwayCubes(Defaults.FILENAME);
    System.out.println(cubes.countActive(3, 6));
    System.out.println(cubes.countActive(4, 6));
  }

  public int countActive(int dimensions, int generations) {
    Set<Cell> population = cells.stream()
        .map((cell) -> new Cell(cell, dimensions))
        .collect(Collectors.toCollection(() -> new TreeSet<>(REVERSE_DIMENSION_COMPARATOR)));
    for (int i = 0; i < generations; i++) {
      iterate(population);
    }
    return population.size();
  }

  public void iterate(Set<Cell> actives) {
    Set<Cell> next = new TreeSet<>(REVERSE_DIMENSION_COMPARATOR);
    Map<Cell, Integer> inactives = new TreeMap<>(REVERSE_DIMENSION_COMPARATOR);
    for (Cell cell : actives) {
      Set<Cell> neighbors = cell.getNeighbors();
      for (Cell neighbor : neighbors) {
        if (!actives.contains(neighbor)) {
          inactives.put(neighbor, inactives.getOrDefault(neighbor, 0) + 1);
        }
      }
      neighbors.retainAll(actives);
      int size = neighbors.size();
      if (size >= 2 && size <= 3) {
        next.add(cell);
      }
    }
    inactives.entrySet().stream()
        .filter((entry) -> entry.getValue() == 3)
        .map(Map.Entry::getKey)
        .forEach(next::add);
    actives.clear();
    actives.addAll(next);
  }

}
