package com.umg.gestordbbackend.Controller;

import com.umg.gestordbbackend.Repository.CustomTableRepository;
import com.umg.gestordbbackend.Service.CustomTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custom-table")
public class CustomTableController {

    @Autowired
    private CustomTableRepository customTableRepository;
 //cristian
    @PostMapping
    public void createTable(@RequestParam("tableName") String tableName) {
        CustomTableService.createTable(tableName);
    }


}
