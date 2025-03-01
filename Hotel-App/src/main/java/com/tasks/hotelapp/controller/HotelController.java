package com.tasks.hotelapp.controller;

import com.tasks.hotelapp.model.dto.HotelCreateDto;
import com.tasks.hotelapp.model.dto.HotelDto;
import com.tasks.hotelapp.model.entity.Hotel;
import com.tasks.hotelapp.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/property-view")
public class HotelController {

    private HotelService hotelService;
    @GetMapping("/hotels")
    public List<HotelDto> getHotels(){
        return hotelService.getAllHotels();
    }
    @GetMapping("/hotels/{id}")
    public Hotel getHotelById(@PathVariable Long id){
        return hotelService.getHotelById(id);
    }
    @GetMapping("/search")
    public List<HotelDto> searchHotel(@RequestParam(required = false) String name,
                                      @RequestParam (required = false) String brand,
                                      @RequestParam(required = false) String city,
                                      @RequestParam (required = false) String country,
                                      @RequestParam(required = false) List<String> amenities){
        return hotelService.getFilteredHotels(name,brand,country,city,amenities);
    }
    @PostMapping("/hotels")
    public HotelDto createHotel(@RequestBody HotelCreateDto hotel){
        return hotelService.createHotel(hotel);
    }
    @PostMapping("/hotels/{id}/amenities")
    public void createAmenities(
            @PathVariable Long id,
            @RequestBody List<String> amenities){
        hotelService.addAmenities(id,amenities);
    }
    @GetMapping("/histogram/{param}")
    public Map<String, Integer> getHotelHistogram(@PathVariable() String param){
        return hotelService.getHistogram(param);
    }

}
