package com.xyz.entities;

import java.util.Objects;
import javax.persistence.*;

@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String address1;
    private String address2;
    private String city;
    private String province;    // state is a reserved SQL key word
    private Integer zipCode;
    private String country;
    private String phoneNumber;

    public Contact() {}

    public Contact(String firstName, String lastName, String email, String address1, String address2, String city, String province, Integer zipCode, String country, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.province = province;
        this.zipCode = zipCode;
        this.country = country;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + Objects.hashCode(this.firstName);
        hash = 19 * hash + Objects.hashCode(this.lastName);
        hash = 19 * hash + Objects.hashCode(this.email);
        hash = 19 * hash + Objects.hashCode(this.address1);
        hash = 19 * hash + Objects.hashCode(this.address2);
        hash = 19 * hash + Objects.hashCode(this.city);
        hash = 19 * hash + Objects.hashCode(this.province);
        hash = 19 * hash + Objects.hashCode(this.zipCode);
        hash = 19 * hash + Objects.hashCode(this.country);
        hash = 19 * hash + Objects.hashCode(this.phoneNumber);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Contact other = (Contact) obj;
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        if (!Objects.equals(this.email, other.email)) {
            return false;
        }
        if (!Objects.equals(this.address1, other.address1)) {
            return false;
        }
        if (!Objects.equals(this.address2, other.address2)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        if (!Objects.equals(this.province, other.province)) {
            return false;
        }
        if (!Objects.equals(this.country, other.country)) {
            return false;
        }
        if (!Objects.equals(this.phoneNumber, other.phoneNumber)) {
            return false;
        }
        return Objects.equals(this.zipCode, other.zipCode);
    }

    @Override
    public String toString() {
        return "Contact{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", province=" + province + ", zipCode=" + zipCode + ", country=" + country + ", phoneNumber=" + phoneNumber + '}';
    }
}
