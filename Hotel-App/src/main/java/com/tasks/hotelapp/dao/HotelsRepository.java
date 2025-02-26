package com.tasks.hotelapp.dao;

import com.tasks.hotelapp.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HotelsRepository extends JpaRepository<Hotel,Long>, JpaSpecificationExecutor<Hotel> {

}
