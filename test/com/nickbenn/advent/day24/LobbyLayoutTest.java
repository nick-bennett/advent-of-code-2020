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
package com.nickbenn.advent.day24;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;
import org.junit.jupiter.api.Test;

class LobbyLayoutTest {

  @Test
  void getInitialConfiguration() throws IOException, URISyntaxException {
    LobbyLayout layout = new LobbyLayout(Defaults.TEST_FILENAME);
    Set<HexagonalCell> population = layout.getInitialConfiguration();
    assertEquals(10, population.size());
  }

  @Test
  void iterate() throws IOException, URISyntaxException {
    LobbyLayout layout = new LobbyLayout(Defaults.TEST_FILENAME);
    Set<HexagonalCell> population = layout.getInitialConfiguration();
    for (int i = 0; i < LobbyLayout.NUM_GENERATIONS; i++) {
      population = layout.iterate(population);
    }
    assertEquals(2208, population.size());
  }

}