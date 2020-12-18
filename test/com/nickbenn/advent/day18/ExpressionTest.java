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

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.day18.Expression.Operator;
import com.nickbenn.advent.util.Defaults;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class ExpressionTest {

  static final Collection<Operator> FLAT = List.of(
      new Operator('+', 1, Long::sum),
      new Operator('*', 1, (a, b) -> a * b)
  );

  static final Collection<Operator> WEIRD = List.of(
      new Operator('+', 2, Long::sum),
      new Operator('*', 1, (a, b) -> a * b)
  );

  @ParameterizedTest
  @CsvFileSource(resources = "test1.txt", numLinesToSkip = 1)
  void getValue_flatPriorities(String input, int expected) {
    Expression expression = new Expression(input, FLAT);
    assertEquals(expected, expression.getValue());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "test2.txt", numLinesToSkip = 1)
  void getValue_weirdPriorities(String input, int expected) {
    Expression expression = new Expression(input, WEIRD);
    assertEquals(expected, expression.getValue());
  }

}