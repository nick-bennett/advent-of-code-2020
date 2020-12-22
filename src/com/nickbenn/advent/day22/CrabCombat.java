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
package com.nickbenn.advent.day22;

import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrabCombat {

  private final List<Integer> player1;
  private final List<Integer> player2;

  public CrabCombat(String player1File, String player2File) throws URISyntaxException, IOException {
    try (
        IntStream player1Stream = new Parser.Builder(getClass().getResource(player1File).toURI())
            .build()
            .intStream();
        IntStream player2Stream = new Parser.Builder(getClass().getResource(player2File).toURI())
            .build()
            .intStream()
    ) {
      player1 = player1Stream
          .boxed()
          .collect(Collectors.toCollection(LinkedList::new));
      player2 = player2Stream
          .boxed()
          .collect(Collectors.toCollection(LinkedList::new));
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    CrabCombat combat = new CrabCombat("input-1.txt", "input-2.txt");
    List<Integer> hand1;
    List<Integer> hand2;
    hand1 = new LinkedList<>(combat.getPlayer1());
    hand2 = new LinkedList<>(combat.getPlayer2());
    combat.playNonRecursive(hand1, hand2);
    System.out.printf("Player 1: %d; Player 2: %d.%n",
        combat.checksum(hand1), combat.checksum(hand2));
    hand1 = new LinkedList<>(combat.getPlayer1());
    hand2 = new LinkedList<>(combat.getPlayer2());
    combat.playRecursive(hand1, hand2);
    System.out.printf("Player 1: %d; Player 2: %d.%n",
        combat.checksum(hand1), combat.checksum(hand2));
  }

  public List<Integer> getPlayer1() {
    return player1;
  }

  public List<Integer> getPlayer2() {
    return player2;
  }

  public boolean playNonRecursive(List<Integer> hand1, List<Integer> hand2) {
    while (!hand1.isEmpty() && !hand2.isEmpty()) {
      int card1 = hand1.remove(0);
      int card2 = hand2.remove(0);
      if (card1 > card2) {
        hand1.add(card1);
        hand1.add(card2);
      } else if (card2 > card1) {
        hand2.add(card2);
        hand2.add(card1);
      } else {
        hand1.add(card1);
        hand2.add(card2);
      }
    }
    return !player1.isEmpty();
  }

  public boolean playRecursive(List<Integer> hand1, List<Integer> hand2) {
    Set<Integer> checksums = new HashSet<>();
    while (!hand1.isEmpty() && !hand2.isEmpty()) {
      int checksum = 125000 * hand1.hashCode() + hand2.hashCode();
      if (checksums.add(checksum)) {
        int card1 = hand1.remove(0);
        int card2 = hand2.remove(0);
        if (card1 <= hand1.size() && card2 <= hand2.size()) {
          List<Integer> subhand1 = new LinkedList<>(hand1.subList(0, card1));
          List<Integer> subhand2 = new LinkedList<>(hand2.subList(0, card2));
          if (playRecursive(subhand1, subhand2)) {
            hand1.add(card1);
            hand1.add(card2);
          } else {
            hand2.add(card2);
            hand2.add(card1);
          }
        } else if (card1 > card2) {
          hand1.add(card1);
          hand1.add(card2);
        } else if (card2 > card1) {
          hand2.add(card2);
          hand2.add(card1);
        } else {
          hand1.add(card1);
          hand2.add(card2);
        }
      } else {
        return true;
      }
    }
    return !hand1.isEmpty();
  }

  public int checksum(List<Integer> cards) {
    int sum = 0;
    int multiplier = cards.size();
    for (int card : cards) {
      sum += card * multiplier--;
    }
    return sum;
  }

}
