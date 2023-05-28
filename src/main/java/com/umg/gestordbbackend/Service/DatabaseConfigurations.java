package com.umg.gestordbbackend.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.umg.gestordbbackend.Entity.ConnectionDB;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DatabaseConfigurations {

    @PostMapping("/connect-db")
    public List<Map<String, Object>> connectToDatabase(@RequestBody ConnectionDB connectionData) {
        String url = connectionData.getUrl();
        String username = connectionData.getUsername();
        String password = connectionData.getPassword();
        String sentencia = connectionData.getSentencia();
        List<Map<String, Object>> response = new ArrayList<>();
        try {
            // Create the DriverManager Crea
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create the Connection
            Connection conn = DriverManager.getConnection(url, username, password);
            // Use the Connection to query the database
            Statement stmt = conn.createStatement();
            boolean isQuery = stmt.execute(sentencia);
            if (isQuery) {
                ResultSet rs = stmt.getResultSet();

                int columnCount = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnValue = rs.getString(i);
                        String columnName = rs.getMetaData().getColumnName(i);
                        row.put(columnName, columnValue);
                    }
                    response.add(row);
                }
            } else {
                int resultado = stmt.getUpdateCount();
                if (resultado >= 0) {
                    Map<String, Object> message = new HashMap<>();
                    message.put("message", connectionData.getSentencia());
                    response.add(message);
                } else {
                    Map<String, Object> message = new HashMap<>();
                    message.put("message", "La sentencia no se pudo ejecutar.");
                    response.add(message);
                }
            }
            // Close the Connection
            conn.close();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            System.out.println("Mensaje ->"+e.getMessage());
            System.out.println("Causa -> "+e.getCause());
            error.put("error","" + e.getMessage());
            response.add(error);
        }
        return response;
    }
}
