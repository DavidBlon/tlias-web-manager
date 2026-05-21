package com.wb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Integer age;
    private Integer deptId;
    private String status;
    private String deptName;
}
