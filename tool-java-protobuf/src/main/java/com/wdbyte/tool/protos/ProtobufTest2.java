package com.wdbyte.tool.protos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.wdbyte.tool.protos.Person.PhoneNumber;
import com.wdbyte.tool.protos.Person.PhoneType;

/**
 *
 * @author https://www.wdbyte.com
 */
public class ProtobufTest2 {

    public static void main(String[] args) throws IOException {
        PhoneNumber phoneNumber1 = PhoneNumber.newBuilder().setNumber("18388888888").setType(PhoneType.HOME).build();
        Person person1 = Person.newBuilder().setId(1).setName("www.wdbyte.com").setEmail("xxx@wdbyte.com").addPhones(phoneNumber1).build();
        AddressBook addressBook1 = AddressBook.newBuilder().addPeople(person1).build();

        // 序列化成字节数组
        byte[] byteArray = addressBook1.toByteArray();
        // 反序列化 - 字节数组转对象
        AddressBook addressBook2 = AddressBook.parseFrom(byteArray);
        System.out.println("字节数组反序列化：");
        System.out.println(addressBook2);

        // 序列化到文件
        addressBook1.writeTo(new FileOutputStream("AddressBook1.txt"));
        // 读取文件反序列化
        AddressBook addressBook3 = AddressBook.parseFrom(new FileInputStream("AddressBook1.txt"));
        System.out.println("文件读取反序列化：");
        System.out.println(addressBook3);
    }
}
