package com.tasks.hotelapp.model.entity;

import com.tasks.hotelapp.model.Address;
import com.tasks.hotelapp.model.ArrivalTime;
import com.tasks.hotelapp.model.Contacts;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 1000,nullable = true)
    private String description;

    private String brand;

    @Embedded
    private Address address;

    @Embedded
    private Contacts contacts;

    @Embedded
    private ArrivalTime arrivalTime;


    @ElementCollection
    @CollectionTable(name = "hotel_amenities", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "amenity")
    private List<String> amenities;

    public boolean addToAmenities(List<String> amenitiesToAdd){
        if(amenities.containsAll(amenitiesToAdd)){
            return false;
        }
        amenities.addAll(amenitiesToAdd);
        return true;
    }
}
