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

    public boolean cumple = false;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String sentence;
    private String expression, expressionDrop, expressionAlter;
    private String message;


    @PostMapping
    public String DDLTable(@RequestBody CustomEntity tabla) {
        String myString = tabla.getSentencia().toUpperCase();
        char firstChar = myString.charAt(0);

        if (firstChar == 'C') {
            return CreateTable(tabla);
        } else if (firstChar == 'D') {
            return DeleteTable(tabla);
        } else if (firstChar == 'A') {
            return AlterTable(tabla);
        } else {
            message = "1 Sentencia no es DDL";
            return message;
        }
    }

    public String CreateTable(@RequestBody CustomEntity table) {
        sentence = table.getSentencia().toUpperCase();
        System.out.println(sentence);
        expression = "CREATE TABLE [A-Z0-9]+ \\((([A-Z0-9]+ (INT|(VARCHAR\\([0-9]+\\)))),?)+\\);";
        validateRegularExpression(sentence, expression);

        if (cumple) {
            try {
                jdbcTemplate.execute(sentence);
                return "0 Exito se crea \n" + sentence;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
                return "1 Error -> " + e.getCause();
            }
        }
        System.out.println("Cumple la condicion -> " + cumple);
        return "1 Sintaxys erronea -> " + sentence;
    }

    public String DeleteTable(@RequestBody CustomEntity table) {
        sentence = table.getSentencia().toUpperCase();
        expression = "DROP TABLE \\w+;";
        validateRegularExpression(sentence, expression);
        if (cumple) {
            try {
                jdbcTemplate.execute(sentence);
                return "0 Se Elimina -> " + sentence;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
                return "1 Error -> " + e.getCause();
            }
        }

        return "1 Sintaxys erronea -> " + sentence;
    }

    public String AlterTable(@RequestBody CustomEntity table) {
        sentence = table.getSentencia().toUpperCase();
        System.out.println(sentence);
        expression = "ALTER TABLE \\w+ ADD COLUMN \\w+ (INT|VARCHAR\\([0-9]+\\))\\s*?;";
        expressionDrop = "ALTER TABLE \\w+ DROP COLUMN \\w+\\s*?;";
        expressionAlter = "ALTER TABLE \\w+ MODIFY COLUMN \\w+ (INT|VARCHAR\\([0-9]+\\))\\s*;";

        validateRegularExpression(sentence, expression);
        if (cumple) {
            try {
                jdbcTemplate.execute(sentence);
                return "0 Exito se altera \n" + sentence;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
                return "1 Error -> " + e.getCause();
            }

        } else if (validateRegularExpression(sentence, expressionDrop)) {
            try {
                jdbcTemplate.execute(sentence);
                return "0 Exito se altera \n" + sentence;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
                return "1 Error -> " + e.getCause();
            }
        } else if (validateRegularExpression(sentence, expressionAlter)) {

            try {
                jdbcTemplate.execute(sentence);
                return "0 Exito se altera \n" + sentence;
            } catch (Exception e) {
                System.out.println("Causa -> " + e.getCause());
                System.out.println("Exception -> " + e.getMessage());
                return "1 Error -> " + e.getCause();
            }
        }
        return "1 Sintaxys erronea -> " + sentence;
    }

    public boolean validateRegularExpression(String texto, String expresionRegular) {
        Pattern patron = Pattern.compile(expresionRegular);
        Matcher matcher = patron.matcher(texto);
        return cumple = matcher.matches();
    }

}
