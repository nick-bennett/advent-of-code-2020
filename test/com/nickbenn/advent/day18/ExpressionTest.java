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

import com.nickbenn.advent.util.Defaults;
import java.util.Map;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class ExpressionTest {

  static final Map<Character, Integer> FLAT_PRIORITIES = Map.of('+', 1, '-', 1, '*', 1, '/', 1);
  static final Map<Character, Integer> WEIRD_PRIORITIES = Map.of('+', 2, '-', 2, '*', 1, '/', 1);

  @ParameterizedTest
  @CsvFileSource(resources = "test1.txt", numLinesToSkip = 1)
  void getValue_flatPriorities(String input, int expected) {
    Expression expression = new Expression(input, FLAT_PRIORITIES);
    assertEquals(expected, expression.getValue());
  }

  @ParameterizedTest
  @CsvFileSource(resources = "test2.txt", numLinesToSkip = 1)
  void getValue_weirdPriorities(String input, int expected) {
    Expression expression = new Expression(input, WEIRD_PRIORITIES);
    assertEquals(expected, expression.getValue());
  }

}