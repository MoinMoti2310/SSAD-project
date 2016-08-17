package ourfood.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import ourfood.domain.Organization;
import ourfood.domain.User;
import ourfood.service.repositories.OrganizationRepository;
import ourfood.service.repositories.UserRepository;

@Component
@Transactional
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Organization getOrganization(Long organizationId) {
        return organizationRepository.findOne(organizationId);
    }

    // FIXME update the parent children
    @Override
    public Long save(Organization organization) {
        organizationRepository.save(organization);
        return organization.getId();

    }

    // TODO propagate this to child organizations
    @Override
    public void deactivate(Long organizationId) {
        Organization organization = organizationRepository.findOne(organizationId);
        organization.setIsActive(false);
    }

    @Override
    public void activate(Long organizationId) {
        Organization organization = organizationRepository.findOne(organizationId);
        organization.setIsActive(true);
    }

    @Override
    public void approve(Long organizationId) {
        Organization organization = organizationRepository.findOne(organizationId);
        organization.setIsActive(true);
        organization.setIsPending(false);
    }

    @Override
    public List<User> getUsers(Long organizationId) {
        return userRepository.getByOrganizationId(organizationId);
    }

    @Override
    public List<Organization> listOrganizations() {
        return organizationRepository.findByIsPendingFalse();
    }

    @Override
    public List<Organization> listPendingOrganizations() {
        return organizationRepository.findByIsPendingTrue();
    }

    @Override
    public void reject(Long organizationId) {
        Organization organization = organizationRepository.findOne(organizationId);
        organization.setIsActive(false);
        organization.setIsPending(false);
    }

    @Override
    public void addUser(Long organizationId, String primaryEmail) {
        Organization organization = organizationRepository.findOne(organizationId);
        Assert.notNull(organization);
        User user = userRepository.getUserByPrimaryEmail(primaryEmail);
        Assert.notNull(user);
        user.setOrganization(organization);

    }
}