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
package com.nickbenn.advent.day12;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RainRisk {

  private static final Pattern COMMAND_PATTERN = Pattern.compile("^([A-Z])(\\d+)\\s*$");

  private final List<Command> commands;

  public RainRisk(String filename) throws URISyntaxException, IOException {
    try (
        Stream<Command> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
            .map(Command::new)
    ) {
      commands = stream.collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    RainRisk rainRisk = new RainRisk(Defaults.FILENAME);
    System.out.println(rainRisk.basicDistance());
    System.out.println(rainRisk.waypointDistance());
  }

  public int basicDistance() {
    int x = 0;
    int y = 0;
    int heading = 90;
    for (Command command : commands) {
      char direction = command.getDirection();
      int magnitude = command.getMagnitude();
      switch (direction) {
        case 'N':
          y += magnitude;
          break;
        case 'E':
          x += magnitude;
          break;
        case 'W':
          x -= magnitude;
          break;
        case 'S':
          y -= magnitude;
          break;
        case 'L':
          heading = Math.floorMod(heading - magnitude, 360);
          break;
        case 'R':
          heading = Math.floorMod(heading + magnitude, 360);
          break;
        case 'F':
          switch (heading) {
            case 0:
              y += magnitude;
              break;
            case 90:
              x += magnitude;
              break;
            case 180:
              y -= magnitude;
              break;
            case 270:
              x -= magnitude;
              break;
          }
      }
    }
    return Math.abs(x) + Math.abs(y);
  }

  public int waypointDistance() {
    int x = 0;
    int y = 0;
    int offsetX = 10;
    int offsetY = 1;
    for (Command command : commands) {
      char direction = command.getDirection();
      int magnitude = command.getMagnitude();
      switch (direction) {
        case 'N':
          offsetY += magnitude;
          break;
        case 'E':
          offsetX += magnitude;
          break;
        case 'W':
          offsetX -= magnitude;
          break;
        case 'S':
          offsetY -= magnitude;
          break;
        case 'L':
          for (int i = 90; i <= magnitude; i += 90) {
            int temp = offsetX;
            offsetX = -offsetY;
            offsetY = temp;
          }
          break;
        case 'R':
          for (int i = 90; i <= magnitude; i += 90) {
            int temp = offsetX;
            offsetX = offsetY;
            offsetY = -temp;
          }
          break;
        case 'F':
          x += magnitude * offsetX;
          y += magnitude * offsetY;
          break;
      }
    }
    return Math.abs(x) + Math.abs(y);
  }

  private static class Command {

    private final char direction;
    private final int magnitude;

    public Command(String input) {
      direction = input.charAt(0);
      magnitude = Integer.parseInt(input.substring(1));
    }

    public char getDirection() {
      return direction;
    }

    public int getMagnitude() {
      return magnitude;
    }

  }

}
