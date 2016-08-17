package ourfood.web;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.google.api.Google;
import org.springframework.social.google.api.plus.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ourfood.domain.Organization;
import ourfood.domain.RoleType;
import ourfood.domain.User;
import ourfood.domain.enums.UserType;
import ourfood.exception.InvalidInputException;
import ourfood.form.Register;
import ourfood.service.OrganizationService;
import ourfood.service.UserService;
import ourfood.service.repositories.RoleRepository;
import ourfood.utils.CommonUtil;

/**
 * End point to do CRUD operations on user account
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/users")
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    RoleRepository roleRepository;

    // TODO move this to SecurityConfiguration
    @Autowired
    AuthenticationManager authenticationManager;

    // TODO move this to SecurityConfiguration
    @Autowired
    RememberMeServices rememberMeServices;

    private Facebook facebook;

    private Google google;

    @Inject
    public UserController(Facebook facebook, Google google) {
        this.facebook = facebook;
        this.google = google;
    }

    /**
     * Method to display a form to create user.
     * <p>
     * NOTE: Self registration is removed. Registration is restricted only to invited users only.
     * </p>
     */
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String registerForm(@ModelAttribute Register register) {

        // NOTE: Remove @PreAuthorize("isAuthenticated()") to allow self user registration
        return "user/form";
    }

    /**
     * Method to create a new user
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public ModelAndView register(@ModelAttribute Register register, BindingResult result, RedirectAttributes redirect) {
        try {
            // TODO validate based on profiles
            this.userService.register(register);
            redirect.addFlashAttribute("successMessage", "Successfully created a new user");
            return new ModelAndView("redirect:/users/list");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return new ModelAndView("redirect:/blank");
        }
    }

    /**
     * Method to list the users of the org. This is accessible only to org admin or power user
     * 
     * @todo list will not contain users of child org
     */
    // TODO include users of child org as well
    // TODO verify if the org admin has access to the given org
    @RequestMapping(value = "/list-org/{organizationId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String listUsersOrg(@PathVariable Long organizationId, RedirectAttributes redirect,
            Authentication authentication, Model model) {
        List<User> users = organizationService.getUsers(organizationId);
        Organization org = organizationService.getOrganization(organizationId);
        model.addAttribute("org", org);
        model.addAttribute("users", users);
        model.addAttribute("navlink", "List Users");
        return "user/list";
    }

    /**
     * Method to list all users. accessible only to super user.
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String listUsers(RedirectAttributes redirect, Authentication authentication, Model model) {

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("navlink", "View Users");
        return "user/list";
    }

    /**
     * Method to display a form to invite user.
     */
    @RequestMapping(value = "/invite", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String inviteForm(@ModelAttribute Register register, Model model) {
        List<Organization> organizations = organizationService.listOrganizations();
        model.addAttribute("organizations", organizations);
        return "user/invite";
    }

    /**
     * Method to invite a new user through e-mail
     */
    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public ModelAndView invite(@ModelAttribute Register register, BindingResult result, RedirectAttributes redirect,
            Authentication authentication) {
        try {
            // TODO validate based on profiles
            User user = (User) authentication.getPrincipal();
            // setting user org if he is not Super User
            if (register.getOrg() == null) {
                register.setOrg(user.getOrganization().getId());
            }

            // Setting role if the user is ORG_PRI_ADMIN

            if (register.getRole() == null) {
                register.setRole(RoleType.ROLE_BUSINESS_USER);
            }

            register.setUserType(UserType.WEB);
            this.userService.invite(register);
            redirect.addFlashAttribute("successMessage", "Successfully invited a new user");
            return new ModelAndView("redirect:/users/list");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return new ModelAndView("redirect:/blank");
        }
    }

    /**
     * Method to display a for for forgot password
     */
    @RequestMapping(value = "/forgot", method = RequestMethod.GET)
    public String forgotPasswordForm() {
        return "user/forgot";
    }

    /**
     * Method to verify if the password provided by the user is valid or not Does not do actual authentication
     */
    @RequestMapping(value = "/validatePassword", method = RequestMethod.POST)
    public ResponseEntity<Void> validatePassword(@RequestParam String login, @RequestParam String password) {
        try {
            userService.login(login, password);
            return new ResponseEntity<Void>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method to verify if the provided email is already registered or not
     */
    @RequestMapping(value = "/validatePrimaryEmail", method = RequestMethod.GET)
    public ResponseEntity<Void> validatePrimaryEmail(@RequestParam String primaryEmail) {
        boolean isUnique = this.userService.isEmailUnique(primaryEmail);
        if (isUnique) {
            return new ResponseEntity<Void>(HttpStatus.OK);
        }
        return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Method to display change password form
     */
    @RequestMapping(value = "/setPassword", method = RequestMethod.GET)
    public String setPasswordForm(@RequestParam String resetToken, @RequestParam String login, Model model) {
        model.addAttribute("resetToken", resetToken);
        model.addAttribute("login", login);
        return "user/setPassword";
    }

    /**
     * Method to display login form to the user
     */
    // TODO catch the invalid login attempt and show a message
    // TODO show a captcha after certain failed attempts
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm() {
        return "user/login";
    }

    /**
     * Method to perform the login operation
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam String login, @RequestParam String password,
            @RequestParam(required = false) String requestUrl, RedirectAttributes redirect, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            Authentication token = new UsernamePasswordAuthenticationToken(login, password);
            Authentication authenticate = authenticationManager.authenticate(token);
            if (authenticate.isAuthenticated()) {
                rememberMeServices.loginSuccess(request, response, authenticate);
                SecurityContextHolder.getContext().setAuthentication(authenticate);
                redirect.addFlashAttribute("successMessage", "Welcome");
                if (requestUrl != null) {
                    return new ModelAndView("redirect:" + requestUrl);
                } else {
                    return new ModelAndView("redirect:/");
                }
            }
        } catch (DisabledException e) {
            return new ModelAndView("user/login", "login", "disabled");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("user/login", "login", "invalid");
    }

    /**
     * Method to validate the user
     */
    @RequestMapping(value = "/validate", method = RequestMethod.GET)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public ModelAndView validate(RedirectAttributes redirect, @RequestParam String token, @RequestParam String login) {

        try {
            User user = this.userService.getUser(login);
            this.userService.validateUser(user, token);
            redirect.addFlashAttribute("message", "Validated");
            redirect.addFlashAttribute("messageStatus", "success");
        } catch (Exception ex) {
            redirect.addFlashAttribute("message", "Validated");
            redirect.addFlashAttribute("messageStatus", "fail");
        }

        return new ModelAndView("redirect:/");
    }

    /**
     * Method to connect with facebook
     */
    @RequestMapping(value = "/connect/facebook", method = RequestMethod.GET)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public String facebookConnect(Authentication authentication) {
        if (!facebook.isAuthorized()) {
            // TODO log fatal error
            throw new RuntimeException("User not connected via FB");
        }
        FacebookProfile facebookProfile = facebook.userOperations().getUserProfile();
        String facebookId = facebookProfile.getId();
        // case 1 - User logged into scanon, facebook not linked
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            if (null == user.getFacebookId()) {
                user.setFacebookId(facebookId);
                userService.save(user);
            } else if (facebookId != user.getFacebookId()) {
                // TODO we have not implemented this
                throw new RuntimeException("User authenticated");
            } else {
                throw new RuntimeException("User Authenticated and connected with same FB acccount");
            }
            return "redirect:/dashboard";
        }
        // case 2 - User not logged into scanon, facebook linked
        User user;
        user = userService.getUserByFacebokId(facebookId);
        if (user != null) {
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "OAuth",
                    user.getAuthorities());
            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return "redirect:/dashboard";
        }
        // case 3 - User exists in scanon but facebook not linked
        user = userService.getUserByPrimaryEmail(facebookProfile.getEmail());
        if (user != null) {
            // TODO set facebook id only if user is validate or fb id same as primaryemail
            user.setFacebookId(facebookId);
            userService.save(user);
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "OAuth",
                    user.getAuthorities());
            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return "redirect:/dashboard";
        }
        // case 4 - completely new user
        user = new User();
        user.setPrimaryEmail(facebookProfile.getEmail());
        user.setFacebookId(facebookProfile.getId());
        user.setFirstName(facebookProfile.getFirstName());
        user.setLastName(facebookProfile.getLastName());
        user.setIsVerified(true);
        userService.save(user);
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "OAuth",
                user.getAuthorities());
        Authentication authenticate = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return "redirect:/dashboard";
    }

    /**
     * Method to connect via google
     */
    @RequestMapping(value = "/connect/google", method = RequestMethod.GET)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public String googleConnect(Authentication authentication) {
        if (!google.isAuthorized()) {
            // TODO log fatal error
            throw new RuntimeException("User not connected via Google");
        }
        Person googleProfile = google.plusOperations().getGoogleProfile();
        String googleId = googleProfile.getId();
        // case 1 - User logged into scanon, google not linked
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            if (null == user.getGoogleId()) {
                user.setGoogleId(googleId);
                userService.save(user);
            } else if (googleId != user.getGoogleId()) {
                // TODO we have not implemented this
                throw new RuntimeException("User authenticated");
            } else {
                throw new RuntimeException("User Authenticated and connected with same Google acccount");
            }
            return "redirect:/dashboard";
        }
        // case 2 - User not logged into scanon, google linked
        User user;
        user = userService.getUserByGoogleId(googleId);
        if (user != null) {
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "OAuth",
                    user.getAuthorities());
            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return "redirect:/dashboard";
        }
        // case 3 - User exists in scanon but google not linked
        user = userService.getUserByPrimaryEmail(googleProfile.getAccountEmail());
        if (user != null) {
            user.setGoogleId(googleId);
            userService.save(user);
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "OAuth",
                    user.getAuthorities());
            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            return "redirect:/dashboard";
        }
        // case 4 - completely new user
        user = new User();
        user.setPrimaryEmail(googleProfile.getAccountEmail());
        user.setGoogleId(googleProfile.getId());
        user.setFirstName(googleProfile.getGivenName());
        user.setLastName(googleProfile.getFamilyName());
        user.setIsVerified(true);
        userService.save(user);
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user, "OAuth",
                user.getAuthorities());
        Authentication authenticate = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return "redirect:/dashboard";
    }

    /**
     * To display form to change current Password.
     * 
     * @param authentication
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/change-pwd", method = RequestMethod.GET)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public String changePassword(Authentication authentication, RedirectAttributes redirect) {
        User user = (User) authentication.getPrincipal();
        if (null == user) {
            return "redirect:/";
        }
        return "user/change-pwd";
    }

    /**
     * Method to handle changing of current password.
     * 
     * @param currentPassword
     * @param password
     * @param confirmPass
     * @param authentication
     * @param redirect
     * @return
     */
    @RequestMapping(value = "/change-pwd", method = RequestMethod.POST)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public String passwordChange(@RequestParam String currentPassword, @RequestParam String password,
            @RequestParam String confirmPass, Authentication authentication, RedirectAttributes redirect) {
        try {
            User user = (User) authentication.getPrincipal();
            if (null == user) {
                throw new RuntimeException("User does not exists");
            }
            Assert.isTrue(password.equals(confirmPass));

            if (currentPassword.equals(password)) {
                throw new InvalidInputException("New password and current password cannot be same.");
            }
            if(password.length() < 6){
                throw new InvalidInputException("Password should contain 6 to 15 characters.");
            }
            this.userService.changePassword(user, currentPassword, password);
            redirect.addFlashAttribute("successMessage", "Password has been changed succesfully.");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("errorMessage", "New password and confirm password do not match.");
        } catch (InvalidInputException e) {
            redirect.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator.");
        }

        return "redirect:/users/change-pwd";
    }

    /**
     * Method to send email if the user forgot password
     */
    @RequestMapping(value = "/forgot", method = RequestMethod.POST)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public ModelAndView resetPassword(@RequestParam String login, RedirectAttributes redirect) {
        try {
            User user = this.userService.getUser(login);
            if (null == user) {
                return new ModelAndView("user/forgot", "notfound", "true");
            }
            this.userService.resetPassword(user);
            redirect.addFlashAttribute("successMessage",
                    "An email has been sent to the registered email address with next action");
            return new ModelAndView("redirect:/");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return new ModelAndView("redirect:/blank");
        }
    }

    /**
     * Method to change the user password from link sent to user
     */
    @RequestMapping(value = "/setPassword", method = RequestMethod.POST)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    public ModelAndView setPassword(@RequestParam String login, @RequestParam String resetToken,
            @RequestParam String password, @RequestParam String confirmPass, RedirectAttributes redirect) {
        try {
            User user = this.userService.getUser(login);
            if (null == user) {
                throw new RuntimeException("User does not exists");
            }
            Assert.isTrue(password.equals(confirmPass));
            this.userService.setPassword(user, resetToken, password);
            redirect.addFlashAttribute("successMessage", "Password has been changed succesfully");
            return new ModelAndView("redirect:/");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return new ModelAndView("redirect:/blank");
        }
    }

    /**
     * Show the form to impersonate a user
     * 
     * IMPORTANT accessible only to the power user. Revalidate credentials
     */
    @RequestMapping(value = "/impersonate", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String impersonateForm() {
        return "user/impersonate";
    }

    /**
     * View and edit user form
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String viewEditForm(@PathVariable("id") Long userId, Model model) {

        User user = userService.getUser(userId);
        List<Organization> organizations = organizationService.listOrganizations();
        model.addAttribute("organizations", organizations);
        model.addAttribute("user", user);
        return "user/edit-form";
    }

    /**
     * View and edit user method
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE')")
    public String viewEdit(@ModelAttribute User user, @RequestParam Long userId,
            @RequestParam(required = false) String roleType, BindingResult result, Authentication authentication,
            RedirectAttributes redirect) throws Exception {

        try {

            user.setId(userId);

            String registeredMobile = user.getRegisteredMobile();

            if (registeredMobile == null || registeredMobile.trim().isEmpty()) {
                user.setRegisteredMobile(null);
            } else {
                user.setRegisteredMobile(CommonUtil.getMobileFormat(registeredMobile.trim()));
            }

            user.setFirstName(user.getFirstName().trim());
            user.setLastName(user.getLastName().trim());
            user.setPrimaryEmail(user.getPrimaryEmail().trim());
            user.setAuthorizedActionSetIds(user.getAuthorizedActionSetIds().trim());

            userService.save(user, true);
            redirect.addFlashAttribute("successMessage", "Successfully updated");
        } catch (ConstraintViolationException cve) {
            redirect.addFlashAttribute("errorMessage", "Duplicates are not allowed for Primary Email/Mobile No.");
            return "redirect:/users/edit/" + userId;
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return "redirect:/blank";
        }

        return "redirect:/users/list";
    }

    /**
     * Impersonate the user. Accessible only to the power user. Revalidate credentials
     */
    @RequestMapping(value = "/impersonate", method = RequestMethod.POST)
    @Transactional(rollbackOn = { Exception.class, RuntimeException.class })
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String impersonate(@RequestParam String primaryEmail, @RequestParam String password,
            Authentication authentication, RedirectAttributes redirect) {
        try {
            User user = userService.getUser(primaryEmail);
            if (user == null) {
                redirect.addFlashAttribute("warningMessage", "User does not exist");
                return "redirect:/";
            }
            PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(user,
                    "PERM_PLATFORM_UPDATE", user.getAuthorities());
            Authentication authenticate = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            redirect.addFlashAttribute("successMessage", "Success");
            return "redirect:/";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return "redirect:/blank";
        }
    }
}