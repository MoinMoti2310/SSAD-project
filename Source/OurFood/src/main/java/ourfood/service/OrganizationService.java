package ourfood.service;

import java.util.List;

import ourfood.domain.Organization;
import ourfood.domain.User;

public interface OrganizationService {
    Organization getOrganization(Long organizationId);

    Long save(Organization organization);

    void deactivate(Long organizationId);

    void activate(Long organizationId);

    List<User> getUsers(Long organizationId);

    List<Organization> listOrganizations();

    List<Organization> listPendingOrganizations();

    void approve(Long organizationId);

    void reject(Long organizationId);

    void addUser(Long organizationId, String primaryEmail);
}