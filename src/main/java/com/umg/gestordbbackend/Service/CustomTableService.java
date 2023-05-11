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

    //Autor Cristian Cáceres

    @Autowired
    private JdbcTemplate jdbcTemplate;
    public boolean cumple = false;
    private String sentencia;
    private String expresion;
    private String mensaje;



    @PostMapping
    public String DDLTable(@RequestBody CustomEntity tabla) {
        String myString = tabla.getSentencia().toUpperCase();
        char firstChar = myString.charAt(0);

        if (firstChar == 'C') {
            return createTable(tabla);
        } else if (firstChar == 'D') {
            return DeleteTable(tabla);
        } else {
            mensaje = "1 Sentencia no es DDL";
            return mensaje;
        }
    }

    public String createTable(@RequestBody CustomEntity table) {
        sentencia = table.getSentencia().toUpperCase();
        System.out.println(sentencia);
        expresion = "CREATE TABLE [A-Z0-9]+ \\((([A-Z0-9]+ (INT|(VARCHAR\\([0-9]+\\)))),?)+\\);";
        validarExpresionRegular(sentencia, expresion);

        if (cumple) {
            try {
                jdbcTemplate.execute(sentencia);
                return "0 Exito se crea \n" + sentencia;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
            return "1 Error -> " + e.getCause();
            }
        }
        System.out.println("Cumple la condicion -> " + cumple);
        return "1 Sintaxys erronea -> " + sentencia;
    }

    public String DeleteTable(@RequestBody CustomEntity table) {
        sentencia = table.getSentencia().toUpperCase();
        expresion = "DROP TABLE \\w+;";
        validarExpresionRegular(sentencia, expresion);
        if (cumple) {
            try {
                jdbcTemplate.execute(sentencia);
                return "0 Se Elimina -> " + sentencia;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
                return "1 Error -> " + e.getCause();
            }
        }
        System.out.println("Cumple la condicion -> " + cumple);
        return "1 Sintaxys erronea -> " + sentencia;
    }

    public boolean validarExpresionRegular(String texto, String expresionRegular) {
        Pattern patron = Pattern.compile(expresionRegular);
        Matcher matcher = patron.matcher(texto);
        return cumple = matcher.matches();
    }

}
