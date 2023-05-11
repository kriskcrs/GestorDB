package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.CustomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("/custom-table")
public class CustomTableService {

    public boolean cumple = false;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Autor Cristian Cáceres
    @PostMapping
    public String createTable(@RequestBody CustomEntity table) {


        String sentencia = table.getSentencia();
        String expresion = "create table [a-z0-9]+ \\((([a-z]+ (int|(varchar\\([0-9]+\\)))),?)+\\);";

        validarExpresionRegular(sentencia, expresion);

        if (cumple) {
            try {
                jdbcTemplate.execute(sentencia);
                return "0 Exito en la creacion de la tabla";
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
            }
        }
        return "1 Hubo un problema, tabla no creada";
    }

    public boolean validarExpresionRegular(String texto, String expresionRegular) {
        Pattern patron = Pattern.compile(expresionRegular);
        Matcher matcher = patron.matcher(texto);
        return cumple = matcher.matches();
    }
}
