package ourfood.service;

import java.util.List;

import ourfood.domain.ProcessingCenter;
import ourfood.domain.User;

public interface ProcessingCenterService {

    ProcessingCenter get(Long id);

    void save(ProcessingCenter account);

    List<ProcessingCenter> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}