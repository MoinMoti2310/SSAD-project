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

import ourfood.domain.SellerAccount;
import ourfood.domain.User;
import ourfood.service.SellerAccountService;

/**
 * Endpoint for Seller Account CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/sellacc")
@Controller
public class SellerAccountController {

    @Autowired
    private SellerAccountService sellAccService;

    /**
     * Display form to create seller account
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute SellerAccount account, Model model) {

        // NOTE: Spring follows naming conventions for default autowiring of objects
        // Adding model attribute may not be required if the parameter name is sellerAccount instead of account
        model.addAttribute("account", account);
        return "sellacc/form";
    }

    /**
     * Create new seller account
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute SellerAccount account, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            sellAccService.save(account);
            return "redirect:/sellacc/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit seller account
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        SellerAccount account = sellAccService.get(id);
        model.addAttribute("account", account);
        return "sellacc/edit-form";
    }

    /**
     * Edit seller account
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute SellerAccount account, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            sellAccService.save(account);
            return "redirect:/sellacc/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list seller accounts
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<SellerAccount> accounts = sellAccService.getAll();
        model.addAttribute("accounts", accounts);

        return "sellacc/list";
    }

    /**
     * Method to delete a seller accounts
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the seller accounts is verified in the service
            this.sellAccService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected accounts.");
            return "redirect:/sellacc/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
