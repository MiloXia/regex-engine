package com.mx.fa;

import com.mx.utils.MapUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 子集构造法将 ε-NFA 转换为 DFA
 */
public class DFAOp {
    /**
     * 判断 NFA 状态子集合 S 是否为 DFA 的接受状态
     */
    private static boolean isEnd(Set<State> states) {
        return states.stream().anyMatch(State::isEnd);
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

    /**
     * 获取某 NFA 状态集合 S 在某符号上的 下一个状态的 ε-闭包集合
     */
    private static Set<State> getNextState(Set<State> states, Character symbol) {
        Set<State> res = new HashSet<>();
        for (State s : states) {
            State next = s.getTransition().get(symbol);
            if (next != null) {
                res.addAll(eclose(next, new HashSet<>()));
            }
        }
        return res;
    }

    /**
     * 计算 DFA 某状态（对于 NFA 状态集合）的状态转移函数
     */
    private static Map<Character, Set<State>> getTransitions(Set<State> states) {
        // 符号集合
        Set<Character> symbols = states.stream()
                .flatMap(s -> s.getTransition().keySet().stream())
                .collect(Collectors.toSet());
        Map<Character, Set<State>> res = new HashMap<>();
        for (Character symbol : symbols) {
            res.put(symbol, getNextState(states, symbol));
        }
        return res;
    }

    private static DFAState createDFAState(Set<State> states) {
        if (getTransitions(states).isEmpty()) {
            return DFAState.create(states, new HashMap<>(), isEnd(states));
        } else {
            Map<Character, Set<State>> transitions = getTransitions(states);
            DFAState dfaState = DFAState.create(states, isEnd(states));
            dfaState.setTransitions(MapUtils.mapValue(transitions, s -> {
                if (s.containsAll(states) && states.containsAll(s)) {
                    return dfaState;
                }
                return createDFAState(s);
            }));
            return dfaState;
        }
    }

    private static DFAState convertToDFA(Table nfa) {
        DFAState dfaStart = createDFAState(eclose(nfa.getStart(), new HashSet<>()));
        return dfaStart;
    }

    private static boolean searchByDFA(DFAState state, String word, int position) {
        if (position == word.length()) {
            return state.isEnd();
        } else {
            Character symbol = word.charAt(position);
            DFAState next = state.getTransitions().get(symbol);
            if (next != null) {
                return searchByDFA(next, word, position + 1);
            } else {
                return false;
            }
        }
    }

    public static boolean recognizeByDFA(Table nfa, String word) {
        return searchByDFA(convertToDFA(nfa), word, 0);
    }
}
