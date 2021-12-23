package com.wdbyte;

import java.util.EnumSet;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * 枚举类遍历测试
 *
 * @author https://www.wdbyte.com
 * @date 2021/12/07
 */
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
public class EnumIteration {

    enum FourteenEnum {
        a, b, c, d, e, f, g,
        h, i, j, k, l, m, n,
        o, p, q,    r, s, t,
        u, v, w,    x, y, z;

        static final FourteenEnum[] VALUES;
        static {
            VALUES = values();
        }
    }

    @Benchmark
    public void valuesEnum(Blackhole bh) {
        for (FourteenEnum value : FourteenEnum.values()) {
            bh.consume(value.ordinal());
        }
    }

    @Benchmark
    public void enumSetEnum(Blackhole bh) {
        for (FourteenEnum value : EnumSet.allOf(FourteenEnum.class)) {
            bh.consume(value.ordinal());
        }
    }

    @Benchmark
    public void cacheEnums(Blackhole bh) {
        for (FourteenEnum value : FourteenEnum.VALUES) {
            bh.consume(value.ordinal());
        }
    }
}

