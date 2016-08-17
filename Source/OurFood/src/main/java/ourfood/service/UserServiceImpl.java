package ourfood.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.apache.mailet.MailAddress;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ourfood.domain.ContactVerificationLog;
import ourfood.domain.Permission;
import ourfood.domain.ResetToken;
import ourfood.domain.Role;
import ourfood.domain.RoleType;
import ourfood.domain.User;
import ourfood.domain.UserToken;
import ourfood.domain.enums.UserType;
import ourfood.exception.InvalidInputException;
import ourfood.extservice.sms.SmsParam;
import ourfood.extservice.sms.SmsService;
import ourfood.form.Register;
import ourfood.service.repositories.ContactVerificationLogRepository;
import ourfood.service.repositories.OrganizationRepository;
import ourfood.service.repositories.ResetTokenRepository;
import ourfood.service.repositories.RoleRepository;
import ourfood.service.repositories.UserRepository;
import ourfood.service.repositories.UserTokenRepository;
import ourfood.utils.CommonUtil;
import ourfood.utils.ProjectUtil;

@Component
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService,
        AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserTokenRepository tokenRepository;

    @Autowired
    ResetTokenRepository resetTokenRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    ContactVerificationLogRepository contactVerificationLogRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    TemplateEngine templateEngine;

    @PersistenceContext
    EntityManager entityManager;

    @Value("${platform.url}")
    String platformUrl;

    final String CONTACT_TYPE_MOBILE = "MOBILE";
    final String CONTACT_TYPE_EMAIL = "EMAIL";

    /**
     * Method to get the user by userid
     */
    @Override
    public User getUser(Long userId) {
        Assert.notNull(userId, "User id must not be null");

        return this.userRepository.findOne(userId);
    }

    /**
     * Method to get the user by userid
     */
    @Override
    public User getUser(String login) {
        Assert.notNull(login, "Login name must not be null");
        return this.userRepository.getUserByLogin(login);
    }

    @Override
    public Long register(Register register) {
        // Check if the user is unique
        boolean isUnique = isEmailUnique(register.getPrimaryEmail());
        Assert.isTrue(isUnique, "User E-mail ID is not unique");

        return register(register, false);
    }

    @Override
    public Long register(Register register, boolean isOrgAdmin) {

        User user = new User();
        user.setPrimaryEmail(register.getPrimaryEmail());
        user.setPassword(register.getPassword());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        if (register.getOrg() != null) {
            user.setOrganization(organizationRepository.findOne(register.getOrg()));
        }

        if (isOrgAdmin) {

            Role role = roleRepository.findByCode(RoleType.ROLE_ORG_PRI_ADMIN.toString());
            user.setRole(role);
        }

        return register(user);
    }

    /**
     * Method to register the user
     */
    @Override
    public Long register(User user) {

        MailAddress address = null;

        try {
            address = new MailAddress(user.getPrimaryEmail());
        } catch (AddressException e1) {
            throw new RuntimeException("Invalid email " + user.getPrimaryEmail());
        }

        try (Scanner scanner = new Scanner(new File("domain-blacklist.txt"));) {
            while (scanner.hasNextLine()) {
                if (address.getDomain().equalsIgnoreCase(scanner.nextLine().trim())) {
                    throw new RuntimeException("Email " + user.getPrimaryEmail() + " present in blacklist");
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

        try (Scanner scanner = new Scanner(new File("email-blacklist.txt"));) {
            while (scanner.hasNextLine()) {
                if (address.toString().equalsIgnoreCase(scanner.nextLine().trim())) {
                    throw new RuntimeException("Email " + user.getPrimaryEmail() + " present in blacklist");
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        // Generate random password for user registration
        String password = ProjectUtil.getRandomPassword();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        /**
         * Uncomment to implement, password to be set by user while registration.
         * 
         * String password = user.getPassword(); // TODO handle open id, remove the save method Assert.notNull(password,
         * "Password should not be null"); password = encoder.encode(password); user.setPassword(password);
         */

        if (!user.getIsVerified()) {
            ResetToken resetToken = createResetToken(user);
            // send validate mail
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("name", user.getUsername());
            ctx.setVariable("login", user.getPrimaryEmail());
            ctx.setVariable("password", password);
            ctx.setVariable("registrationDate", new Date());
            ctx.setVariable("resetToken", resetToken);
            ctx.setVariable("platformUrl", platformUrl);
            final String htmlContent = this.templateEngine.process("invitation", ctx);

            try {
                message.setFrom("no-reply@scanon.in");
                message.setTo(user.getPrimaryEmail());
                message.setSubject("Applite Platform - User Verification");
                message.setText(htmlContent, true);
                this.mailSender.send(mimeMessage);
                user.setIsVerified(true);
            } catch (MessagingException e) {
                throw new RuntimeException();
            }
        }

        user.setCreatedDate(new Date());

        // currently userType is hardcoded but should be changed.
        user.setUserType(UserType.WEB);

        // save the user and return the id
        this.userRepository.save(user);
        return user.getId();
    }

    /**
     * Invite user through e-mail
     */
    @Override
    public Long invite(Register register) {

        // Check if the user is unique
        boolean isUnique = isEmailUnique(register.getPrimaryEmail());
        Assert.isTrue(isUnique, "User E-mail ID is not unique");

        User user = new User();
        user.setPrimaryEmail(register.getPrimaryEmail());
        user.setPassword(register.getPassword());
        user.setFirstName(register.getFirstName());
        user.setLastName(register.getLastName());
        user.setUserType(register.getUserType());
        user.setRole(roleRepository.findByCode(register.getRole().toString()));

        if (register.getOrg() != null) {
            user.setOrganization(organizationRepository.findOne(register.getOrg()));
        }

        MailAddress address = null;

        try {
            address = new MailAddress(user.getPrimaryEmail());
        } catch (AddressException e1) {
            throw new RuntimeException("Invalid email " + user.getPrimaryEmail());
        }

        try (Scanner scanner = new Scanner(new File("domain-blacklist.txt"));) {
            while (scanner.hasNextLine()) {
                if (address.getDomain().equalsIgnoreCase(scanner.nextLine().trim())) {
                    throw new RuntimeException("Email " + user.getPrimaryEmail() + " present in blacklist");
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }

        try (Scanner scanner = new Scanner(new File("email-blacklist.txt"));) {
            while (scanner.hasNextLine()) {
                if (address.toString().equalsIgnoreCase(scanner.nextLine().trim())) {
                    throw new RuntimeException("Email " + user.getPrimaryEmail() + " present in blacklist");
                }
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        // Generate random password for user registration
        String password = ProjectUtil.getRandomPassword();
        String encodedPassword = encoder.encode(password);
        user.setPassword(encodedPassword);

        if (!user.getIsVerified()) {
            ResetToken resetToken = createResetToken(user);
            // send validate mail
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            final Context ctx = new Context(LocaleContextHolder.getLocale());
            ctx.setVariable("name", user.getUsername());
            ctx.setVariable("login", user.getPrimaryEmail());
            ctx.setVariable("password", password);
            ctx.setVariable("registrationDate", new Date());
            ctx.setVariable("resetToken", resetToken);
            ctx.setVariable("platformUrl", platformUrl);
            final String htmlContent = this.templateEngine.process("invitation", ctx);

            try {
                message.setFrom("no-reply@scanon.in");
                message.setTo(user.getPrimaryEmail());
                message.setSubject("Applite Platform - User Verification");
                message.setText(htmlContent, true);
                this.mailSender.send(mimeMessage);
                // user.setIsVerified(true);
            } catch (MessagingException e) {
                throw new RuntimeException();
            }
        }

        user.setCreatedDate(new Date());

        // save the user and return the id
        this.userRepository.save(user);
        return user.getId();
    }

    @Override
    public UserToken createToken(User user) {
        // TODO assert if the application can create a token in the first place
        Assert.notNull(user, "User cannot be empty");
        UserToken token = new UserToken();
        token.setToken(encoder.encode(System.currentTimeMillis() + ""));
        token.setUser(user);
        token.setValidTru(new Date(System.currentTimeMillis() + 60 * 60 * 1000));
        // TODO set authorities here
        // TODO check which permissions are assigned to the application
        tokenRepository.save(token);
        return token;
    }

    @Override
    public UserToken getUserTokenByToken(String token) {
        return tokenRepository.getUserTokenByToken(token);
    }

    @Override
    public ResetToken createResetToken(User user) {

        String token = "";
        token += user.getId();
        token += System.currentTimeMillis();
        token = new Md5PasswordEncoder().encodePassword(token, null);
        ResetToken resetToken = new ResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setValidTru(new Date(System.currentTimeMillis() + 3600 * 24 * 100));
        this.resetTokenRepository.save(resetToken);
        return resetToken;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {

        User user = this.userRepository.getUserByLogin(login);

        if (user == null) {
            throw new UsernameNotFoundException(login + " not found");
        }

        /*
         * UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
         * user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
         */
        new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));

        return user;
    }

    @Override
    public void validateUser(User user, String token) {

        ResetToken resetToken = this.resetTokenRepository.getResetTokenByToken(token);
        Assert.notNull(user);
        Assert.notNull(resetToken);
        Assert.isTrue(resetToken.getUserId().equals(user.getId()));
        user.setIsVerified(true);
        user.setIsActive(true);
        this.userRepository.save(user);
        this.resetTokenRepository.delete(resetToken);
    }

    @Override
    public void resetPassword(User user) {
        Assert.notNull(user);

        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ResetToken resetToken = createResetToken(user);
        ctx.setVariable("name", user.getUsername());
        ctx.setVariable("login", user.getUsername());
        ctx.setVariable("resetToken", resetToken);
        ctx.setVariable("platformUrl", platformUrl);
        final String htmlContent = this.templateEngine.process("resetPassword", ctx);

        try {
            message.setFrom("no-reply@scanon.in");
            message.setTo(user.getPrimaryEmail());
            message.setSubject("Reset Password");
            message.setText(htmlContent, true);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void setPassword(User user, String token, String password) {

        ResetToken resetToken = this.resetTokenRepository.getResetTokenByToken(token);
        Assert.notNull(user);
        Assert.isTrue(resetToken.getUserId().equals(user.getId()));
        Assert.isTrue(new Date().before(resetToken.getValidTru()));
        user.setPassword(this.encoder.encode(password));
        this.userRepository.save(user);
    }

    @Override
    public void changePassword(User user, String currentPassword, String password) throws InvalidInputException {

        Assert.notNull(user);

        if (!this.encoder.matches(currentPassword, user.getPassword())) {
            throw new InvalidInputException("The current password is not correct, please try again.");
        }
        user.setPassword(this.encoder.encode(password));
        this.userRepository.save(user);

    }

    @Override
    public User login(String login, String password) {
        User user = (User) loadUserByUsername(login);
        if (encoder.matches(password, user.getPassword())) {
            return user;
        }
        // TODO sort out this exception
        throw new RuntimeException();
    }

    @Override
    public boolean isEmailUnique(String primaryEmail) {
        User user = this.userRepository.getUserByPrimaryEmail(primaryEmail);
        if (user == null) {
            return true;
        }
        return false;
    }

    @Override
    public User getUserByFacebokId(String facebookId) {
        return userRepository.getUserByFacebookId(facebookId);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public void save(User user, boolean fromWeb) throws Exception {

        User user1 = userRepository.findOne(user.getId());

        // Mandatory fields cannot be updated to blank
        if (!user.getFirstName().isEmpty()) {
            user1.setFirstName(user.getFirstName());
        }

        if (!user.getLastName().isEmpty()) {
            user1.setLastName(user.getLastName());
        }

        if (!user.getPrimaryEmail().isEmpty()) {
            user1.setPrimaryEmail(user.getPrimaryEmail());
        }

        user1.setRegisteredEmail(user.getRegisteredEmail());
        user1.setRegisteredMobile(user.getRegisteredMobile());

        user1.setUserType(user.getUserType());
        user1.setOrganization(user.getOrganization());
        user1.setAuthorizedActionSetIds(user.getAuthorizedActionSetIds());

        // Supporting single role to user
        if (user.getRoles() != null) {

            Role role = user.getRoles().get(0);
            if (role != null && role.getCode() != null && !role.getCode().isEmpty()) {
                Role role1 = roleRepository.findByCode(role.getCode());

                if (role1 != null) {
                    user1.setOnlyRole(role1);
                }
            } else {
                Role role1 = roleRepository.findByCode(RoleType.ROLE_MOBILE_USER.toString());
                user.setOnlyRole(role1);
            }
        } else {
            Role role1 = roleRepository.findByCode(RoleType.ROLE_MOBILE_USER.toString());
            user.setOnlyRole(role1);
        }

        try {
            userRepository.save(user1);
            entityManager.flush();
        } catch (PersistenceException pex) {
            ConstraintViolationException cve = (ConstraintViolationException) pex.getCause();
            throw cve;
        }
    }

    @Override
    public User getUserByPrimaryEmail(String email) {
        if (email == null)
            return null;
        return userRepository.getUserByPrimaryEmail(email);
    }

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken token) throws UsernameNotFoundException {

        Assert.notNull(token);
        Assert.notNull(token.getCredentials());

        if (token.isAuthenticated()) {

            if (token.getCredentials().equals("OAuth")) {
                return (User) token.getPrincipal();
            }

            if (token.getCredentials().equals(RoleType.ROLE_PLATFORM_POWER_ADMIN.toString())) {
                return (User) token.getPrincipal();
            }
        }

        throw new UsernameNotFoundException("Pre authentication failed");
    }

    @Override
    public User getUserByGoogleId(String googleId) {
        return userRepository.getUserByGoogleId(googleId);
    }

    @Override
    public void activate(Long id) {
        User user = userRepository.findOne(id);
        user.setIsActive(true);
    }

    @Override
    public void deactivate(Long id) {
        User user = userRepository.findOne(id);
        user.setIsActive(true);
    }

    @Override
    public List<User> getAllUsers() {
        Iterator<User> users = userRepository.findAll().iterator();
        List<User> ret = new ArrayList<User>();
        while (users.hasNext()) {
            ret.add(users.next());
        }
        return ret;
    }

    @Override
    public void updateRoles(Long userId, List<Role> roles) {

        User user = userRepository.findOne(userId);
        user.setRoles(roles);
    }

    @Override
    public void sendFeedback(String title, String feedback, String email) {
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        ctx.setVariable("title", title);
        ctx.setVariable("feedback", feedback);
        ctx.setVariable("email", email);
        final String htmlContent = this.templateEngine.process("feedback", ctx);
        try {
            message.setFrom("no-reply@scanon.in");
            message.setTo("scanon.in@gmail.com");
            message.setSubject("Feedback");
            message.setText(htmlContent, true);
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void updateGcmToken(Long userId, String gcmToken) {

        Assert.notNull(userId, "User ID cannot be null.");
        Assert.notNull(gcmToken, "GCM Token cannot be null.");
        User user = this.userRepository.findOne(userId);

        user.setGcmToken(gcmToken);
        this.userRepository.save(user);
    }

    @Override
    public void requestOtp(String mobileNo, Long userId) throws Exception {

        // Validate mobile no. uniqueness
        /*
         * int count = userRepository.countByRegisteredMobile(mobileNo); if (count > 0) { throw new
         * Exception("ERROR:E1100-MOBILE-NO-IN-USE"); }
         */

        // Generate OTP
        int otp = CommonUtil.randInt(100000, 999999);

        // Send OTP to the mobileNo through SMS gateway
        SmsService smsService = new SmsService();
        SmsParam param = new SmsParam();
        param.setRegistrationIdsStr(mobileNo);

        String otpMessage = "Dear Applite User, One Time Password (OTP) to verify your mobile no. is " + otp + ".";
        param.setData(otpMessage);
        smsService.postMessage(param);

        // Log the OTP record in ContactVerificationLog for verification
        ContactVerificationLog contactLog = new ContactVerificationLog();
        contactLog.setContactInfo(mobileNo);
        contactLog.setContactType(CONTACT_TYPE_MOBILE);
        contactLog.setOtp(String.valueOf(otp));
        contactLog.setUserId(userId);
        contactLog.setTimestamp(new Date());

        contactVerificationLogRepository.save(contactLog);
    }

    @Override
    public int verifyMobileNumberWithOtp(final String mobileNo, final String otp, final Long userId) {

        return verifyMobileNumberWithOtpWithDetails(mobileNo, otp, userId, null);
    }

    @Override
    public int verifyMobileNumberWithOtpWithDetails(final String mobileNo, final String otp, final Long userId,
            final String name) {

        // Verify mobileNo & OTP from ContactVerificationLog
        Date oneHrPastDate = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1));

        int count = contactVerificationLogRepository
                .countByContactInfoAndContactTypeAndOtpAndUserIdAndTimestampGreaterThan(mobileNo, CONTACT_TYPE_MOBILE,
                        otp, userId, oneHrPastDate);
        boolean isValidOtp = (count > 0);

        if (isValidOtp) {

            // Check if any other user exists with the same mobile no.
            // Invalidate the registered mobile no.
            // Reset the GCM token for the existing user
            User requestedUser = userRepository.findOne(userId);
            User userWithPrimaryEmail = userRepository.findByPrimaryEmail(mobileNo);

            if (userWithPrimaryEmail != null) {
                String tempMobileNo = mobileNo + "-" + CommonUtil.randInt(100000, 999999);
                userWithPrimaryEmail.setPrimaryEmail(tempMobileNo);
                userWithPrimaryEmail.setRegisteredMobile(tempMobileNo);
                userRepository.save(userWithPrimaryEmail);
                entityManager.flush();
            }

            User registeredUser = userRepository.findByRegisteredMobile(mobileNo);

            if (registeredUser != null) {
                String tempMobileNo = mobileNo + "-" + CommonUtil.randInt(100000, 999999);
                registeredUser.setRegisteredMobile(tempMobileNo);
                userRepository.save(registeredUser);
                entityManager.flush();
            }

            if (name != null && !name.isEmpty()) {
                String[] names = ProjectUtil.splitName(name);
                requestedUser.setFirstName(names[0]);
                requestedUser.setLastName(names[1]);
            }

            requestedUser.setPrimaryEmail(mobileNo);
            requestedUser.setRegisteredMobile(mobileNo);
            requestedUser.setCreatedDate(new Date());
            userRepository.save(requestedUser);
        }

        return isValidOtp ? 0 : 1;
    }

    @Override
    public int syncAppId(final Long userId) {

        User user = userRepository.findOne(userId);

        if (user.getRegisteredMobile() != null && !user.getRegisteredMobile().isEmpty()) {
            user.setPrimaryEmail(user.getRegisteredMobile());
            return 0;
        }

        return 1;
    }

    @Override
    public void resetMobileNo(String mobileNo, Long userId) {

        // Reset mobile no. for there user
        User user = userRepository.findOne(userId);
        user.setRegisteredMobile(mobileNo);
    }

    public Role getRole(RoleType role) {

        return roleRepository.findByCode(RoleType.ROLE_END_USER.toString());
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> permissions = new ArrayList<String>();
        List<Permission> collection = new ArrayList<Permission>();
        for (Role role : roles) {
            collection.addAll(role.getPermissions());
        }
        for (Permission item : collection) {
            permissions.add(item.getName());
        }
        return permissions;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> permissions) {

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String privilege : permissions) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }
}