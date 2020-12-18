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
package com.nickbenn.advent.day18;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationOrder {

  private static final Map<Character, Integer> FLAT_PRIORITIES =
      Map.of('+', 1, '-', 1, '*', 1, '/', 1);
  private static final Map<Character, Integer> WEIRD_PRIORITIES =
      Map.of('+', 2, '-', 2, '*', 1, '/', 1);

  private final List<String> lines;

  public OperationOrder(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
    ) {
      lines = stream
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    OperationOrder operationOrder = new OperationOrder(Defaults.FILENAME);
    System.out.println(operationOrder.getSum(FLAT_PRIORITIES));
    System.out.println(operationOrder.getSum(WEIRD_PRIORITIES));
  }

  public long getSum(Map<Character, Integer> priorities) {
    return lines.stream()
        .map((line) -> new Expression(line, priorities))
        .mapToLong(Expression::getValue)
        .sum();
  }
}
