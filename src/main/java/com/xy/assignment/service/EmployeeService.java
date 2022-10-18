package com.xy.assignment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.assignment.entity.EmployeeEntity;

import java.util.List;
import java.util.Map;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:27 AM 10/16/2022
 * @ Description: employee service interface
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */


public interface EmployeeService extends IService<EmployeeEntity> {
    List<EmployeeEntity> queryAll(Map<String, String> params);

    boolean checkLoginDuplicate(String id, String login);
}
