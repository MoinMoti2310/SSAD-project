package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.BuyerOrderItem;

public interface BuyerOrderItemRepository extends CrudRepository<BuyerOrderItem, Long> {

    List<BuyerOrderItem> findAll();
}
