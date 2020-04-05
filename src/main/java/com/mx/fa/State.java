package com.mx.fa;

import lombok.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 有限自动机状态类
 */
@Getter
@Setter
@AllArgsConstructor
public class State {
    /**
     * 是否是接受状态
     */
    private boolean isEnd;
    /**
     * 状态转换函数（接受符号）
     */
    private Map<Character, State> transition;
    /**
     * 状态转换函数（接受空串：ε）
     */
    private List<State> epsilonTransitions;

    public static State create(boolean isEnd) {
        return new State(isEnd, new HashMap<>(), new LinkedList<>());
    }

    public void addEpsilonTransitions(State to) {
        this.epsilonTransitions.add(to);
    }

    public void addTransition(Character symbol, State to) {
        this.transition.put(symbol, to);
    }
}
