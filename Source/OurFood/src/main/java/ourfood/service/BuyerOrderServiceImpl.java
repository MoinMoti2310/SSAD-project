package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.BuyerOrder;
import ourfood.domain.Permissions;
import ourfood.domain.User;
import ourfood.service.repositories.BuyerOrderRepository;

@Component
@Transactional
public class BuyerOrderServiceImpl implements BuyerOrderService {

    @Autowired
    BuyerOrderRepository buyerOrderRepo;

    @Override
    public BuyerOrder get(Long id) {

        BuyerOrder buyerOrder = buyerOrderRepo.findOne(id);
        return buyerOrder;
    }

    @Override
    public void save(BuyerOrder buyerOrder) {

        buyerOrderRepo.save(buyerOrder);
    }

    @Override
    public List<BuyerOrder> getAll() {
        return buyerOrderRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        BuyerOrder buyerOrder = buyerOrderRepo.findOne(id);

        if (buyerOrder != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                buyerOrderRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            BuyerOrder buyerOrder = buyerOrderRepo.findOne(id);

            if (buyerOrder != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    buyerOrderRepo.delete(id);
                }
            }
        }
    }
}