package com.wb.config;

import com.wb.entity.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Result handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.warn("数据完整性违反异常: {}", e.getMessage());
        return Result.error("该部门下存在员工，无法删除");
    }
}
