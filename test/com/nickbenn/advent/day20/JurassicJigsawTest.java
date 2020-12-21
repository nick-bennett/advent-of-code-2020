package com.nickbenn.advent.day20;

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class JurassicJigsawTest {

  @Test
  void solve() throws IOException, URISyntaxException {
    JurassicJigsaw jigsaw = new JurassicJigsaw(Defaults.TEST_FILENAME);
    Tile[][] solution = jigsaw.solve();
    assertEquals(20899048083289L, (long) solution[0][0].getId() * solution[0][solution.length - 1].getId()
        * solution[solution.length - 1][0].getId()
        * solution[solution.length - 1][solution.length - 1].getId());
  }

  @Test
  void getRoughness() throws IOException, URISyntaxException {
    JurassicJigsaw jigsaw = new JurassicJigsaw(Defaults.TEST_FILENAME);
    Tile[][] solution = jigsaw.solve();
    Tile image = jigsaw.merge(solution);
    assertEquals(273, jigsaw.getRoughness(image));
  }

}