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
package com.nickbenn.advent.day2;

import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) throws URISyntaxException, IOException {
    List<Entry> entries = new Parser.Builder(Main.class)
        .build()
        .stream()
        .map(Entry::new)
        .collect(Collectors.toUnmodifiableList());
    System.out.println(getValidByCount(entries));
    System.out.println(getValidByPosition(entries));
  }

  private static long getValidByCount(List<Entry> entries) throws IOException {
    return entries.stream()
        .filter((entry) -> {
          String password = entry.getPassword();
          int occurrences = password.length() - password.replace(entry.getRequired(), "").length();
          return occurrences >= entry.getMinimum() && occurrences <= entry.getMaximum();
        })
        .count();
  }

  private static long getValidByPosition(List<Entry> entries) throws IOException {
    return entries.stream()
        .filter((entry) -> {
          char[] password = entry.getPassword().toCharArray();
          char requiredChar = entry.getRequired().charAt(0);
          return (password[entry.getMinimum() - 1] == requiredChar)
              ^ (password[entry.getMaximum() - 1] == requiredChar);
        })
        .count();
  }

}
