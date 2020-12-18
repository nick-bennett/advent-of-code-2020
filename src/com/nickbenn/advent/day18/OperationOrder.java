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

import com.nickbenn.advent.day18.Expression.Operator;
import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OperationOrder {

  private static final Collection<Operator> FLAT = List.of(
      new Operator('+', 1, Long::sum),
      new Operator('*', 1, (a, b) -> a * b)
  );

  private static final Collection<Operator> WEIRD = List.of(
      new Operator('+', 2, Long::sum),
      new Operator('*', 1, (a, b) -> a * b)
  );

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
    System.out.println(operationOrder.getSum(FLAT));
    System.out.println(operationOrder.getSum(WEIRD));
  }

  public long getSum(Collection<Operator> operators) {
    return lines.stream()
        .map((line) -> new Expression(line, operators))
        .mapToLong(Expression::getValue)
        .sum();
  }

}
