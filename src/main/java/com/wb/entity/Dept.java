package com.wb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

//快速创建get,set方法
@Data
//快速设置传参构造器
@AllArgsConstructor
//快速设置无参构造器
@NoArgsConstructor
public class Dept {

    private Integer id;
    private String name;
    private LocalDateTime updateTime;

}
