package com.mx;

import com.mx.fa.DFAOp;
import com.mx.fa.NFAOp;
import com.mx.fa.Table;
import com.mx.parser.Expr;

import java.util.function.Function;

public class Pattern {
    public static Function<String, Boolean> compile(String regx) {
        Table nfa = NFAOp.toNFA(Expr.toPostfix(Expr.addConcatOperator(regx)));
        return word -> NFAOp.recognizeByMState(nfa, word);
    }

    public static void main(String[] args) {
        Function<String, Boolean> match = compile("a*b");
        System.out.println(match.apply(""));
        System.out.println(match.apply("b"));
        System.out.println(match.apply("aab"));
        System.out.println(match.apply("abb"));
    }
}
