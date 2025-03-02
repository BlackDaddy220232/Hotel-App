package com.tasks.hotelapp.specification;

import com.tasks.hotelapp.model.entity.Hotel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Join;

import java.util.List;
//Specification for JPA filtering
@NoArgsConstructor
public class HotelSpecification {

    public static Specification<Hotel> byName(String name) {
        return (root, query, cb) ->
                name == null ? cb.conjunction() : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Hotel> byBrand(String brand) {
        return (root, query, cb) ->
                brand == null ? cb.conjunction() : cb.like(cb.lower(root.get("brand")), "%" + brand.toLowerCase() + "%");
    }

    public static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("address").get("city"), city); // Путь через address
    }

    public static Specification<Hotel> byCountry(String country) {
        return (root, query, cb) ->
                country == null ? cb.conjunction() : cb.like(cb.lower(root.get("address").get("country")), "%" + country.toLowerCase() + "%");
    }

    public static Specification<Hotel> byAmenities(List<String> amenities) {
        return (root, query, cb) -> {
            if (amenities == null || amenities.isEmpty()) {
                return cb.conjunction();
            }
            Join<Hotel, String> amenitiesJoin = root.join("amenities"); // Делаем JOIN с таблицей удобств
            return amenitiesJoin.in(amenities); // WHERE amenities IN (...)
        };
    }

}

