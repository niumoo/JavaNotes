package com.wdbyte.tool.protos;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wdbyte.tool.protos.AddressBook.Builder;
import com.wdbyte.tool.protos.AddressBookJava.PersonJava;
import com.wdbyte.tool.protos.AddressBookJava.PhoneNumberJava;
import com.wdbyte.tool.protos.AddressBookJava.PhoneTypeJava;
import com.wdbyte.tool.protos.Person.PhoneNumber;
import com.wdbyte.tool.protos.Person.PhoneType;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * @author https://www.wdbyte.com
 */
@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ProtobufTest4 {

    private AddressBookJava addressBookJava;
    private AddressBook addressBook;

    @Setup
    public void init() {
        addressBookJava = createAddressBookJava(1000);
        addressBook = createAddressBook(1000);
    }

    @Benchmark
    public AddressBookJava testJSON() {
        // 转 JSON
        String jsonString = JSON.toJSONString(addressBookJava);
        // JSON 转对象
        return JSON.parseObject(jsonString, AddressBookJava.class);
    }

    @Benchmark
    public AddressBook testProtobuf() throws InvalidProtocolBufferException {
        // 转 JSON
        byte[] addressBookByteArray = addressBook.toByteArray();
        // JSON 转对象
        return AddressBook.parseFrom(addressBookByteArray);
    }

    public static AddressBook createAddressBook(int personCount) {
        Builder builder = AddressBook.newBuilder();
        for (int i = 0; i < personCount; i++) {
            builder.addPeople(Person.newBuilder()
                .setId(i)
                .setName("www.wdbyte.com")
                .setEmail("xxx@126.com")
                .addPhones(PhoneNumber.newBuilder()
                    .setNumber("18333333333")
                    .setType(PhoneType.HOME)
                )
            );
        }
        return builder.build();
    }

    public static AddressBookJava createAddressBookJava(int personCount) {
        AddressBookJava addressBookJava = new AddressBookJava();
        addressBookJava.setPersonJavaList(new ArrayList<>());
        for (int i = 0; i < personCount; i++) {
            PersonJava personJava = new PersonJava();
            personJava.setId(i);
            personJava.setName("www.wdbyte.com");
            personJava.setEmail("xxx@126.com");

            PhoneNumberJava numberJava = new PhoneNumberJava();
            numberJava.setNumber("18333333333");
            numberJava.setPhoneTypeJava(PhoneTypeJava.HOME);

            personJava.setPhones(numberJava);
            addressBookJava.getPersonJavaList().add(personJava);
        }
        return addressBookJava;
    }
}

