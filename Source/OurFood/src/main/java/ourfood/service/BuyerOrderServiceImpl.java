package ourfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ourfood.domain.BuyerOrder;
import ourfood.domain.BuyerOrderItem;
import ourfood.domain.Permissions;
import ourfood.domain.User;
import ourfood.domain.enums.BuyerOrderItemStatus;
import ourfood.domain.enums.BuyerOrderStatus;
import ourfood.domain.enums.PaymentStatus;
import ourfood.service.repositories.BuyerOrderItemRepository;
import ourfood.service.repositories.BuyerOrderRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Component
@Transactional
public class BuyerOrderServiceImpl implements BuyerOrderService {

    @Autowired
    BuyerOrderRepository buyerOrderRepo;

    @Autowired
    BuyerOrderItemRepository buyerOrderItemRepo;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void create(BuyerOrder order, User user) {

        try {

            order.setStatus(BuyerOrderStatus.ORDERED);
            order.setPaymentStatus(PaymentStatus.NOT_PAID);

            BuyerOrderItem buyerOrderItem = order.getOrderItems().get(0);
            buyerOrderItem.setOrder(order);
            buyerOrderItem.setStatus(BuyerOrderItemStatus.ORDERED);

            buyerOrderRepo.save(order);
            entityManager.flush();

        } catch (Exception ex) {
            String exMessage = ex.getMessage();
        }
    }

    @Override
    public void update(BuyerOrder buyerOrder, User user) {

        BuyerOrder orderRecord = buyerOrderRepo.findOne(buyerOrder.getId());

        orderRecord.setName(buyerOrder.getName());
        orderRecord.setDescription(buyerOrder.getDescription());
        orderRecord.setPinCode(buyerOrder.getPinCode());
        orderRecord.setStatus(buyerOrder.getStatus());
        orderRecord.setPaymentStatus(buyerOrder.getPaymentStatus());

        BuyerOrderItem buyerOrderItem = buyerOrder.getOrderItems().get(0);

        BuyerOrderItem orderItemRecord = orderRecord.getOrderItems().get(0);
        orderItemRecord.setProduceGrade(buyerOrderItem.getProduceGrade());
        orderItemRecord.setStatus(buyerOrderItem.getStatus());
        orderItemRecord.setQuantity(buyerOrderItem.getQuantity());
        orderItemRecord.setPrice(buyerOrderItem.getPrice());

        buyerOrderRepo.save(orderRecord);
    }

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