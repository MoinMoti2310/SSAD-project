package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.InventoryProduct;

public interface InventoryProductRepository extends CrudRepository<InventoryProduct, Long> {

    List<InventoryProduct> findAll();
}
