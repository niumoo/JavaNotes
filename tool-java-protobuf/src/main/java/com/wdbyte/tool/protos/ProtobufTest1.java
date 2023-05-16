package com.wdbyte.tool.protos;

import com.google.protobuf.InvalidProtocolBufferException;
import com.wdbyte.tool.protos.Person.PhoneNumber;
import com.wdbyte.tool.protos.Person.PhoneType;


/**
 * @author https://www.wdbyte.com
 * @date 2023/05/07
 */
public class ProtobufTest1 {

    public static void main(String[] args) {
        // 直接构建
        PhoneNumber phoneNumber1 = PhoneNumber.newBuilder().setNumber("18388888888").setType(PhoneType.HOME).build();
        Person person1 = Person.newBuilder().setId(1).setName("www.wdbyte.com").setEmail("xxx@wdbyte.com").addPhones(phoneNumber1).build();
        AddressBook addressBook1 = AddressBook.newBuilder().addPeople(person1).build();
        System.out.println(addressBook1);
        System.out.println("------------------");

        //  链式构建
        AddressBook addressBook2 = AddressBook
            .newBuilder()
            .addPeople(Person.newBuilder()
                             .setId(2)
                             .setName("www.wdbyte.com")
                             .setEmail("yyy@126.com")
                            .addPhones(PhoneNumber.newBuilder()
                                                  .setNumber("18388888888")
                                                  .setType(PhoneType.HOME)
                            )
            )
            .build();
        System.out.println(addressBook2);
    }
}
