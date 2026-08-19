package com.agentforge.tool.builtin;

import com.agentforge.tool.annotation.Tool;
import com.agentforge.tool.annotation.ToolParam;

/**
 * 内置计算器工具
 */
public class CalculatorTool {

    @Tool(name = "calculate", description = "执行数学计算表达式，支持加减乘除、括号等基本运算")
    public String calculate(
            @ToolParam(description = "数学表达式，例如: 2 + 3 * 4, (10 - 5) / 2") String expression) {
        try {
            double result = evaluateExpression(expression.trim());
            if (result == (long) result) {
                return String.valueOf((long) result);
            }
            return String.valueOf(result);
        } catch (Exception e) {
            return "计算错误: " + e.getMessage();
        }
    }

    private double evaluateExpression(String expr) {
        // 简单的表达式求值（支持 +, -, *, /, 括号）
        return new ExpressionParser(expr).parse();
    }

    /**
     * 简单递归下降解析器
     */
    private static class ExpressionParser {
        private final String input;
        private int pos;

        ExpressionParser(String input) {
            this.input = input.replaceAll("\\s+", "");
            this.pos = 0;
        }

        double parse() {
            double result = parseExpression();
            if (pos < input.length()) {
                throw new RuntimeException("Unexpected character: " + input.charAt(pos));
            }
            return result;
        }

        private double parseExpression() {
            double result = parseTerm();
            while (pos < input.length()) {
                char op = input.charAt(pos);
                if (op == '+') {
                    pos++;
                    result += parseTerm();
                } else if (op == '-') {
                    pos++;
                    result -= parseTerm();
                } else {
                    break;
                }
            }
            return result;
        }

        private double parseTerm() {
            double result = parseFactor();
            while (pos < input.length()) {
                char op = input.charAt(pos);
                if (op == '*') {
                    pos++;
                    result *= parseFactor();
                } else if (op == '/') {
                    pos++;
                    double divisor = parseFactor();
                    if (divisor == 0) throw new ArithmeticException("Division by zero");
                    result /= divisor;
                } else {
                    break;
                }
            }
            return result;
        }

        private double parseFactor() {
            if (pos < input.length() && input.charAt(pos) == '(') {
                pos++; // skip '('
                double result = parseExpression();
                if (pos < input.length() && input.charAt(pos) == ')') {
                    pos++; // skip ')'
                }
                return result;
            }

            // 处理负号
            boolean negative = false;
            if (pos < input.length() && input.charAt(pos) == '-') {
                negative = true;
                pos++;
            }

            int start = pos;
            while (pos < input.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) == '.')) {
                pos++;
            }
            if (start == pos) {
                throw new RuntimeException("Expected number at position " + pos);
            }

            double value = Double.parseDouble(input.substring(start, pos));
            return negative ? -value : value;
        }
    }
}
