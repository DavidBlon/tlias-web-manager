package com.wb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Integer id;
    private String title;
    private Integer createdBy;
    private Boolean read;
    private LocalDateTime createTime;
}
