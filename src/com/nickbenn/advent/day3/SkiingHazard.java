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
package com.nickbenn.advent.day3;

import com.nickbenn.advent.util.Parser.Builder;
import java.io.IOException;
import java.net.URISyntaxException;

public class SkiingHazard {

  private static final int[][] slopes1 = {
      {3, 1}
  };
  private static final int[][] slopes2 = {
      {1, 1},
      {3, 1},
      {5, 1},
      {7, 1},
      {1, 2},
  };

  private final char[][] terrain;

  public SkiingHazard() throws IOException, URISyntaxException {
    terrain = new Builder(getClass())
        .build()
        .lineStream()
        .map(String::toCharArray)
        .toArray(char[][]::new);
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    SkiingHazard hazard = new SkiingHazard();
    System.out.println(hazard.trees(slopes1));
    System.out.println(hazard.trees(slopes2));
  }

  private long trees(int[][] slopes) {
    long product = 1;
    for (int[] slope : slopes) {
      int right = slope[0];
      int down = slope[1];
      long trees = 0;
      int width = terrain[0].length;
      for (int row = 0, col = 0; row < terrain.length; row += down, col += right) {
        col %= width;
        if (terrain[row][col] == '#') {
          trees++;
        }
      }
      product *= trees;
    }
    return product;
  }

}
