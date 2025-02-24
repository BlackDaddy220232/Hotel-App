package com.tasks.hotelapp.service;

import com.tasks.hotelapp.dao.HotelsRepository;
import com.tasks.hotelapp.model.dto.HotelDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {
    private HotelsRepository hotelsRepository;
    public List<HotelDto> getAllHotels() {
        return hotelsRepository.findAll()
                .stream()
                .map(hotel -> new HotelDto(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getDescription(),
                        (hotel.getAddress().getHouseNumber() +hotel.getAddress().getStreet()+", "
                        +hotel.getAddress().getCity()+ ", "+ hotel.getAddress().getPostCode()+", "+ hotel.getAddress().getCountry()),
                        hotel.getContacts().getPhone()
                ))
                .collect(Collectors.toList());
    }
}
