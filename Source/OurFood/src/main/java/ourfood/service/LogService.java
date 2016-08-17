package ourfood.service;


public interface LogService {

    Long logAction(Long appliteId, Long actionId, Long userId, Long timestamp);
}