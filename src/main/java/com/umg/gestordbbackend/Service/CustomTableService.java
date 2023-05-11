package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.CustomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
        String firstTwoChars = myString.substring(0, 2);

        //return generalQry(tabla);
        return general(tabla);
    }

    public String general(@RequestBody CustomEntity table) {
        sentence = table.getSentencia().toUpperCase();
        try {
            jdbcTemplate.execute(sentence);
            return "0 Exito sentencia \n" + sentence;
        } catch (Exception e) {
            System.out.println("Causa -> " + e.getCause());
            System.out.println("Exception -> " + e.getMessage());
            return "1 Error -> " + e.getCause();
        }
    }

    public String generalQry(@RequestBody CustomEntity table) {
        sentence = table.getSentencia().toUpperCase();
        try {

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbOS2", "root", "cristian13");

            // Crear objeto Statement y ejecutar consulta
            Statement stmt = conn.createStatement();
            // ResultSet rs = stmt.executeQuery(sentence);
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sentence);

            // ResultSet rs = (ResultSet) jdbcTemplate.queryForRowSet(sentence);
            System.out.println(rowSet);

            while (rowSet.next()) {
                String nombre = rowSet.getString("nombre");
                System.out.println(nombre);
                String dpi = rowSet.getString("dpi");
                System.out.println(dpi);
                // hacer algo con el nombre de la persona
            }



            // Cerrar ResultSet, Statement y Connection

            stmt.close();
            conn.close();


            return "0 Exito sentencia \n";
        } catch (Exception e) {
            System.out.println("Causa -> " + e.getCause());
            System.out.println("Exception -> " + e.getMessage());
            return "1 Error -> " + e.getCause();
        }
    }

}
