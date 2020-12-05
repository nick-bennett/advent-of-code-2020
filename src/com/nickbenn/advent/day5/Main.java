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
package com.nickbenn.advent.day5;

import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.IntStream;

public class Main {

  public static void main(String[] args) throws URISyntaxException, IOException {
    int[] data = new Parser.Builder(Main.class)
        .build()
        .stream()
        .mapToInt(Main::getId)
        .toArray();
    System.out.println(max(data));
    System.out.println(missing(data));
  }

  private static int max(int[] data) {
    return IntStream.of(data)
        .max()
        .orElse(0);
  }

  private static int missing(int[] data) {
    return 1 + IntStream.of(data)
        .sorted()
        .reduce((a, b) -> (b - a > 1) ? a : b)
        .orElse(0);
  }

  private static int getId(String assignment) {
    return assignment.chars()
        .map((c) -> (c == 'R' || c == 'B') ? 1 : 0)
        .reduce((a, b) -> (a << 1) + b)
        .orElse(0);
  }

}
