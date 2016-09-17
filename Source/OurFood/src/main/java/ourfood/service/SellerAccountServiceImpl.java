package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ourfood.domain.Permissions;
import ourfood.domain.SellerAccount;
import ourfood.domain.User;
import ourfood.service.repositories.SellerAccountRepository;

@Component
@Transactional
public class SellerAccountServiceImpl implements SellerAccountService {

    @Autowired
    SellerAccountRepository accountRepo;

    @Override
    public SellerAccount get(Long id) {

        SellerAccount account = accountRepo.findOne(id);
        return account;
    }

    @Override
    public void save(SellerAccount account) {

        accountRepo.save(account);
    }

    @Override
    public List<SellerAccount> getAll() {
        return accountRepo.findAll();
    }

    @Override
    public void delete(Long id, User user) {
        SellerAccount account = accountRepo.findOne(id);

        if (account != null) {
            if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                accountRepo.delete(id);
            }
        }
    }

    @Override
    public void delete(Long[] ids, User user) {

        for (Long id : ids) {
            SellerAccount account = accountRepo.findOne(id);

            if (account != null) {
                if (user.hasRole(Permissions.PERM_PLATFORM_UPDATE)) {
                    accountRepo.delete(id);
                }
            }
        }
    }
}