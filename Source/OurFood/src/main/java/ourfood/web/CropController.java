package ourfood.web;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ourfood.domain.Crop;
import ourfood.domain.User;
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
     * Display form to create crop
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute Crop crop, Model model) {

        return "crop/form";
    }

    /**
     * Create new crop
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute Crop crop, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            cropService.save(crop);
            return "redirect:/crop/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        Crop crop = cropService.get(id);
        model.addAttribute("crop", crop);
        return "crop/edit-form";
    }

    /**
     * Edit crop
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute Crop crop, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            cropService.save(crop);
            return "redirect:/crop/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list crops
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<Crop> crops = cropService.getAll();
        model.addAttribute("crops", crops);

        return "crop/list";
    }

    /**
     * Method to delete a crops
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the crop is verified in the service
            this.cropService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected crops.");
            return "redirect:/crop/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
