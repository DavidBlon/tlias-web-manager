package com.wb;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {

    //定义日志记录对象
    private final Logger log = LoggerFactory.getLogger(LogTest.class);

    @Test
    public void testLog(){
        int sum = 0;
        log.info("开始计算......");
        try {
            int[] nums = {1,5,4,8,9,6,3,5,4,2,5,8,4};
            for (int i = 0; i < nums.length; i++) {
                sum += nums[i];
            }
        } catch (Exception e) {
            log.info("计算出错......");
            throw new RuntimeException(e);
        }
        log.info("计算结果为:" + sum);
    }
}
