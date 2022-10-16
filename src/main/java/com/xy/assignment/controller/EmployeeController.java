package com.xy.assignment.controller;

import com.xy.assignment.entity.EmployeeEntity;
import com.xy.assignment.service.impl.EmployeeServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:31 AM 10/16/2022
 * @ Description: employee service handler
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class EmployeeController {

    @Resource
    private EmployeeServiceImpl employeeService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<Object> list(@RequestParam Map<String, Object> params) {

        if (!params.containsKey("minSalary")) {
            return ResponseEntity.badRequest().body("Miss min salary param.");
        } else if (!params.containsKey("maxSalary")) {
            return ResponseEntity.badRequest().body("Miss max salary param.");
        } else if (!params.containsKey("offset")) {
            return ResponseEntity.badRequest().body("Miss offset param.");
        } else if (!params.containsKey("limit")) {
            return ResponseEntity.badRequest().body("Miss limit param.");
        } else if (!params.containsKey("sort")) {
            return ResponseEntity.badRequest().body("Miss sort param");
        }

        List<EmployeeEntity> list = employeeService.queryAll(params);
        Map<String, List<EmployeeEntity>> results = new HashMap<>();
        results.put("results", list);

        return ResponseEntity.ok().body(results);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> info(@PathVariable("id") String id) {
        if (id != null && !id.isEmpty()) {
            EmployeeEntity employee = employeeService.queryByEId(id);
            return ResponseEntity.ok().body(employee);
        }
        return ResponseEntity.badRequest().body("Invalid id");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Object> save(@PathVariable("id") String id, @RequestBody EmployeeEntity employee) {
        employee.setEid(id);
        boolean save = employeeService.save(employee);
        if (save) {
            return ResponseEntity.ok().body("Success.");
        }
        return ResponseEntity.badRequest().body("Failed");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<Object> update(@PathVariable("id") String id, @RequestBody EmployeeEntity employee) {
        employee.setEid(id);
        boolean update = employeeService.updateByEId(employee);
        if (update) {
            return ResponseEntity.ok().body("Success.");
        }
        return ResponseEntity.badRequest().body("Failed");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        boolean delete = employeeService.deleteByEId(id);
        if (delete) {
            return ResponseEntity.ok().body("Success.");
        }
        return ResponseEntity.badRequest().body("Failed");
    }

}
