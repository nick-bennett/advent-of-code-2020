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
  private static final int CUPS_TO_MOVE = 3;
  private static final int MAX_TASK_RANGE = 100_000;
  private static final long PROGRESS_REPORT_INTERVAL = 60_000;
  private static final String PROGRESS_REPORT_FORMAT = "Completed %d moves in %02d:%02d:%02d.%n";

  private final List<Integer> circle;
  private final int circleSize;

  private ExecutorService pool;
  private List<SearchTask> tasks;

  public CrabCups(String input) {
    this(input, input.length());
  }

  public CrabCups(String input, int circleSize) {
    this.circleSize = circleSize;
    circle = IntStream.concat(
        input.chars()
            .map((value) -> value - '0'),
        IntStream.rangeClosed(input.length() + 1, circleSize)
    )
        .boxed()
        .collect(Collectors.toCollection(ArrayList::new));
    pool = new ForkJoinPool();
  }

  public static void main(String[] args)
      throws IOException, URISyntaxException, ExecutionException, InterruptedException {
    CrabCups crabCups = new CrabCups(INITIAL_ARRANGEMENT);
    crabCups.play(MOVES_IN_GAME_1);
    System.out.println(crabCups.reportCircle());
    crabCups = new CrabCups(INITIAL_ARRANGEMENT, CUPS_IN_GAME_2);
    crabCups.play(MOVES_IN_GAME_2);
    System.out.println(crabCups.reportNextProduct());
  }

  public void play(int moves) throws ExecutionException, InterruptedException {
    setupSearchTasks();
    int[] counter = {0};
    long start = System.currentTimeMillis();
    TimerTask progressTask = new TimerTask() {
      @Override
      public void run() {
        reportProgress(counter[0], System.currentTimeMillis() - start);
      }
    };
    Timer timer = new Timer();
    timer.scheduleAtFixedRate(progressTask, PROGRESS_REPORT_INTERVAL, PROGRESS_REPORT_INTERVAL);
    while (counter[0] < moves) {
      move();
      counter[0]++;
    }
    timer.cancel();
    reportProgress(counter[0], System.currentTimeMillis() - start);
  }

  public String reportCircle() {
    int position = circle.indexOf(1);
    List<Integer> extracted = new ArrayList<>(circle.subList(0, position));
    circle.subList(0, position + 1).clear();
    circle.addAll(extracted);
    return circle.stream()
        .map(String::valueOf)
        .collect(Collectors.joining());
  }

  public long reportNextProduct() {
    int position = circle.indexOf(1);
    List<Integer> extracted = new ArrayList<>(circle.subList(0, position));
    circle.addAll(extracted.subList(0, Math.min(2, extracted.size())));
    return (long) circle.remove(position + 1) * circle.remove(position + 1);
  }

  private void setupSearchTasks() {
    pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    tasks = new ArrayList<>();
    SearchTask.setCircle(circle);
    for (int rangeBegin = 0; rangeBegin < circle.size(); rangeBegin += MAX_TASK_RANGE) {
      tasks.add(new SearchTask(rangeBegin, rangeBegin + MAX_TASK_RANGE));
    }
  }

  private void move() throws ExecutionException, InterruptedException {
    int current = circle.remove(0);
    List<Integer> extracted = new LinkedList<>(circle.subList(0, CUPS_TO_MOVE));
    circle.subList(0, CUPS_TO_MOVE).clear();
    SearchTask.setValue(getDestinationValue(current, extracted));
    int destinationPosition = pool.invokeAny(tasks);
    circle.subList(destinationPosition + 1, destinationPosition + 1).addAll(extracted);
    circle.add(current);
  }

  private int getDestinationValue(int current, List<Integer> extracted) {
    int nextLower = Stream.concat(
        Stream.of(current),
        extracted.stream()
    )
        .filter((value) -> value <= current)
        .sorted(Comparator.reverseOrder())
        .reduce((a, b) -> (b == a - 1) ? b : a)
        .map((value) -> value - 1)
        .orElseThrow();
    if (nextLower == 0) {
      nextLower = Stream.concat(
          Stream.of(circleSize + 1),
          extracted.stream()
      )
          .sorted(Comparator.reverseOrder())
          .reduce((a, b) -> (b == a - 1) ? b : a)
          .map((value) -> value - 1)
          .orElseThrow();
    }
    return nextLower;
  }

  private static void reportProgress(int moves, long ms) {
    long seconds = Math.round(ms / 1000d);
    long minutes = seconds / 60;
    seconds %= 60;
    long hours = minutes / 60;
    minutes %= 60;
    System.out.printf(PROGRESS_REPORT_FORMAT, moves, hours, minutes, seconds);
  }

  private static class SearchTask implements Callable<Integer> {

    private static List<Integer> circle;
    private static int value;

    private final int rangeBegin;
    private final int rangeEnd;

    private SearchTask(int rangeBegin, int rangeEnd) {
      this.rangeBegin = rangeBegin;
      this.rangeEnd = rangeEnd;
    }

    public static void setCircle(List<Integer> circle) {
      SearchTask.circle = circle;
    }

    public static void setValue(int value) {
      SearchTask.value = value;
    }

    @Override
    public Integer call() throws Exception {
      int position = circle.subList(rangeBegin, Math.min(circle.size(), rangeEnd)).indexOf(value);
      if (position < 0) {
        throw new NoSuchElementException();
      }
      return position + rangeBegin;
    }

  }

}
