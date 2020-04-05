package com.mx.parser;

import com.mx.fa.NFAOp;
import com.mx.fa.Table;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NFAOpTest {
    @Test
    public void testRecognizeByDFS() {
        Table nfa = NFAOp.toNFA(Expr.toPostfix(Expr.addConcatOperator("a*b")));
        Assertions.assertTrue(NFAOp.recognizeByDFS(nfa, "aab"));
        Assertions.assertFalse(NFAOp.recognizeByDFS(nfa, "abb"));
    }
}
