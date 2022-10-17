package com.xy.assignment.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.xy.assignment.validator.group.AddGroup;
import com.xy.assignment.validator.group.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:07 AM 10/16/2022
 * @ Description: employee entity
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("employee")
public class EmployeeEntity implements Serializable {
    private static final long serialVersionID = 1L;

    @TableId
    @NotBlank(message = "Id cannot be empty", groups = {AddGroup.class, UpdateGroup.class})
    private String id;

    @NotBlank(message = "Login cannot be empty", groups = {AddGroup.class})
    private String login;

    @NotBlank(message = "Name cannot be empty", groups = {AddGroup.class})
    private String name;

    @Positive(message = "Salary must be > 0.0", groups = {AddGroup.class})
    private Double salary;

}
