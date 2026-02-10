package com.nm.novamart.Controller;

import com.nm.novamart.Dto.OrderDto.CheckoutOrderReqDto;
import com.nm.novamart.Dto.OrderDto.OrderResponseDto;
import com.nm.novamart.Dto.OrderDto.PlaceOrderBuyNowDto;
import com.nm.novamart.Dto.OrderDto.UpdateOrderStatusDto;
import com.nm.novamart.Service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("api/order")
public class OrderController {

    private final OrderServiceImpl orderService;

    @GetMapping("{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable String orderId) {
        OrderResponseDto responseDto = orderService.getOrderById(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserId(@PathVariable UUID userId) {
        orderService.getAllOrdersByUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrdersByUser(userId));
    }


    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getAllOrders());
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> buyNow(@RequestBody PlaceOrderBuyNowDto placeOrderBuyNowDto) {
        OrderResponseDto response = orderService.placeOrderBuyNow(placeOrderBuyNowDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cartItem")
    public ResponseEntity<OrderResponseDto> checkOutOrder(@RequestBody CheckoutOrderReqDto checkoutOrderReqDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.checkOutOrder(checkoutOrderReqDto));
    }

    @PutMapping
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateOrderStatusDto statusDto){
        orderService.updateOrderStatus(statusDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
