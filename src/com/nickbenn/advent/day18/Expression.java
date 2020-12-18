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

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Expression {

  private static final char SUB_EXPRESSION_START = '(';
  private static final char SUB_EXPRESSION_END = ')';

  private final String input;
  private final Map<Character, Operator> operatorMap;
  private int position;
  private final char[] chars;
  private final Deque<Long> operands;
  private final Deque<Operator> operators;

  public Expression(String input, Collection<Operator> operators) {
    this.input = input;
    this.operatorMap = operators.stream()
        .collect(Collectors.toMap(Operator::getToken, (operator) -> operator));
    position = 0;
    chars = input.toCharArray();
    operands = new LinkedList<>();
    this.operators = new LinkedList<>();
  }

  public long getValue() {
    operands.push(nextOperand());
    Operator operator;
    while ((operator = nextOperator()) != null) {
      processStacks(operator.getPriority());
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
    if (chars[position] == SUB_EXPRESSION_START) {
      int end = findSubExpressionEnd(++position);
      Expression nested =
          new Expression(input.substring(position, end).trim(), operatorMap.values());
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

  private Operator nextOperator() {
    Operator next;
    skipWhitespace();
    if (position >= chars.length) {
      next = null;
    } else if (operatorMap.containsKey(chars[position])) {
      next = operatorMap.get(chars[position]);
      position++;
    } else {
      throw new UnexpectedTokenException();
    }
    return next;
  }

  private void processStacks(int priority) {
    while (!operators.isEmpty() && operators.peek().getPriority() >= priority) {
      Operator operator = operators.pop();
      Long operand2 = operands.pop();
      Long operand1 = operands.pop();
      long result = operator.compute(operand1, operand2);
      this.operands.push(result);
    }
  }

  private int findSubExpressionEnd(int start) {
    int level = 1;
    for (int i = start; i < chars.length; i++) {
      if (chars[i] == SUB_EXPRESSION_START) {
        level++;
      } else if (chars[i] == SUB_EXPRESSION_END && --level == 0) {
        return i;
      }
    }
    throw new UnterminatedSubExpressionException();
  }

  private void skipWhitespace() {
    while (position < chars.length && Character.isWhitespace(chars[position])) {
      position++;
    }
  }

  public static class Operator {

    private final char token;
    private final int priority;
    private final BinaryOperator<Long> operation;

    public Operator(char token, int priority, BinaryOperator<Long> operation) {
      this.token = token;
      this.priority = priority;
      this.operation = operation;
    }

    public char getToken() {
      return token;
    }

    public int getPriority() {
      return priority;
    }

    public long compute(Long operand1, Long operand2) {
      return operation.apply(operand1, operand2);
    }

  }

  public class UnexpectedTokenException extends IllegalArgumentException {

    public static final String MESSAGE_FORMAT =
        "Unexpected token '%s' found at position %d in \"%s\".";

    public UnexpectedTokenException() {
      super(String.format(MESSAGE_FORMAT, input.charAt(position), position, input));
    }

  }

  public class UnterminatedSubExpressionException extends IllegalArgumentException {

    public static final String MESSAGE_FORMAT =
        "Sub-expression started at position %d in \"%s\" not terminated.";

    public UnterminatedSubExpressionException() {
      super(String.format(MESSAGE_FORMAT, position, input));
    }

  }

}
