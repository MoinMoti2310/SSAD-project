package ourfood.web;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ourfood.domain.Organization;
import ourfood.domain.RoleType;
import ourfood.domain.User;
import ourfood.form.Register;
import ourfood.service.OrganizationService;
import ourfood.service.UserService;

/**
 * Endpoint for organization CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/organization")
@Controller
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private UserService userService;

    /**
     * Display form to create an organization
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute Organization organization, @ModelAttribute Register register,
            Model model) {
        return "organization/form";
    }

    /**
     * Handle creating new organization and an admin
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute Organization organization, @ModelAttribute Register register,
            BindingResult result, RedirectAttributes redirect, Authentication authentication)
            throws NoSuchMethodException, SecurityException {
        try {
            organization.setIsActive(true);
            organization.setIsPending(false);
            Long organizationId = organizationService.save(organization);            
            redirect.addFlashAttribute("successMessage", "Organization created successfully.");
            return "redirect:/organization/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while creating, please contact administrator");
            return "redirect:/blank";
        }
    }
    
    /**
     * Handle creating new organization and an admin
     */
    @RequestMapping(value = "/create-admin", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String createAdmin(@ModelAttribute Organization organization, @ModelAttribute Register register,
            BindingResult result, RedirectAttributes redirect, Authentication authentication)
            throws NoSuchMethodException, SecurityException {
        try {
            organization.setIsActive(true);
            organization.setIsPending(false);
            Long organizationId = organizationService.save(organization);
            register.setOrg(organizationId);
            userService.register(register, true);
            redirect.addFlashAttribute("successMessage", "Organization created successfully.");
            return "redirect:/organization/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while creating, please contact administrator");
            return "redirect:/blank";
        }
    }

    /**
     * Handle creating new organization and an admin
     */
    @RequestMapping(value = "/create-old", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute Organization organization, String primaryEmail, BindingResult result,
            RedirectAttributes redirect, Authentication authentication)
            throws NoSuchMethodException, SecurityException {
        try {
            organization.setIsActive(true);
            organization.setIsPending(false);
            organizationService.save(organization);
            User user = userService.getUser(primaryEmail);
            user.setOrganization(organization);
            user.setRole(userService.getRole(RoleType.ROLE_ORG_PRI_ADMIN));
            userService.save(user);
            redirect.addFlashAttribute("successMessage", "Organization created successfully.");
            return "redirect:/organization/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while creating, please contact administrator");
            return "redirect:/blank";
        }
    }

    /**
     * AJAX Method to enable an org. This is accessible only to parent org admin
     */
    // TODO admin of the parent org should be able to do this
    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public void activate(@RequestParam Long organizationId, RedirectAttributes redirect,
            Authentication authentication) {
        organizationService.activate(organizationId);
    }

    /**
     * AJAX Method to approve an org. This is accessible only to power user
     */
    @RequestMapping(value = "/approve", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public void approve(@RequestParam Long organizationId, RedirectAttributes redirect, Authentication authentication) {
        organizationService.approve(organizationId);
    }

    /**
     * Method to display organizaion details. This is accessible only to power user.
     * 
     * @param organizationId
     * @param redirect
     * @param authentication
     * @param model
     * @return
     */
    @RequestMapping(value = "/edit/{organizationId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String viewOrg(@PathVariable Long organizationId, RedirectAttributes redirect, Authentication authentication,
            Model model) {
        Organization org = organizationService.getOrganization(organizationId);
        model.addAttribute("org", org);
        return "organization/edit-form";
    }

    /**
     * Method to edit organization details. This is accessible only to power user.
     * 
     * @param orgId
     * @param organization
     * @param result
     * @param redirect
     * @param authentication
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String edit(@RequestParam Long orgId, @ModelAttribute Organization organization, BindingResult result,
            RedirectAttributes redirect, Authentication authentication)
            throws NoSuchMethodException, SecurityException {
        try {
            Long id = orgId;
            organization.setIsActive(true);
            organization.setIsPending(false);
            organization.setId(id);
            organizationService.save(organization);
            redirect.addFlashAttribute("successMessage", "Organization updated successfully.");
            return "redirect:/organization/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return "redirect:/blank";
        }
    }

    /**
     * AJAX Method to disable an org. This is accessible only to parent org admin
     */
    // TODO admin of the parent org should be able to do this
    @RequestMapping(value = "/deactivate", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public void deactivate(@RequestParam Long organizationId, RedirectAttributes redirect,
            Authentication authentication) {
        if (organizationId != 1) {
            organizationService.deactivate(organizationId);
        }
    }

    /**
     * AJAX Method to reject an org. This is accessible only to parent org admin
     */
    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(rollbackOn = Exception.class)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public void reject(@RequestParam Long organizationId, RedirectAttributes redirect, Authentication authentication) {
        organizationService.reject(organizationId);
    }

    /**
     * Method to list all the children of the org. This is accessible only to org admin
     */
    // TODO not implemented
    @RequestMapping(value = "/list/{organizationId}", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String listOrgs(@PathVariable Long organizationId, RedirectAttributes redirect,
            Authentication authentication, Model model) {
        return null;
    }

    /**
     * Method to list all the organizations listed currently. This is for the power user only
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String listAllOrgs(RedirectAttributes redirect, Authentication authentication, Model model) {
        List<Organization> organizations = organizationService.listOrganizations();
        model.addAttribute("organizations", organizations);
        return "organization/list";
    }

    /**
     * Method to list all the organizations listed currently. This is for the power user only
     */
    @RequestMapping(value = "/list-pending", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String listPendingOrgs(RedirectAttributes redirect, Authentication authentication, Model model) {
        List<Organization> organizations = organizationService.listPendingOrganizations();
        model.addAttribute("organizations", organizations);
        return "organization/approve";
    }

    @RequestMapping(value = "/add-user", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String addUserForm(@RequestParam Long organizationId, RedirectAttributes redirect,
            Authentication authentication, Model model) {
        Organization organization = organizationService.getOrganization(organizationId);
        model.addAttribute("organization", organization);
        return "organization/add-user";
    }

    @RequestMapping(value = "/add-user", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String addUser(@RequestParam Long organizationId, @RequestParam String primaryEmail,
            RedirectAttributes redirect, Authentication authentication, Model model) {
        try {
            organizationService.addUser(organizationId, primaryEmail);
            redirect.addFlashAttribute("successMessage", "Successfully added");
            return "redirect:/";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured, please contact administrator");
            return "redirect:/blank";
        }
    }

    /**
     * Display form to add a new user as admin of the org.
     */
    // TODO not implemented
    @RequestMapping(value = "/add-admin", method = RequestMethod.GET)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String addAdminForm(@PathVariable Long organizationId, RedirectAttributes redirect,
            Authentication authentication, Model model) {
        return null;
    }

    /**
     * Method to add a new user as admin of the org. This method is accessible to org admin
     */
    // TODO not implemented
    @RequestMapping(value = "/add-admin", method = RequestMethod.POST)
    @PreAuthorize("hasAnyRole('PERM_PLATFORM_UPDATE','PERM_ORG_UPDATE')")
    public String addAdmin(@RequestParam Long organizationId, RedirectAttributes redirect,
            Authentication authentication, Model model) {
        return null;
    }

}
