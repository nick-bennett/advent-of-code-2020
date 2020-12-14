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
package com.nickbenn.advent.day14;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DockingData {

  private static final Pattern LINE_PARSER =
      Pattern.compile("^\\s*(?:mask\\s*=\\s*([X10]{36}))|(?:mem\\[(\\d+)])\\s*=\\s*(\\d+)\\s*$");
  private static final String INITIAL_MASK = "X".repeat(36);

  private final String[] lines;

  public DockingData(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
    ) {
      lines = stream.toArray(String[]::new);
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    DockingData dockingData = new DockingData(Defaults.FILENAME);
    System.out.println(dockingData.getV1Sum());
    System.out.println(dockingData.getV2Sum());
  }

  public long getV1Sum() {
    Map<Long, Long> memory = new HashMap<>();
    process(memory, this::updateV1);
    return memory.values().stream()
        .mapToLong(Long::longValue)
        .sum();
  }

  public long getV2Sum() {
    Map<Long, Long> memory = new HashMap<>();
    process(memory, this::updateV2);
    return memory.values().stream()
        .mapToLong(Long::longValue)
        .sum();
  }

  private void process(Map<Long, Long> memory, Updater updater) {
    char[] mask = INITIAL_MASK.toCharArray();
    for (String line : lines) {
      Matcher matcher = LINE_PARSER.matcher(line);
      if (matcher.matches()) {
        if (matcher.group(1) != null && !matcher.group(1).isEmpty()) {
          mask = new StringBuilder(matcher.group(1)).reverse().toString().toCharArray();
        } else {
          long location = Long.parseLong(matcher.group(2));
          long value = Long.parseLong(matcher.group(3));
          updater.update(memory, mask, location, value);
        }
      }
    }
  }

  private void updateV1(Map<Long, Long> memory, char[] mask, long location, long value) {
    for (int i = 0; i < mask.length; i++) {
      if (mask[i] == '1') {
        value |= (1L << i);
      } else if (mask[i] == '0') {
        value &= ~(1L << i);
      }
    }
    memory.put(location, value);
  }

  private void updateV2(Map<Long, Long> memory, char[] mask, long location, long value) {
    write(memory, mask, location, value, 0);
  }

  private void write(Map<Long, Long> memory, char[] mask, long location, long value, int start) {
    boolean branched = false;
    for (int i = start; i < mask.length; i++) {
      if (mask[i] == '1') {
        location |= (1L << i);
      } else if (mask[i] == 'X') {
        write(memory, mask, location & ~(1L << i), value, i + 1);
        write(memory, mask, location | (1L << i), value, i + 1);
        branched = true;
        break;
      }
    }
    if (!branched) {
      memory.put(location, value);
    }
  }

  private interface Updater {

    void update(Map<Long, Long> memory, char[] mask, long location, long value);

  }

}
