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
import ourfood.domain.BuyerOrder;
import ourfood.domain.Product;
import ourfood.domain.User;
import ourfood.service.BuyerAccountService;
import ourfood.service.BuyerOrderService;
import ourfood.service.ProductService;

/**
 * Endpoint for Buyer Account CRUD
 *
 * @author raghu.mulukoju
 */
@RequestMapping(value = "/buyerorder")
@Controller
public class BuyerOrderController {

    @Autowired
    private BuyerOrderService buyerOrderService;

    @Autowired
    private BuyerAccountService buyerAccService;

    @Autowired
    private ProductService productService;

    /**
     * Display form to create buyer order
     */
    @RequestMapping(value = "/create/{productId}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@PathVariable Long productId, @ModelAttribute BuyerOrder order, Model model) {

        List<BuyerAccount> accounts = buyerAccService.getAll();
        List<Product> products = productService.getAll();
        Product product = productService.get(productId);

        // NOTE: Spring follows naming conventions for default autowiring of objects
        // Adding model attribute may not be required if the parameter name is buyerAccount instead of order
        model.addAttribute("order", order);
        model.addAttribute("accounts", accounts);
        model.addAttribute("products", products);
        model.addAttribute("product", product);
        return "buyerorder/form";
    }

    /**
     * Create new buyer order
     */
    @RequestMapping(value = "/create/{productId}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute BuyerOrder order, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        User user = (User) auth.getPrincipal();
        try {

            buyerOrderService.create(order, user);
            return "redirect:/buyerorder/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit buyer order
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        BuyerOrder order = buyerOrderService.get(id);
        List<BuyerAccount> accounts = buyerAccService.getAll();
        List<Product> products = productService.getAll();

        model.addAttribute("order", order);
        model.addAttribute("accounts", accounts);
        model.addAttribute("products", products);

        return "buyerorder/edit-form";
    }

    /**
     * Edit buyer order
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute BuyerOrder order, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {

            User user = (User) auth.getPrincipal();

            buyerOrderService.update(order, user);
            return "redirect:/buyerorder/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list buyer orders
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<BuyerOrder> orders = buyerOrderService.getAll();
        model.addAttribute("orders", orders);

        return "buyerorder/list";
    }

    /**
     * Method to delete a buyer orders
     */
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {

        User user = (User) auth.getPrincipal();

        try {
            // User authorization to delete the buyer orders is verified in the service
            this.buyerOrderService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected orders.");
            return "redirect:/buyerorder/list";
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact administrator");
            return ("redirect:/blank");
        }
    }
}
