package com.mx.fa;

import com.mx.parser.Expr;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DFAOpTest {
    @Test
    public void testRecognizeByDFA() {
        Table nfa = NFAOp.toNFA(Expr.toPostfix(Expr.addConcatOperator("a*b")));
        Assertions.assertTrue(DFAOp.recognizeByDFA(nfa, "aab"));
        Assertions.assertFalse(DFAOp.recognizeByDFA(nfa, "abb"));
    }
}
