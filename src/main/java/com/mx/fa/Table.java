package com.mx.fa;

import lombok.Value;

/**
 * 有限自动机用状态表来表示
 */
@Value(staticConstructor = "of")
public class Table {
    State start;
    State end;
}
