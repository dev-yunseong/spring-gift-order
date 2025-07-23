package gift.controller;

import gift.service.OptionService;
import gift.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class AdminController {

    private final ProductService productService;
    private final OptionService optionService;

    public AdminController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping("/admin/products")
    public String adminProducts(Model model) {
        model.addAttribute("products", productService.findAllProducts());
        return "products";
    }

    @GetMapping("/admin/products/{productId}/options")
    public String adminOptions(@PathVariable long productId, Model model) {
        model.addAttribute("product", productService.findProductById(productId));
        model.addAttribute("options", optionService.getProductOptions(productId, Pageable.unpaged()));
        return "product-options";
    }
}