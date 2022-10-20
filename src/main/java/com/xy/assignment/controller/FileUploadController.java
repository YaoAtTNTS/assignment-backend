package com.xy.assignment.controller;

import com.xy.assignment.entity.EmployeeEntity;
import com.xy.assignment.service.impl.EmployeeServiceImpl;
import com.xy.assignment.utils.JsonUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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

    private final Lock lock = new ReentrantLock();

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<String> upload(MultipartFile file) throws Exception {
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
                result = readCSVData(dir);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
                dir.deleteOnExit();
            }

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

    public String readCSVData(File file) throws Exception {
        DataInputStream inputStream = new DataInputStream(new FileInputStream(file));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String row;
        reader.readLine();
        ArrayList<String> idList = new ArrayList<>();
        ArrayList<String> loginList = new ArrayList<>();
        ArrayList<EmployeeEntity> employeeList = new ArrayList<>();
        int rowNo = 1;
        while ((row = reader.readLine()) != null) {
            rowNo++;
            String[] rowItems = row.split(",");
            if (rowItems.length < 4) {
                return "Row 1 " + " has too few columns.";
            }
            for (int i = 0; i < 4; i++) {
                if (rowItems[i].isEmpty()) {
                    return "Row " + rowNo + " has too few columns.";
                }
            }
            if (rowItems.length > 4) {
                for (int i = 4; i < rowItems.length; i++) {
                    if (!rowItems[i].isEmpty()) {
                        return "Row " + rowNo + " has too many columns.";
                    }
                }
                continue;
            }
            if (rowItems[0].startsWith("#")) {
                continue;
            }
            if (idList.contains(rowItems[0])) {
                int i = idList.indexOf(rowItems[0]) + 2;
                return "Row " + rowNo + " and Row " + i + " has duplicate employee IDs.";
            }
            idList.add(rowItems[0]);
            if (loginList.contains(rowItems[1])) {
                int i = loginList.indexOf(rowItems[1]) + 2;
                return "Row " + rowNo + " and Row " + i + " has duplicate employee logins.";
            }
            if (employeeService.checkLoginDuplicate(rowItems[0], rowItems[1])) {
                return "Row " + rowNo + " has duplicate logins with an existing ID in the database.";
            }
            loginList.add(rowItems[1]);
            if (!isPositiveDouble(rowItems[3])) {
                return "Invalid salary in Row " + rowNo;
            }
            employeeList.add(new EmployeeEntity(rowItems[0], rowItems[1], rowItems[2], Double.parseDouble(rowItems[3])));
        }
        if (rowNo <= 1) {
            return "Empty file";
        }
        int newRecords = employeeService.saveOrUpdateBatchById(employeeList);
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
