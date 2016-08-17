package ourfood.service.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import ourfood.domain.Notification;
import ourfood.domain.User;

public interface NotificationRepository extends CrudRepository<Notification, Long> {
    Page<Notification> getByUserAndIsReadFalse(User user, Pageable pageable);
}
