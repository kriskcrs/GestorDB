package com.umg.gestordbbackend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name ="custom-table")
public class CustomEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    String sentencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSentencia() {
        return sentencia;
    }

    public void setSentencia(String sentencia) {
        this.sentencia = sentencia;
    }
}
