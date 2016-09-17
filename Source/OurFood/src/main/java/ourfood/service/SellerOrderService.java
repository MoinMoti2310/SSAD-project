package ourfood.service;

import java.util.List;

import ourfood.domain.SellerOrder;
import ourfood.domain.User;

public interface SellerOrderService {

    SellerOrder get(Long id);

    void save(SellerOrder account);

    List<SellerOrder> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}