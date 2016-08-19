package ourfood.web;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ourfood.domain.Crop;
import ourfood.service.CropService;

/**
 * Endpoint for crop CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/crop")
@Controller
public class CropController {

    @Autowired
    private CropService cropService;

    /**
     * Display form to create an organization
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute Crop crop, Model model) {

        return "crop/form";
    }

    /**
     * Handle creating new crop
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute Crop crop, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            cropService.saveCrop(crop);
            // Long organizationId = organizationService.save(organization);
            // redirect.addFlashAttribute("successMessage", "Organization created successfully.");
            return "redirect:/crop/list";
        } catch (Exception e) {
            // redirect.addFlashAttribute("errorMessage",
            // "An error occured while creating, please contact administrator");
            return "redirect:/blank";
        }
    }

    /**
     * Display form to create an organization
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list() {

        return "crop/list";
    }
}
