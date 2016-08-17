package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.ResetToken;

public interface ResetTokenRepository extends CrudRepository<ResetToken, Long> {
    ResetToken getResetTokenByToken(String token);
}
