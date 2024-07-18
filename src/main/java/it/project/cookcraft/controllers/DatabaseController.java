package it.project.cookcraft.controllers;

import it.project.cookcraft.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class DatabaseController {

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/data")
    public List<Map<String, Object>> getData() {
        return databaseService.getAllData();
    }
}
