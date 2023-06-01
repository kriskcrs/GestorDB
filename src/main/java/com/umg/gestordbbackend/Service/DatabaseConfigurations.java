package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.UserDB;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.*;

@RestController
@CrossOrigin
public class DatabaseConfigurations {

    private String username;
    private String password;
    private String url;
    private String local = "localhost";

    @PostMapping("/execute-db")
    public List<Map<String, Object>> connectToDatabase(@RequestBody UserDB userDB) {

        printLog(userDB);
        CollectorData(userDB);
        List<Map<String, Object>> response = new ArrayList<>();
        List<String> sentencias = Arrays.asList(userDB.getSentencia().split(";"));

        try {
            //Crea el Administrador del Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea la conexion
            Connection conn = DriverManager.getConnection(url, username, password);
            // usa la conexion para la consulta
            Statement stmt = conn.createStatement();

            for (String senten : sentencias) {
                if (!senten.trim().isEmpty()) {
                    boolean isQuery = stmt.execute(senten);
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
        printLog(userDB);
        CollectorData(userDB);
        Map<String, Object> response = new HashMap<>();
        try {
            //Crea el Administrador del Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea la conexion
            Connection conn = DriverManager.getConnection(url, username, password);
            response.put("message", "Conexión exitosa a la base de datos.");
            // Cierra la conexion
            conn.close();
        } catch (Exception e) {
            response.put("error", "Error: " + e.getMessage()+"\n");
        }
        return response;
    }

    public void printLog(UserDB userDB) {
        System.out.println("Setencia -> " + userDB.getSentencia());
        System.out.println("usuario -> " + userDB.getUsername());
        System.out.println("---------------------------------------------");
    }

    public void CollectorData(UserDB userDB) {
        url = "jdbc:mysql://" + local + ":3306/" + userDB.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        username = userDB.getUsername();
        password = userDB.getPassword();
    }
}
