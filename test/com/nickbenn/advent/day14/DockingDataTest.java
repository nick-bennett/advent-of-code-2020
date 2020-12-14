package com.nickbenn.advent.day14;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Test;

class DockingDataTest {

  @Test
  void getV1Sum() throws IOException, URISyntaxException {
    DockingData dockingData = new DockingData("test1.txt");
    assertEquals(165L, dockingData.getV1Sum());
  }

  @Test
  void getV2Sum() throws IOException, URISyntaxException {
    DockingData dockingData = new DockingData("test2.txt");
    assertEquals(208L, dockingData.getV2Sum());
  }

}