package com.xy.assignment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.assignment.dao.EmployeeDao;
import com.xy.assignment.entity.EmployeeEntity;
import com.xy.assignment.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        wrapper.le("salary", params.get("maxSalary"));
        wrapper.ge("salary", params.get("minSalary"));
        String sort = params.get("sort").toString();
        String sortField = sort.substring(1);
        if (sort.startsWith("+")) {
            wrapper.orderByAsc(sortField);
        } else {
            wrapper.orderByDesc(sortField);
        }
        wrapper.last("limit " + params.get("limit") + " offset " + params.get("offset"));
        return list(wrapper);
    }

    @Override
    public boolean checkLoginDuplicate(String id, String login) {
        QueryWrapper<EmployeeEntity> wrapper = new QueryWrapper<>();
        wrapper.select("id");
        wrapper.ne("id", id);
        wrapper.eq("login", login);
        EmployeeEntity one = getOne(wrapper);
        return one != null;
    }

    @Transactional(
        rollbackFor = {Exception.class}
    )
    public int saveOrUpdateBatchByEId(List<EmployeeEntity> employees) {
        int count = 0;
        for (EmployeeEntity employee : employees) {
            try {
                count += saveOrUpdateCountNew(employee);
            } catch (Exception e) {
                if (e.getMessage().equals("Update failed")) {
                    return -1;
                } else {
                    return -2;
                }
            }
        }
        return count;
    }

    private int saveOrUpdateCountNew(EmployeeEntity employee) throws Exception {
        EmployeeEntity one = this.getById(employee.getId());
        if (one != null) {
            boolean update = updateById(employee);
            if (!update) {
                throw new Exception("Update failed");
            }
            return 0;
        } else {
            boolean save = save(employee);
            if (!save) {
                throw new Exception("Save failed");
            }
            return 1;
        }
    }

}
