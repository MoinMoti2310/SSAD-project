package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Produce;
import ourfood.domain.Permissions;
import ourfood.domain.SellerOrder;
import ourfood.domain.SellerOrderItem;
import ourfood.domain.User;
import ourfood.domain.enums.SellerOrderItemStatus;
import ourfood.service.repositories.ProduceRepository;
import ourfood.service.repositories.SellerOrderRepository;

@Component
@Transactional
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    SellerOrderRepository sellerOrderRepo;

    @Autowired
    ProduceRepository cropRepo;

    @Override
    public SellerOrder get(Long id) {

        SellerOrder sellerOrder = sellerOrderRepo.findOne(id);
        return sellerOrder;
    }

    @Override
    public void save(SellerOrder sellerOrder) {

        if (sellerOrder.getId() != null) {

            SellerOrderItem orderItem = new SellerOrderItem();
            orderItem.setPrice(100L);
            orderItem.setQuantity(200L);
            orderItem.setSellerOrderItemStatus(SellerOrderItemStatus.ORDERED);

            Produce produce = cropRepo.findOne(1L);
            // orderItem.setCrop(crop);

            // sellerOrder.addOrderItem(orderItem);
        }

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