package se.su.dsv.RegisterSystem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    private static final String DEFAULT_NAME = "Erik Andersson";
    private static final String DEFAULT_ADDRESS = "Lingonvägen 17";
    private static final String DEFAULT_SOCIAL_SECURITY_NUMBER = "19990325-5634";
    private static final String DEFAULT_PHONE_NUMBER = "0722371555";
    private static final String DEFAULT_MAIL = "erik.andresson@gmail.com";

    //Test constructor with valid parameters
    @Test
    void constructorValidParameterTest() {
        Customer customer = new Customer(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_SOCIAL_SECURITY_NUMBER, DEFAULT_PHONE_NUMBER, DEFAULT_MAIL);
        System.out.println(new Customer(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_SOCIAL_SECURITY_NUMBER, DEFAULT_PHONE_NUMBER, DEFAULT_MAIL).toString());
        assertEquals(DEFAULT_NAME, customer.getName());
        assertEquals(DEFAULT_ADDRESS, customer.getAddress());
        assertEquals(DEFAULT_SOCIAL_SECURITY_NUMBER, customer.getSocialSecurityNumber());
        assertEquals(DEFAULT_PHONE_NUMBER, customer.getPhoneNumber());
        assertEquals(DEFAULT_MAIL, customer.getMail());
    }

    //Test whether null name in constructor throws
    @Test
    void constructorThrowsIAEWhenNameIsNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(null, DEFAULT_ADDRESS, DEFAULT_SOCIAL_SECURITY_NUMBER, DEFAULT_PHONE_NUMBER, DEFAULT_MAIL);
        });
    }

    //Test whether null address in constructor throws
    @Test
    void constructorThrowsIAEWhenAddressIsNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(DEFAULT_NAME, null, DEFAULT_SOCIAL_SECURITY_NUMBER, DEFAULT_PHONE_NUMBER, DEFAULT_MAIL);
        });
    }

    //Test whether wrong social security number in constructor throws
    //SSN = Social security number
    @Test
    void constructorThrowsIAEWhenSSNIsWrongFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(DEFAULT_NAME, DEFAULT_ADDRESS, "0asd0asdasd", DEFAULT_PHONE_NUMBER, DEFAULT_MAIL);
        });
    }

    //Test whether wrong phone number in constructor throws
    @Test
    void constructorThrowsIAEWhenPhoneNumberIsWrongFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_SOCIAL_SECURITY_NUMBER, "072345667789",
                    DEFAULT_MAIL);
        });
    }

    //Test whether wrong mail in constructor throws
    @Test
    void constructorThrowsIAEWhenMailIsWrongFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Customer(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_SOCIAL_SECURITY_NUMBER, DEFAULT_PHONE_NUMBER,
                    "erik..andresson@gmail.com");
        });
    }

    //Test toString method of customer Test
    @Test
    void toStringTest() {
        assertEquals("Name: Erik Andersson\n" +
                "Address: Lingonvägen 17\n" +
                "Social Security Number: 19990325-5634\n" +
                "Phone number: 0722371555\n" +
                "E-mail: erik.andresson@gmail.com\n", new Customer(DEFAULT_NAME, DEFAULT_ADDRESS, DEFAULT_SOCIAL_SECURITY_NUMBER,
                DEFAULT_PHONE_NUMBER, DEFAULT_MAIL).toString());
    }

}
