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
package com.nickbenn.advent.day8;

import com.nickbenn.advent.interpreter.InterpreterState;
import com.nickbenn.advent.util.Defaults;
import com.nickbenn.advent.util.Parser;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandheldHalting {

  private final List<Instruction> instructions;

  public HandheldHalting(String filename) throws IOException, URISyntaxException {
    try (
        Stream<String> stream = new Parser.Builder(getClass().getResource(filename).toURI())
            .build()
            .lineStream()
    ) {
      instructions = stream
          .map(Instruction::new)
          .collect(Collectors.toList());
    }
  }

  public static void main(String[] args) throws IOException, URISyntaxException {
    HandheldHalting check = new HandheldHalting(Defaults.FILENAME);
    System.out.println(check.executeUntilLoop());
    System.out.println(check.findAndFix());
  }

  public InterpreterState executeUntilLoop() {
    try {
      execute();
      throw new IllegalStateException();
    } catch (LoopException e) {
      return e.getState();
    }
  }

  public InterpreterState findAndFix() {
    for (Instruction instruction : instructions) {
      Instruction.Opcode opcode = instruction.getOpcode();
      try {
        InterpreterState state;
        switch (opcode) {
          case JMP:
            instruction.setOpcode(Instruction.Opcode.NOP);
            state = execute();
            break;
          case NOP:
            instruction.setOpcode(Instruction.Opcode.JMP);
            state = execute();
            break;
          default:
            continue;
        }
        return state;
      } catch (LoopException e) {
        instruction.setOpcode(opcode);
      }
    }
    throw new IllegalStateException();
  }

  public InterpreterState execute() throws LoopException {
    Set<Integer> executed = new HashSet<>();
    InterpreterState state = new InterpreterState();
    while (state.getInstructionPointer() < instructions.size()) {
      if (!executed.add(state.getInstructionPointer())) {
        throw new LoopException(state);
      }
      step(state);
    }
    return state;
  }

  public void step(InterpreterState state) {
    instructions.get(state.getInstructionPointer()).execute(state);
  }

  public static class LoopException extends Exception {

    private static final String MESSAGE = "Infinite loop detected";

    private final InterpreterState state;

    public LoopException(InterpreterState state) {
      super(MESSAGE);
      this.state = state;
    }

    public LoopException(InterpreterState state, Throwable cause) {
      super(MESSAGE, cause);
      this.state = state;
    }

    public InterpreterState getState() {
      return state;
    }

  }

}
