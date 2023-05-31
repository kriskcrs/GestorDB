package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Entity.ConnectionDB;
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

    Map<String, Object> response = new HashMap<>();
    private String local = "localhost";
    private String ipvirtual = "192.168.1.32";
    private String ipfisica = "192.168.15";
    private boolean valid = false;

    @PostMapping("/execute-db")
    public List<Map<String, Object>> connectToDatabase(@RequestBody UserDB userDB) {

        String url = "jdbc:mysql://" + local + ":3306/" + userDB.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String username = userDB.getUsername();
        String password = userDB.getPassword();
        //String sentencia = userDB.getSentencia();
        List<Map<String, Object>> response = new ArrayList<>();
        List<String> sentencias = Arrays.asList(userDB.getSentencia().split(";"));

        try {
            //Crea el Administrador del Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea la conexion
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("valor de conn " + conn);
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

        connection(userDB);
        if (valid) {
            connection2(userDB);
        }
        return response;
    }

    public Map<String, Object> connection2(UserDB userDB) {
        String url = "jdbc:mysql://" + ipfisica + ":3306/" + userDB.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        String username = userDB.getUsername();
        String password = userDB.getPassword();

        try {
            //Crea el Administrador del Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea la conexion
            Connection conn = DriverManager.getConnection(url, username, password);

            response.put("message", "Conexión exitosa a la base de datos.");

            // Cierra la conexion
            conn.close();
        } catch (Exception e) {

            response.put("error", "Error: " + e.getMessage());
        }
        return response;

    }


    public Map<String, Object> connection(UserDB userDB) {
        String url = "jdbc:mysql://" + ipvirtual + ":3306/" + userDB.getUrl() + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

        String username = userDB.getUsername();
        String password = userDB.getPassword();

        try {
            //Crea el Administrador del Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Crea la conexion
            Connection conn = DriverManager.getConnection(url, username, password);

            response.put("message", "Conexión exitosa a la base de datos.");

            // Cierra la conexion
            conn.close();
        } catch (Exception e) {
            valid = true;
            response.put("error", "Error: " + e.getMessage());
        }
        return response;
    }
}
