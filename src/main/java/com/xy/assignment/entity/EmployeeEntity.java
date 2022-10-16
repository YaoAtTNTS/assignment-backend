package com.xy.assignment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:07 AM 10/16/2022
 * @ Description: employee entity
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */

@Data
@TableName("employee")
public class EmployeeEntity implements Serializable {
    private static final long serialVersionID = 1L;

    @TableId
    @JsonProperty("aid")
    private Integer id;

    @JsonProperty("id")
    private String eid;

    private String login;

    private String name;

    private Double salary;

    public EmployeeEntity(String eid, String login, String name, Double salary) {
        this.eid = eid;
        this.login = login;
        this.name = name;
        this.salary = salary;
    }
}
