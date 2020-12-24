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
package com.nickbenn.advent.day24;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public class HexagonalCell implements Comparable<HexagonalCell> {

  private static final Comparator<HexagonalCell> COMPARATOR =
      Comparator.comparingInt(HexagonalCell::getY).thenComparingInt(HexagonalCell::getX);

  private final int x;
  private final int y;
  private final int hash;

  public HexagonalCell(int x, int y) {
    this.x = x;
    this.y = y;
    hash = 31 * x + y;
  }

  @Override
  public int hashCode() {
    return hash;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  @Override
  public boolean equals(Object obj) {
    boolean comparison;
    if (this == obj) {
      comparison = true;
    } else if (obj instanceof HexagonalCell) {
      HexagonalCell other = (HexagonalCell) obj;
      comparison = hash == other.hash
          && x == other.x
          && y == other.y;
    } else {
      comparison = false;
    }
    return comparison;
  }

  @Override
  public String toString() {
    return Arrays.toString(new int[]{x, y});
  }

  @Override
  public int compareTo(HexagonalCell other) {
    return COMPARATOR.compare(this, other);
  }

  public Set<HexagonalCell> getNeighbors() {
    return Arrays.stream(HexagonalDirection.values())
        .map((direction) -> new HexagonalCell(x + direction.getX(), y + direction.getY()))
        .collect(Collectors.toSet());
  }

}
