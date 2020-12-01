package com.nickbenn.advent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.LongStream;

public class Day1 {

  private static final long SUM = 2020;
  private static final String filename = "day-1.txt";

  public static void main(String[] args) throws URISyntaxException, IOException {
    Path path = Path.of(Day1.class.getResource(filename).toURI());
    System.out.println(part1(path));
    System.out.println(part2(path));
  }

  private static long part1(Path path) throws IOException {
    Set<Long> values = new HashSet<>();
    long found = parse(path)
        .filter((value) -> {
          if (values.contains(SUM - value)) {
            return true;
          } else {
            values.add(value);
            return false;
          }
        })
        .findFirst()
        .orElseThrow(NoSuchElementException::new);
    return found * (SUM - found);
  }

  private static long part2(Path path) throws IOException {
    Set<Long> singles = new HashSet<>();
    Map<Long, Long> doubles = new HashMap<>();
    Set<Long> doubleSums = doubles.keySet();
    long product = 0;
    long[] values = parse(path)
        .peek(singles::add)
        .toArray();
    outer:
    for (int i = 0; i < values.length; i++) {
      long outerValue = values[i];
      long outerComplement = SUM - outerValue;
      if (doubleSums.contains(outerComplement)) {
        product = outerValue * doubles.get(outerComplement);
        break;
      }
      for (int j = i + 1; j < values.length; j++) {
        long innerValue = values[j];
        long innerComplement = outerComplement - innerValue;
        if (singles.contains(innerComplement)) {
          product = outerValue * innerValue * innerComplement;
          break outer;
        } else {
          doubles.put(innerValue + outerValue, innerValue * outerValue);
        }
      }
    }
    return product;
  }

  private static LongStream parse(Path path) throws IOException {
    return Files.lines(path)
        .map(String::trim)
        .filter(Predicate.not(String::isEmpty))
        .mapToLong(Long::parseLong);
  }

}
