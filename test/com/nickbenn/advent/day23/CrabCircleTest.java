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

class CrabCircleTest {

  static final String INITIAL_ARRANGEMENT = "389125467";
  static final String EXPECTED_ARRANGEMENT_GAME_1 = "67384529";
  static final long EXPECTED_PRODUCT_GAME_2 = 149245887792L;

  @Test
  void play() {
    CupCircle circle;
    circle = new CupCircle(INITIAL_ARRANGEMENT, INITIAL_ARRANGEMENT.length());
    for (int i = 0; i < CrabCups.MOVES_IN_GAME_1; i++) {
      circle.move();
    }
    assertEquals(EXPECTED_ARRANGEMENT_GAME_1, circle.reportItems());
  }

  @Test
  void play_long() {
    CupCircle circle;
    circle = new CupCircle(INITIAL_ARRANGEMENT, 1_000_000);
    for (int i = 0; i < CrabCups.MOVES_IN_GAME_2; i++) {
      circle.move();
    }
    assertEquals(EXPECTED_PRODUCT_GAME_2, circle.reportProduct(2));
  }

}