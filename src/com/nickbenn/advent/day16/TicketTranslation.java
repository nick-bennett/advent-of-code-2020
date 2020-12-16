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

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TicketTranslation {

  private static final Pattern RULE_PATTERN = Pattern.compile(
      "^\\s*(.*)\\s*:\\s*(\\d+)\\s*-\\s*(\\d+)\\s*or\\s*(\\d+)\\s*-\\s*(\\d+)\\s*$");
  private static final Pattern LINE_SPLITTER = Pattern.compile("\\r\\n?");
  private static final Pattern VALUE_SPLITTER = Pattern.compile("\\s*,\\s*");

  private final Map<String, List<int[]>> ruleRanges;
  private final List<Integer> ticketValues;
  private final List<List<Integer>> nearbyTicketValues;

  public TicketTranslation(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineGroupStream()
    ) {
      String[] groups = stream.toArray(String[]::new);
      ruleRanges = new HashMap<>();
      for (String line : LINE_SPLITTER.split(groups[0])) {
        Matcher matcher = RULE_PATTERN.matcher(line);
        if (matcher.matches()) {
          List<int[]> ranges = List.of(
              new int[]{Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))},
              new int[]{Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5))});
          ruleRanges.put(matcher.group(1), ranges);
        }
      }
      ticketValues = LINE_SPLITTER.splitAsStream(groups[1])
          .skip(1)
          .flatMap(VALUE_SPLITTER::splitAsStream)
          .map(Integer::valueOf)
          .collect(Collectors.toList());
      nearbyTicketValues = LINE_SPLITTER.splitAsStream(groups[2])
          .skip(1)
          .map((line) -> VALUE_SPLITTER.splitAsStream(line)
              .map(Integer::valueOf)
              .collect(Collectors.toList()))
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    TicketTranslation translation = new TicketTranslation(Defaults.FILENAME);
    System.out.println(translation.sumInvalidValues());
    System.out.println(translation.getProductForPrefix("departure"));
  }

  public int sumInvalidValues() {
    List<int[]> validRanges = ruleRanges.values().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return nearbyTicketValues.stream()
        .flatMap(List::stream)
        .filter((value) -> {
          boolean valid = false;
          for (int[] range : validRanges) {
            if (value >= range[0] && value <= range[1]) {
              valid = true;
              break;
            }
          }
          return !valid;
        })
        .mapToInt(Integer::intValue)
        .sum();
  }

  public long getProductForPrefix(String prefix) {
    Map<String, BitSet> possibleColumns = initializeBitSets(ruleRanges.size());
    removeInvalid(possibleColumns);
    Map<String, Integer> assignments = getAssignments(possibleColumns);
    return assignments.entrySet().stream()
        .filter((entry) -> entry.getKey().startsWith(prefix))
        .map(Entry::getValue)
        .map(ticketValues::get)
        .mapToLong(Integer::longValue)
        .reduce(1L, (a, b) -> a * b);
  }

  private Map<String, BitSet> initializeBitSets(int numFields) {
    return ruleRanges.keySet().stream()
        .collect(Collectors.toMap(
            (key) -> key,
            (key) -> {
              BitSet possibles = new BitSet(numFields);
              possibles.set(0, numFields);
              return possibles;
            }
        ));
  }

  private void removeInvalid(Map<String, BitSet> possibleColumns) {
    potentiallyValidTickets()
        .forEach((values) -> {
          int column = 0;
          for (int value : values) {
            for (String field : ruleRanges.keySet()) {
              if (possibleColumns.get(field).get(column)) {
                boolean valid = false;
                for (int[] range : ruleRanges.get(field)) {
                  if (value >= range[0] && value <= range[1]) {
                    valid = true;
                    break;
                  }
                }
                if (!valid) {
                  possibleColumns.get(field).clear(column);
                }
              }
            }
            column++;
          }
        });
  }

  private Map<String, Integer> getAssignments(Map<String, BitSet> possibleColumns) {
    Map<String, Integer> assignments = new HashMap<>();
    while (assignments.size() < possibleColumns.size()) {
      long assignmentsFound;
      do {
        assignmentsFound = possibleColumns.entrySet().stream()
            .filter((entry) -> entry.getValue().cardinality() == 1)
            .peek((entry) -> {
              int column = entry.getValue().nextSetBit(0);
              assignments.put(entry.getKey(), column);
              for (String key : possibleColumns.keySet()) {
                possibleColumns.get(key).clear(column);
              }
            })
            .count();
      } while (assignmentsFound > 0);
    }
    return assignments;
  }

  private Stream<List<Integer>> potentiallyValidTickets() {
    List<int[]> validRanges = ruleRanges.values().stream()
        .flatMap(List::stream)
        .collect(Collectors.toList());
    return nearbyTicketValues.stream()
        .filter((values) -> values.stream()
            .allMatch((value) -> {
              boolean valid = false;
              for (int[] range : validRanges) {
                if (value >= range[0] && value <= range[1]) {
                  valid = true;
                  break;
                }
              }
              return valid;
            }));
  }

}
