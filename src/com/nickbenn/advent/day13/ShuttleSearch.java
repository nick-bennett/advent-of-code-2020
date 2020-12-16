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
    var threshold = Integer.parseInt(input.get(0));
    List<Integer> routeLengths = ROUTE_LENGTH_SPLITTER.splitAsStream(input.get(1))
        .map((value) -> value.equals(NULL_INPUT_VALUE) ? null : Integer.valueOf(value))
        .collect(Collectors.toList());
    System.out.println(getMinWaitProduct(threshold, routeLengths));
    System.out.println(findOffsetSynchSmartForce(routeLengths));
//    System.out.println(findOffsetSynchModInverse(routeLengths));
  }

  public static int getMinWaitProduct(int threshold, List<Integer> routeLengths) {
    var bestInterval = -1;
    var bestWait = Integer.MAX_VALUE;
    for (var length : routeLengths.stream()
        .filter(Objects::nonNull)
        .collect(Collectors.toList())) {
      var wait = length - threshold % length;
      if (wait < bestWait) {
        bestWait = wait;
        bestInterval = length;
      }
    }
    return bestWait * bestInterval;
  }

  public static long findOffsetSynchSmartForce(List<Integer> routeLengths) {
    var baseline = 0L;
    var offset = 0;
    var cycleLength = routeLengths.get(0).longValue();
    for (var length : routeLengths.subList(1, routeLengths.size())) {
      offset++;
      if (length != null) {
        while ((baseline + offset) % length != 0) {
          baseline += cycleLength;
        }
        cycleLength *= length;
      }
    }
    return baseline;
  }

  public static long findOffsetSynchModInverse(List<Integer> routeLengths) {
    var baseline = 0L;
    var offset = 0;
    var cycleLength = routeLengths.get(0).longValue();
    for (Integer length : routeLengths.subList(1, routeLengths.size())) {
      offset++;
      if (length != null) {
        var additionalOffset = baseline % length;
        var gap = offset + additionalOffset;
        baseline -= additionalOffset;
        var inverse = modInverse(length, cycleLength);
//            BigInteger.valueOf(length).modInverse(BigInteger.valueOf(cycleLength)).longValue();
        baseline += inverse * gap % cycleLength * length - offset;
        cycleLength *= length;
      }
    }
    return baseline;
  }

  private static long modInverse(long value, long modulus) {
    var prevR = value;
    var r = modulus;
    var prevS = 1L;
    var s = 0L;
    while (r != 1) {
      var quotient = prevR / r;
      var nextR = prevR - quotient * r;
      prevR = r;
      r = nextR;
      var nextS = prevS - quotient * s;
      prevS = s;
      s = nextS;
      if (r == 0) {
        throw new IllegalArgumentException(String.format("%d is not invertible for modulus %d", value, modulus));
      }
    }
    return (s > 0) ? s : modulus + s;
  }

}
