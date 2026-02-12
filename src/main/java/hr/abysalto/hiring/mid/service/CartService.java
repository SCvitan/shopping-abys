package hr.abysalto.hiring.mid.service;

import hr.abysalto.hiring.mid.dto.AddToCartRequest;
import hr.abysalto.hiring.mid.dto.CartItemResponse;
import hr.abysalto.hiring.mid.dto.CartResponse;
import hr.abysalto.hiring.mid.dto.ProductDto;
import hr.abysalto.hiring.mid.model.Cart;
import hr.abysalto.hiring.mid.model.CartItem;
import hr.abysalto.hiring.mid.model.User;
import hr.abysalto.hiring.mid.repository.CartItemRepository;
import hr.abysalto.hiring.mid.repository.CartRepository;
import hr.abysalto.hiring.mid.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final DummyJsonClient dummyJsonClient;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, DummyJsonClient dummyJsonClient) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.dummyJsonClient = dummyJsonClient;
    }

    public void addToCart(User user, AddToCartRequest request) {

        Cart cart = user.getCart();
        dummyJsonClient.getProductById(request.productId());

        CartItem item = cartItemRepository.findByCartAndProductId(cart, request.productId())
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setProductId(request.productId());
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + request.quantity());
        cartItemRepository.save(item);
    }

    public void removeFromCart(User user, Long productId) {

        Cart cart = user.getCart();

        CartItem item = cartItemRepository
                .findByCartAndProductId(cart, productId)
                .orElseThrow(() -> new RuntimeException("Product not in cart"));

        cartItemRepository.delete(item);
    }

    public CartResponse getCart(User user) {

        Cart cart = cartRepository
                .findByUserIdWithItems(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItemResponse> items = new ArrayList<>();
        double total = 0;

        for (CartItem item : cart.getItem()) {
            ProductDto product = dummyJsonClient.getProductById(item.getProductId());

            double itemTotal = product.price() * item.getQuantity();
            total += itemTotal;

            items.add(new CartItemResponse(
                    product.id(),
                    product.title(),
                    product.price(),
                    item.getQuantity()
            ));
        }

        return new CartResponse(items, total);
    }
}
