package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.SellerAccount;

public interface SellerAccountRepository extends CrudRepository<SellerAccount, Long> {

    List<SellerAccount> findAll();
}
