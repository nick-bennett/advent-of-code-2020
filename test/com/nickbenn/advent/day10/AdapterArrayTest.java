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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class AdapterArrayTest {

  private static Stream<Arguments> sumMaxChainDifferences() {
    return Stream.of(
        Arguments.of("test1.txt", 35),
        Arguments.of("test2.txt", 220)
    );
  }

  private static Stream<Arguments> countViableChains() {
    return Stream.of(
        Arguments.of("test1.txt", 8),
        Arguments.of("test2.txt", 19208)
    );
  }

  @ParameterizedTest
  @MethodSource
  void sumMaxChainDifferences(String filename, long expected)
      throws IOException, URISyntaxException {
    AdapterArray adapterArray = new AdapterArray(filename);
    assertEquals(expected, adapterArray.productMaxChainDifferences());
  }

  @ParameterizedTest
  @MethodSource
  void countViableChains(String filename, long expected) throws IOException, URISyntaxException {
    AdapterArray adapterArray = new AdapterArray(filename);
    assertEquals(expected, adapterArray.countViableChains());
  }
}