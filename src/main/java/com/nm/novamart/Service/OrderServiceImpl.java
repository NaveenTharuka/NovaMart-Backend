package com.nm.novamart.Service;

import com.nm.novamart.Dto.OrderResponseDto;
import com.nm.novamart.Dto.PlaceOrderBuyNowDto;
import com.nm.novamart.Entity.Order;
import com.nm.novamart.Entity.Product;
import com.nm.novamart.Entity.User;
import com.nm.novamart.Enum.OrderStatus;
import com.nm.novamart.Exeptions.OrderNotFoundException;
import com.nm.novamart.Exeptions.ProductNotFoundException;
import com.nm.novamart.Exeptions.UserNotFoundException;
import com.nm.novamart.Mapper.OrderItemMapper;
import com.nm.novamart.Mapper.OrderMapper;
import com.nm.novamart.Repository.OrderItemRepository;
import com.nm.novamart.Repository.OrderRepository;
import com.nm.novamart.Repository.ProductRepository;
import com.nm.novamart.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderItemMapper orderItemMapper;
    private final OrderMapper orderMapper;

    public OrderResponseDto placeOrderBuyNow (PlaceOrderBuyNowDto requestDto) {

        if (!userRepository.existsById(requestDto.getUserId())){
            throw new UserNotFoundException("User not found");
        }

        if(!productRepository.existsById(requestDto.getProductId())){
            throw new ProductNotFoundException("Product not found");
        }

        Product product = productRepository.findById(requestDto.getProductId()).get();
        User user = userRepository.findById(requestDto.getUserId()).get();

        double price = product.getPrice()*requestDto.getQuantity();


        Order order = new Order();

        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(price);
        orderRepository.save(order);

        order.setOrderId(generateOrderId(order));

        product.setQuantity(product.getQuantity() -  requestDto.getQuantity());
        productRepository.save(product);

        order.setOrderItems(orderItemMapper.toOrderItem(product, order, requestDto, price));
        orderItemRepository.saveAll(order.getOrderItems());

        return orderMapper.toResponse(order);

    }

    public OrderResponseDto getOrderById (String orderId){
        if(!orderRepository.existsByOrderId(orderId)){
            throw new OrderNotFoundException(orderId);
        }
        Order order = orderRepository.getOrderByOrderId(orderId);
        return orderMapper.toResponse(order);
    }


    private String generateOrderId(Order order){
        Long orderId = order.getId();
        String prefix = "NVM-";
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String postFix = String.format("%05d", orderId);

        return prefix + timeStamp + postFix;
    }

}
