package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.UserToken;

public interface UserTokenRepository extends CrudRepository<UserToken, Long> {

    UserToken getUserTokenByToken(String token);
}
