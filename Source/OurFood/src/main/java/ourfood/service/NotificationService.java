package ourfood.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ourfood.domain.Notification;

public interface NotificationService {
    public void notifyUser(Notification notification, Long userId);

    Page<Notification> getNotifications(Long userId);
}