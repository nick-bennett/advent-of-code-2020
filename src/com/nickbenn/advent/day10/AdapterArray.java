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
package com.nickbenn.advent.day10;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class AdapterArray {

  private final long[] values;

  public AdapterArray(String filename) throws URISyntaxException, IOException {
    try (
        LongStream stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .longStream()
            .sorted()
    ) {
      long[] intermediateValues = stream.toArray();
      values = new long[intermediateValues.length + 1];
      System.arraycopy(intermediateValues, 0, values, 1, intermediateValues.length);
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    AdapterArray adapterArray = new AdapterArray(Defaults.FILENAME);
    System.out.println(adapterArray.productMaxChainDifferences());
    System.out.println(adapterArray.countViableChains());
  }

  public long productMaxChainDifferences() {
    Map<Integer, Integer> counts = IntStream.range(0, values.length - 1)
        .map((i) -> (int) (values[i + 1] - values[i]))
        .collect(
            (Supplier<HashMap<Integer, Integer>>) HashMap::new,
            (map, value) -> map.put(value, map.getOrDefault(value, 0) + 1),
            HashMap::putAll
        );
    return (long) counts.get(1) * (counts.get(3) + 1);
  }

  public long countViableChains() {
    BitSet removable = removableAdapters();
    int start = removable.nextSetBit(0);
    long product = 1;
    while (start >= 0) {
      int end = removable.nextClearBit(start);
      product *= (countViableChains(values[start - 1], start, end) + 1);
      start = removable.nextSetBit(end);
    }
    return product;
  }

  private BitSet removableAdapters() {
    BitSet removable = new BitSet(values.length);
    for (int i = 1; i < values.length - 1; i++) {
      if (values[i + 1] <= values[i - 1] + 3) {
        removable.set(i);
      }
    }
    return removable;
  }

  private long countViableChains(long base, int start, int end) {
    long count = 0;
    if (start < end && values[start + 1] <= base + 3) {
      count++;
      count += countViableChains(base, start + 1, end);
    }
    for (int i = start + 1; i < end; i++) {
      long baseCandidate = values[i - 1];
      if (values[i + 1] <= baseCandidate + 3) {
        count++;
        count += countViableChains(baseCandidate, i + 1, end);
      }
    }
    return count;
  }

}
