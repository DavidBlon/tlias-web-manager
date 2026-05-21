package com.wb.service;

import com.wb.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> doGetAllByUser(Integer userId);

    void doCreate(Notification notification);

    void doMarkRead(Integer notificationId, Integer userId);

    void doMarkAllRead(Integer userId);
}
