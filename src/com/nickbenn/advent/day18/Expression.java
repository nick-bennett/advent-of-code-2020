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
    this.input = input;
    this.priorities = priorities;
    position = 0;
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

  private long nextOperand() {
    long value;
    skipWhitespace();
    int sign;
    if (chars[position] == '-') {
      sign = -1;
      skipWhitespace();
    } else if (chars[position] == '+') {
      sign = 1;
      skipWhitespace();
    } else {
      sign = 1;
    }
    if (chars[position] == '(') {
      position++;
      int end = findClosingParenthesis(position);
      Expression nested = new Expression(input.substring(position, end).trim(), priorities);
      value = nested.getValue();
      position = end + 1;
    } else {
      int endPosition = position;
      while (endPosition < chars.length && Character.isDigit(chars[endPosition])) {
        endPosition++;
      }
      value = Long.parseLong(input, position, endPosition, 10);
      position = endPosition;
    }
    return sign * value;
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

  private int findClosingParenthesis(int start) {
    int level = 1;
    for (int i = start; i < chars.length; i++) {
      if (chars[i] == '(') {
        level++;
      } else if (chars[i] == ')' && --level == 0) {
        return i;
      }
    }
    throw new IllegalArgumentException();
  }

}
