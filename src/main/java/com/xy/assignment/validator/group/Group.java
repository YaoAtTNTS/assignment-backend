package com.xy.assignment.validator.group;

import javax.validation.GroupSequence;

/**
 * @ Author: Xiong Yao
 * @ Date: Created at 11:31 PM 10/16/2022
 * @ Description:
 * @ Version: 1.0
 * @ Email: gongchen711@gmail.com
 */


@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}

