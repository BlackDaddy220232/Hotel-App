package com.tasks.hotelapp.service;

import com.tasks.hotelapp.dao.HotelsRepository;
import com.tasks.hotelapp.model.dto.HotelCreateDto;
import com.tasks.hotelapp.model.dto.HotelDto;
import com.tasks.hotelapp.model.entity.Hotel;
import com.tasks.hotelapp.specification.HotelSpecification;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class HotelService {
    private HotelsRepository hotelsRepository;
    public List<HotelDto> getAllHotels() {
        return convertToHotelDto(hotelsRepository.findAll());
    }
    public Hotel getHotelById(Long id){
        return hotelsRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Hotel with this id not found!"));
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
    public HotelDto createHotel(HotelCreateDto hotelCreateDto){
        if(hotelsRepository.existsHotelByAddressAndName(hotelCreateDto.getAddress(),hotelCreateDto.getName())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Hotel with this name and address already exists");
        }
        Hotel hotel = Hotel.builder().name(hotelCreateDto.getName())
                .address(hotelCreateDto.getAddress())
                .contacts(hotelCreateDto.getContacts())
                .arrivalTime(hotelCreateDto.getArrivalTime())
                .brand(hotelCreateDto.getBrand())
                .description(hotelCreateDto.getDescription()).build();
        hotelsRepository.save(hotel);
        return convertToHotelDto(hotel);
    }
    public void addAmenities(Long id, List<String> amenities){
        Hotel hotel = hotelsRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Hotel with this id not found!"));
        if(hotel.addToAmenities(amenities)){
            hotelsRepository.save(hotel);
        }else{
            throw new ResponseStatusException(HttpStatus.CONFLICT,"This amenities is also added");
        }
    }
    public Map<String, Integer> getHistogram(String param) {
        List<Hotel> hotels = hotelsRepository.findAll(); // Получение списка всех отелей
        Map<String, Integer> histogram = new HashMap<>();

        for (Hotel hotel : hotels) {
            processHotel(hotel, param, histogram);
        }

        return histogram;
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
    private HotelDto convertToHotelDto(Hotel hotel){
        return new HotelDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                (hotel.getAddress().getHouseNumber() +hotel.getAddress().getStreet()+", "
                        +hotel.getAddress().getCity()+ ", "+ hotel.getAddress().getPostCode()+", "+ hotel.getAddress().getCountry()),
                hotel.getContacts().getPhone());
    }

    private void processHotel(Hotel hotel, String param, Map<String, Integer> histogram) {
        String key;

        switch (param.toLowerCase()) {
            case "brand":
                key = hotel.getBrand();
                break;
            case "city":
                key = hotel.getAddress().getCity();
                break;
            case "country":
                key = hotel.getAddress().getCountry();
                break;
            case "amenities":
                processAmenities(hotel.getAmenities(), histogram);
                return; // Пропустить остальную часть метода
            default:
                throw new IllegalArgumentException("Invalid parameter: " + param);
        }

        updateHistogram(histogram, key);
    }

    private void processAmenities(List<String> amenities, Map<String, Integer> histogram) {
        for (String amenity : amenities) {
            updateHistogram(histogram, amenity);
        }
    }
    private void updateHistogram(Map<String, Integer> histogram, String key) {
        histogram.put(key, histogram.getOrDefault(key, 0) + 1);
    }

}
