package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ourfood.domain.Organization;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    List<Organization> findByIsPendingTrue();

    List<Organization> findByIsPendingFalse();
}
