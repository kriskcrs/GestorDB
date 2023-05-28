package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.ConnectionDB;
import com.umg.gestordbbackend.Entity.UserDB;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DatabaseConfigurations {

    @PostMapping("/connect-db")
    public List<Map<String, Object>> connectToDatabase(@RequestBody UserDB userDB) {

        String url = "jdbc:mysql://localhost:3306/" + userDB.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = userDB.getUsername();
        String password = userDB.getPassword();
        String sentencia = userDB.getSentencia();

        List<Map<String, Object>> response = new ArrayList<>();
        try {
            //Crea el Administrador del Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea la conexion
            Connection conn = DriverManager.getConnection(url, username, password);
            // usa la conexion para la consulta
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
                int result = stmt.getUpdateCount();
                if (result >= 0) {
                    Map<String, Object> message = new HashMap<>();
                    message.put("message", userDB.getSentencia());
                    response.add(message);
                } else {
                    Map<String, Object> message = new HashMap<>();
                    message.put("message", "La sentencia no se pudo ejecutar.");
                    response.add(message);
                }
            }
            // Cierra la conexion
            conn.close();
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            System.out.println("Mensaje ->" + e.getMessage());
            System.out.println("Causa -> " + e.getCause());
            error.put("error", "" + e.getMessage());
            response.add(error);
        }
        return response;
    }

    @PostMapping("/check-db")
    public Map<String, Object> checkDatabaseAccess(@RequestBody UserDB userDB) {
        String url = "jdbc:mysql://localhost:3306/" + userDB.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = userDB.getUsername();
        String password = userDB.getPassword();

        Map<String, Object> response = new HashMap<>();
        try {
            // Create the DriverManager
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Create the Connection
            Connection conn = DriverManager.getConnection(url, username, password);

            response.put("message", "Conexión exitosa a la base de datos.");
            // Close the Connection
            conn.close();
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage());
        }
        return response;
    }

}
