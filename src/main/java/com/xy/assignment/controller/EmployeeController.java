package com.xy.assignment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xy.assignment.entity.EmployeeEntity;
import com.xy.assignment.service.impl.EmployeeServiceImpl;
import com.xy.assignment.utils.JsonUtils;
import com.xy.assignment.validator.ValidatorUtils;
import com.xy.assignment.validator.group.AddGroup;
import com.xy.assignment.validator.group.UpdateGroup;
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
    public ResponseEntity<Object> list(@RequestParam Map<String, String> params) {

        if (!params.containsKey("minSalary")) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Miss min salary param."));
        } else if (!params.containsKey("maxSalary")) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Miss max salary param."));
        } else if (!params.containsKey("offset")) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Miss offset param."));
        } else if (!params.containsKey("limit")) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Miss limit param."));
        } else if (!params.containsKey("sort")) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Miss sort param"));
        }

        List<EmployeeEntity> list = employeeService.queryAll(params);
        Map<String, List<EmployeeEntity>> results = new HashMap<>();
        results.put("results", list);

        return ResponseEntity.ok().body(JSON.toJSONString(results));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Object> info(@PathVariable("id") String id) {
        if (id != null && !id.isEmpty()) {
            EmployeeEntity employee = employeeService.getById(id);
            if (employee != null) {
                return ResponseEntity.ok().body(JsonUtils.toJsonString("result", employee));
            } else {
                return ResponseEntity.badRequest().body("No employee matched.");
            }
        }
        return ResponseEntity.badRequest().body("Invalid id");
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> save(@RequestBody String json) {
        EmployeeEntity employee = JSONObject.parseObject(json, EmployeeEntity.class);
        boolean validate = ValidatorUtils.validateEntity(employee, AddGroup.class);
        if (!validate) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Invalid employee format."));
        }
        boolean save = employeeService.save(employee);
        if (save) {
            return ResponseEntity.ok().body(JsonUtils.toJsonString("result", "Success."));
        }
        return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Failed"));
    }

    @RequestMapping(value = "", method = RequestMethod.PATCH)
    public ResponseEntity<Object> update(@RequestBody String json) {
        EmployeeEntity employee = JSONObject.parseObject(json, EmployeeEntity.class);
        boolean validate = ValidatorUtils.validateEntity(employee, UpdateGroup.class);
        if (!validate) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Invalid employee format."));
        }
        boolean duplicate = employeeService.checkLoginDuplicate(employee.getId(), employee.getLogin());
        if (duplicate) {
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Duplicate Login."));
        }
        boolean update = employeeService.updateById(employee);
        if (update) {
            return ResponseEntity.ok().body(JsonUtils.toJsonString("result", "Success."));
        }
        return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Failed"));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        boolean delete = employeeService.removeById(id);
        if (delete) {
            return ResponseEntity.ok().body(JsonUtils.toJsonString("result", "Success."));
        }
        return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Failed"));
    }

}
