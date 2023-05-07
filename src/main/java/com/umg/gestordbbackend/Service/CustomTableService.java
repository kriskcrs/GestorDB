package com.umg.gestordbbackend.Service;

import com.umg.gestordbbackend.Repository.CustomTableRepository;
import com.umg.gestordbbackend.Entity.CustomTable;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;


public class CustomTableService {

    @Autowired
    private static CustomTableRepository customTableRepository;

    @Autowired
    private static EntityManager entityManager;

    public static void createTable(String tableName) {
        CustomTable customTable = new CustomTable();
        customTable.setTableName(tableName);

        customTableRepository.save(customTable);

        String createTableSql = "CREATE TABLE " + tableName + " (id BIGINT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255), email VARCHAR(255))";
        entityManager.createNativeQuery(createTableSql).executeUpdate();
    }

}

