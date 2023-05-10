package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.CustomEntity;
import com.umg.gestordbbackend.Repository.CustomTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;

@RestController
@CrossOrigin
@RequestMapping("/custom-table")
public class CustomTableService {

    @Autowired
    private CustomTableRepository customTableRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @PostMapping
    public String createTable(@RequestBody CustomEntity table) {
        try {
            String createTableSql = "CREATE TABLE " + table.getNombre() + "(ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,nombre VARCHAR(50), email VARCHAR(100))";
            System.out.println(createTableSql);
            jdbcTemplate.execute(createTableSql);
            return "0 Exito en la creacion de la tabla";
        } catch (Exception e) {
            System.out.println("Causa -> " + e.getCause());
            System.out.println("Excepcion -> " + e.getMessage());
            return "1 Hubo un problema, tabla no creada";
        }
    }
}

