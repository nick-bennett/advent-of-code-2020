package com.nickbenn.advent.day17;

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class ConwayCubesTest {

  @Test
  void countActive() throws IOException, URISyntaxException {
    ConwayCubes cubes = new ConwayCubes(Defaults.TEST_FILENAME);
    assertEquals(112, cubes.countActive(3, 6));
    assertEquals(848, cubes.countActive(4, 6));
  }

}