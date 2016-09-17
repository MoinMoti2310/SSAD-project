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

import ourfood.domain.BuyerAccount;
import ourfood.domain.User;
import ourfood.service.BuyerAccountService;

/**
 * Endpoint for Buyer Account CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/buyacc")
@Controller
public class BuyerAccountController {

    @Autowired
    private BuyerAccountService buyAccService;

    /**
     * Display form to create buyer account
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute BuyerAccount account, Model model) {

        // NOTE: Spring follows naming conventions for default autowiring of objects
        // Adding model attribute may not be required if the parameter name is buyerAccount instead of account
        model.addAttribute("account", account);
        return "buyacc/form";
    }

    /**
     * Create new buyer account
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute BuyerAccount account, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            buyAccService.save(account);
            return "redirect:/buyacc/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit buyer account
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        BuyerAccount account = buyAccService.get(id);
        model.addAttribute("account", account);
        return "buyacc/edit-form";
    }

    /**
     * Edit buyer account
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute BuyerAccount account, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            buyAccService.save(account);
            return "redirect:/buyacc/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list buyer accounts
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<BuyerAccount> accounts = buyAccService.getAll();
        model.addAttribute("accounts", accounts);

        return "buyacc/list";
    }

    /**
     * Method to delete a buyer accounts
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the buyer accounts is verified in the service
            this.buyAccService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected accounts.");
            return "redirect:/buyacc/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
