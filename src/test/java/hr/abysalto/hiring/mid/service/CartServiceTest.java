package hr.abysalto.hiring.mid.service;

import hr.abysalto.hiring.mid.dto.AddToCartRequest;
import hr.abysalto.hiring.mid.dto.ProductDto;
import hr.abysalto.hiring.mid.model.Cart;
import hr.abysalto.hiring.mid.model.CartItem;
import hr.abysalto.hiring.mid.model.User;
import hr.abysalto.hiring.mid.repository.CartItemRepository;
import hr.abysalto.hiring.mid.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    CartRepository cartRepository;

    @Mock
    CartItemRepository cartItemRepository;

    @Mock
    DummyJsonClient dummyJsonClient;

    @InjectMocks
    CartService cartService;

    @Test
    void shouldAddProductToCart() {
        User user = new User();
        user.setId(1L);
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);

        AddToCartRequest request = new AddToCartRequest(10L, 2);

        when(dummyJsonClient.getProductById(10L))
                .thenReturn(new ProductDto(10L, "Phone", "desc", 500.0, null));

        when(cartItemRepository.findByCartAndProductId(cart, 10L))
                .thenReturn(Optional.empty());

        cartService.addToCart(user, request);

        verify(cartItemRepository).save(argThat(item ->
                item.getProductId().equals(10L) &&
                        item.getQuantity() == 2
        ));
    }

    @Test
    void shouldRemoveItemFromCart() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);

        CartItem item = new CartItem();
        item.setCart(cart);
        item.setProductId(10L);
        item.setQuantity(3);

        when(cartItemRepository.findByCartAndProductId(cart, 10L))
                .thenReturn(Optional.of(item));

        cartService.removeFromCart(user, 10L);

        verify(cartItemRepository).delete(item);
    }

    @Test
    void shouldThrowWhenRemovingNonExistingItem() {
        User user = new User();
        user.setId(1L);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        user.setCart(cart);

        when(cartItemRepository.findByCartAndProductId(cart, 999L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> cartService.removeFromCart(user, 999L));
    }
}

