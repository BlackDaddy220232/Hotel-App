package com.tasks.hotelapp.model.dto;

import com.tasks.hotelapp.model.Address;
import com.tasks.hotelapp.model.ArrivalTime;
import com.tasks.hotelapp.model.Contacts;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class HotelCreateDto {
    private String name;

    @Column(length = 1000)
    private String description;

    private String brand;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Embedded
    private ArrivalTime arrivalTime;

}
