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
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ShuttleSearch {

  private static final Pattern INTERVAL_SPLITTER = Pattern.compile("\\s*,\\s*");
  private static final String MOD_DIVIDE_FAILURE_MESSAGE = "Divisor and modulus are not coprime, "
      + "and dividend was not reached using the extended Euclidean algorithm.";

  private final int threshold;
  private final int[] intervals;
  private final int[] offsets;

  public ShuttleSearch(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
    ) {
      String[] lines = stream.toArray(String[]::new);
      threshold = Integer.parseInt(lines[0]);
      Integer[] rawIntervals = INTERVAL_SPLITTER.splitAsStream(lines[1])
          .map((s) -> s.equals("x") ? null : Integer.valueOf(s))
          .toArray(Integer[]::new);
      offsets = IntStream.range(0, rawIntervals.length)
          .mapToObj((i) -> (rawIntervals[i] != null) ? i : null)
          .filter(Objects::nonNull)
          .mapToInt(Integer::intValue)
          .toArray();
      intervals = Arrays.stream(rawIntervals)
          .filter(Objects::nonNull)
          .mapToInt(Integer::intValue)
          .toArray();
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    ShuttleSearch shuttleSearch = new ShuttleSearch(Defaults.FILENAME);
    System.out.println(shuttleSearch.getMinWaitProduct());
    System.out.println(shuttleSearch.getOffsetSynchTimestamp());
  }

  public int getMinWaitProduct() {
    int bestInterval = -1;
    int bestWait = Integer.MAX_VALUE;
    for (int interval : intervals) {
      int wait = (interval - threshold % interval) % interval;
      if (wait < bestWait) {
        bestWait = wait;
        bestInterval = interval;
      }
    }
    return bestWait * bestInterval;
  }

  public long getOffsetSynchTimestamp() {
    // Assume that all of the numbers are relative primes; none of this works otherwise.
    long baseline = 0;
    long difference = intervals[0];
    for (int i = 1; i < intervals.length; i++) {
      long gap = offsets[i] + baseline % intervals[i];
      baseline -= baseline % intervals[i];
      long inverse = BigInteger.valueOf(intervals[i])
          .modInverse(BigInteger.valueOf(difference))
          .longValue();
      baseline += inverse * gap % difference * intervals[i] - offsets[i];
//      baseline += modDivide(gap, intervals[i], difference) * intervals[i] - offsets[i];
      difference = intervals[i] * difference;
    }
    return baseline;
  }

  public long modDivide(long dividend, long divisor, long modulus) {
    // See https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm#Description and
    // https://en.wikipedia.org/wiki/Extended_Euclidean_algorithm#Example
    long result;
    dividend %= modulus;
    long remainder = modulus;
    long previousRemainder = divisor;
    long coefficient = 0;
    long previousCoefficient = 1;
    while (remainder != dividend && remainder > 1) {
      long quotient = previousRemainder / remainder;
      long nextRemainder = previousRemainder % remainder;
      long nextProduct = previousCoefficient - quotient * coefficient;
      previousRemainder = remainder;
      remainder = nextRemainder;
      previousCoefficient = coefficient;
      coefficient = nextProduct;
    }
    if (remainder == dividend) {
      result = (coefficient > 0) ? coefficient : modulus + coefficient;
    } else if (remainder == 1) {
      result = (coefficient * dividend) % modulus;
    } else {
      throw new ArithmeticException(MOD_DIVIDE_FAILURE_MESSAGE);
    }
    return result;
  }

}
