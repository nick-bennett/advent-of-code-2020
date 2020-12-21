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
package com.nickbenn.advent.day21;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllergenAssessment {

  private static final Pattern LINE_PATTERN =
      Pattern.compile("^\\s*([^(]*)\\s+\\(contains\\s+([^)]*)\\s*\\)\\s*$");
  private static final Pattern INGREDIENTS_SPLITTER = Pattern.compile("\\s+");
  private static final Pattern ALLERGENS_SPLITTER = Pattern.compile("\\s*,\\s*");

  private final Map<String, Set<String>> potentialAllergens;
  private final Map<String, Set<String>> potentialIngredients;

  public AllergenAssessment(String filename) throws URISyntaxException, IOException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream();
    ) {
      potentialAllergens = new HashMap<>();
      potentialIngredients = new HashMap<>();
      stream
          .map(LINE_PATTERN::matcher)
          .filter(Matcher::matches)
          .forEach((matcher) -> {
            Set<String> ingredients = INGREDIENTS_SPLITTER.splitAsStream(matcher.group(1))
                .collect(Collectors.toSet());
            Set<String> allergens = ALLERGENS_SPLITTER.splitAsStream(matcher.group(2))
                .collect(Collectors.toSet());
            ingredients.forEach((ingredient) -> {
              Set<String> associatedAllergens =
                  potentialAllergens.getOrDefault(ingredient, new HashSet<>());
              associatedAllergens.addAll(allergens);
              potentialAllergens.putIfAbsent(ingredient, associatedAllergens);
            });
            allergens.forEach((allergen) -> {
              Set<String> associatedIngredients =
                  potentialIngredients.getOrDefault(allergen, ingredients);
              associatedIngredients.retainAll(ingredients);
              potentialIngredients.putIfAbsent(allergen, associatedIngredients);
            });
          });
      reduce();
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    AllergenAssessment assessment = new AllergenAssessment(Defaults.FILENAME);
  }

  private void reduce() {
    long reductions;
    do {
      reductions = potentialIngredients.entrySet().stream()
          .filter((allergenEntry) -> allergenEntry.getValue().size() == 1)
          .filter((allergenEntry) -> {
            String allergen = allergenEntry.getKey();
            String ingredient = allergenEntry.getValue().stream()
                .findFirst()
                .get();
            Set<String> allergenSet = Set.of(allergen);
            return !allergenSet.equals(potentialAllergens.put(ingredient, allergenSet))
                | potentialAllergens.entrySet().stream()
                .filter((ingredientEntry) -> !ingredientEntry.getKey().equals(ingredient))
                .filter((ingredientEntry) -> ingredientEntry.getValue().contains(allergen))
                .peek((ingredientEntry) -> ingredientEntry.getValue().remove(allergen))
                .count() > 0
                | potentialIngredients.entrySet().stream()
                .filter((otherAllergenEntry) -> !otherAllergenEntry.getKey().equals(allergen))
                .filter((otherAllergenEntry) -> otherAllergenEntry.getValue().contains(ingredient))
                .peek((otherAllergenEntry) -> otherAllergenEntry.getValue().remove(ingredient))
                .count() > 0;
          })
          .count();
    } while (reductions > 0);
  }

}
