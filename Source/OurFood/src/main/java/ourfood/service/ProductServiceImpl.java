package ourfood.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ourfood.domain.Permissions;
import ourfood.domain.Product;
import ourfood.domain.User;
import ourfood.service.repositories.ProductRepository;

import java.util.List;

/**
 * Created by moinhussain.moti on 01-10-2016.
 */

@Component
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepo;

    @Override
    public Product get(Long id) {

        Product product = productRepo.findOne(id);
        return product;
    }

    @Override
    public void save(Product product) {
        productRepo.save(product);
    }

    @Override
    public List<Product> getAll() { return productRepo.findAll(); }

    @Override
    public void delete(Long id, User user) {
        Product product = productRepo.findOne(id);

        if(product != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                productRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            Product product = productRepo.findOne(id);

            if(product != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    productRepo.delete(id);
                }
            }
        }
    }
}
