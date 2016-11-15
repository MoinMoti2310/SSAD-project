package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.InventoryProduce;

public interface InventoryProduceRepository extends CrudRepository<InventoryProduce, Long> {

    List<InventoryProduce> findAll();
}
