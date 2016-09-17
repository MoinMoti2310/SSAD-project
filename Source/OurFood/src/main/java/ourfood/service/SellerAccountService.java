package ourfood.service;

import java.util.List;

import ourfood.domain.SellerAccount;
import ourfood.domain.User;

public interface SellerAccountService {

    SellerAccount get(Long id);

    void save(SellerAccount account);

    List<SellerAccount> getAll();

    void delete(Long id, User user);

    void delete(Long[] ids, User user);
}