package com.mx.parser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ExprTest {
    @Test
    public void testAddConcatOperator() {
        Assertions.assertEquals("(a|b)*.c", Expr.addConcatOperator("(a|b)*c"));
    }

    @Test
    public void testToPostfix() {
        Assertions.assertEquals("ab|*c.", Expr.toPostfix("(a|b)*.c"));
    }
}
