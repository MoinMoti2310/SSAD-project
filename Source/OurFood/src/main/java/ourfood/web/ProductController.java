package ourfood.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ourfood.domain.Product;
import ourfood.domain.User;
import ourfood.service.ProductService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * ENDPOINT for Product CRUD
 *
 * Created by moinhussain.moti on 01-10-2016.
 */

@RequestMapping(value = "/product")
@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Display form to create product
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String createForm(@ModelAttribute Product product, Model model) {

        return "product/form";
    }

    /**
     * Create new product
     */
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String create(@ModelAttribute  Product product, Authentication auth) throws NoSuchMethodException,
            SecurityException {

        try {
            productService.save(product);
            return "redirect:/product/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to edit
     */
    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String editForm(@PathVariable Long id, Model model) {

        Product product = productService.get(id);
        model.addAttribute("product", product);
        return "product/edit-form";
    }

    /**
     * Edit product
     */
    @RequestMapping(value = "edit/{id}", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    @Transactional(rollbackOn = Exception.class)
    public String editAjax(@ModelAttribute Product product, Authentication auth) throws NoSuchMethodException,
            SecurityException {
        try {
            productService.save(product);
            return "redirect:product/list";
        } catch (Exception e) {
            return "redirect:/blank";
        }
    }

    /**
     * Display form to list products
     */
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String list(Model model) {

        List<Product> products = productService.getAll();
        model.addAttribute("products", products);

        return "product/list";
    }

    /**
     * Improved interface for list
     */
    @RequestMapping(value = "shop", method = RequestMethod.GET)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String shop(@RequestParam(value = "query", required = false) String query, Model model) {

        if (query != null) {
            List<Product> allProducts = productService.getAll();
            List<Product> products = new ArrayList<Product>();
            for (Product product : allProducts) {
                String s1 = product.getName();
                String s2 = product.getDescription();
                String s3 = String.valueOf(product.getProduceCategory());
                boolean name = Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE).matcher(s1).find();
                boolean desc = Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE).matcher(s2).find();
                boolean category = Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE).matcher(s3).find();
                if (name || desc || category) products.add(product);
                model.addAttribute("products", products);
            }
        } else {
            List<Product> products = productService.getAll();
            model.addAttribute("products", products);
        }

        return "product/shop";
    }

    /**
     * Method to delete products
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @PreAuthorize("hasRole('PERM_PLATFORM_UPDATE')")
    public String delete(@RequestParam("itemIds[]") Long[] itemIds, Authentication auth, RedirectAttributes redirect) {
        User user = (User) auth.getPrincipal();

        try {
            // serviceImplimentation verifies the user authentication to delete the product
            this.productService.delete(itemIds, user);
            redirect.addFlashAttribute("successMessage", "Successfully deleted selected products.");
            return ("redirect:/product/list");
        } catch (Exception e) {
            redirect.addFlashAttribute("errorMessage", "An error occured while deleting, please contact Adiministrator");
            return ("redirect:/blank");
        }
    }

}
