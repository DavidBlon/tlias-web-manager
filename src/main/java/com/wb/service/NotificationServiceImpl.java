package com.wb.service;

import com.wb.entity.Notification;
import com.wb.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public List<Notification> doGetAllByUser(Integer userId) {
        return notificationMapper.getAllWithReadStatus(userId);
    }

    @Override
    public void doCreate(Notification notification) {
        notification.setCreateTime(LocalDateTime.now());
        notificationMapper.insertNotification(notification);
    }

    @Override
    public void doMarkRead(Integer notificationId, Integer userId) {
        notificationMapper.markRead(notificationId, userId);
    }

    @Override
    public void doMarkAllRead(Integer userId) {
        notificationMapper.markAllRead(userId);
    }
}
