package com.xyz.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/*
    This class is not mandatory. However, some people want snake case in JSON files.
    This is for that purpose. In addition, we cannot put JACKSON annotations in the
    Java Entitiy class.
*/
public class ContactDTO {

    @JsonProperty("id")
    private Long id;

    @NotEmpty
     @JsonProperty("first_name")
    private String firstName;

    @NotEmpty
     @JsonProperty("last_name")
    private String lastName;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String address1;

    private String address2;

    @NotEmpty
    private String city;

    @NotEmpty
     @JsonProperty("state")
    private String province;

    @NotNull
     @JsonProperty("zip_code")
    private Integer zipCode;

    @NotEmpty
    private String country;

    @NotEmpty
     @JsonProperty("phone_number")
    private String phoneNumber; // TODO validate using regular expression

    public ContactDTO() {
    }

    public ContactDTO(String firstName, String lastName, String email, String address1, String address2, String city, String province, Integer zipCode, String country, String phoneNumber) {
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.firstName);
        hash = 29 * hash + Objects.hashCode(this.lastName);
        hash = 29 * hash + Objects.hashCode(this.email);
        hash = 29 * hash + Objects.hashCode(this.address1);
        hash = 29 * hash + Objects.hashCode(this.address2);
        hash = 29 * hash + Objects.hashCode(this.city);
        hash = 29 * hash + Objects.hashCode(this.province);
        hash = 29 * hash + Objects.hashCode(this.zipCode);
        hash = 29 * hash + Objects.hashCode(this.country);
        hash = 29 * hash + Objects.hashCode(this.phoneNumber);
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
        final ContactDTO other = (ContactDTO) obj;
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
        return ReflectionToStringBuilder.toString(this);
    }
}
