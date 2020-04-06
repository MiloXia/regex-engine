package com.mx.fa;

import java.util.*;

/**
 * Thompson NFA 构造 & 搜索算法
 * NFA 构造算法：正则表达式进行结构归纳，构造对应的 ε-NFA
 * 搜索算法：同时进行多状态搜索，在搜索的同时，计算其等价的 DFA 的下一个状态，等价于构建 DFA 进行搜索
 */
public class NFAOp {
    /**
     * 归纳基底：仅包含 ε 的 nfa
     */
    private static Table fromEpsilon() {
        State start = State.create(false);
        State end = State.create(true);
        start.addEpsilonTransitions(end);
        return Table.of(start, end);
    }

    /**
     * 归纳基底：仅包含符号的 nfa
     */
    private static Table fromSymbol(Character symbol) {
        State start = State.create(false);
        State end = State.create(true);
        start.addTransition(symbol, end);
        return Table.of(start, end);
    }

    /**
     * 两个 nfa 的连接
     */
    private static Table concat(Table first, Table second) {
        first.getEnd().addEpsilonTransitions(second.getStart());
        first.getEnd().setEnd(false);
        return Table.of(first.getStart(), second.getEnd());
    }

    /**
     * 两个 nfa 的并
     */
    private static Table union(Table first, Table second) {
        State newStart = State.create(false);
        newStart.addEpsilonTransitions(first.getStart());
        newStart.addEpsilonTransitions(second.getStart());

        State newEnd = State.create(true);
        first.getEnd().addEpsilonTransitions(newEnd);
        first.getEnd().setEnd(false);
        second.getEnd().addEpsilonTransitions(newEnd);
        second.getEnd().setEnd(false);
        return Table.of(newStart, newEnd);
    }

    /**
     * nfa 的闭包
     */
    private static Table closure(Table nfa) {
        State newStart = State.create(false);
        State newEnd = State.create(true);

        newStart.addEpsilonTransitions(newEnd);
        newStart.addEpsilonTransitions(nfa.getStart());
        nfa.getEnd().addEpsilonTransitions(newEnd);
        nfa.getEnd().addEpsilonTransitions(nfa.getStart());
        nfa.getEnd().setEnd(false);
        return Table.of(newStart, newEnd);
    }

    /**
     * 后缀正则表达式转为 NFA
     */
    public static Table toNFA(String postifx) {
        if (postifx.isEmpty()) {
            return fromEpsilon();
        }
        Stack<Table> stack = new Stack<>();
        for(char token : postifx.toCharArray()) {
            switch (token) {
                case '*':
                    stack.push(closure(stack.pop()));
                    break;
                case '|': {
                    Table right = stack.pop();
                    Table left = stack.pop();
                    stack.push(union(left, right));
                    break;
                }
                case '.': {
                    Table right = stack.pop();
                    Table left = stack.pop();
                    stack.push(concat(left, right));
                    break;
                }
                default:
                    stack.push(fromSymbol(token));
                    break;
            }
        }
        return stack.pop();
    }

    /**
     * 深度优先搜索
     * 1）对于 Thompson NFA 只有 | 会引入分叉，且是 ε-trainsitions
     * 2）ε-trainsitions 和 symbol-trainsitions 分开遍历
     * @param state
     * @param visited
     * @param input
     * @param position
     * @return
     */
    private static boolean recursiveBacktrackingSearch(State state, Set<State> visited, String input, int position) {
        if (visited.contains(state)) {
            return false;
        }
        visited.add(state);

        if (position == input.length()) {
            // 若到句子尾部，且当前状态是接受状态，则成功
            if (state.isEnd()) {
                return true;
            }
            // 处理 ε-trainsitions
            return state.getEpsilonTransitions().stream()
                    .anyMatch(s -> recursiveBacktrackingSearch(s, visited, input, position));
        } else {
            // 若非句尾，流转到下一个状态，继续搜索
            State nextState = state.getTransition().get(input.charAt(position));
            if (nextState != null) {
                return recursiveBacktrackingSearch(nextState, new HashSet<>(), input, position + 1);
            } else {
                // 若无可转换状态，则取当前状态的 ε 闭包状态集合进行查找
                return state.getEpsilonTransitions().stream()
                        .anyMatch(s -> recursiveBacktrackingSearch(s, visited, input, position));
            }
        }
    }

    public static boolean recognizeByDFS(Table nfa, String word) {
        return recursiveBacktrackingSearch(nfa.getStart(), new HashSet<>(), word, 0);
    }

    /**
     * 计算 NFA 状态 state 的 ε-闭包
     * visited 防止无限递归
     */
    private static Set<State> eclose(State state, Set<State> visited) {
        Set<State> res = new HashSet<>();
        if (!visited.contains(state)) {
            // 1) 自身
            res.add(state);
            visited.add(state);
        }
        // 2) 递归求 eclose(ε-trainsitions)
        res.addAll(state.getEpsilonTransitions().stream()
                .map(s -> eclose(s, visited))
                .reduce((r, s) -> {
                    r.addAll(s);
                    return r;
                }).orElse(new HashSet<>()));
        return res;
    }

    private static boolean multipleStatesSearch(Table nfa, String word) {
        Set<State> currentStates = eclose(nfa.getStart(), new HashSet<>());
        for (char symbol : word.toCharArray()) {
            Set<State> nextStates = new HashSet<>();
            for (State state : currentStates) {
                State next = state.getTransition().get(symbol);
                if (next != null) {
                    nextStates = eclose(next, new HashSet<>());
                }
            }
            currentStates = nextStates;
        }
        return currentStates.stream().anyMatch(State::isEnd);
    }

    public static boolean recognizeByMState(Table nfa, String word) {
        return multipleStatesSearch(nfa, word);
    }

}
