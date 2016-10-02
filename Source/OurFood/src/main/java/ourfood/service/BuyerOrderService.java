package ourfood.service;

import java.util.List;

import ourfood.domain.BuyerOrder;
import ourfood.domain.User;

public interface BuyerOrderService {

    void create(BuyerOrder order, User user);

    void update(BuyerOrder order, User user);

    BuyerOrder get(Long id);

    void save(BuyerOrder account);

    List<BuyerOrder> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}