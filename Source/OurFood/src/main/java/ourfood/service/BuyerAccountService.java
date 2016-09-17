package ourfood.service;

import java.util.List;

import ourfood.domain.BuyerAccount;
import ourfood.domain.User;

public interface BuyerAccountService {

    BuyerAccount get(Long id);

    void save(BuyerAccount account);

    List<BuyerAccount> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}