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
package com.nickbenn.advent.day9;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class EncodingErrorTest {

  @Test
  void firstInvalid() throws IOException, URISyntaxException {
    EncodingError encodingError = new EncodingError("test.txt", 5);
    long badValue = encodingError.firstInvalid();
    assertEquals(127, badValue);
  }

  @Test
  void bracketSum() throws IOException, URISyntaxException {
    EncodingError encodingError = new EncodingError("test.txt", 5);
    assertEquals(62, encodingError.bracketSum(127));
  }

}