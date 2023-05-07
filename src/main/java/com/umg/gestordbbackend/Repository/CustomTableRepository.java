package com.umg.gestordbbackend.Repository;

import com.umg.gestordbbackend.Entity.CustomTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomTableRepository extends JpaRepository<CustomTable, Long> {


}
