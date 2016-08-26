package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Crop;
import ourfood.service.repositories.CropRepository;

@Component
@Transactional
public class CropServiceImpl implements CropService {

    @Autowired
    CropRepository cropRepo;

    @Override
    public Crop getCrop(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void saveCrop(Crop crop) {

        cropRepo.save(crop);
    }

    @Override
    public List<Crop> getCrops() {
        // TODO Auto-generated method stub
        return null;
    }

}