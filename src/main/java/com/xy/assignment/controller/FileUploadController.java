package com.xy.assignment.controller;

import com.opencsv.CSVReader;
import com.xy.assignment.entity.EmployeeEntity;
import com.xy.assignment.service.impl.EmployeeServiceImpl;
import com.xy.assignment.utils.JsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 2:52 PM 10/16/2022
 * @ Description: file upload handler
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowCredentials = "true")
public class FileUploadController {

    @Resource
    private EmployeeServiceImpl employeeService;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> upload(MultipartFile file) throws Exception {
        Lock lock = new ReentrantLock();
        if (lock.tryLock()) {
            String tempDir;

            if (System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                tempDir = "C:\\temp\\xiong\\";
            } else {
                tempDir = "/tmp/xiong/";
            }

            String filename = file.getOriginalFilename();
            String tempPath = tempDir + filename + UUID.randomUUID();
            File dir = new File(tempPath);
            String result = null;
            if (!dir.exists()) {
                dir.mkdirs();
            }
            try {
                file.transferTo(dir);
                result = readCSVData(tempPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            lock.unlock();
            dir.delete();
            if (result == null) {
                return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", "Unknown error."));
            }
            if (result.matches("^[0-9]+.*")) {
                return ResponseEntity.ok().body(JsonUtils.toJsonString("result", result));
            }
            return ResponseEntity.badRequest().body(JsonUtils.toJsonString("result", result));
        } else {
            return ResponseEntity.status(503).body(JsonUtils.toJsonString("result", "An existing file is uploading. Please wait."));
        }

    }

    private String readCSVData (String tempPath) throws Exception {
        FileReader reader = new FileReader(tempPath);
        CSVReader csvReader = new CSVReader(reader);
        csvReader.skip(1);
        ArrayList<String> idList = new ArrayList<>();
        ArrayList<String> loginList = new ArrayList<>();
        ArrayList<EmployeeEntity> employeeList = new ArrayList<>();
        String[] nextRow;
        int rowNo = 1;
        while ((nextRow = csvReader.readNext()) != null) {
            rowNo++;
            if (nextRow.length > 4) {
                List<String> nextRowStrings = Arrays.asList(nextRow);
                while(nextRowStrings.remove(""));
                if (nextRowStrings.size() == 4) {
                    continue;
                }
                return "Row " + rowNo + " has too many columns.";
            }
            if (nextRow[0].isEmpty() || nextRow[1].isEmpty() || nextRow[2].isEmpty() || nextRow[3].isEmpty()) {
                return "Row " + rowNo + " has too few columns.";
            }
            if (nextRow[0].startsWith("#")) {
                continue;
            }
            if (idList.contains(nextRow[0])) {
                int i = idList.indexOf(nextRow[0]) + 2;
                return "Row " + rowNo + " and Row " + i + " has duplicate employee IDs.";
            }
            idList.add(nextRow[0]);
            if (loginList.contains(nextRow[1])) {
                int i = loginList.indexOf(nextRow[1]) + 2;
                return "Row " + rowNo + " and Row " + i + " has duplicate employee logins.";
            }
            if (employeeService.checkLoginDuplicate(nextRow[0], nextRow[1])) {
                return "Row " + rowNo + " has duplicate logins with an existing ID in the database.";
            }
            loginList.add(nextRow[1]);
            if (isPositiveDouble(nextRow[3])) {
                return "Invalid salary in Row " + rowNo;
            }
            System.out.println("Id: " + nextRow[0] + ", Name: " + nextRow[1] + ", Login: " + nextRow[2] + ", Salary: " + nextRow[3]);
            employeeList.add(new EmployeeEntity(nextRow[0], nextRow[1], nextRow[2], Double.parseDouble(nextRow[3])));
        }
        if (rowNo == 1) {
            return "Empty file";
        }
        int newRecords = employeeService.saveOrUpdateBatchByEId(employeeList);
        if (newRecords == -1) {
            return "Duplicate logins";
        }
        if (newRecords == -2) {
            return "Duplicate Ids";
        }
        int existingRecords = idList.size() - newRecords;
        return newRecords + " new records uploaded, " + existingRecords + " existing records updated successfully.";
    }

    private boolean isPositiveDouble (String d) {
        return d.trim().matches("^[0-9]+(\\.[0-9])?$");
    }
}
