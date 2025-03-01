package com.tasks.hotelapp.model;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Contacts {
    private String phone;

    private String email;

}
