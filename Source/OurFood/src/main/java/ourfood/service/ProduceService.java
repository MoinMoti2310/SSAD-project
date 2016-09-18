package ourfood.service;

import java.util.List;

import ourfood.domain.Produce;
import ourfood.domain.User;

public interface ProduceService {

    Produce get(Long id);

    void save(Produce produce);

    List<Produce> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}