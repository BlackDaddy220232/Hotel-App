package com.tasks.hotelapp.controller;

import com.tasks.hotelapp.model.dto.HotelDto;
import com.tasks.hotelapp.model.entity.Hotel;
import com.tasks.hotelapp.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
