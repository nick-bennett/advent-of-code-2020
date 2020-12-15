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
package com.nickbenn.advent.day15;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RambunctiousRecitation {

  private static final Pattern ELEMENT_DELIMITER = Pattern.compile("\\s*,\\s*");

  public static void main(String[] args) throws IOException, URISyntaxException {
    try (
        Stream<int[]> stream =
            new Parser.Builder(RambunctiousRecitation.class.getResource(Defaults.FILENAME).toURI())
                .build()
                .lineStream()
                .map((line) ->
                    ELEMENT_DELIMITER.splitAsStream(line)
                        .mapToInt(Integer::parseInt)
                        .toArray()
                )
    ) {
      stream.forEach((row) -> {
        int term = row[0];
        int[] inputValues = Arrays.copyOfRange(row, 1, row.length);
        System.out.println(process(inputValues, term));
      });
    }
  }

  public static int process(int[] inputValues, int limit) {
    Map<Integer, Integer> positions = new HashMap<>();
    int position = 0;
    int last;
    Integer lastPosition = null;
    for (int value : inputValues) {
      last = value;
      lastPosition = positions.put(last, ++position);
    }
    do {
      if (lastPosition == null) {
        last = 0;
      } else {
        last = position - lastPosition;
      }
      lastPosition = positions.put(last, ++position);
    } while (position < limit);
    return last;
  }

}
