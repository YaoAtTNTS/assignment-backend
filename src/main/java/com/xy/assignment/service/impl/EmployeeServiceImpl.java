package com.xy.assignment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.assignment.dao.EmployeeDao;
import com.xy.assignment.entity.EmployeeEntity;
import com.xy.assignment.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:28 AM 10/16/2022
 * @ Description:
 * @ Version:
 * @ Email: gongchen711@gmail.com
 */

@Service("employeeService")
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, EmployeeEntity> implements EmployeeService {

    @Override
    public List<EmployeeEntity> queryAll(Map<String, Object> params) {
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.ge("salary", params.get("maxSalary"));
        wrapper.le("salary", params.get("minSalary"));
        String sort = params.get("sort").toString();
        String sortField = sort.substring(1);
        if (sortField.equals("id")) {
            sortField = "eid";
        }
        if (sort.startsWith("+")) {
            wrapper.orderByAsc(sortField);
        } else {
            wrapper.orderByDesc(sortField);
        }
        wrapper.last("limit " + params.get("limit") + " offset " + params.get("offset"));
        return list(wrapper);
    }

    @Override
    public EmployeeEntity queryByEId(String id) {
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("eid", id);
        return getOne(wrapper);
    }

    @Override
    public boolean updateByEId(EmployeeEntity employee) {
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("eid", employee.getEid());
        return update(employee, wrapper);
    }

    @Override
    public boolean deleteByEId(String id) {
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("eid", id);
        return remove(wrapper);
    }

}
