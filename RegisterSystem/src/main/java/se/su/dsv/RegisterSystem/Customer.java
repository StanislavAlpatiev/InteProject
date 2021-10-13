package se.su.dsv.RegisterSystem;

public class Customer {
    String name;
    String address;
    String socialSecurityNumber;
    String phoneNumber;
    String mail;

    public Customer(String name, String address, String socialSecurityNumber, String phoneNumber, String mail){
        if(name == null){
            throw new IllegalArgumentException("name is null");
        }
        if(address == null){
            throw new IllegalArgumentException("address is null");
        }
        if(!socialSecurityNumber.matches("\\d{8}-\\d{4}")){
            throw new IllegalArgumentException("social security number is in the wrong format");
        }
        if(!phoneNumber.matches("\\d{10}")){
            throw new IllegalArgumentException("phone number is in the wrong format");
        }
        if(!mail.matches("^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")){
            throw new IllegalArgumentException("mail is in the wrong format");
        }
        this.name = name;
        this.address = address;
        this.socialSecurityNumber = socialSecurityNumber;
        this.phoneNumber = phoneNumber;
        this.mail = mail;
    }

    public String getName(){
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getSocialSecurityNumber(){
        return socialSecurityNumber;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public String getMail(){
        return mail;
    }

}
