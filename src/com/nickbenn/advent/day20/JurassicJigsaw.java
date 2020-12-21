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
package com.nickbenn.advent.day20;

import com.nickbenn.advent.day20.Tile.Edge;
import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JurassicJigsaw {

  public static final List<List<Integer>> SEA_MONSTER_POSITIONS = List.of(
      List.of(18),
      List.of(0, 5, 6, 11, 12, 17, 18, 19),
      List.of(1, 4, 7, 10, 13, 16)
  );

  private static final Pattern HEADER_PATTERN = Pattern.compile("Tile\\s+(\\d+):");
  private static final Pattern TILE_SPLITTER = Pattern.compile("\\r?\\n");
  private final Map<Integer, Tile> tiles;
  private final Map<Integer, Set<Integer>> buckets;

  public JurassicJigsaw(String filename)
      throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineGroupStream()
    ) {
      buckets = new HashMap<>();
      tiles = stream
          .map((block) -> {
            List<String> lines = TILE_SPLITTER.splitAsStream(block)
                .collect(Collectors.toList());
            Matcher matcher = HEADER_PATTERN.matcher(lines.get(0));
            if (matcher.matches()) {
              int id = Integer.parseInt(matcher.group(1));
              boolean[][] data = lines.subList(1, lines.size()).stream()
                  .map(String::trim)
                  .filter(Predicate.not(String::isEmpty))
                  .map(String::toCharArray)
                  .map((chars) -> {
                    boolean[] bits = new boolean[chars.length];
                    for (int i = 0; i < chars.length; i++) {
                      bits[i] = (chars[i] == '#');
                    }
                    return bits;
                  })
                  .toArray(boolean[][]::new);
              return new Tile(id, data);
            } else {
              throw new IllegalArgumentException();
            }
          })
          .peek((tile) -> {
            for (int side : tile.getEdges()) {
              Set<Integer> tiles = buckets.getOrDefault(side, new HashSet<>());
              tiles.add(tile.getId());
              buckets.putIfAbsent(side, tiles);
            }
          })
          .collect(Collectors.toMap(Tile::getId, (tile) -> tile));
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    JurassicJigsaw jigsaw = new JurassicJigsaw(Defaults.FILENAME);
    Tile[][] solution = jigsaw.solve();
    System.out.println((long) solution[0][0].getId() * solution[0][solution.length - 1].getId()
        * solution[solution.length - 1][0].getId()
        * solution[solution.length - 1][solution.length - 1].getId());
    Tile image = jigsaw.merge(solution);
    System.out.println(jigsaw.getRoughness(image));
  }

  public Tile[][] solve() {
    int size = (int) Math.sqrt(tiles.size());
    Tile[][] solution = new Tile[size][size];
    Tile tile = getTopLeftCorner();
    solution[0][0] = tile;
    int matchValue = tile.getEdge(Edge.RIGHT_TOP_TO_BOTTOM);
    for (int col = 1; col < size; col++) {
      tile = getMatch(Edge.LEFT_TOP_TO_BOTTOM, matchValue);
      solution[0][col] = tile;
      matchValue = tile.getEdge(Edge.RIGHT_TOP_TO_BOTTOM);
    }
    for (int row = 1; row < size; row++) {
      matchValue = solution[row - 1][0].getEdge(Edge.BOTTOM_LEFT_TO_RIGHT);
      tile = getMatch(Edge.TOP_LEFT_TO_RIGHT, matchValue);
      solution[row][0] = tile;
      matchValue = tile.getEdge(Edge.RIGHT_TOP_TO_BOTTOM);
      for (int col = 1; col < size; col++) {
        tile = getMatch(Edge.LEFT_TOP_TO_BOTTOM, matchValue);
        solution[row][col] = tile;
        matchValue = tile.getEdge(Edge.RIGHT_TOP_TO_BOTTOM);
      }
    }
    return solution;
  }

  public Tile merge(Tile[][] solution) {
    int fragmentSize = solution[0][0].size() - 2;
    int size = solution.length * fragmentSize;
    boolean[][] data = new boolean[size][size];
    for (int row = 0; row < size; row++) {
      int tileRow = row / fragmentSize;
      int residueRow = row % fragmentSize + 1;
      for (int col = 0; col < size; col++) {
        int tileCol = col / fragmentSize;
        int residueCol = col % fragmentSize + 1;
        boolean[][] fragment = solution[tileRow][tileCol].getData();
        data[row][col] = fragment[residueRow][residueCol];
      }
    }
    return new Tile(0, data);
  }

  public int getRoughness(Tile image) {
    for (Tile variation : image.variations()) {
      boolean found = false;
      boolean[][] data = variation.getData();
      for (int row = 0; row < data.length; row++) {
        for (int col = 0; col < data.length; col++) {
          if (isSeaMonsterAt(data, row, col)) {
            found = true;
            for (int rowOffset = 0; rowOffset < SEA_MONSTER_POSITIONS.size(); rowOffset++) {
              for (int colOffset : SEA_MONSTER_POSITIONS.get(rowOffset)) {
                data[row + rowOffset][col + colOffset] = false;
              }
            }
          }
        }
      }
      if (found) {
        int count = 0;
        for (boolean[] row : data) {
          for (boolean pixel : row) {
            if (pixel) {
              count++;
            }
          }
        }
        return count;
      }
    }
    return 0;
  }

  private Tile getTopLeftCorner() {
    Tile corner = null;
    for (Tile tile : tiles.values()) {
      int exteriorEdges = 0;
      for (int side : tile.getEdges()) {
        if (buckets.get(side).size() == 1) {
          exteriorEdges++;
        }
      }
      if (exteriorEdges >= 4) {
        corner = tile;
        break;
      }
    }
    for (Tile variation : corner.variations()) {
      if (buckets.get(variation.getEdge(Edge.LEFT_TOP_TO_BOTTOM)).size() == 1
          && buckets.get(variation.getEdge(Edge.TOP_LEFT_TO_RIGHT)).size() == 1) {
        for (int edgeValue : variation.getEdges()) {
          buckets.get(edgeValue).remove(variation.getId());
        }
        corner = variation;
        break;
      }
    }
    return corner;
  }

  private Tile getMatch(Edge edge, int match) {
    Tile tile = tiles.get(buckets.get(match).stream()
        .findFirst()
        .orElseThrow())
        .match(edge, match);
    for (int edgeValue : tile.getEdges()) {
      buckets.get(edgeValue).remove(tile.getId());
    }
    return tile;
  }

  private boolean isSeaMonsterAt(boolean[][] data, int row, int col) {
    for (int rowOffset = 0; rowOffset < SEA_MONSTER_POSITIONS.size(); rowOffset++) {
      for (int colOffset : SEA_MONSTER_POSITIONS.get(rowOffset)) {
        if (row + rowOffset >= data.length
            || col + colOffset >= data.length
            || !data[row + rowOffset][col + colOffset]) {
          return false;
        }
      }
    }
    return true;
  }

}
