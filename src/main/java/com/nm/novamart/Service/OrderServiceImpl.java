package com.nm.novamart.Service;

import com.nm.novamart.Dto.CartItems.CartItemRequestDto;
import com.nm.novamart.Dto.OrderDto.CheckoutOrderReqDto;
import com.nm.novamart.Dto.OrderDto.OrderResponseDto;
import com.nm.novamart.Dto.OrderDto.PlaceOrderBuyNowDto;
import com.nm.novamart.Dto.OrderDto.UpdateOrderStatusDto;
import com.nm.novamart.Entity.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

        double price = product.getPrice() * requestDto.getQuantity();

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);
        order.setAddress(requestDto.getAddress() == null ? user.getAddress() : requestDto.getAddress());
        order.setComment(requestDto.getComment());
        order.setTotalAmount(price);

        // Save order first to generate its ID
        order = orderRepository.save(order);

        // Generate orderId using the saved order's ID
        order.setOrderId(generateOrderId(order));

        // Update product quantity
        product.setQuantity(product.getQuantity() - requestDto.getQuantity());
        productRepository.save(product);

        // Create order items
        List<OrderItems> orderItems = orderItemMapper.toOrderItem(product, order, requestDto);

        // Save order items to generate their IDs
        orderItems = orderItemRepository.saveAll(orderItems);

        // Set order items on the order
        order.setOrderItems(orderItems);

        // Save order again with everything set
        order = orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    public OrderResponseDto checkOutOrder(CheckoutOrderReqDto reqDto) {
        if (!userRepository.existsById(reqDto.getUserId())) {
            throw new UserNotFoundException("User not found");
        }

        List<CartItemRequestDto> orderItems = reqDto.getCartItems();
        List<Product> products = new ArrayList<>();
        double totalPrice = 0;

        // Validate cart is not empty
        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty");
        }

        for (CartItemRequestDto item : orderItems) {
            // Validate quantity is positive
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException(
                        "Order quantity must be positive for product: " + item.getProductId()
                );
            }

            // Safe product retrieval
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(
                            "Product not found: " + item.getProductId()
                    ));

            // Validate price is positive
            if (product.getPrice() <= 0) {
                throw new IllegalStateException(
                        "Product price must be positive for: " + product.getName()
                );
            }

            products.add(product);
            totalPrice += product.getPrice() * item.getQuantity();
        }

        Order order = Order.builder()
                .user(userRepository.findById(reqDto.getUserId()).get())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .totalAmount(totalPrice)
                .address(reqDto.getAddress())
                .comment(reqDto.getComment())
                .build();

        order = orderRepository.save(order);
        order.setOrderId(generateOrderId(order));

        List<OrderItems> orders = orderItemMapper.toOrderItems(order, products, reqDto);

        orderItemRepository.saveAll(orders);
        order.setOrderItems(orders);

        // Update stock for all products
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int orderedQty = orderItems.get(i).getQuantity();

            int newStock = product.getQuantity() - orderedQty;
            if (newStock < 0) {
                throw new IllegalStateException("Stock became negative for: " + product.getName());
            }

            product.setQuantity(newStock);
            productRepository.save(product);
        }

        order = orderRepository.save(order);

        return orderMapper.toResponse(order);
    }

    public List<OrderResponseDto> getAllOrders(){
        List<OrderResponseDto> responses = new ArrayList<>();
        List<Order> orders = orderRepository.findAll();
        for(Order order : orders ){
            OrderResponseDto response = orderMapper.toResponse(order);
            responses.add(response);
        }

        return responses;
    }

    public List<OrderResponseDto> getAllOrdersByUser(UUID userId){
        List<OrderResponseDto> responses = new ArrayList<>();

        List<Order> orders = orderRepository.getOrdersByUser_Id(userId);

        for(Order order : orders){
            OrderResponseDto response = orderMapper.toResponse(order);
            responses.add(response);
        }

        return responses;
    }


    public OrderResponseDto getOrderById (String orderId){
        if(!orderRepository.existsByOrderId(orderId)){
            throw new OrderNotFoundException(orderId);
        }
        Order order = orderRepository.getOrderByOrderId(orderId);
        return orderMapper.toResponse(order);
    }

    public void updateOrderStatus (UpdateOrderStatusDto requestDto){
        if(requestDto.getOrderId()==null || requestDto.getStatus()==null){
            throw new IllegalArgumentException("Order Id and Status cannot be empty");
        }

        OrderStatus status = parseStatus(requestDto.getStatus());

        if(!orderRepository.existsByOrderId(requestDto.getOrderId())){
            throw new OrderNotFoundException(requestDto.getOrderId());
        }

        Order order = orderRepository.getOrderByOrderId(requestDto.getOrderId());

        if(status == OrderStatus.CANCELED){
            List<OrderItems> orderItems = order.getOrderItems();
            List<Product> products = orderItemMapper.toProducts(orderItems);

            for (OrderItems orderItem : orderItems) {
                for (Product product : products) {
                    if (orderItem.getProduct().getId().equals(product.getId())) {
                        product.setQuantity(product.getQuantity() + orderItem.getQuantity());
                    }
                }
            }
        }

        order.setStatus(status);

        if (status == OrderStatus.CANCELED && requestDto.getCancellationReason() != null) {
            order.setCancellationReason(requestDto.getCancellationReason());
        }

        orderRepository.save(order);
    }


    private String generateOrderId(Order order){
        Long orderId = order.getId();
        String prefix = "NVM-";
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String postFix = String.format("%05d", orderId);

        return prefix +"-"+ timeStamp + "-"+ postFix;
    }

    public OrderStatus parseStatus(String status) {
        try {
            return OrderStatus.valueOf(status.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

}
