package com.wdbyte;

import java.util.HashMap;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author https://www.wdbyte.com
 * @date 2021/12/06
 */
@State(Scope.Benchmark)
@Warmup(iterations = 3,time = 3)
@Measurement(iterations = 5,time = 3)
public class HashMapSize {

    @Param({"14"})
    int keys;

    @Param({"16", "32"})
    int size;

    @Benchmark
    public HashMap<Integer, Integer> getHashMap() {
        HashMap<Integer, Integer> map = new HashMap<>(size);
        for (int i = 0; i < keys; i++) {
            map.put(i, i);
        }
        return map;
    }

}
