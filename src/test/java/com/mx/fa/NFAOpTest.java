package com.mx.fa;

import com.mx.parser.Expr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NFAOpTest {
    @Test
    public void testRecognizeByDFS() {
        Table nfa = NFAOp.toNFA(Expr.toPostfix(Expr.addConcatOperator("a*b")));
        Assertions.assertTrue(NFAOp.recognizeByDFS(nfa, "aab"));
        Assertions.assertFalse(NFAOp.recognizeByDFS(nfa, "abb"));
    }

    @Test
    public void testRecognizeByMState() {
        Table nfa = NFAOp.toNFA(Expr.toPostfix(Expr.addConcatOperator("a*b")));
        Assertions.assertTrue(NFAOp.recognizeByMState(nfa, "aab"));
        Assertions.assertFalse(NFAOp.recognizeByMState(nfa, "abb"));
    }
}
