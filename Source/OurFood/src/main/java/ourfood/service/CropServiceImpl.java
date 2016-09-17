package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Crop;
import ourfood.domain.Permissions;
import ourfood.domain.User;
import ourfood.service.repositories.CropRepository;

@Component
@Transactional
public class CropServiceImpl implements CropService {

    @Autowired
    CropRepository cropRepo;

    @Override
    public Crop get(Long id) {

        Crop crop = cropRepo.findOne(id);
        return crop;
    }

    @Override
    public void save(Crop crop) {

        cropRepo.save(crop);
    }

    @Override
    public List<Crop> getAll() {
        return cropRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        Crop crop = cropRepo.findOne(id);

        if (crop != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                cropRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            Crop crop = cropRepo.findOne(id);

            if (crop != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    cropRepo.delete(id);
                }
            }
        }
    }
}