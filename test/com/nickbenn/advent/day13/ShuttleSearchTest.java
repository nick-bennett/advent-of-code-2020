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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ShuttleSearchTest {

  static final int THRESHOLD = 939;
  static final Integer[] routeLengths = {7, 13, null, null, 59, null, 31, 19};
  static final List<Integer> ROUTE_LENGTHS;

  static {
    //noinspection FuseStreamOperations
    ROUTE_LENGTHS = Collections.unmodifiableList(
        Arrays.stream(routeLengths)
            .collect(Collectors.toList())
    );
  }

  @Test
  void getMinWaitProduct() throws IOException, URISyntaxException {
    assertEquals(295, ShuttleSearch.getMinWaitProduct(THRESHOLD, ROUTE_LENGTHS));
  }

  @Test
  void getOffsetSynchTimestamp() throws IOException, URISyntaxException {
    assertEquals(1068781L, ShuttleSearch.findOffsetSynchSmartForce(ROUTE_LENGTHS));
  }

}