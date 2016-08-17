package ourfood.service;

import java.util.List;

import ourfood.domain.ResetToken;
import ourfood.domain.Role;
import ourfood.domain.RoleType;
import ourfood.domain.User;
import ourfood.domain.UserToken;
import ourfood.exception.InvalidInputException;
import ourfood.form.Register;

public interface UserService {

    User getUser(Long id);

    User getUser(String login);

    Long register(User user);

    Long register(Register register);

    UserToken createToken(User user);

    UserToken getUserTokenByToken(String token);

    void resetPassword(User user);

    void validateUser(User user, String resetToken);

    void setPassword(User user, String resetToken, String password);

    void changePassword(User user, String currentPassword, String password) throws InvalidInputException;

    ResetToken createResetToken(User user);

    User login(String login, String password);

    boolean isEmailUnique(String primaryEmail);

    User getUserByFacebokId(String facebookId);

    void save(User user);

    void activate(Long id);

    void deactivate(Long id);

    User getUserByPrimaryEmail(String email);

    User getUserByGoogleId(String googleId);

    List<User> getAllUsers();

    Long register(Register register, boolean b);

    void sendFeedback(String title, String message, String email);

    void updateGcmToken(Long userId, String gcmToken);

    Long invite(Register register);

    Role getRole(RoleType role);

    void requestOtp(String mobileNo, Long userId) throws Exception;

    int verifyMobileNumberWithOtp(String mobileNo, String otp, Long userId);

    int verifyMobileNumberWithOtpWithDetails(String mobileNo, String otp, Long userId, String name);

    void resetMobileNo(String mobileNo, Long userId);

    int syncAppId(Long userId);

    void save(User user, boolean fromWeb) throws Exception;

    void updateRoles(Long userId, List<Role> roles);
}