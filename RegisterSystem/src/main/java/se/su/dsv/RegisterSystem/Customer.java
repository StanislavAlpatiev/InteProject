package se.su.dsv.RegisterSystem;

import java.time.LocalDate;

public class Customer {
    private final String name;
    private final String address;
    private final LocalDate birthday;
    private final String phoneNumber;
    private final String mail;

    public Customer(String name, String address, LocalDate birthday, String phoneNumber, String mail) {
        //Checks whether the birthday is ahead of calender
        if (!birthday.isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Birthday is ahead of calender");
        }

        argumentRegexFilter(name, "^[\\p{L} .'-]+$"); //Checks whether the name is Null or empty
        argumentRegexFilter(address, "[\\p{L} 0-9.'-]+$"); //Checks whether the address is Null or empty
        argumentRegexFilter(phoneNumber, "\\d{10}"); //Checks that the string is of 10 digits
        //Filters mail through regex so that it complies with permitted email standards from RFC 5322
        argumentRegexFilter(mail,
                "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");

        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
    }

    private void argumentRegexFilter(String argument, String regex) throws IllegalArgumentException {
        if (argument == null || argument.isEmpty() || !argument.matches(regex)) {
            throw new IllegalArgumentException(argument + " does not match regex!");
        }
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMail() {
        return mail;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name + "\n");
        sb.append("Address: " + address + "\n");
        sb.append("Birthday: " + birthday + "\n");
        sb.append("Phone number: " + phoneNumber + "\n");
        sb.append("E-mail: " + mail + "\n");
        return sb.toString();
    }
}
