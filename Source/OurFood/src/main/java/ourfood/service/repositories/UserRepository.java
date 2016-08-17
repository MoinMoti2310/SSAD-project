package ourfood.service.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ourfood.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query("SELECT u FROM User u where u.primaryEmail = ?1 OR u.registeredMobile = ?1")
    User getUserByLogin(String login);

    User getUserByPrimaryEmail(String primaryEmail);

    User getUserByFacebookId(String facebookId);

    User getUserByGoogleId(String googleId);

    List<User> getByOrganizationId(Long organizationId);

    List<User> findByLastNameAndFirstName(String lastName, String firstName);

    int countByRegisteredMobile(String registeredMobile);

    User findByRegisteredMobile(String registeredMobile);

    User findByPrimaryEmail(String primaryEmail);

    List<User> getTop10ByLastNameAndFirstNameAndLastName(String lastName, String firstName, String lastName1);

    Page<User> findAll(Pageable pageable);

    @Query(value = "SELECT u.gcm_token FROM user u WHERE u.gcm_token IS NOT NULL AND u.registered_mobile IN (:tokens)", nativeQuery = true)
    Object[] getGcmTokensByRegisteredMobile(@Param("tokens") List<String> tokens);
}
