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
package com.nickbenn.advent.day19;

import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MonsterMessages {

  private static final Pattern RULE_PATTERN =
      Pattern.compile("^(\\d+):\\s*(?:\"(.)\"|([\\d|\\s]*))\\s*$");
  private static final Pattern BRANCH_SPLITTER = Pattern.compile("\\s*\\|\\s*");
  private static final Pattern SEQUENCE_SPLITTER = Pattern.compile("\\s+");

  private final Map<Integer, Rule> rules;
  private final List<String> messages;

  public MonsterMessages(String rulesFile, String messagesFile)
      throws URISyntaxException, IOException {
    try (
        Stream<String> rulesStream = new Parser.Builder(getClass().getResource(rulesFile).toURI())
            .build()
            .lineStream();
        Stream<String> messagesStream =
            new Parser.Builder(getClass().getResource(messagesFile).toURI())
                .build()
                .lineStream();
    ) {
      messages = messagesStream
          .collect(Collectors.toList());
      rules = rulesStream
          .map(RULE_PATTERN::matcher)
          .filter(Matcher::matches)
          .collect(Collectors.toMap(
              (matcher) -> Integer.valueOf(matcher.group(1)),
              (matcher) -> {
                Rule rule;
                if (matcher.group(2) != null && !matcher.group(2).isEmpty()) {
                  rule = new Literal(matcher.group(2).charAt(0));
                } else {
                  List<List<Integer>> sequences = BRANCH_SPLITTER.splitAsStream(matcher.group(3))
                      .map(String::trim)
                      .filter(Predicate.not(String::isEmpty))
                      .map((part) -> SEQUENCE_SPLITTER.splitAsStream(part)
                          .map(String::trim)
                          .map(Integer::valueOf)
                          .collect(Collectors.toList())
                      )
                      .collect(Collectors.toList());
                  rule = new Recursive(sequences);
                }
                return rule;
              }
          ));
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    MonsterMessages part1 = new MonsterMessages("rules-1.txt", "messages.txt");
    System.out.println(part1.countValid());
    MonsterMessages part2 = new MonsterMessages("rules-2.txt", "messages.txt");
    System.out.println(part2.countValid());
  }

  public long countValid() {
    Rule root = rules.get(0);
    return messages.stream()
        .map(List::of)
        .map(root::matches)
        .filter(Predicate.not(List::isEmpty))
        .filter((list) -> list.stream().anyMatch(String::isEmpty))
        .count();
  }

  private interface Rule {

    List<String> matches(List<String> inputs);

  }

  private static class Literal implements Rule {

    private final char letter;

    public Literal(char letter) {
      this.letter = letter;
    }

    @Override
    public List<String> matches(List<String> inputs) {
      List<String> results = new LinkedList<>();
      for (String input : inputs) {
        if (input != null && !input.isEmpty() && input.charAt(0) == letter) {
          results.add(input.substring(1));
        }
      }
      return results;
    }

  }

  public class Recursive implements Rule {

    private final List<List<Integer>> ruleReferences;

    public Recursive(List<List<Integer>> ruleReferences) {
      this.ruleReferences = List.copyOf(ruleReferences);
    }

    @Override
    public List<String> matches(List<String> inputs) {
      List<String> results = new LinkedList<>();
      for (List<Integer> referenceList : ruleReferences) { // Iterate over all OR branch sequences.
        List<String> outputs = new LinkedList<>(inputs);
        for (Integer reference : referenceList) { // Iterate over rules in sequence.
          outputs = rules.get(reference).matches(outputs); // Pass outputs from previous as inputs.
          if (outputs.isEmpty()) {
            break; // Don't continue with sequence
          }
        }
        results.addAll(outputs);
      }
      return results;
    }

  }

}
