package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;
import ourfood.domain.Product;

import java.util.List;

/**
 * Created by moinhussain.moti on 01-10-2016.
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAll();
}
