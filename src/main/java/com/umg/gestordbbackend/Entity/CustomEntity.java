package com.umg.gestordbbackend.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Entity
@Table(name ="custom-table")
public class CustomEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String nombre;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


}
