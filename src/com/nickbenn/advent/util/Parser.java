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
package com.nickbenn.advent.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Parser {

  private static final Pattern BLANK_LINE_SPLITTER = Pattern.compile("\\n\\s*\\n");

  private final Path path;
  private final boolean trimmed;
  private final boolean stripped;

  private Parser(Path path, boolean trimmed, boolean stripped) {
    this.path = path;
    this.trimmed = trimmed;
    this.stripped = stripped;
  }

  public Stream<String> lineStream() throws IOException {
    return rawLines()
        .map((line) -> trimmed ? line.trim() : line)
        .filter((line) -> !(stripped && line.isEmpty()));
  }

  public IntStream intStream() throws IOException {
    return lineStream()
        .mapToInt(Integer::parseInt);
  }

  public LongStream longStream() throws IOException {
    return lineStream()
        .mapToLong(Long::parseLong);
  }

  public DoubleStream doubleStream() throws IOException {
    return lineStream()
        .mapToDouble(Double::parseDouble);
  }

  public BitSet bitSet() throws IOException {
    try (IntStream stream = intStream()) {
      return stream.collect(BitSet::new, BitSet::set, BitSet::or);
    }
  }

  public String rawString() throws IOException {
    return Files.readString(path);
  }

  public String joinedString() throws IOException {
    return joinedString("");
  }

  public String joinedString(CharSequence delimiter) throws IOException {
    try (Stream<String> stream = lineStream()) {
      return stream.collect(Collectors.joining(delimiter));
    }
  }

  public Stream<String> lineGroupStream() throws IOException {
    return BLANK_LINE_SPLITTER.splitAsStream(rawString())
        .map((line) -> trimmed ? line.trim() : line)
        .filter((line) -> !(stripped && line.isEmpty()));
  }

  private Stream<String> rawLines() throws IOException {
    return Files.lines(path);
  }

  public static class Builder {


    private final URI uri;
    private boolean trimmed;
    private boolean stripped;

    public Builder(URI uri) {
      this.uri = uri;
    }

    public Builder setTrimmed(boolean trimmed) {
      this.trimmed = trimmed;
      return this;
    }

    public Builder setStripped(boolean stripped) {
      this.stripped = stripped;
      return this;
    }

    public Parser build() throws URISyntaxException, IOException {
      return new Parser(Path.of(uri), trimmed, stripped);
    }

  }

}
