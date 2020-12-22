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
package com.nickbenn.advent.day22;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class CrabCombatTest {

  @Test
  void playNonRecursive() throws IOException, URISyntaxException {
    CrabCombat combat = new CrabCombat("test-1.txt", "test-2.txt");
    combat.playNonRecursive(combat.getPlayer1(), combat.getPlayer2());
    assertEquals(306, combat.checksum(combat.getPlayer2()));
  }

  @Test
  void playRecursive() throws IOException, URISyntaxException {
    CrabCombat combat = new CrabCombat("test-1.txt", "test-2.txt");
    combat.playRecursive(combat.getPlayer1(), combat.getPlayer2());
    assertEquals(291, combat.checksum(combat.getPlayer2()));
  }

}