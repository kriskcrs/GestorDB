package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.ConnectionDB;
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
@RequestMapping("/")
public class CustomTableService {

    //Autor Cristian Cáceres
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String sentence;
    private String mensaje;

    @PostMapping("/custom-table")
    public String DDLTable(@RequestBody CustomEntity tabla) {
        try {
            String myString = tabla.getSentencia();
            if (myString.length() >= 6) {
                String firstChars = myString.substring(0, 6);
                if (firstChars.equalsIgnoreCase("SELECT")) {
                    System.out.println("entro en dql -> " + myString);
                    return generalQry(tabla);
                }
                System.out.println("entro en dml o ddl -> " + myString);
                return general(tabla);
            } else {
                return "Response -> java.sql.SQLSyntaxErrorException: You have an error in your SQL syntax;" +
                        " check the manual that corresponds to your MySQL server version for the right syntax to use near '" + myString + "' at line 1 personal";
            }
        } catch (Exception e) {
            mensaje = String.valueOf(e.getCause());
            System.out.println(e.getMessage());
        }
        return "Response -> " + mensaje;
    }

    public String general(@RequestBody CustomEntity table) {
        sentence = table.getSentencia();
        try {
            jdbcTemplate.execute(sentence);
            return "Response -> " + sentence;
        } catch (Exception e) {
            System.out.println("Causa -> " + e.getCause());
            return "Response -> " + e.getCause();
        }
    }

    public String generalQry(@RequestBody CustomEntity table) {
        sentence = table.getSentencia();
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
            return new ObjectMapper().writeValueAsString(rows);
        } catch (Exception e) {
            return "Response -> " + e.getCause();
        }
    }


    @PostMapping("/connect-db")
    public String connectToDatabase(@RequestBody ConnectionDB connectionData) {
        String url = connectionData.getUrl();
        String username = connectionData.getUsername();
        String password = connectionData.getPassword();
        String setencia = connectionData.getSetencia();
        try {
            // Create the DriverManager
            //Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create the Connection
            Connection conn = DriverManager.getConnection(url, username, password);
            // Use the Connection to query the database
            Statement stmt = conn.createStatement();
            CustomEntity customEntity = new CustomEntity();
            customEntity.setSentencia(setencia);
            mensaje = DDLTable(customEntity);
            // Close the Connection
            conn.close();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return mensaje;
    }
}