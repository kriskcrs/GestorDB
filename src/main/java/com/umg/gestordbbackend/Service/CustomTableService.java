package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.CustomEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.ResultSetWrappingSqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@CrossOrigin
@RequestMapping("/custom-table")
public class CustomTableService {

    //Autor Cristian Cáceres

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private String sentence;

    @PostMapping
    public String DDLTable(@RequestBody CustomEntity tabla) {
        System.out.println(tabla.getSentencia());

        String myString = tabla.getSentencia();
        String firstChars = myString.substring(0,1);

        if (firstChars.equals("S") || firstChars.equals("s") ) {
            System.out.println("entro en consulta -> ");
            return generalQry(tabla);
        }
        System.out.println("entro en modo no consulta -> ");
        return general(tabla);
    }

    public String general(@RequestBody CustomEntity table) {
        sentence = table.getSentencia();
        try {
            jdbcTemplate.execute(sentence);
            return "Sentencia ejecutada -> " + sentence;
        } catch (Exception e) {
            System.out.println("Causa -> " + e.getCause());
            return "Sentencia no ejecutada causa -> " + e.getCause();
        }
    }

    public String generalQry(@RequestBody CustomEntity table) {
        sentence = table.getSentencia();
        System.out.println(sentence);
        List<ObjectNode> rows = new ArrayList<>();
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sentence);
            ResultSet rs = ((ResultSetWrappingSqlRowSet) rowSet).getResultSet();
            ResultSetMetaData metadata = rs.getMetaData();
            int columnCount = metadata.getColumnCount();
            while (rs.next()) {
                ObjectNode row = new ObjectMapper().createObjectNode();
                for (int i = 1; i <= columnCount; i++) {
                    String columnValue = rs.getString(i);
                    String columnName = metadata.getColumnName(i);
                    columnValue = columnValue.replaceAll("[áäàâ]", "a").replaceAll("[éëèê]", "e").replaceAll("[íïìî]", "i").replaceAll("[óöòô]", "o").replaceAll("[úüùû]", "u").replaceAll("\\?", " ");
                    row.put(columnName, columnValue);
                }
                rows.add(row);
            }
            String json = new ObjectMapper().writeValueAsString(rows);
            System.out.println(json);

            return json;
        } catch (Exception e) {
            return "Sentencia no ejecutada causa -> " + e.getCause();
        }
    }

}
