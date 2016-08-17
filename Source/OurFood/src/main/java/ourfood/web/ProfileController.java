package ourfood.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ourfood.domain.User;
import ourfood.service.UserService;

/**
 * End point to do CRUD operations on user account
 * 
 \* @author raghu.mulukoju
 */
@RequestMapping(value = "/profile")
@Controller
public class ProfileController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/list-notifications", method = RequestMethod.GET)
    public String listNotifications(Authentication authentication, Model model) {
        return null;
    }

    @RequestMapping(value = "/view/{userId}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String viewProfile(@PathVariable Long userId, Authentication authentication, Model model) {
        User user = userService.getUser(userId);
        model.addAttribute("user", user);
        return "profile/view";
    }
}
