package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.SellerOrderItem;

public interface SellerOrderItemRepository extends CrudRepository<SellerOrderItem, Long> {

    List<SellerOrderItem> findAll();
}
