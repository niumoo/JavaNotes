package com.wdbyte;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OperationsPerInvocation;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author https://www.wdbyte.com
 * @date 2021/12/06
 */
@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class HashMapKey {

    private int size = 1024;
    private Map<String, Object> stringMap;
    private Map<Pair, Object> pairMap;
    private String[] prefixes;
    private String[] suffixes;

    @Setup(Level.Trial)
    public void setup() {
        prefixes = new String[size];
        suffixes = new String[size];
        stringMap = new HashMap<>();
        pairMap = new HashMap<>();
        for (int i = 0; i < size; ++i) {
            prefixes[i] = UUID.randomUUID().toString();
            suffixes[i] = UUID.randomUUID().toString();
            stringMap.put(prefixes[i] + ";" + suffixes[i], i);
            // use new String to avoid reference equality speeding up the equals calls
            pairMap.put(new MutablePair(prefixes[i], suffixes[i]), i);
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void stringKey(Blackhole bh) {
        for (int i = 0; i < prefixes.length; i++) {
            bh.consume(stringMap.get(prefixes[i] + ";" + suffixes[i]));
        }
    }

    @Benchmark
    @OperationsPerInvocation(1024)
    public void pairMap(Blackhole bh) {
        for (int i = 0; i < prefixes.length; i++) {
            bh.consume(pairMap.get(new MutablePair(prefixes[i], suffixes[i])));
        }
    }

}
