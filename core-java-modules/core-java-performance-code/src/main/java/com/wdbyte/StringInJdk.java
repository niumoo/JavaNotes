package com.wdbyte;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * @author niulang
 * @date 2021/12/23
 */
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
public class StringInJdk {

    @Param({"10000"})
    private int size;
    private String[] stringArray;
    private List<byte[]> byteList;

    @Setup(Level.Trial)
    public void setup() {
        byteList = new ArrayList<>(size);
        stringArray = new String[size];
        for (int i = 0; i < size; i++) {
            String uuid = UUID.randomUUID().toString();
            stringArray[i] = uuid;
            byteList.add(uuid.getBytes(StandardCharsets.UTF_8));
        }
    }

    @Benchmark
    public void byteToString(Blackhole bh) {
        for (byte[] bytes : byteList) {
            bh.consume(new String(bytes, StandardCharsets.UTF_8));
        }
    }

    @Benchmark
    public void stringToByte(Blackhole bh) {
        for (String s : stringArray) {
            bh.consume(s.getBytes(StandardCharsets.UTF_8));
        }
    }
}
