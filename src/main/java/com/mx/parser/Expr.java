package com.mx.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * 正则表达式解析类
 * 对正则表达式进行预处理
 */
public class Expr {
    /**
     * 运算符优先级
     */
    private static final Map<Character, Integer> operatorPrecedence = new HashMap<>(3);
    static {
        operatorPrecedence.put('|', 0);
        operatorPrecedence.put('.', 1);
        operatorPrecedence.put('*', 2);
    }

    /**
     * 为正则表达式插入连接符(.)：(a∣b)*c → (a∣b)*.c
     * @param regex 正则表达式
     * @return 插入连接符的正则表达式
     */
    public static String addConcatOperator(String regex) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < regex.length(); i++) {
            char token = regex.charAt(i);
            output.append(token);
            if (token == '(' || token == '|') {
                continue;
            }
            if (i < regex.length() - 1) {
                char lookahead = regex.charAt(i + 1);
                if (lookahead == '*' || lookahead == '|' || lookahead == ')') {
                    continue;
                }
                output.append('.');
            }
        }
        return output.toString();
    }

    /**
     * 中缀转换为后缀：(a∣b)*.c → ab∣*c.
     * @param regex 中缀正则表达式
     * @return 后缀正则表达式
     */
    public static String toPostfix(String regex) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char token : regex.toCharArray()) {
            if (token == '.' || token == '|' || token == '*') {
                // 1) 若为 '.|*' => 弹出所有优先级比它高的操作符加入后缀表达式，且压入自己
                while (stack.size() > 0 && !stack.peek().equals('(')
                        && operatorPrecedence.get(stack.peek()) >= operatorPrecedence.get(token)) {
                    postfix.append(stack.pop());
                }
                stack.push(token);
            } else if (token == '(' || token == ')') {
                if (token == '(') {
                    // 2) 若为 '(' => 入栈 '('
                    stack.push(token);
                } else {
                    // 3) 若为 ')' => 弹出所有操作符加入后缀表达式，且弹出 '('
                    while(!stack.peek().equals('(')) {
                        postfix.append(stack.pop());
                    }
                    stack.pop();
                }
            } else {
                // 4) 若为非操作符 => 加入后缀表达式
                postfix.append(token);
            }

        }
        // 5) 清空栈中的操作符，加入表达式
        while (stack.size() > 0) {
            postfix.append(stack.pop());
        }

        return postfix.toString();
    }
}
