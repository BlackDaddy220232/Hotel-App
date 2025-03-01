package com.tasks.hotelapp.service;

import com.tasks.hotelapp.dao.HotelsRepository;
import com.tasks.hotelapp.exception.HotelAlreadyExistsException;
import com.tasks.hotelapp.exception.HotelNotFoundException;
import com.tasks.hotelapp.exception.NotValidDataException;
import com.tasks.hotelapp.model.Address;
import com.tasks.hotelapp.model.ArrivalTime;
import com.tasks.hotelapp.model.Contacts;
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
    private static final String HOTEL_EXISTS = "Hotel with this name and address already exists";
    private static final String NOT_VALID_DATA = "Incorrect fields!";
    private static final String HOTEL_NOT_FOUND = "Hotel not found!";
    private static final String WRONG_PARAMETER= "Wrong parameter";

    private final HotelsRepository hotelsRepository;

    public List<HotelDto> getAllHotels() {
        return convertToHotelDto(hotelsRepository.findAll());
    }

    public Hotel getHotelById(Long id) {
        return hotelsRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException(HOTEL_NOT_FOUND));
    }

    public List<HotelDto> getFilteredHotels(String name, String brand, String country, String city, List<String> amenities) {
        Specification<Hotel> spec = Specification
                .where(HotelSpecification.byName(name))
                .and(HotelSpecification.byBrand(brand))
                .and(HotelSpecification.byCity(city))
                .and(HotelSpecification.byCountry(country))
                .and(HotelSpecification.byAmenities(amenities));

        return convertToHotelDto(hotelsRepository.findAll(spec));
    }

    public HotelDto createHotel(HotelCreateDto hotelCreateDto) {
        validateHotelCreation(hotelCreateDto);
        Hotel hotel = mapToHotelEntity(hotelCreateDto);
        hotelsRepository.save(hotel);
        return convertToHotelDto(hotel);
    }

    public void addAmenities(Long id, List<String> amenities) {
        Hotel hotel = getHotelById(id);
        if (hotel.addToAmenities(amenities)) {
            hotelsRepository.save(hotel);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "These amenities are already added");
        }
    }

    public Map<String, Integer> getHistogram(String param) {
        List<Hotel> hotels = hotelsRepository.findAll();
        Map<String, Integer> histogram = new HashMap<>();

        for (Hotel hotel : hotels) {
            processHotel(hotel, param, histogram);
        }

        return histogram;
    }

    private List<HotelDto> convertToHotelDto(List<Hotel> hotels) {
        return hotels.stream()
                .map(this::convertToHotelDto)
                .collect(Collectors.toList());
    }

    private HotelDto convertToHotelDto(Hotel hotel) {
        String address = String.format("%d %s, %s, %s, %s",
                hotel.getAddress().getHouseNumber(),
                hotel.getAddress().getStreet(),
                hotel.getAddress().getCity(),
                hotel.getAddress().getPostCode(),
                hotel.getAddress().getCountry());

        return new HotelDto(
                hotel.getId(),
                hotel.getName(),
                hotel.getDescription(),
                address,
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
                return;
            default:
                throw new NotValidDataException(WRONG_PARAMETER);
        }

        updateHistogram(histogram, key);
    }

    private void processAmenities(List<String> amenities, Map<String, Integer> histogram) {
        amenities.forEach(amenity -> updateHistogram(histogram, amenity));
    }

    private void updateHistogram(Map<String, Integer> histogram, String key) {
        histogram.put(key, histogram.getOrDefault(key, 0) + 1);
    }

    private void validateHotelCreation(HotelCreateDto hotelCreateDto) {
        if (hotelsRepository.existsHotelByAddressAndName(hotelCreateDto.getAddress(), hotelCreateDto.getName())) {
            throw new HotelAlreadyExistsException(HOTEL_EXISTS);
        }
        if (!validationHotelDTO(hotelCreateDto)) {
            throw new NotValidDataException(NOT_VALID_DATA);
        }
    }

    private Hotel mapToHotelEntity(HotelCreateDto hotelCreateDto) {
        return Hotel.builder()
                .name(hotelCreateDto.getName())
                .address(hotelCreateDto.getAddress())
                .contacts(hotelCreateDto.getContacts())
                .arrivalTime(hotelCreateDto.getArrivalTime())
                .brand(hotelCreateDto.getBrand())
                .description(hotelCreateDto.getDescription())
                .build();
    }

    private boolean validationHotelDTO(HotelCreateDto hotelCreateDto) {
        return hotelCreateDto != null &&
                isNameValid(hotelCreateDto.getName()) &&
                isBrandValid(hotelCreateDto.getBrand()) &&
                isAddressValid(hotelCreateDto.getAddress()) &&
                isContactsValid(hotelCreateDto.getContacts()) &&
                isArrivalTimeValid(hotelCreateDto.getArrivalTime());
    }

    private boolean isNameValid(String name) {
        return isNonEmpty(name);
    }

    private boolean isBrandValid(String brand) {
        return isNonEmpty(brand);
    }

    private boolean isAddressValid(Address address) {
        return address != null &&
                address.getHouseNumber() > 0 &&
                isNonEmpty(address.getStreet()) &&
                isNonEmpty(address.getCity()) &&
                isNonEmpty(address.getCountry()) &&
                isNonEmpty(address.getPostCode());
    }

    private boolean isContactsValid(Contacts contacts) {
        return contacts != null &&
                isNonEmpty(contacts.getPhone()) &&
                isNonEmpty(contacts.getEmail());
    }

    private boolean isArrivalTimeValid(ArrivalTime arrivalTime) {
        return arrivalTime != null && isNonEmpty(arrivalTime.getCheckIn());
    }

    private boolean isNonEmpty(String str) {
        return str != null && !str.isEmpty();
    }
}