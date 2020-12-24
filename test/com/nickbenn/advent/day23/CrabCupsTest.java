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
package com.nickbenn.advent.day23;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

class CrabCupsTest {

  static final String INITIAL_ARRANGEMENT = "389125467";
  static final String EXPECTED_ARRANGEMENT_GAME_1 = "67384529";
  static final long EXPECTED_PRODUCT_GAME_2 = 149245887792L;

  @Test
  void play() throws ExecutionException, InterruptedException {
    CrabCups crabCups = new CrabCups(INITIAL_ARRANGEMENT);
    crabCups.play(CrabCups.MOVES_IN_GAME_1);
    assertEquals(EXPECTED_ARRANGEMENT_GAME_1, crabCups.reportCircle());
  }

  @Test
  void play_long() throws ExecutionException, InterruptedException {
    CrabCups crabCups = new CrabCups(INITIAL_ARRANGEMENT, CrabCups.CUPS_IN_GAME_2);
    crabCups.play(CrabCups.MOVES_IN_GAME_2);
    assertEquals(EXPECTED_PRODUCT_GAME_2, crabCups.reportNextProduct());
  }

}