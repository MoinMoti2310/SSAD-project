package ourfood.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class LogServiceImpl implements LogService {

    @Override
    public Long logAction(Long appliteId, Long actionId, Long userId, Long timestamp) {

        try {

        } catch (Exception ex) {
            // Log exception
        }

        return 0L;
    }
}