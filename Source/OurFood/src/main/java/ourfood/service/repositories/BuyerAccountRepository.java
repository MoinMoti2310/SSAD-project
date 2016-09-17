package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.BuyerAccount;

public interface BuyerAccountRepository extends CrudRepository<BuyerAccount, Long> {

    List<BuyerAccount> findAll();
}
