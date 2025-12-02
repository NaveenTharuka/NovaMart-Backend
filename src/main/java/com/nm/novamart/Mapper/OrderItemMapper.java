package com.nm.novamart.Mapper;

import com.nm.novamart.Dto.OrderItemResponseDto;
import com.nm.novamart.Dto.PlaceOrderBuyNowDto;
import com.nm.novamart.Entity.Order;
import com.nm.novamart.Entity.OrderItems;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {

    private final ProductRepository productRepository;

    public List<OrderItems> toOrderItem (Product product, Order order, PlaceOrderBuyNowDto requestDto, double totalPrice) {
        List<OrderItems> orderItems = new ArrayList<>();

        OrderItems orderItem =  OrderItems.builder()
                .order(order)
                .product(product)
                .price(totalPrice)
                .quantity(requestDto.getQuantity())
                .build();
        orderItems.add(orderItem);
        return orderItems;
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

        for  (OrderItems orderItem : orderItems) {
            OrderItemResponseDto item = OrderItemResponseDto.builder()
                    .price(orderItem.getPrice())
                    .productName(orderItem.getProduct().getName())
                    .quantity(orderItem.getQuantity())
                    .build();
            orderItemResponse.add(item);
        }
        return orderItemResponse;
    }

}
