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
package com.nickbenn.advent.day16;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class TicketTranslationTest {

  @Test
  void sumInvalidValues() throws IOException, URISyntaxException {
    TicketTranslation translation = new TicketTranslation("test1.txt");
    assertEquals(71, translation.sumInvalidValues());
  }

  @Test
  void getProdctForPrefix() throws IOException, URISyntaxException {
    TicketTranslation translation = new TicketTranslation("test2.txt");
    assertEquals(12, translation.getProductForPrefix("class"));
    assertEquals(11, translation.getProductForPrefix("row"));
    assertEquals(13, translation.getProductForPrefix("seat"));
  }

}