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

import ourfood.domain.ProcessingCenter;
import ourfood.domain.User;
import ourfood.service.ProcessingCenterService;

/**
 * Endpoint for ProcessingCenter CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/procen")
@Controller
public class ProcessingCenterController {

    @Autowired
    private ProcessingCenterService proCenService;

    /**
     * Display form to create processing center
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute ProcessingCenter proCen, Model model) {

        // NOTE: Spring follows naming conventions for default autowiring of objects
        // Adding model attribute may not be required if the parameter name is proCen instead of processingCenter
        model.addAttribute("proCen", proCen);
        return "procen/form";
    }

    /**
     * Create new processing center
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute ProcessingCenter proCen, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            proCenService.save(proCen);
            return "redirect:/procen/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit processing center
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        ProcessingCenter proCen = proCenService.get(id);
        model.addAttribute("proCen", proCen);
        return "procen/edit-form";
    }

    /**
     * Edit processing center
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute ProcessingCenter proCen, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            proCenService.save(proCen);
            return "redirect:/procen/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list processing centers
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<ProcessingCenter> proCens = proCenService.getAll();
        model.addAttribute("proCens", proCens);

        return "procen/list";
    }

    /**
     * Method to delete a processing centers
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the processing centers is verified in the service
            this.proCenService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected processing centers.");
            return "redirect:/procen/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
