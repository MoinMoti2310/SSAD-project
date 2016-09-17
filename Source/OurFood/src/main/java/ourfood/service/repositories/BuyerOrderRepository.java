package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.BuyerOrder;

public interface BuyerOrderRepository extends CrudRepository<BuyerOrder, Long> {

    List<BuyerOrder> findAll();
}
