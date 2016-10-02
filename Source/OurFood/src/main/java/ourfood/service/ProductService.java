package ourfood.service;

import ourfood.domain.Product;
import ourfood.domain.User;

import java.util.List;

/**
 * Created by moinhussian.moti on 01-10-2016.
 */
public interface ProductService {

    Product get(Long id);

    void save (Product product);

    List<Product> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}
