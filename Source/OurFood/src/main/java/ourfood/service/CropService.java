package ourfood.service;

import java.util.List;

import ourfood.domain.Crop;

public interface CropService {

    Crop getCrop(int id);

    void saveCrop(Crop crop);

    List<Crop> getCrops();
}