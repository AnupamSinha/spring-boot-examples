package com.anupam.htmx.controller;

import com.anupam.htmx.model.Product;
import com.anupam.htmx.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * MVC controller serving both full pages and HTML fragments for HTMX-powered
 * partial page updates.
 * <p>
 * Supports CRUD operations on products with HTMX attributes (hx-get, hx-post,
 * hx-delete) for seamless in-page interactivity without client-side JavaScript.
 * </p>
 *
 * @author Anupam
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    /**
     * Constructs the controller with the product service.
     *
     * @param productService the service managing product data
     */
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Full page render — serves the complete products page with all items.
     *
     * @param model the Spring MVC model
     * @return the Thymeleaf template name for the products page
     */
    @GetMapping
    public String productsPage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    /**
     * Fragment render — returns only the product list table body.
     * <p>
     * Used by hx-get for partial page updates without full reload.
     * </p>
     *
     * @param model the Spring MVC model
     * @return the Thymeleaf fragment template for the product list
     */
    @GetMapping("/list")
    public String productList(Model model) {
        model.addAttribute("products", productService.findAll());
        return "fragments/product-list";
    }

    /**
     * Creates a new product via hx-post and returns the new product row
     * fragment for hx-swap insertion into the DOM.
     *
     * @param name  the product name from the form
     * @param price the product price from the form
     * @param model the Spring MVC model
     * @return the Thymeleaf fragment template for a single product row
     */
    @PostMapping
    public String createProduct(@RequestParam String name,
                                @RequestParam BigDecimal price,
                                Model model) {
        Product product = productService.create(name, price);
        model.addAttribute("product", product);
        return "fragments/product-row";
    }

    /**
     * Deletes a product via hx-delete and returns an empty response.
     * <p>
     * The empty response causes hx-swap="outerHTML" to remove the row from the DOM.
     * </p>
     *
     * @param id the product ID to delete
     * @return an empty string (row is removed by HTMX swap)
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "";
    }

    /**
     * Searches products by name — triggered by hx-trigger="input changed delay:500ms".
     * <p>
     * Returns the filtered product list fragment for partial DOM update.
     * </p>
     *
     * @param query the search query string
     * @param model the Spring MVC model
     * @return the Thymeleaf fragment template with filtered results
     */
    @GetMapping("/search")
    public String searchProducts(@RequestParam(defaultValue = "") String query, Model model) {
        model.addAttribute("products", productService.search(query));
        return "fragments/product-list";
    }
}
