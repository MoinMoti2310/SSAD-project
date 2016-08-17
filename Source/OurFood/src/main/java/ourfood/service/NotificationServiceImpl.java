package ourfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ourfood.domain.Notification;
import ourfood.domain.User;
import ourfood.service.repositories.NotificationRepository;
import ourfood.service.repositories.UserRepository;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public void notifyUser(Notification notification, Long userId) {
        User user = userRepository.findOne(userId);
        notification.setUser(user);
        notificationRepository.save(notification);

    }

    @Override
    public Page<Notification> getNotifications(Long userId) {
        User user = userRepository.findOne(userId);
        Pageable pageable = new PageRequest(0, 5);
        return notificationRepository.getByUserAndIsReadFalse(user, pageable);
    }

}
