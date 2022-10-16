package com.xy.assignment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xy.assignment.entity.EmployeeEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:25 AM 10/16/2022
 * @ Description: employee data mapper
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */

@Mapper
public interface EmployeeDao extends BaseMapper<EmployeeEntity> {
}
