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
package com.nickbenn.advent.day3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TobogganTrajectoryTest {

  private static Stream<Arguments> trees() {
    return Stream.of(
        Arguments.of(Defaults.TEST_FILENAME, TobogganTrajectory.slopes1, 7),
        Arguments.of(Defaults.TEST_FILENAME, TobogganTrajectory.slopes2, 336)
    );
  }

  @ParameterizedTest
  @MethodSource
  void trees(String filename, int[][] slopes, long expected)
      throws IOException, URISyntaxException {
    assertEquals(expected, new TobogganTrajectory(filename).trees(slopes));
  }
}