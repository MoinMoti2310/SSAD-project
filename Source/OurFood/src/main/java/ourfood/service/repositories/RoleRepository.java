package ourfood.service.repositories;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByCode(String code);
}
