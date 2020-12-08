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
package com.nickbenn.advent.interpreter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class InterpreterState {

  public static final String TO_STRING_FORMAT = "{ip=%1$d; registers=%2$s}";

  private final Map<String, Long> registers;
  private int instructionPointer;

  public InterpreterState() {
    instructionPointer = 0;
    registers = new HashMap<>();
  }

  public int getInstructionPointer() {
    return instructionPointer;
  }

  public int moveInstructionPointer(int offset) {
    return instructionPointer += offset;
  }

  public Map<String, Long> getRegisters() {
    return registers;
  }

  public Long get(String name) {
    return registers.get(name);
  }

  public Long getOrDefault(String name, Long defaultValue) {
    return registers.getOrDefault(name, defaultValue);
  }

  public Long put(String name, Long value) {
    return registers.put(name, value);
  }

  public Long putIfAbsent(String name, Long value) {
    return registers.putIfAbsent(name, value);
  }

  public Long update(String name, UnaryOperator<Long> updater) {
    return registers.put(name, updater.apply(registers.getOrDefault(name, 0L)));
  }

  public Long update(String name, UnaryOperator<Long> updater, Long defaultValue) {
    return registers.put(name, updater.apply(registers.getOrDefault(name, defaultValue)));
  }

  @Override
  public String toString() {
    return String.format(TO_STRING_FORMAT, instructionPointer, registers);
  }

}
