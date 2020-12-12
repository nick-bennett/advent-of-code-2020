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
package com.nickbenn.advent.day11;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.stream.Stream;

public class SeatingSystem {

  private static final char FLOOR = '.';
  private static final char UNOCCUPIED = 'L';
  private static final char OCCUPIED = '#';

  private final char[][] tableau;
  private final char[][] current;
  private final char[][] work;

  public SeatingSystem(String filename) throws URISyntaxException, IOException {
    try (
        Stream<char[]> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .charArrayStream()
    ) {
      tableau = stream.toArray(char[][]::new);
      current = new char[tableau.length][];
      work = new char[tableau.length][];
      reset();
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    SeatingSystem seatingSystem = new SeatingSystem(Defaults.FILENAME);
    System.out.println(seatingSystem.countMooreStablePopulation());
    seatingSystem.reset();
    System.out.println(seatingSystem.countLineOfSightStablePopulation());
  }

  public void reset() {
    for (int i = 0; i < tableau.length; i++) {
      current[i] = Arrays.copyOf(tableau[i], tableau[i].length);
      work[i] = new char[tableau[i].length];
    }
  }

  public int countMooreStablePopulation() {
    //noinspection StatementWithEmptyBody
    while (iterateMoore()) {};
    return countCurrentPopulation();
  }

  public int countLineOfSightStablePopulation() {
    //noinspection StatementWithEmptyBody
    while (iterateLineOfSight()) {};
    return countCurrentPopulation();
  }

  private boolean iterateMoore() {
    boolean changed = false;
    for (int row = 0; row < current.length; row++) {
      for (int col = 0; col < current[row].length; col++) {
        char state = current[row][col];
        if (state != FLOOR) {
          int count = countMooreNeighborhoodPopulation(row, col);
          if (state == UNOCCUPIED && count == 0) {
            work[row][col] = OCCUPIED;
            changed = true;
          } else if (state == OCCUPIED && count >= 4) {
            work[row][col] = UNOCCUPIED;
            changed = true;
          } else {
            work[row][col] = state;
          }
        } else {
          work[row][col] = state;
        }
      }
    }
    for (int row = 0; row < current.length; row++) {
      current[row] = Arrays.copyOf(work[row], work[row].length);
    }
    return changed;
  }

  private boolean iterateLineOfSight() {
    boolean changed = false;
    for (int row = 0; row < current.length; row++) {
      for (int col = 0; col < current[row].length; col++) {
        char state = current[row][col];
        if (state != FLOOR) {
          int count = countLineOfSiteNeighborhoodPopulation(row, col);
          if (state == UNOCCUPIED && count == 0) {
            work[row][col] = OCCUPIED;
            changed = true;
          } else if (state == OCCUPIED && count >= 5) {
            work[row][col] = UNOCCUPIED;
            changed = true;
          } else {
            work[row][col] = state;
          }
        } else {
          work[row][col] = state;
        }
      }
    }
    for (int row = 0; row < current.length; row++) {
      current[row] = Arrays.copyOf(work[row], work[row].length);
    }
    return changed;
  }

  private int countCurrentPopulation() {
    int count = 0;
    for (char[] row : current) {
      for (char state : row) {
        if (state == OCCUPIED) {
          count++;
        }
      }
    }
    return count;
  }

  private int countMooreNeighborhoodPopulation(int row, int col) {
    int count = 0;
    for (int r = Math.max(0, row - 1); r <= Math.min(current.length - 1, row + 1); r++) {
      for (int c = Math.max(0, col - 1); c <= Math.min(current[r].length - 1, col + 1); c++) {
        if (current[r][c] == OCCUPIED) {
          count++;
        }
      }
    }
    return count - ((current[row][col] == OCCUPIED) ? 1 : 0);
  }

  private int countLineOfSiteNeighborhoodPopulation(int row, int col) {
    int count = 0;
    for (Direction dir : Direction.values()) {
      int rowOffset = dir.getRowOffset();
      int colOffset = dir.getColOffset();
      for (int r = row + rowOffset, c = col + colOffset;
          r >= 0 && r < current.length && c >= 0 && c < current[r].length;
          r += rowOffset, c += colOffset) {
        char state = current[r][c];
        if (state != FLOOR) {
          if (state == OCCUPIED) {
            count++;
          }
          break;
        }
      }
    }
    return count;
  }

  private enum Direction {

    NORTH(-1, 0),
    NORTH_EAST(-1, 1),
    EAST(0, 1),
    SOUTH_EAST(1, 1),
    SOUTH(1, 0),
    SOUTH_WEST(1, -1),
    WEST(0, -1),
    NORTH_WEST(-1, -1);

    Direction(int rowOffset, int colOffset) {
      this.rowOffset = rowOffset;
      this.colOffset = colOffset;
    }

    private final int rowOffset;
    private final int colOffset;

    public int getRowOffset() {
      return rowOffset;
    }

    public int getColOffset() {
      return colOffset;
    }
  }
}
