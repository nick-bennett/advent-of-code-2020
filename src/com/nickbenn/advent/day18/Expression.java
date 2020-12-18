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
package com.nickbenn.advent.day18;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;

public class Expression {

  private final String input;
  private final Map<Character, Integer> priorities;
  private int position;
  private final char[] chars;
  private final Deque<Long> operands;
  private final Deque<Character> operators;

  public Expression(String input, Map<Character, Integer> priorities) {
    this(input, priorities, 0);
  }

  private Expression(String input, Map<Character, Integer> priorities, int position) {
    this.input = input;
    this.priorities = priorities;
    this.position = position;
    chars = input.toCharArray();
    operands = new LinkedList<>();
    operators = new LinkedList<>();
  }

  public long getValue() {
    operands.push(nextOperand());
    Character operator;
    while ((operator = nextOperator()) != null) {
      processStacks(priorities.get(operator));
      operators.push(operator);
      operands.push(nextOperand());
    }
    processStacks(Integer.MIN_VALUE);
    return operands.pop();
  }

  @Override
  public String toString() {
    return input;
  }

  private int getPosition() {
    return position;
  }

  private long nextOperand() {
    long value;
    skipWhitespace();
    if (isExpressionStart()) {
      Expression nested = new Expression(input, priorities, position + 1);
      value = nested.getValue();
      position = nested.getPosition();
    } else if (isExpressionEnd()) {
      throw new IllegalArgumentException();
    } else {
      int sign;
      if (chars[position] == '-') {
        sign = -1;
        skipWhitespace();
      } else if (chars[position] == '+') {
        sign = 1;
        skipWhitespace();
      } else if (!Character.isDigit(chars[position])) {
        throw new IllegalArgumentException();
      } else {
        sign = 1;
      }
      int endPosition = position + 1;
      while (endPosition < chars.length && Character.isDigit(chars[endPosition])) {
        endPosition++;
      }
      value = sign * Long.parseLong(input, position, endPosition, 10);
      position = endPosition;
    }
    return value;
  }

  private Character nextOperator() {
    Character next;
    skipWhitespace();
    if (position >= chars.length) {
      next = null;
    } else if (chars[position] == ')') {
      next = null;
      position++;
    } else if (priorities.containsKey(chars[position])) {
      next = chars[position];
      position++;
    } else {
      throw new IllegalArgumentException();
    }
    return next;
  }

  private void processStacks(int priority) {
    while (!operators.isEmpty() && priorities.get(operators.peek()) >= priority) {
      char operator = operators.pop();
      long operand2 = operands.pop();
      long operand1 = operands.pop();
      switch (operator) {
        case '+':
          operand1 += operand2;
          break;
        case '*':
          operand1 *= operand2;
          break;
        case '-':
          operand1 -= operand2;
          break;
        case '/':
          operand1 /= operand2;
          break;
        default:
          throw new IllegalArgumentException();
      }
      operands.push(operand1);
    }
  }

  private void skipWhitespace() {
    while (position < chars.length && Character.isWhitespace(chars[position])) {
      position++;
    }
  }

  private boolean isExpressionStart() {
    return chars[position] == '(';
  }

  private boolean isExpressionEnd() {
    return position >= chars.length || chars[position] == ')';
  }

}
