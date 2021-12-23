package com.wdbyte;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.SplittableRandom;
import java.util.UUID;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
public class EnumMapBenchmark {

    enum AnEnum {
        a, b, c, d, e, f, g,
        h, i, j, k, l, m, n,
        o, p, q,    r, s, t,
        u, v, w,    x, y, z;
    }

    /** 要查找的 key 的数量 */
    private static int size = 10000;
    /** 随机数种子 */
    private static int seed = 99;

    @State(Scope.Benchmark)
    public static class EnumMapState {
        private EnumMap<AnEnum, String> map;
        private AnEnum[] values;

        @Setup(Level.Trial)
        public void setup() {
            map = new EnumMap<>(AnEnum.class);
            values = new AnEnum[size];
            AnEnum[] enumValues = AnEnum.values();
            SplittableRandom random = new SplittableRandom(seed);
            for (int i = 0; i < size; i++) {
                int nextInt = random.nextInt(0, Integer.MAX_VALUE);
                values[i] = enumValues[nextInt % enumValues.length];
            }
            for (AnEnum value : enumValues) {
                map.put(value, UUID.randomUUID().toString());
            }
        }
    }

    @State(Scope.Benchmark)
    public static class HashMapState{
        private HashMap<String, String> map;
        private String[] values;

        @Setup(Level.Trial)
        public void setup() {
            map = new HashMap<>();
            values = new String[size];
            AnEnum[] enumValues = AnEnum.values();
            int pos = 0;
            SplittableRandom random = new SplittableRandom(seed);
            for (int i = 0; i < size; i++) {
                int nextInt = random.nextInt(0, Integer.MAX_VALUE);
                values[i] = enumValues[nextInt % enumValues.length].toString();
            }
            for (AnEnum value : enumValues) {
                map.put(value.toString(), UUID.randomUUID().toString());
            }
        }
    }

    @Benchmark
    public void enumMap(EnumMapState state, Blackhole bh) {
        for (AnEnum value : state.values) {
            bh.consume(state.map.get(value));
        }
    }

    @Benchmark
    public void hashMap(HashMapState state, Blackhole bh) {
        for (String value : state.values) {
            bh.consume(state.map.get(value));
        }
    }
}


