package com.tasks.hotelapp.service;

import com.tasks.hotelapp.dao.HotelsRepository;
import com.tasks.hotelapp.model.dto.HotelDto;
import com.tasks.hotelapp.model.entity.Hotel;
import com.tasks.hotelapp.specification.HotelSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {
    private HotelsRepository hotelsRepository;
    public List<HotelDto> getAllHotels() {
        return convertToHotelDto(hotelsRepository.findAll());
    }
    public Hotel getHotelById(Long id){
        return hotelsRepository.findById(id).orElseThrow(()->new RuntimeException("Hotel with id not found!"));
    }
    public List<HotelDto> getFilteredHotels(String name, String brand,String country, String city, List<String> amenities) {
        Specification<Hotel> spec = Specification
                .where(HotelSpecification.byName(name))
                .and(HotelSpecification.byBrand(brand))
                .and(HotelSpecification.byCity(city))
                .and(HotelSpecification.byCountry(country))
                .and(HotelSpecification.byAmenities(amenities));

         return convertToHotelDto(hotelsRepository.findAll(spec));
    }
    private List<HotelDto> convertToHotelDto(List<Hotel> hotels){
        return hotels.stream().map(hotel -> new HotelDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                (hotel.getAddress().getHouseNumber() +hotel.getAddress().getStreet()+", "
                        +hotel.getAddress().getCity()+ ", "+ hotel.getAddress().getPostCode()+", "+ hotel.getAddress().getCountry()),
                hotel.getContacts().getPhone()
        )).collect(Collectors.toList());
    }
}
