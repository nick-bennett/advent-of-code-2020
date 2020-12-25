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
package com.nickbenn.advent.day23;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CupCircle {

  private static final int PICKUP_COUNT = 3;

  private final Cup minimum;

  private Cup current;

  public CupCircle(String preassigned, int size) {
    Cup[] cups = new Cup[size];
    Map<Integer, Cup> valueMap = new HashMap<>();
    char[] chars = preassigned.toCharArray();
    Cup max = null;
    Cup min = null;
    for (int i = 0; i < size; i++) {
      int value = (i >= chars.length) ? (i + 1) : chars[i] - '0';
      Cup cup = new Cup(value);
      if (min == null || value < min.getValue()) {
        min = cup;
      }
      if (max == null || value > max.getValue()) {
        max = cup;
      }
      cups[i] = cup;
      valueMap.put(value, cup);
    }
    minimum = min;
    current = cups[0];
    for (int i = 0; i < cups.length; i++) {
      cups[i].setNext(cups[(i + 1) % cups.length]);
      cups[i].setLower(valueMap.getOrDefault(cups[i].getValue() - 1, max));
    }
  }

  public void move() {
    Cup pickUpHead = current.getNext();
    Cup pickUpTail = current;
    Set<Cup> pickUp = new HashSet<>();
    for (int i = 0; i < PICKUP_COUNT; i++) {
      pickUpTail = pickUpTail.getNext();
      pickUp.add(pickUpTail);
    }
    Cup insertPoint = current.getLower();
    while (pickUp.contains(insertPoint)) {
      insertPoint = insertPoint.getLower();
    }
    current.setNext(pickUpTail.getNext());
    current = current.getNext();
    pickUpTail.setNext(insertPoint.getNext());
    insertPoint.setNext(pickUpHead);
  }

  public String reportItems() {
    StringBuilder builder = new StringBuilder();
    Cup item = minimum.getNext();
    while (item != minimum) {
      builder.append(item.value);
      item = item.getNext();
    }
    return builder.toString();
  }

  public long reportProduct(int count) {
    long product = 1;
    Cup item = minimum.getNext();
    while (item != minimum && count-- > 0) {
      product *= item.getValue();
      item = item.getNext();
    }
    return product;
  }

  private static class Cup {

    private final int value;

    private Cup lower;
    private Cup next;

    public Cup(int value) {
      this.value = value;
    }

    public int getValue() {
      return value;
    }

    public Cup getLower() {
      return lower;
    }

    public void setLower(Cup lower) {
      this.lower = lower;
    }

    public Cup getNext() {
      return next;
    }

    public void setNext(Cup next) {
      this.next = next;
    }

  }

}
