package hr.abysalto.hiring.mid.controller;

import hr.abysalto.hiring.mid.dto.ProductDto;
import hr.abysalto.hiring.mid.dto.ProductListResponse;
import hr.abysalto.hiring.mid.model.User;
import hr.abysalto.hiring.mid.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ProductListResponse> getProducts(@RequestParam(defaultValue = "10") int limit, @RequestParam(defaultValue = "0") int skip) {
        return ResponseEntity.ok(productService.getAllProducts(limit, skip));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @PostMapping("/{id}/favorite")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        productService.addToFavorites(user, id);
        return ResponseEntity.noContent().build();
    }
}
