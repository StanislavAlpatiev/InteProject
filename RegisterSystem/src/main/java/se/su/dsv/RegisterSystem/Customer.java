package se.su.dsv.RegisterSystem;

import java.time.LocalDate;

public class Customer {
    private final String name;
    private final String address;
    private final LocalDate birthday;
    private final String phoneNumber;
    private final String mail;

    public Customer(String name, String address, LocalDate birthday, String phoneNumber, String mail) {
        // Kollar så namnet inte är null eller tomt
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("name is null");
        }
        // Kollar så adressen inte är null eller tom
        if (address == null || address.equals("")) {
            throw new IllegalArgumentException("address is null");
        }
        // Kollar så att en kund inte kan vara förr före kalenderns datum
        if (!birthday.isBefore(LocalDate.now().plusDays(1))) {
            throw new IllegalArgumentException("Birthday is ahead of calender");
        }
        // Kollar så att telefon nummret har rätt längd (Svenska telefon nummer)
        if (!phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("phone number is in the wrong format");
        }
        // Simplifierad regex för att kolla mail formatet
        if (!mail.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("mail is in the wrong format");
        }
        this.name = name;
        this.address = address;
        this.birthday = birthday;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
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
