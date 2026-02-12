package hr.abysalto.hiring.mid.controller;

import hr.abysalto.hiring.mid.dto.AddToCartRequest;
import hr.abysalto.hiring.mid.dto.CartResponse;
import hr.abysalto.hiring.mid.model.User;
import hr.abysalto.hiring.mid.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addToCart(@RequestBody AddToCartRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.addToCart(user, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cartService.getCart(user));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cartService.removeFromCart(user, productId);
        return ResponseEntity.noContent().build();
    }
}
