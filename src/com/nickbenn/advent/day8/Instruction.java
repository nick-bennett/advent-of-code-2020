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
import com.nickbenn.advent.interpreter.Operator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Instruction {

  private static final Pattern INSTRUCTION_PATTERN = Pattern.compile("^(\\S+)\\s+([-+]\\d+)$");
  private static final String INSTRUCTION_FORMAT = "%s %+d";

  private Opcode opcode;
  private int operand;

  public Instruction(String input) {
    Matcher matcher = INSTRUCTION_PATTERN.matcher(input);
    if (matcher.matches()) {
      opcode = Opcode.valueOf(matcher.group(1).toUpperCase());
      operand = Integer.parseInt(matcher.group(2));
    } else {
      throw new IllegalArgumentException();
    }
  }

  public Instruction(Opcode opcode, int operand) {
    this.opcode = opcode;
    this.operand = operand;
  }

  public void execute(InterpreterState state) {
    opcode.execute(state, operand);
  }

  public Opcode getOpcode() {
    return opcode;
  }

  public void setOpcode(Opcode opcode) {
    this.opcode = opcode;
  }

  public int getOperand() {
    return operand;
  }

  public void setOperand(int operand) {
    this.operand = operand;
  }

  @Override
  public String toString() {
    return String.format(INSTRUCTION_FORMAT, opcode, operand);
  }

  public enum Opcode implements Operator {

    ACC {
      @Override
      public void execute(InterpreterState state, Object... operands) {
        state.moveInstructionPointer(1);
        state.update(ACCUMULATOR_REGISTER, (v) -> v + (Integer) operands[0]);
      }
    },
    JMP {
      @Override
      public void execute(InterpreterState state, Object... operands) {
        state.moveInstructionPointer((Integer) operands[0]);
      }
    },
    NOP {
      @Override
      public void execute(InterpreterState state, Object... operands) {
        state.moveInstructionPointer(1);
      }
    };

    public static final String ACCUMULATOR_REGISTER = "accumulator";

    @Override
    public String toString() {
      return name().toLowerCase();
    }

  }

}
