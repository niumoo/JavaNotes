package com.wdbyte.tool.protos;

import java.util.List;

public class AddressBookJava {
    List<PersonJava> personJavaList;

    public static class PersonJava {
        private int id;
        private String name;
        private String email;
        private PhoneNumberJava phones;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public PhoneNumberJava getPhones() {
            return phones;
        }

        public void setPhones(PhoneNumberJava phones) {
            this.phones = phones;
        }
    }

    public static class PhoneNumberJava {
        private String number;
        private PhoneTypeJava phoneTypeJava;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public PhoneTypeJava getPhoneTypeJava() {
            return phoneTypeJava;
        }

        public void setPhoneTypeJava(PhoneTypeJava phoneTypeJava) {
            this.phoneTypeJava = phoneTypeJava;
        }
    }

    public enum PhoneTypeJava {
        MOBILE,
        HOME,
        WORK;
    }

    public List<PersonJava> getPersonJavaList() {
        return personJavaList;
    }

    public void setPersonJavaList(List<PersonJava> personJavaList) {
        this.personJavaList = personJavaList;
    }
}