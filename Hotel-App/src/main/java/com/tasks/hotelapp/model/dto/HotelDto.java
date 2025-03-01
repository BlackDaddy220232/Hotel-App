package com.tasks.hotelapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Data
public class HotelDto {
    private long id;
    private String name;
    private String description;
    private String address;
    private String phone;
}
