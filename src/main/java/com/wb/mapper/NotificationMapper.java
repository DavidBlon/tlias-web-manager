package com.wb.mapper;

import com.wb.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    List<Notification> getAllWithReadStatus(@Param("userId") Integer userId);

    int insertNotification(Notification notification);

    int markRead(@Param("notificationId") Integer notificationId, @Param("userId") Integer userId);

    int markAllRead(@Param("userId") Integer userId);
}
