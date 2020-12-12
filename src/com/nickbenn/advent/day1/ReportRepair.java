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
package com.nickbenn.advent.day1;

import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.BitSet;
import java.util.NoSuchElementException;

public class ReportRepair {

  private static final int SUM = 2020;

  private final BitSet data;

  public ReportRepair(String filename) throws IOException, URISyntaxException {
    data = new Parser.Builder(getClass().getResource(filename).toURI())
        .build()
        .bitSet();
  }

  public static void main(String[] args) throws URISyntaxException, IOException {
    ReportRepair reportRepair = new ReportRepair(Defaults.FILENAME);
    System.out.println(reportRepair.dualSumProduct());
    System.out.println(reportRepair.tripleSumProduct());
  }

  public int dualSumProduct() {
    for (int value = data.nextSetBit(0); value >= 0; value = data.nextSetBit(value + 1)) {
      int complement = SUM - value;
      if (complement >= 0 && data.get(complement)) {
        return value * complement;
      }
    }
    throw new NoSuchElementException();
  }

  public int tripleSumProduct() throws IOException {
    for (int outer = data.nextSetBit(0); outer >= 0; outer = data.nextSetBit(outer + 1)) {
      for (int inner = data.nextSetBit(outer + 1); inner >= 0; inner = data.nextSetBit(inner + 1)) {
        int doubleSum = inner + outer;
        int complement = SUM - doubleSum;
        if (complement >= 0 && data.get(complement)) {
          return outer * inner * complement;
        }
      }
    }
    throw new NoSuchElementException();
  }

}
