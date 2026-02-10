package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.CartItems.CartItemRequestDto;
import com.nm.novamart.Dto.OrderDto.CheckoutOrderReqDto;
import com.nm.novamart.Dto.OrderDto.OrderItemResponseDto;
import com.nm.novamart.Dto.OrderDto.PlaceOrderBuyNowDto;
import com.nm.novamart.Entity.CartItems;
import com.nm.novamart.Entity.Order;
import com.nm.novamart.Entity.OrderItems;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Exeptions.InsufficientStockException;
import com.nm.novamart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ProductRepository productRepository;

    public List<OrderItems> toOrderItem(Product product, Order order, PlaceOrderBuyNowDto requestDto) {
        List<OrderItems> orderItems = new ArrayList<>();

        double price = product.getPrice() * requestDto.getQuantity();

        OrderItems orderItem = OrderItems.builder()
                .order(order)
                .product(product)
                .unitPrice(product.getPrice())
                .totalPrice(price)
                .quantity(requestDto.getQuantity())
                .build();

        orderItems.add(orderItem);
        return orderItems;
    }

    public List<OrderItems> toOrderItems(Order order, List<Product> products, CheckoutOrderReqDto reqDto) {
        List<OrderItems> orderItems = new ArrayList<>();
        List<CartItemRequestDto> cartItems = reqDto.getCartItems();

        // Create a quick lookup map: productId -> order quantity
        Map<UUID, Integer> orderQuantities = new HashMap<>();
        for (CartItemRequestDto cartItem : cartItems) {
            orderQuantities.put(cartItem.getProductId(), cartItem.getQuantity());
        }

        for (Product product : products) {
            // Get the ORDER QUANTITY from the cart, not product stock
            Integer qty = orderQuantities.get(product.getId());

            if (qty == null || qty <= 0) {
                throw new IllegalStateException(
                        "Missing or invalid quantity for product: " + product.getName()
                );
            }

            double unitPrice = product.getPrice();
            double totalPrice = unitPrice * qty;

            OrderItems orderItem = OrderItems.builder()
                    .order(order)
                    .product(product)
                    .unitPrice(unitPrice)
                    .quantity(qty)           // This is ORDER quantity
                    .totalPrice(totalPrice)
                    .build();

            orderItems.add(orderItem);

            // Update product stock (if you want to reduce stock)
            // product.setQuantity(product.getQuantity() - qty);
            // productRepository.save(product);
        }

        return orderItems;
    }

    public List<Product> toProducts(List<OrderItems> orderItems){
        List<Product> products = new ArrayList<>();
        for (OrderItems orderItem : orderItems) {
            products.add(orderItem.getProduct());
        }
        return products;
    }

    //    public List<OrderItems> toOrderItem (List<UUID> productsIds, Order order, PlaceOrderBuyNowDto requestDto) {
    //        List<Product> products = productRepository.findAllById(productsIds);
    //        List<OrderItems> orderItems = new ArrayList<>();
    //
    //        for (Product product : products) {
    //            OrderItems Items = new OrderItems();
    //            Items.setProduct(product);
    //            Items.setQuantity(requestDto.getQuantity());
    //            Items.setPrice(requestDto.getQuantity()*product.getPrice());
    //        }
    //        return orderItems;}

    public List<OrderItemResponseDto> toResponse(List<OrderItems> orderItems) {
        List<OrderItemResponseDto> orderItemResponse = new ArrayList<>();

        for (OrderItems orderItem : orderItems) {
            OrderItemResponseDto item = OrderItemResponseDto.builder()
                    .id(orderItem.getId())  // This is the missing line that adds the ID
                    .productName(orderItem.getProduct().getName())
                    .unitPrice(orderItem.getUnitPrice())
                    .quantity(orderItem.getQuantity())
                    .price(orderItem.getTotalPrice())
                    .build();
            orderItemResponse.add(item);
        }
        return orderItemResponse;
    }
}