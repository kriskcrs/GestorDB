package com.umg.gestordbbackend.Repository;

import com.umg.gestordbbackend.Entity.CustomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomTableRepository extends JpaRepository<CustomEntity, Long> {




}
