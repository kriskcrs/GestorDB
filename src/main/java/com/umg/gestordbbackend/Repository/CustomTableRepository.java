package com.umg.gestordbbackend.Repository;

import com.umg.gestordbbackend.Entity.CustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomTableRepository extends JpaRepository<CustomEntity, Long> {

}
