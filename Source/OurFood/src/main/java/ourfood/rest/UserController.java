package ourfood.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ourfood.domain.User;
import ourfood.domain.UserToken;
import ourfood.service.UserService;

/**
 * This class is responsible for the user modification actions like - register a new user - create a new token for the
 * user - add email - add phone - verify user
 * 
 \* @author raghu.mulukoju
 * 
 */
@RequestMapping(value = "/rest/user")
@RestController("restUserController")
public class UserController {
    @Autowired
    UserService userService;

    /**
     * Method to validate the user name and password and generate a unique token to be used with with the application
     * 
     * @param userName
     * @param password
     * @return
     */
    @RequestMapping(value = "/token", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional(rollbackFor = Exception.class)
    public UserToken createToken(@RequestParam String login, @RequestParam String password) {
        User user = userService.login(login, password);
        // TODO get app id from the header and send
        return this.userService.createToken(user);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional(rollbackFor = Exception.class)
    public UserToken register(User user) {
        Long userId = userService.register(user);
        User createdUser = userService.getUser(userId);
        return this.userService.createToken(createdUser);
    }

    @RequestMapping(value = "/request-mobile-otp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("isAuthenticated()")
    public Object requestOtp(String mobileNo, Authentication authentication) {

        Object obj = authentication.getPrincipal();
        Long userId = 0L;

        try {

            if (obj instanceof UserToken) {
                UserToken userToken = (UserToken) obj;
                userId = userToken.getUser().getId();
            } else if (obj instanceof User) {
                User user = (User) obj;
                userId = user.getId();
            }

            this.userService.requestOtp(mobileNo, userId);

        } catch (Exception e) {
            // Return error code
            return e.getMessage();
        }

        return null;
    }

    /**
     * Verifies mobile OTP from user. Optionally saves user name as registered user.
     * 
     * @param mobileNo
     * @param otp
     * @param name
     * @param authentication
     * @return
     */
    @RequestMapping(value = "/verify-mobile-otp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("isAuthenticated()")
    public Object verifyMobile(String mobileNo, String otp, @RequestParam(required = false) String name,
            Authentication authentication) {

        Object obj = authentication.getPrincipal();
        Long userId = 0L;

        try {

            if (obj instanceof UserToken) {
                UserToken userToken = (UserToken) obj;
                userId = userToken.getUser().getId();
            } else if (obj instanceof User) {
                User user = (User) obj;
                userId = user.getId();
            }

        } catch (Exception e) {
            // TODO: handle exception
        }

        return this.userService.verifyMobileNumberWithOtpWithDetails(mobileNo, otp, userId, name);
    }

    @RequestMapping(value = "/sync-app-id", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("isAuthenticated()")
    public Object syncAppId(Authentication authentication) {

        Object obj = authentication.getPrincipal();
        Long userId = 0L;

        try {

            if (obj instanceof UserToken) {
                UserToken userToken = (UserToken) obj;
                userId = userToken.getUser().getId();
            } else if (obj instanceof User) {
                User user = (User) obj;
                userId = user.getId();
            }
        } catch (Exception e) {
            return e.getMessage();
        }

        return this.userService.syncAppId(userId);
    }

    @RequestMapping(value = "/reset-mobile-no", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("isAuthenticated()")
    public Object resetMobileNo(@RequestParam(required = false) String mobileNo, Authentication authentication) {

        Object obj = authentication.getPrincipal();
        Long userId = 0L;

        try {

            if (obj instanceof UserToken) {
                UserToken userToken = (UserToken) obj;
                userId = userToken.getUser().getId();
            } else if (obj instanceof User) {
                User user = (User) obj;
                userId = user.getId();
            }

            this.userService.resetMobileNo(mobileNo, userId);

        } catch (Exception e) {
            // Return error code
            return e.getMessage();
        }

        return null;
    }
}