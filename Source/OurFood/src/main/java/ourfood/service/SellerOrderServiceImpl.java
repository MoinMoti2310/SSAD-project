package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Permissions;
import ourfood.domain.SellerOrder;
import ourfood.domain.SellerOrderItem;
import ourfood.domain.User;
import ourfood.domain.enums.PaymentStatus;
import ourfood.domain.enums.SellerOrderItemStatus;
import ourfood.domain.enums.SellerOrderStatus;
import ourfood.service.repositories.ProduceRepository;
import ourfood.service.repositories.SellerOrderItemRepository;
import ourfood.service.repositories.SellerOrderRepository;

@Component
@Transactional
public class SellerOrderServiceImpl implements SellerOrderService {

    @Autowired
    SellerOrderRepository sellerOrderRepo;

    @Autowired
    SellerOrderItemRepository sellerOrderItemRepo;

    @Autowired
    ProduceRepository cropRepo;

    @Override
    public SellerOrder get(Long id) {

        SellerOrder sellerOrder = sellerOrderRepo.findOne(id);
        return sellerOrder;
    }

    @Override
    public void create(SellerOrder order, User user) {

        order.setStatus(SellerOrderStatus.ORDERED);
        order.setPaymentStatus(PaymentStatus.NOT_PAID);

        SellerOrderItem sellerOrderItem = order.getOrderItems().get(0);
        sellerOrderItem.setOrder(order);
        sellerOrderItem.setStatus(SellerOrderItemStatus.ORDERED);

        sellerOrderRepo.save(order);
    }

    @Override
    public void update(SellerOrder sellerOrder, User user) {

        // Update required fields
        SellerOrder orderRecord = sellerOrderRepo.findOne(sellerOrder.getId());

        orderRecord.setName(sellerOrder.getName());
        orderRecord.setDescription(sellerOrder.getDescription());
        orderRecord.setPinCode(sellerOrder.getPinCode());
        orderRecord.setStatus(sellerOrder.getStatus());
        orderRecord.setPaymentStatus(sellerOrder.getPaymentStatus());
        orderRecord.setStatus(orderRecord.getStatus());

        SellerOrderItem orderItemRecord = orderRecord.getOrderItems().get(0);
        orderItemRecord.setQuantity(sellerOrder.getOrderItems().get(0).getQuantity());
        orderItemRecord.setStatus(sellerOrder.getOrderItems().get(0).getStatus());

        sellerOrderRepo.save(orderRecord);
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