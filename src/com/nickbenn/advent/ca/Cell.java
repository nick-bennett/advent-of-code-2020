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
package com.nickbenn.advent.ca;

import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class Cell implements Comparable<Cell> {

  private final int[] coordinates;
  private final int hash;

  private Cell(int dimensions, int... coordinates) {
    this.coordinates = Arrays.copyOf(coordinates, dimensions);
    hash = Arrays.hashCode(this.coordinates);
  }

  private Cell(Collection<Integer> coordinates) {
    this(coordinates.stream()
        .mapToInt(Integer::intValue)
        .toArray());
  }

  public Cell(int... coordinates) {
    this(coordinates.length, coordinates);
  }

  public Cell(Cell other) {
    this(other, other.coordinates.length);
  }

  public Cell(Cell other, int dimensions) {
    this(dimensions, other.coordinates);
  }

  public int getDimensions() {
    return coordinates.length;
  }

  public int get(int dimension) {
    return coordinates[dimension];
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    return this == obj
        || (obj instanceof Cell
        && Arrays.equals(coordinates, ((Cell) obj).coordinates));
  }

  @Override
  public String toString() {
    return Arrays.toString(coordinates);
  }

  @Override
  public int compareTo(Cell other) {
    int comparison = 0;
    for (int i = 0; i < Math.max(coordinates.length, other.coordinates.length); i++) {
      if ((comparison = coordinates[i] - other.coordinates[i]) != 0) {
        break;
      }
    }
    return comparison;
  }

  public Set<Cell> getNeighbors() {
    Set<Cell> neighbors = new HashSet<>();
    collectNeighbors(neighbors, new LinkedList<>());
    neighbors.remove(this);
    return neighbors;
  }

  private void collectNeighbors(Set<Cell> neighbors, Deque<Integer> partialCoordinates) {
    if (partialCoordinates.size() == coordinates.length) {
      neighbors.add(new Cell(partialCoordinates));
    } else {
      for (int i = -1; i <= 1; i++) {
        partialCoordinates.addLast(coordinates[partialCoordinates.size()] + i);
        collectNeighbors(neighbors, partialCoordinates);
        partialCoordinates.removeLast();
      }
    }
  }

}
