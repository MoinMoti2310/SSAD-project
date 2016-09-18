package ourfood.service;

import java.util.List;

import ourfood.domain.SellerOrder;
import ourfood.domain.User;

public interface SellerOrderService {

    SellerOrder get(Long id);    

    List<SellerOrder> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);

    void create(SellerOrder sellerOrder, User user);
    
    void update(SellerOrder sellerOrder, User user);
}