package com.nickbenn.advent.day2;

import static org.junit.jupiter.api.Assertions.*;

import com.nickbenn.advent.util.Defaults;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class PasswordPhilosophyTest {

  @Test
  void countValidByCount() throws IOException, URISyntaxException {
    PasswordPhilosophy philosophy = new PasswordPhilosophy(Defaults.TEST_FILENAME);
    assertEquals(2, philosophy.countValidByCount());
  }

  @Test
  void countValidByPosition() throws IOException, URISyntaxException {
    PasswordPhilosophy philosophy = new PasswordPhilosophy(Defaults.TEST_FILENAME);
    assertEquals(1, philosophy.countValidByPosition());
  }

}