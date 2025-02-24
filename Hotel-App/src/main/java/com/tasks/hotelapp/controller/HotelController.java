package com.tasks.hotelapp.controller;

import com.tasks.hotelapp.model.dto.HotelDto;
import com.tasks.hotelapp.service.HotelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class HotelController {

    private HotelService hotelService;
    @GetMapping("/hotels")
    public List<HotelDto> getHotels(){
        return hotelService.getAllHotels();
    }
}
