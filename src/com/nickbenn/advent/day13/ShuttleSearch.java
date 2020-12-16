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
package com.nickbenn.advent.day13;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShuttleSearch {

  private static final Pattern ROUTE_LENGTH_SPLITTER = Pattern.compile("\\s*,\\s*");
  private static final String NULL_INPUT_VALUE = "x";

  public static void main(String[] args) throws IOException, URISyntaxException {
    List<String> input =
        Files.readAllLines(Path.of(ShuttleSearch.class.getResource(Defaults.FILENAME).toURI()));
    int threshold = Integer.parseInt(input.get(0));
    List<Integer> routeLengths = ROUTE_LENGTH_SPLITTER.splitAsStream(input.get(1))
        .map((value) -> value.equals(NULL_INPUT_VALUE) ? null : Integer.valueOf(value))
        .collect(Collectors.toList());
    System.out.println(getMinWaitProduct(threshold, routeLengths));
    System.out.println(getOffsetSynchTimestamp(routeLengths));
  }

  public static int getMinWaitProduct(int threshold, List<Integer> routeLengths) {
    int bestInterval = -1;
    int bestWait = Integer.MAX_VALUE;
    for (Integer length : routeLengths.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList())) {
      int wait = length - threshold % length;
      if (wait < bestWait) {
        bestWait = wait;
        bestInterval = length;
      }
    }
    return bestWait * bestInterval;
  }

  public static long getOffsetSynchTimestamp(List<Integer> routeLengths) {
    long baseline = 0;
    int offset = 0;
    long cycleLength = routeLengths.get(0);
    for (Integer length : routeLengths.subList(1, routeLengths.size())) {
      offset++;
      if (length != null) {
        long additionalOffset = baseline % length;
        long gap = offset + additionalOffset;
        baseline -= additionalOffset;
        long inverse =
            BigInteger.valueOf(length).modInverse(BigInteger.valueOf(cycleLength)).longValue();
        baseline += inverse * gap % cycleLength * length - offset;
        cycleLength *= length;
      }
    }
    return baseline;
  }

}
