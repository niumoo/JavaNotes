package com.wdbyte.tool.protos;

import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSON;

import com.wdbyte.tool.protos.AddressBook.Builder;
import com.wdbyte.tool.protos.AddressBookJava.PersonJava;
import com.wdbyte.tool.protos.AddressBookJava.PhoneNumberJava;
import com.wdbyte.tool.protos.AddressBookJava.PhoneTypeJava;
import com.wdbyte.tool.protos.Person.PhoneNumber;
import com.wdbyte.tool.protos.Person.PhoneType;

/**
 * @author https://www.wdbyte.com
 */
public class ProtobufTest3 {

    public static void main(String[] args) throws IOException {
        AddressBookJava addressBookJava = createAddressBookJava(1000);
        String jsonString = JSON.toJSONString(addressBookJava);
        System.out.println("json string size:" + jsonString.length());

        AddressBook addressBook = createAddressBook(1000);
        byte[] addressBookByteArray = addressBook.toByteArray();
        System.out.println("protobuf byte array size:" + addressBookByteArray.length);
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

