package ourfood.service;

import java.util.List;

import ourfood.domain.Crop;
import ourfood.domain.User;

public interface CropService {

    Crop get(Long id);

    void save(Crop crop);

    List<Crop> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}