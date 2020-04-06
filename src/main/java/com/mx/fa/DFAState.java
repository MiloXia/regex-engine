package com.mx.fa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class DFAState {
    /**
     * NFA 的状态子集：S ⊆ Q(nfa) 使得 S = ECOLSE(S)
     */
    private Set<State> nfaStates;
    /**
     * 状态转换函数（接受符号）
     */
    private Map<Character, DFAState> transitions;
    /**
     * 是否是接受状态
     */
    private boolean isEnd;

    public static DFAState create(Set<State> states, Map<Character, DFAState> transitions, boolean isEnd) {
        return new DFAState(states, transitions, isEnd);
    }

    public static DFAState create(Set<State> states, boolean isEnd) {
        return new DFAState(states, new HashMap<>(), isEnd);
    }
}
