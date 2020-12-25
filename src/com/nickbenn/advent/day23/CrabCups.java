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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CrabCups {

  public static final int CUPS_IN_GAME_2 = 1_000_000;
  public static final int MOVES_IN_GAME_1 = 100;
  public static final int MOVES_IN_GAME_2 = 10_000_000;

  private static final String INITIAL_ARRANGEMENT = "389547612";
//  private static final String INITIAL_ARRANGEMENT = "389125467";

  public static void main(String[] args) {
    CupCircle circle;
    circle = new CupCircle(INITIAL_ARRANGEMENT, INITIAL_ARRANGEMENT.length());
    for (int i = 0; i < MOVES_IN_GAME_1; i++) {
      circle.move();
    }
    System.out.println(circle.reportItems());
    circle = new CupCircle(INITIAL_ARRANGEMENT, CUPS_IN_GAME_2);
    for (int i = 0; i < MOVES_IN_GAME_2; i++) {
      circle.move();
    }
    System.out.println(circle.reportProduct(2));
  }

}
