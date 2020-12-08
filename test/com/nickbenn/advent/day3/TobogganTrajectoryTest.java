package com.nickbenn.advent.day3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TobogganTrajectoryTest {

  private static Stream<Arguments> trees() {
    return Stream.of(
        Arguments.of(Defaults.TEST_FILENAME, TobogganTrajectory.slopes1, 7),
        Arguments.of(Defaults.TEST_FILENAME, TobogganTrajectory.slopes2, 336)
    );
  }

  @ParameterizedTest
  @MethodSource
  void trees(String filename, int[][] slopes, long expected)
      throws IOException, URISyntaxException {
    assertEquals(expected, new TobogganTrajectory(filename).trees(slopes));
  }
}