package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.BuyerAccount;
import ourfood.domain.Permissions;
import ourfood.domain.User;
import ourfood.service.repositories.BuyerAccountRepository;

@Component
@Transactional
public class BuyerAccountServiceImpl implements BuyerAccountService {

    @Autowired
    BuyerAccountRepository accountRepo;

    @Override
    public BuyerAccount get(Long id) {

        BuyerAccount account = accountRepo.findOne(id);
        return account;
    }

    @Override
    public void save(BuyerAccount account) {

        accountRepo.save(account);
    }

    @Override
    public List<BuyerAccount> getAll() {
        return accountRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        BuyerAccount account = accountRepo.findOne(id);

        if (account != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                accountRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            BuyerAccount account = accountRepo.findOne(id);

            if (account != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    accountRepo.delete(id);
                }
            }
        }
    }
}