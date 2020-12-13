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
package com.nickbenn.advent.day13;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class ShuttleSearchTest {

  @Test
  void getMinWaitProduct() throws IOException, URISyntaxException {
    ShuttleSearch shuttleSearch = new ShuttleSearch(Defaults.TEST_FILENAME);
    assertEquals(295, shuttleSearch.getMinWaitProduct());
  }

  @Test
  void getOffsetSynchTimestamp() throws IOException, URISyntaxException {
    ShuttleSearch shuttleSearch = new ShuttleSearch(Defaults.TEST_FILENAME);
    assertEquals(1068781L, shuttleSearch.getOffsetSynchTimestamp());
  }

}