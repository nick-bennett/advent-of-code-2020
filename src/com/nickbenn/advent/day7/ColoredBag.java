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
package com.nickbenn.advent.day7;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ColoredBag {

  private static final Pattern WHITESPACE_SPLITTER = Pattern.compile("\\s+");
  private static final String SPINAL_CASE_DELIMITER = "-";
  private static final Map<String, ColoredBag> bags = new HashMap<>();

  private final String name;
  private final int hash;
  private final Map<ColoredBag, Integer> components;
  private final Set<ColoredBag> containers;

  private ColoredBag(String name) {
    this.name = name;
    hash = name.hashCode();
    components = new HashMap<>();
    containers = new HashSet<>();
  }

  public static ColoredBag getInstance(String name) {
    name = WHITESPACE_SPLITTER.splitAsStream(name)
        .map(String::toLowerCase)
        .collect(Collectors.joining(SPINAL_CASE_DELIMITER));
    return bags.computeIfAbsent(name, ColoredBag::new);
  }

  public String getName() {
    return name;
  }

  public static Collection<ColoredBag> getBags() {
    return Collections.unmodifiableCollection(bags.values());
  }

  public void addComponent(ColoredBag component, int quantity) {
    components.put(component, quantity);
    component.addContainer(this);
  }

  public Map<ColoredBag, Integer> getComponents() {
    return Collections.unmodifiableMap(components);
  }

  public Set<ColoredBag> getContainers() {
    return Collections.unmodifiableSet(containers);
  }

  public Map<ColoredBag, Integer> getAllComponents() {
    Map<ColoredBag, Integer> components = new HashMap<>();
    findAllComponents(components, 1);
    return components;
  }

  public Set<ColoredBag> getAllContainers() {
    Set<ColoredBag> containers = new HashSet<>();
    findAllContainers(containers);
    return containers;
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    return obj == this
        || (obj instanceof ColoredBag
        && ((ColoredBag) obj).hash == hash
        && ((ColoredBag) obj).name.equals(name));
  }

  @Override
  public String toString() {
    return name;
  }

  private void addContainer(ColoredBag container) {
    containers.add(container);
  }

  private void findAllContainers(Set<ColoredBag> containers) {
    for (ColoredBag container : this.containers) {
      if (!containers.contains(container)) {
        containers.add(container);
        container.findAllContainers(containers);
      }
    }
  }

  private void findAllComponents(Map<ColoredBag, Integer> components, int multiplier) {
    this.components.forEach((bag, quantity) -> {
      components.put(bag, quantity * multiplier + components.getOrDefault(bag, 0));
      bag.findAllComponents(components, quantity * multiplier);
    });
  }

}
