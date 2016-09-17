package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Permissions;
import ourfood.domain.ProcessingCenter;
import ourfood.domain.User;
import ourfood.service.repositories.ProcessingCenterRepository;

@Component
@Transactional
public class ProcessingCenterServiceImpl implements ProcessingCenterService {

    @Autowired
    ProcessingCenterRepository proCenRepo;

    @Override
    public ProcessingCenter get(Long id) {

        ProcessingCenter proCen = proCenRepo.findOne(id);
        return proCen;
    }

    @Override
    public void save(ProcessingCenter proCen) {

        proCenRepo.save(proCen);
    }

    @Override
    public List<ProcessingCenter> getAll() {
        return proCenRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        ProcessingCenter proCen = proCenRepo.findOne(id);

        if (proCen != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                proCenRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            ProcessingCenter proCen = proCenRepo.findOne(id);

            if (proCen != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    proCenRepo.delete(id);
                }
            }
        }
    }
}