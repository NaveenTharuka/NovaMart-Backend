package com.nm.novamart.Service;

import com.nm.novamart.Dto.*;
import com.nm.novamart.Entity.*;
import com.nm.novamart.Exeptions.UserNotFoundExeption;
import com.nm.novamart.Mapper.CartItemMapper;
import com.nm.novamart.Repository.*;
import com.nm.novamart.Utility.PriceCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public void addToCart(UUID userId, UUID productId, int quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundExeption(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        if(product.getQuantity() < quantity || quantity <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        if(user.getCart()==null){
            Cart cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cartRepository.save(cart);
        }

        CartItems existingItem = user.getCart().getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if(!(existingItem == null)) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setSubtotal(product.getPrice()*quantity);

        }else {
            CartItems newItem = CartItemMapper.toCartItem(product, quantity, user);
            user.getCart().getItems().add(newItem);
        }

        double total = PriceCalculator.getTotalPrice(user.getCart());
        user.getCart().setTotalPrice(total);

        cartRepository.save(user.getCart());

    }

    public List<CartItemResponseDto> getCartItems(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundExeption(userId));

        Cart cart = user.getCart();

        return CartItemMapper.toCartResponse(cart);
    }

    @Transactional
    public List<CartItemResponseDto> updateCartItem(CartRequestDto cartRequestDto, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundExeption(userId));
        Cart cart = user.getCart();
        Product product = productRepository.findById(cartRequestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found!"));

        if(product.getQuantity() < cartRequestDto.getQuantity() ||  product.getQuantity() <= 0) {
            throw new RuntimeException("Invalid quantity");
        }

        CartItems cartItem = cart.getItems().stream()
                        .filter(cartItems -> cartItems.getProduct().getId().equals(cartRequestDto.getProductId()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Product not found!"));

        cartItem.setQuantity(cartRequestDto.getQuantity());
        cartItem.setSubtotal(product.getPrice()*cartRequestDto.getQuantity());

        double total = PriceCalculator.getTotalPrice(user.getCart());
        user.getCart().setTotalPrice(total);

        cartItemRepository.save(cartItem);
        cartRepository.save(user.getCart());

        return CartItemMapper.toCartResponse(cart);

    }

}
