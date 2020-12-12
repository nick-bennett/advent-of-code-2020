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
package com.nickbenn.advent.day8;

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class HandheldHaltingTest {

  @Test
  void executeUntilLoop() throws IOException, URISyntaxException {
    HandheldHalting handheldHalting = new HandheldHalting(Defaults.TEST_FILENAME);
    assertEquals(5, handheldHalting.executeUntilLoop().getRegisters().get("accumulator"));
  }

  @Test
  void findAndFix() throws IOException, URISyntaxException {
    HandheldHalting handheldHalting = new HandheldHalting(Defaults.TEST_FILENAME);
    assertEquals(8, handheldHalting.findAndFix().getRegisters().get("accumulator"));
  }

}