package com.tasks.hotelapp.service;

import com.tasks.hotelapp.dao.HotelsRepository;
import com.tasks.hotelapp.exception.HotelAlreadyExistsException;
import com.tasks.hotelapp.exception.HotelNotFoundException;
import com.tasks.hotelapp.exception.NotValidDataException;
import com.tasks.hotelapp.model.*;
import com.tasks.hotelapp.model.dto.HotelCreateDto;
import com.tasks.hotelapp.model.dto.HotelDto;
import com.tasks.hotelapp.model.entity.Hotel;
import com.tasks.hotelapp.service.HotelService;
import com.tasks.hotelapp.specification.HotelSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HotelServiceTest {

    @Mock
    private HotelsRepository hotelsRepository;

    @InjectMocks
    private HotelService hotelService;

    private HotelCreateDto validHotelCreateDto;
    private Hotel hotelEntity;
    private HotelDto hotelDto;

    @BeforeEach
    void setUp() {
        validHotelCreateDto = new HotelCreateDto(
                "Hotel California",
                "A lovely place",
                "BrandX",
                new Address(1, "Sunset Blvd", "Los Angeles", "USA", "90001"),
                new Contacts("123-456-7890", "info@hotelcalifornia.com"),

                new ArrivalTime("14:00", "12:00")
        );

        hotelEntity = Hotel.builder()
                .id(1L)
                .name("Hotel California")
                .description("A lovely place")
                .brand("BrandX")
                .address(new Address(1, "Sunset Blvd", "Los Angeles", "USA", "90001"))
                .contacts(new Contacts("123-456-7890", "info@hotelcalifornia.com"))
                .arrivalTime(new ArrivalTime("14:00", "12:00"))
                .amenities(new ArrayList<>())
                .build();

        hotelDto = new HotelDto(
                1L,
                "Hotel California",
                "A lovely place",
                "1 Sunset Blvd, Los Angeles, 90001, USA",
                "123-456-7890"
        );
    }
    @Test
    void getAllHotels_ShouldReturnListOfHotelDtos() {
        // Arrange
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity));

        // Act
        List<HotelDto> result = hotelService.getAllHotels();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(hotelDto, result.get(0));
        verify(hotelsRepository, times(1)).findAll();
    }
    @Test
    void getHotelById_ShouldReturnHotel_WhenHotelExists() {
        // Arrange
        when(hotelsRepository.findById(1L)).thenReturn(Optional.of(hotelEntity));

        // Act
        Hotel result = hotelService.getHotelById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(hotelEntity, result);
        verify(hotelsRepository, times(1)).findById(1L);
    }
    @Test
    void getHotelById_ShouldThrowHotelNotFoundException_WhenHotelDoesNotExist() {
        // Arrange
        when(hotelsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(HotelNotFoundException.class, () -> hotelService.getHotelById(1L));
        verify(hotelsRepository, times(1)).findById(1L);
    }
    @Test
    void getFilteredHotels_ShouldReturnFilteredList() {
        // Arrange
        Specification<Hotel> expectedSpec = Specification
                .where(HotelSpecification.byName("Hotel California"))
                .and(HotelSpecification.byBrand("BrandX"))
                .and(HotelSpecification.byCity("Los Angeles"))
                .and(HotelSpecification.byCountry("USA"))
                .and(HotelSpecification.byAmenities(List.of("WiFi")));

        // Используем any(Specification.class), чтобы указать тип аргумента
        when(hotelsRepository.findAll(any(Specification.class))).thenReturn(List.of(hotelEntity));

        // Act
        List<HotelDto> result = hotelService.getFilteredHotels(
                "Hotel California", "BrandX", "USA", "Los Angeles", List.of("WiFi")
        );

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(hotelDto, result.get(0));
        verify(hotelsRepository, times(1)).findAll(any(Specification.class));
    }
    @Test
    void createHotel_ShouldReturnHotelDto_WhenDataIsValid() {
        // Arrange
        when(hotelsRepository.existsHotelByAddressAndName(any(), any())).thenReturn(false);

        // Устанавливаем ID для сохраненного объекта
        when(hotelsRepository.save(any(Hotel.class))).thenAnswer(invocation -> {
            Hotel savedHotel = invocation.getArgument(0);
            savedHotel.setId(1L); // Устанавливаем ID
            return savedHotel;
        });

        // Act
        HotelDto result = hotelService.createHotel(validHotelCreateDto);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId()); // Проверяем, что ID установлен
        assertEquals("Hotel California", result.getName());
        assertEquals("A lovely place", result.getDescription());
        assertEquals("1 Sunset Blvd, Los Angeles, 90001, USA", result.getAddress());
        assertEquals("123-456-7890", result.getPhone());
        verify(hotelsRepository, times(1)).save(any(Hotel.class));
    }
    @Test
    void createHotel_ShouldThrowHotelAlreadyExistsException_WhenHotelExists() {
        // Arrange
        when(hotelsRepository.existsHotelByAddressAndName(any(), any())).thenReturn(true);

        // Act & Assert
        assertThrows(HotelAlreadyExistsException.class, () -> hotelService.createHotel(validHotelCreateDto));
        verify(hotelsRepository, never()).save(any());
    }
    @Test
    void addAmenities_ShouldAddAmenities_WhenAmenitiesAreNotPresent() {
        // Arrange
        when(hotelsRepository.findById(1L)).thenReturn(Optional.of(hotelEntity));
        when(hotelsRepository.save(any())).thenReturn(hotelEntity);

        // Act
        hotelService.addAmenities(1L, List.of("WiFi", "Pool"));

        // Assert
        assertTrue(hotelEntity.getAmenities().containsAll(List.of("WiFi", "Pool")));
        verify(hotelsRepository, times(1)).save(hotelEntity);
    }
    @Test
    void addAmenities_ShouldThrowResponseStatusException_WhenAmenitiesAlreadyExist() {
        // Arrange
        hotelEntity.setAmenities(List.of("WiFi"));
        when(hotelsRepository.findById(1L)).thenReturn(Optional.of(hotelEntity));

        // Act & Assert
        assertThrows(ResponseStatusException.class, () -> hotelService.addAmenities(1L, List.of("WiFi")));
        verify(hotelsRepository, never()).save(any());
    }
    @Test
    void getHistogram_ShouldReturnHistogram_WhenParamIsValid() {
        // Arrange
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity));

        // Act
        Map<String, Integer> result = hotelService.getHistogram("brand");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("BrandX"));
        verify(hotelsRepository, times(1)).findAll();
    }
    @Test
    void getHistogram_ShouldThrowNotValidDataException_WhenParamIsInvalid() {
        // Arrange
        String invalidParam = "invalidParam";
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity)); // Мокируем данные

        // Act & Assert
        assertThrows(NotValidDataException.class, () -> hotelService.getHistogram(invalidParam));
    }
    @Test
    void getHistogram_ShouldReturnHistogramByBrand() {
        // Arrange
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity));

        // Act
        Map<String, Integer> result = hotelService.getHistogram("brand");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("BrandX")); // Проверяем, что бренд "BrandX" встречается 1 раз
        verify(hotelsRepository, times(1)).findAll();
    }

    @Test
    void getHistogram_ShouldReturnHistogramByCity() {
        // Arrange
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity));

        // Act
        Map<String, Integer> result = hotelService.getHistogram("city");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("Los Angeles")); // Проверяем, что город "Los Angeles" встречается 1 раз
        verify(hotelsRepository, times(1)).findAll();
    }

    @Test
    void getHistogram_ShouldReturnHistogramByCountry() {
        // Arrange
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity));

        // Act
        Map<String, Integer> result = hotelService.getHistogram("country");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("USA")); // Проверяем, что страна "USA" встречается 1 раз
        verify(hotelsRepository, times(1)).findAll();
    }

    @Test
    void getHistogram_ShouldReturnHistogramByAmenities() {
        // Arrange
        hotelEntity.setAmenities(List.of("WiFi", "Pool")); // Устанавливаем удобства
        when(hotelsRepository.findAll()).thenReturn(List.of(hotelEntity));

        // Act
        Map<String, Integer> result = hotelService.getHistogram("amenities");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.get("WiFi")); // Проверяем, что "WiFi" встречается 1 раз
        assertEquals(1, result.get("Pool")); // Проверяем, что "Pool" встречается 1 раз
        verify(hotelsRepository, times(1)).findAll();
    }
    @Test
    void createHotel_ShouldThrowNotValidDataException_WhenContactsAreInvalid() {
        // Arrange
        HotelCreateDto invalidHotelCreateDto = new HotelCreateDto(
                "Hotel California",
                "A lovely place",
                "BrandX",
                new Address(1, "Sunset Blvd", "Los Angeles", "USA", "90001"),
                new Contacts(null, null), // Невалидные контакты
                new ArrivalTime("14:00", "12:00")
        );

        when(hotelsRepository.existsHotelByAddressAndName(any(), any())).thenReturn(false);

        // Act & Assert
        assertThrows(NotValidDataException.class, () -> hotelService.createHotel(invalidHotelCreateDto));
        verify(hotelsRepository, never()).save(any());
    }
}