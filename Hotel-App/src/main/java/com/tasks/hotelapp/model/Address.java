package com.tasks.hotelapp.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {
    private int houseNumber;
    private String street;

    private String city;

    private String country;

    private String postCode;
}
