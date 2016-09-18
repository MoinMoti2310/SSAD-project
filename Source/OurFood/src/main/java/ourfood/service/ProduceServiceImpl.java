package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Produce;
import ourfood.domain.Permissions;
import ourfood.domain.User;
import ourfood.service.repositories.ProduceRepository;

@Component
@Transactional
public class ProduceServiceImpl implements ProduceService {

    @Autowired
    ProduceRepository produceRepo;

    @Override
    public Produce get(Long id) {

        Produce produce = produceRepo.findOne(id);
        return produce;
    }

    @Override
    public void save(Produce produce) {

        produceRepo.save(produce);
    }

    @Override
    public List<Produce> getAll() {
        return produceRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        Produce produce = produceRepo.findOne(id);

        if (produce != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                produceRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            Produce produce = produceRepo.findOne(id);

            if (produce != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    produceRepo.delete(id);
                }
            }
        }
    }
}