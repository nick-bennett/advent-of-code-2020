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
package com.nickbenn.advent.day6;

import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.BitSet;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DeclarationFormCheck {

  private static final Pattern GROUP_DELIMITER = Pattern.compile("\\n\\s*\\n");
  private static final Pattern PERSON_DELIMITER = Pattern.compile("\\s*\\n\\s*");
  private static final String WHITESPACE = "\\s+";

  private final Collection<String> groups;

  public DeclarationFormCheck() throws IOException, URISyntaxException {
    groups = new Parser.Builder(getClass())
        .build()
        .lineGroupStream()
        .collect(Collectors.toList());
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    DeclarationFormCheck check = new DeclarationFormCheck();
    System.out.println(check.responseUnion());
    System.out.println(check.responseIntersection());
  }

  public int responseUnion() {
    return groups.stream()
        .map((group) -> group.replaceAll(WHITESPACE, ""))
        .mapToInt((group) -> group.chars()
            .collect(BitSet::new, BitSet::set, BitSet::or)
            .cardinality()
        )
        .sum();
  }

  public int responseIntersection() {
    return (int) groups.stream()
        .mapToInt((group) -> PERSON_DELIMITER.splitAsStream(group)
            .map((person) -> person.chars()
                .collect(BitSet::new, BitSet::set, BitSet::or)
            )
            .collect(DeclarationFormCheck::intersectionBase, BitSet::and, BitSet::and)
            .cardinality()
        )
        .sum();
  }

  private static BitSet intersectionBase() {
    BitSet base = new BitSet();
    base.set('a', 'z' + 1);
    return base;
  }

}
