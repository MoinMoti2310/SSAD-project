package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.SellerOrder;

public interface SellerOrderRepository extends CrudRepository<SellerOrder, Long> {

    List<SellerOrder> findAll();
}
