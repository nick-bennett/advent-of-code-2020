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
package com.nickbenn.advent.day9;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

public class EncodingError {

  private final long[] values;
  private final Map<Integer, Map<Integer, Long>> sumPartners;
  private final int window;

  public EncodingError(String filename, int window) throws URISyntaxException, IOException {
    try (
        LongStream stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .longStream()
    ) {
      this.window = window;
      sumPartners = new HashMap<>();
      values = stream.toArray();
      for (int i = 0; i < values.length; i++) {
        long value1 = values[i];
        Map<Integer, Long> outerPartners =
            sumPartners.computeIfAbsent(i, (ignored) -> new HashMap<>());
        for (int j = Math.max(0, i - window + 1); j < i; j++) {
          long value2 = values[j];
          Map<Integer, Long> innerPartners =
              sumPartners.computeIfAbsent(j, (ignored) -> new HashMap<>());
          long sum = value1 + value2;
          outerPartners.put(j, sum);
          innerPartners.put(i, sum);
        }
      }
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    EncodingError encodingError = new EncodingError(Defaults.FILENAME, 25);
    long badValue = encodingError.firstInvalid();
    System.out.println(badValue);
    System.out.println(encodingError.bracketSum(badValue));
  }

  public long firstInvalid() {
    Map<Long, Integer> availableSums = new HashMap<>();
    for (int i = 0; i < window; i++) {
      for (long sum : sumPartners.get(i).values()) {
        availableSums.put(sum, availableSums.getOrDefault(sum, 0) + 1);
      }
    }
    for (int i = window; i < values.length; i++) {
      long value = values[i];
      if (availableSums.getOrDefault(value, 0) <= 0) {
        return value;
      }
      Map<Integer, Long> leaving = sumPartners.get(i - window);
      for (long sum : leaving.values()) {
        availableSums.put(sum, availableSums.get(sum) - 1);
      }
      for (long sum : sumPartners.get(i).values()) {
        availableSums.put(sum, availableSums.getOrDefault(sum, 0) + 1);
      }
    }
    return -1;
  }

  public long bracketSum(long target) {
    int left = 0;
    int right = 1;
    long leftValue = values[left];
    long rightValue = values[right];
    long sum = leftValue + rightValue;
    do {
      if (sum < target) {
        rightValue = values[++right];
        sum += rightValue;
      } else if (sum > target) {
        sum -= leftValue;
        leftValue = values[++left];
      }
    } while (sum != target);
    long smallest = Long.MAX_VALUE;
    long largest = Long.MIN_VALUE;
    for (int i = left; i <= right; i++) {
      long value = values[i];
      smallest = Math.min(smallest, value);
      largest = Math.max(largest, value);
    }
    return smallest + largest;
  }

}
