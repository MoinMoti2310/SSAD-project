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
import ourfood.domain.SellerAccount;
import ourfood.domain.SellerOrder;
import ourfood.domain.User;
import ourfood.domain.enums.SellerOrderItemStatus;
import ourfood.service.ProduceService;
import ourfood.service.SellerAccountService;
import ourfood.service.SellerOrderService;

/**
 * Endpoint for Seller Account CRUD
 * 
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/sellerorder")
@Controller
public class SellerOrderController {

    @Autowired
    private SellerOrderService sellerOrderService;

    @Autowired
    private SellerAccountService sellerAccService;

    @Autowired
    private ProduceService produceService;

    /**
     * Display form to create seller order
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute SellerOrder order, Model model) {

        List<SellerAccount> accounts = sellerAccService.getAll();
        List<Produce> produces = produceService.getAll();

        // NOTE: Spring follows naming conventions for default autowiring of objects
        // Adding model attribute may not be required if the parameter name is sellerAccount instead of order
        model.addAttribute("order", order);
        model.addAttribute("accounts", accounts);
        model.addAttribute("produces", produces);
        return "sellerorder/form";
    }

    /**
     * Create new seller order
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute SellerOrder order, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            User user = (User) auth.getPrincipal();            

            sellerOrderService.create(order, user);
            return "redirect:/sellerorder/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit seller order
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        SellerOrder order = sellerOrderService.get(id);
        List<SellerAccount> accounts = sellerAccService.getAll();
        List<Produce> produces = produceService.getAll();

        model.addAttribute("order", order);
        model.addAttribute("accounts", accounts);
        model.addAttribute("produces", produces);

        return "sellerorder/edit-form";
    }

    /**
     * Edit seller order
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute SellerOrder order, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            User user = (User) auth.getPrincipal();

            sellerOrderService.update(order, user);
            return "redirect:/sellerorder/list";
        } catch (Exception e) {
        	
            return "redirect:/"+(e).toString();
        }
    }

    /**
     * Display form to list seller orders
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<SellerOrder> orders = sellerOrderService.getAll();
        model.addAttribute("orders", orders);

        return "sellerorder/list";
    }

    /**
     * Method to delete a seller orders
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the seller orders is verified in the service
            this.sellerOrderService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected orders.");
            return "redirect:/sellerorder/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
