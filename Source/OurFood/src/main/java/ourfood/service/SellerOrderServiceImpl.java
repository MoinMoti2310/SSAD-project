package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Permissions;
import ourfood.domain.SellerOrder;
import ourfood.domain.User;
import ourfood.service.repositories.SellerOrderRepository;

@Component
@Transactional
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    SellerOrderRepository sellerOrderRepo;

    @Override
    public SellerOrder get(Long id) {

        SellerOrder sellerOrder = sellerOrderRepo.findOne(id);
        return sellerOrder;
    }

    @Override
    public void save(SellerOrder sellerOrder) {

        sellerOrderRepo.save(sellerOrder);
    }

    @Override
    public List<SellerOrder> getAll() {
        return sellerOrderRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        SellerOrder sellerOrder = sellerOrderRepo.findOne(id);

        if (sellerOrder != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                sellerOrderRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            SellerOrder sellerOrder = sellerOrderRepo.findOne(id);

            if (sellerOrder != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    sellerOrderRepo.delete(id);
                }
            }
        }
    }
}