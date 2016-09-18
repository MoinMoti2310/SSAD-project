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

import ourfood.domain.Produce;
import ourfood.domain.User;
import ourfood.service.ProduceService;

/**
 * Endpoint for produce CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/produce")
@Controller
public class ProduceController {

    @Autowired
    private ProduceService produceService;

    /**
     * Display form to create produce
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute Produce produce, Model model) {

        return "produce/form";
    }

    /**
     * Create new produce
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute Produce produce, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            produceService.save(produce);
            return "redirect:/produce/list";
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

        Produce produce = produceService.get(id);
        model.addAttribute("produce", produce);
        return "produce/edit-form";
    }

    /**
     * Edit produce
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute Produce produce, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            produceService.save(produce);
            return "redirect:/produce/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list produces
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<Produce> produces = produceService.getAll();
        model.addAttribute("produces", produces);

        return "produce/list";
    }

    /**
     * Method to delete a produces
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the produce is verified in the service
            this.produceService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected produces.");
            return "redirect:/produce/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
