package se.su.dsv.RegisterSystem;

public class Customer {
    private final String name;
    private final String address;
    private final String socialSecurityNumber;
    private final String phoneNumber;
    private final String mail;

    public Customer(String name, String address, String socialSecurityNumber, String phoneNumber, String mail) {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("name is null");
        }
        if (address == null || address.equals("")) {
            throw new IllegalArgumentException("address is null");
        }
        //TODO: Add date check so that a customer that is bord ahead of calendar Date will be rejected
        if (!socialSecurityNumber.matches("\\d{8}-\\d{4}")) {
            throw new IllegalArgumentException("social security number is in the wrong format");
        }
        if (!phoneNumber.matches("\\d{10}")) {
            throw new IllegalArgumentException("phone number is in the wrong format");
        }
        if (!mail.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            throw new IllegalArgumentException("mail is in the wrong format");
        }
        this.name = name;
        this.address = address;
        this.socialSecurityNumber = socialSecurityNumber;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
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
        sb.append("Social Security Number: " + socialSecurityNumber + "\n");
        sb.append("Phone number: " + phoneNumber + "\n");
        sb.append("E-mail: " + mail + "\n");
        return sb.toString();
    }
}
