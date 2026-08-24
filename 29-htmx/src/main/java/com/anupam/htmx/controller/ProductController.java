package com.anupam.htmx.controller;

import com.anupam.htmx.model.Product;
import com.anupam.htmx.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Full page render — serves the complete products page.
     */
    @GetMapping
    public String productsPage(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products";
    }

    /**
     * Fragment render — returns only the product list table body.
     * Used by hx-get for partial page updates.
     */
    @GetMapping("/list")
    public String productList(Model model) {
        model.addAttribute("products", productService.findAll());
        return "fragments/product-list";
    }

    /**
     * Create a new product via hx-post.
     * Returns the new product row fragment for hx-swap.
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
     * Delete a product via hx-delete.
     * Returns empty response — the row is removed from DOM by hx-swap="outerHTML".
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "";
    }

    /**
     * Search products — triggered by hx-trigger="input changed delay:500ms".
     * Returns the filtered product list fragment.
     */
    @GetMapping("/search")
    public String searchProducts(@RequestParam(defaultValue = "") String query, Model model) {
        model.addAttribute("products", productService.search(query));
        return "fragments/product-list";
    }
}
